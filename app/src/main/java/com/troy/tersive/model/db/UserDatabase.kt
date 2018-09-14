package com.troy.tersive.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.troy.tersive.model.db.convert.ConvertUUID
import com.troy.tersive.model.db.dao.LearnDao
import com.troy.tersive.model.db.dao.UserDao
import com.troy.tersive.model.db.entity.Learn
import com.troy.tersive.model.db.entity.User
import com.troy.tersive.model.db.migrate.AddInitial

@Database(
    version = 1, entities = [
        /*  1 */ User::class, Learn::class
    ]
)
@TypeConverters(ConvertUUID::class)
abstract class UserDatabase : RoomDatabase() {

    abstract val learnDao: LearnDao
    abstract val userDao: UserDao

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
