package com.troy.tersive.model.db.user.migrate

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AddInitial : Migration(0, 1) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS `User` (
            |`id` TEXT NOT NULL PRIMARY KEY,
            |`index` INTEGER NOT NULL,
            |`email` TEXT NOT NULL,
            |`salt` INTEGER NOT NULL,
            |`passHash` INTEGER NOT NULL
            |)""".trimMargin()
        )
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS `Learn` (
            |`id` INTEGER NOT NULL PRIMARY KEY,
            |`userIndex` INTEGER NOT NULL,
            |`type` TEXT NOT NULL,
            |`lvl4` TEXT NOT NULL,
            |`kbd` TEXT NOT NULL,
            |`sort1` INTEGER NOT NULL,
            |`sort2` INTEGER NOT NULL,
            |`time1` INTEGER NOT NULL,
            |`time2` INTEGER NOT NULL,
            |`easy1` INTEGER NOT NULL,
            |`easy2` INTEGER NOT NULL,
            |`tries1` INTEGER NOT NULL,
            |`tries2` INTEGER NOT NULL
            |)""".trimMargin()
        )
    }
}
