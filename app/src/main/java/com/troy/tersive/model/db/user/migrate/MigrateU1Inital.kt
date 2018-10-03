package com.troy.tersive.model.db.user.migrate

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AddInitial : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS `User` (
            |`id` TEXT NOT NULL PRIMARY KEY,
            |`index` INTEGER NOT NULL,
            |`email` TEXT NOT NULL,
            |`salt` INTEGER NOT NULL,
            |`passHash` INTEGER NOT NULL,
            |`lastLogin` TEXT
            |)""".trimMargin()
        )
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS `Learn` (
            |`id` INTEGER NOT NULL PRIMARY KEY,
            |`userIndex` INTEGER NOT NULL,
            |`flags` INTEGER NOT NULL,
            |`tersive` TEXT NOT NULL,
            |`sort` INTEGER NOT NULL,
            |`time` TEXT,
            |`easy` INTEGER NOT NULL,
            |`tries` INTEGER NOT NULL
            |)""".trimMargin()
        )
        db.execSQL(
            """CREATE UNIQUE INDEX IF NOT EXISTS `LearnIndex1`
            | ON `Learn` (
            |`userIndex`, `flags`, `tersive`
            |)""".trimMargin()
        )
        db.execSQL(
            """CREATE INDEX IF NOT EXISTS `LearnIndex2`
            | ON `Learn` (
            |`sort`, `time`
            |)""".trimMargin()
        )
    }
}
