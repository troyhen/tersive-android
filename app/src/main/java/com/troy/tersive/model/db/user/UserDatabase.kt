package com.troy.tersive.model.db.user

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.troy.tersive.model.db.convert.ConvertDateTime
import com.troy.tersive.model.db.convert.ConvertUUID
import com.troy.tersive.model.db.user.dao.LearnDao
import com.troy.tersive.model.db.user.entity.Learn
import com.troy.tersive.model.db.user.migrate.AddInitial

@Database(
    version = 2, entities = [
        /*  2 */ Learn::class
    ]
)
@TypeConverters(ConvertDateTime::class, ConvertUUID::class)
abstract class UserDatabase : RoomDatabase() {

    abstract val learnDao: LearnDao

    companion object {
        val migrations get() = arrayOf<Migration>(AddInitial)
    }

    object Callback : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            db.beginTransaction()
            try {
//                TabDao.onCreate(db)
//                TagDao.onCreate(db)
//                QuestionDao.onCreate(db)
//                AlbumDao.onCreate(db)
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }

        override fun onOpen(db: SupportSQLiteDatabase) {
            db.beginTransaction()
            try {
//                TabDao.onOpen(db)
//                TagDao.onOpen(db)
//                QuestionDao.onOpen(db)
//                AlbumDao.onOpen(db)
                db.setTransactionSuccessful()
            } finally {
                db.endTransaction()
            }
        }
    }
}
