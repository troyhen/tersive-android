package com.troy.tersive.model.db.tersive

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.runBlocking
import org.dbtools.android.room.ext.runInTransaction

@Database(
    version = 2, entities = [
        /*  1 */
        Tersive::class,
        /*  2 */
        Datum::class,
    ]
)
abstract class TersiveDatabase : RoomDatabase() {

    abstract val datumDao: DatumDao
    abstract val tersiveDao: TersiveDao

    companion object {
        val migrations get() = arrayOf<Migration>()
    }

    object Callback : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            db.runInTransaction {
                runBlocking { TersiveDao.onCreate(db) }
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            db.runInTransaction {
                runBlocking { TersiveDao.onOpen(db) }
            }
        }
    }
}
