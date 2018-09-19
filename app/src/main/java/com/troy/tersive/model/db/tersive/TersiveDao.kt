package com.troy.tersive.model.db.tersive

import androidx.room.Dao
import androidx.room.Query
import androidx.sqlite.db.SupportSQLiteDatabase
import com.troy.tersive.model.db.BaseDao

@Dao
interface TersiveDao : BaseDao<Tersive> {

    @Query("select * from Tersive where lvl1 = :lvl1 and kbd = :kbd")
    fun find(lvl1: String, kbd: String): Tersive

    companion object {
        fun onCreate(db: SupportSQLiteDatabase) {
            PopulateTersive().populate(db)
        }

        fun onOpen(db: SupportSQLiteDatabase) {
        }
    }
}
