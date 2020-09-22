package com.troy.tersive.model.db.tersive

import androidx.sqlite.db.SupportSQLiteDatabase
import com.troy.tersive.app.App
import java.io.BufferedReader
import java.io.InputStreamReader

class PopulateTersive {

    suspend fun populate(db: SupportSQLiteDatabase) {
        BufferedReader(InputStreamReader(App.app.assets.open("dump_tersive.sql"))).use { reader ->
            while (true) {
                val line = reader.readLine() ?: break
                db.execSQL(line)
            }
        }
    }
}
