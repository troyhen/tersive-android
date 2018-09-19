package com.troy.tersive.model.db.tersive

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    version = 1, entities = [
        /*  1 */ Tersive::class
    ]
)
abstract class TersiveDatabase : RoomDatabase() {

    abstract val tersiveDao: TersiveDao

    companion object {
        val migrations get() = arrayOf<Migration>(AddTersive)
    }

    object Callback : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            db.beginTransaction()
            try {
                TersiveDao.onCreate(db)
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            db.beginTransaction()
            try {
                TersiveDao.onOpen(db)
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }
    }
}
