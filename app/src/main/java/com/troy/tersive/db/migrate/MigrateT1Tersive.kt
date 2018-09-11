package com.troy.tersive.db.migrate

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AddTersive : Migration(0, 1) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `Tersive` (`id` TEXT NOT NULL, `questionId` TEXT NOT NULL, `answer` TEXT, PRIMARY KEY(`goalId`, `questionId`))")
    }
}
