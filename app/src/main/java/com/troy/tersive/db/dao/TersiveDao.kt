package com.troy.tersive.db.dao

import androidx.room.Dao
import androidx.sqlite.db.SupportSQLiteDatabase
import com.troy.tersive.db.entity.Tersive

@Dao
interface TersiveDao : BaseDao<Tersive> {

    companion object {
        fun onCreate(db: SupportSQLiteDatabase) {
        }

        fun onOpen(db: SupportSQLiteDatabase) {
        }
    }
}
