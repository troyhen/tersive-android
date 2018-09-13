package com.troy.tersive.db.dao

import androidx.room.Dao
import androidx.sqlite.db.SupportSQLiteDatabase
import com.troy.tersive.db.entity.Tersive
import com.troy.tersive.db.migrate.PopulateTersive

@Dao
interface TersiveDao : BaseDao<Tersive> {

    companion object {
        fun onCreate(db: SupportSQLiteDatabase) {
            val populator = PopulateTersive().populate(db)
        }

        fun onOpen(db: SupportSQLiteDatabase) {
        }
    }
}
