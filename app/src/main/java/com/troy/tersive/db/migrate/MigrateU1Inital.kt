package com.troy.tersive.db.migrate

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AddInitial : Migration(0, 1) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `User` (`id` TEXT NOT NULL PRIMARY KEY, `index` INTEGER NOT NULL, `email` TEXT NOT NULL, `salt` INTEGER NOT NULL, `passHash` INTEGER NOT NULL)")
        db.execSQL("CREATE TABLE IF NOT EXISTS `Learn` (`id` TEXT NOT NULL, `questionId` TEXT NOT NULL, `answer` TEXT, PRIMARY KEY(`goalId`, `questionId`))")
    }
}
