package com.troy.tersive.model.db.tersive

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object AddTersive : Migration(0, 1) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS `Tersive` (
            |`id` INTEGER NOT NULL PRIMARY KEY,
            |`phrase` TEXT NOT NULL,
            |`lvl1` TEXT,
            |`lvl2` TEXT,
            |`lvl3` TEXT,
            |`lvl4` TEXT,
            |`kbd` TEXT,
            |`words` INTEGER NOT NULL,
            |`frequency` INTEGER NOT NULL,
            |`sort` INTEGER,
            |`type` INTEGER NOT NULL
            |))""".trimMargin()
        )
    }
}
