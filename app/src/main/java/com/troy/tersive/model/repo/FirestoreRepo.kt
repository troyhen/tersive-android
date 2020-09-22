package com.troy.tersive.model.repo

import com.google.firebase.firestore.DocumentChange.Type.REMOVED
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.troy.tersive.model.db.tersive.Tersive
import com.troy.tersive.model.db.tersive.TersiveDatabaseManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepo @Inject constructor(
    private val tersiveDatabaseManager: TersiveDatabaseManager
) {

    private val db = FirebaseFirestore.getInstance()
    private val tersiveRef = db.collection("lanes").document("stage").collection("tersive")

    init {
        tersiveRef.addSnapshotListener(TersiveListener())
    }

    fun sendAll() = GlobalScope.launch {
        var count = 1
        tersiveDatabaseManager.tersiveDb.tersiveDao.findAll().chunked(500).forEach { chunk ->
            val batch = db.batch()
            chunk.forEach {
                batch.set(tersiveRef.document(it.id.toString()), it.asDocument)
            }
            batch.commit().addOnSuccessListener {
                Timber.d("Sent 500 tersive: ${count++}")
            }.addOnFailureListener {
                Timber.e(it)
            }
        }
    }

    private fun update(query: QuerySnapshot) {
        tersiveDatabaseManager.tersiveDb.runInTransaction {
            for (change in query.documentChanges) {
                val doc = change.document
                if (doc.metadata.hasPendingWrites()) continue   // ignore documents which we just sent
                val tersive = doc.asTersive ?: continue
                when (change.type) {
                    REMOVED -> tersive.delete()
                    else -> tersive.save()
                }
            }
        }
    }

    private val Tersive.asDocument: Map<String, Any>
        get() = mutableMapOf<String, Any>().apply {
            set("id", id)
            set("phrase", phrase)
            lvl1?.let { set("lvl1", it) }
            lvl2?.let { set("lvl2", it) }
            lvl3?.let { set("lvl3", it) }
            lvl4?.let { set("lvl4", it) }
            kbd?.let { set("kbd", it) }
            set("words", words)
            sort?.let { set("sort", it) }
            set("type", type)
        }

    private fun Tersive.delete() = tersiveDatabaseManager.tersiveDb.tersiveDao.delete(this)

    private fun Tersive.save() = tersiveDatabaseManager.tersiveDb.tersiveDao.save(this)

    private val QueryDocumentSnapshot.asTersive: Tersive?
        get() {
            val id = getLong("id") ?: return null
            val phrase = getString("phrase") ?: return null
            val words = getLong("words")?.toInt() ?: return null
            val frequency = getLong("frequency")?.toInt() ?: return null
            val type = getLong("type")?.toInt() ?: return null
            return Tersive(
                id = id,
                phrase = phrase,
                lvl1 = getString("lvl1"),
                lvl2 = getString("lvl2"),
                lvl3 = getString("lvl3"),
                lvl4 = getString("lvl4"),
                kbd = getString("kbd"),
                words = words,
                frequency = frequency,
                sort = getLong("sort")?.toInt(),
                type = type
            )
        }

    inner class TersiveListener : EventListener<QuerySnapshot> {
        override fun onEvent(query: QuerySnapshot?, e: FirebaseFirestoreException?) {
            when {
                e != null -> Timber.e(e)
                query != null -> GlobalScope.launch(Dispatchers.IO) {
                    update(query)
                }
            }
        }
    }
}
