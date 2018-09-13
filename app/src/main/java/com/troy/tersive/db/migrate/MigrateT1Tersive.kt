package com.troy.tersive.db.migrate

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AddTersive : Migration(0, 1) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS `Tersive` (
            |`id` INTEGER NOT NULL PRIMARY KEY,
            |`phrase` TEXT NOT NULL,
            |`lvl1` TEXT NOT NULL,
            |`lvl2` TEXT NOT NULL,
            |`lvl3` TEXT NOT NULL,
            |`lvl4` TEXT NOT NULL,
            |`kbd` TEXT NOT NULL,
            |`words` INTEGER NOT NULL,
            |`frequency` INTEGER NOT NULL,
            |`sort` INTEGER NOT NULL,
            |`type` INTEGER NOT NULL
            |))""".trimMargin()
        )
    }
}
