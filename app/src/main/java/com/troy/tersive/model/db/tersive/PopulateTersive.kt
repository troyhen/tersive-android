package com.troy.tersive.model.db.tersive

import androidx.sqlite.db.SupportSQLiteDatabase
import com.troy.tersive.app.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class PopulateTersive {

    suspend fun populate(db: SupportSQLiteDatabase) {
        withContext(Dispatchers.IO) { BufferedReader(InputStreamReader(App.app.assets.open("dump_tersive.sql"))) }.use { reader ->
            while (true) {
                val line = withContext(Dispatchers.IO) { reader.readLine() } ?: break
                db.execSQL(line)
            }
        }
    }
}
