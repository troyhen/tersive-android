package com.troy.tersive.model.repo

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.troy.tersive.model.db.tersive.Datum
import com.troy.tersive.model.db.tersive.Tersive
import com.troy.tersive.model.db.tersive.TersiveDatabaseManager
import com.troy.tersive.model.repo.FirestoreConst.FIRST
import com.troy.tersive.model.repo.FirestoreConst.FREQUENCY
import com.troy.tersive.model.repo.FirestoreConst.ID
import com.troy.tersive.model.repo.FirestoreConst.KBD
import com.troy.tersive.model.repo.FirestoreConst.LANES
import com.troy.tersive.model.repo.FirestoreConst.LAST
import com.troy.tersive.model.repo.FirestoreConst.LVL1
import com.troy.tersive.model.repo.FirestoreConst.LVL2
import com.troy.tersive.model.repo.FirestoreConst.LVL3
import com.troy.tersive.model.repo.FirestoreConst.LVL4
import com.troy.tersive.model.repo.FirestoreConst.MODIFIED
import com.troy.tersive.model.repo.FirestoreConst.PHRASE
import com.troy.tersive.model.repo.FirestoreConst.SORT
import com.troy.tersive.model.repo.FirestoreConst.STAGE
import com.troy.tersive.model.repo.FirestoreConst.TERSIVE
import com.troy.tersive.model.repo.FirestoreConst.TERSIVE_CACHE
import com.troy.tersive.model.repo.FirestoreConst.TERSIVE_TIMESTAMP
import com.troy.tersive.model.repo.FirestoreConst.TYPE
import com.troy.tersive.model.repo.FirestoreConst.WORDS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant

class FirestoreRepo(
    private val tersiveDatabaseManager: TersiveDatabaseManager
) {

    private val db = FirebaseFirestore.getInstance()

    //    private val tersiveRef = db.collection(LANES).document(STAGE).collection(TERSIVE)
    private val tersiveCacheRef = db.collection(LANES).document(STAGE).collection(TERSIVE_CACHE)

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
            data[FIRST] = chunk.first().id
            data[LAST] = chunk.last().id
            data[MODIFIED] = FieldValue.serverTimestamp()
            chunk.forEach {
                tersive.add(it.toMap())
            }
            data[TERSIVE] = tersive
            tersiveCacheRef.document().set(data).addOnSuccessListener {
                Timber.d("Sent ${chunk.size} to tersiveCache")
            }.addOnFailureListener {
                Timber.e(it)
            }
            tersiveDatabaseManager.tersiveDb.datumDao.save(Datum(TERSIVE_TIMESTAMP, Instant.now().toString()))
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
                val tersiveList = doc.getTersiveList(TERSIVE)
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
            set(ID, id)
            set(PHRASE, phrase)
            lvl1?.let { set(LVL1, it) }
            lvl2?.let { set(LVL2, it) }
            lvl3?.let { set(LVL3, it) }
            lvl4?.let { set(LVL4, it) }
            kbd?.let { set(KBD, it) }
            set(WORDS, words)
            sort?.let { set(SORT, it) }
            set(TYPE, type)
        }
    }

    private fun List<Tersive>.delete() = tersiveDatabaseManager.tersiveDb.tersiveDao.delete(this)
    private fun List<Tersive>.save() = tersiveDatabaseManager.tersiveDb.tersiveDao.save(this)

    private fun Map<*, *>.getInt(key: String) = getLong(key)?.toInt()
    private fun Map<*, *>.getLong(key: String) = get(key) as? Long
    private fun Map<*, *>.getString(key: String) = get(key) as? String

    private fun Map<*, *>.toTersive(): Tersive? {
        val id = getLong(ID) ?: return null
        val phrase = getString(PHRASE) ?: return null
        val words = getInt(WORDS) ?: return null
        val frequency = getInt(FREQUENCY) ?: return null
        val type = getInt(TYPE) ?: return null
        return Tersive(
            id = id,
            phrase = phrase,
            lvl1 = getString(LVL1),
            lvl2 = getString(LVL2),
            lvl3 = getString(LVL3),
            lvl4 = getString(LVL4),
            kbd = getString(KBD),
            words = words,
            frequency = frequency,
            sort = (get(SORT) as? Long)?.toInt(),
            type = type
        )
    }

    private fun QueryDocumentSnapshot.getList(key: String) = get(key) as List<*>

    private fun QueryDocumentSnapshot.getTersiveList(key: String): List<Tersive> {
        val tersive = getList(key) ?: return emptyList()
        val list = ArrayList<Tersive>(tersive.size)
        tersive.forEach {
            val map = it as? Map<*, *> ?: return@forEach
            val id = map.getLong(ID) ?: return@forEach
            val phrase = map.getString(PHRASE) ?: return@forEach
            val words = map.getInt(WORDS) ?: return@forEach
            val frequency = map.getInt(FREQUENCY) ?: return@forEach
            val type = map.getInt(TYPE) ?: return@forEach
            list.add(
                Tersive(
                    id = id,
                    phrase = phrase,
                    lvl1 = map.getString(LVL1),
                    lvl2 = map.getString(LVL2),
                    lvl3 = map.getString(LVL3),
                    lvl4 = map.getString(LVL4),
                    kbd = map.getString(KBD),
                    words = words,
                    frequency = frequency,
                    sort = map.getInt(SORT),
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
