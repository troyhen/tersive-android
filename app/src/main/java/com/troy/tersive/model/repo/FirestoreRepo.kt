package com.troy.tersive.model.repo

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
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

    //    private val tersiveRef = db.collection("lanes").document("stage").collection("tersive")
    private val tersiveCacheRef = db.collection("lanes").document("stage").collection("tersiveCache")

    init {
        tersiveCacheRef.addSnapshotListener(TersiveListener())
//        sendTersiveCache()
    }

    private fun sendTersiveCache() = GlobalScope.launch(Dispatchers.IO) {
        val data = mutableMapOf<Any, Any>()
        val tersive = ArrayList<Map<String, Any>>(chunkSize)
        tersiveDatabaseManager.tersiveDb.tersiveDao.findAll().chunked(chunkSize).forEach { chunk ->
            data.clear()
            tersive.clear()
            data["first"] = chunk.first().id
            data["last"] = chunk.last().id
            data["modified"] = FieldValue.serverTimestamp()
            chunk.forEach {
                tersive.add(it.toMap())
            }
            data["tersive"] = tersive
            tersiveCacheRef.document().set(data).addOnSuccessListener {
                Timber.d("Sent ${chunk.size} to tersiveCache")
            }.addOnFailureListener {
                Timber.e(it)
            }
        }
    }

//    fun sendAll() = GlobalScope.launch {
//        var count = 1
//        tersiveDatabaseManager.tersiveDb.tersiveDao.findAll().chunked(500).forEach { chunk ->
//            val batch = db.batch()
//            chunk.forEach {
//                batch.set(tersiveRef.document(it.id.toString()), it.asDocument)
//            }
//            batch.commit().addOnSuccessListener {
//                Timber.d("Sent 500 tersive: ${count++}")
//            }.addOnFailureListener {
//                Timber.e(it)
//            }
//        }
//    }

    private fun update(query: QuerySnapshot) {
        tersiveDatabaseManager.tersiveDb.runInTransaction {
            for (change in query.documentChanges) {
                val doc = change.document
                if (doc.metadata.hasPendingWrites()) continue   // ignore documents which we just sent
                val tersiveList = doc.getTersiveList("tersive")
                when (change.type) {
                    DocumentChange.Type.REMOVED -> tersiveList.delete()
                    else -> tersiveList.save()
                }
            }
        }
    }
//
//    private fun update(query: QuerySnapshot) {
//        tersiveDatabaseManager.tersiveDb.runInTransaction {
//            for (change in query.documentChanges) {
//                val doc = change.document
//                if (doc.metadata.hasPendingWrites()) continue   // ignore documents which we just sent
//                val tersive = doc.asTersive ?: continue
//                when (change.type) {
//                    REMOVED -> tersive.delete()
//                    else -> tersive.save()
//                }
//            }
//        }
//    }

    private fun Tersive.toMap(): Map<String, Any> {
        return mutableMapOf<String, Any>().apply {
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
    }

    private fun List<Tersive>.delete() = tersiveDatabaseManager.tersiveDb.tersiveDao.delete(this)
    private fun List<Tersive>.save() = tersiveDatabaseManager.tersiveDb.tersiveDao.save(this)

    private fun Map<*, *>.getInt(key: String) = getLong(key)?.toInt()
    private fun Map<*, *>.getLong(key: String) = get(key) as? Long
    private fun Map<*, *>.getString(key: String) = get(key) as? String

    private fun Map<*, *>.toTersive(): Tersive? {
        val id = getLong("id") ?: return null
        val phrase = getString("phrase") ?: return null
        val words = getInt("words") ?: return null
        val frequency = getInt("frequency") ?: return null
        val type = getInt("type") ?: return null
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
            sort = (get("sort") as? Long)?.toInt(),
            type = type
        )
    }

    private fun QueryDocumentSnapshot.getList(key: String) = get(key) as List<*>

    private fun QueryDocumentSnapshot.getTersiveList(key: String): List<Tersive> {
        val tersive = getList(key) ?: return emptyList()
        val list = ArrayList<Tersive>(tersive.size)
        tersive.forEach {
            val map = it as? Map<*, *> ?: return@forEach
            val id = map.getLong("id") ?: return@forEach
            val phrase = map.getString("phrase") ?: return@forEach
            val words = map.getInt("words") ?: return@forEach
            val frequency = map.getInt("frequency") ?: return@forEach
            val type = map.getInt("type") ?: return@forEach
            list.add(
                Tersive(
                    id = id,
                    phrase = phrase,
                    lvl1 = map.getString("lvl1"),
                    lvl2 = map.getString("lvl2"),
                    lvl3 = map.getString("lvl3"),
                    lvl4 = map.getString("lvl4"),
                    kbd = map.getString("kbd"),
                    words = words,
                    frequency = frequency,
                    sort = map.getInt("sort"),
                    type = type
                )
            )
        }
        return list
    }

    companion object {
        private const val chunkSize = 10_000
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
