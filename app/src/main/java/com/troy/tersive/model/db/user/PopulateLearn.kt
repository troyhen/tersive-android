package com.troy.tersive.model.db.user

import android.app.Application
import androidx.sqlite.db.SupportSQLiteDatabase
import com.troy.tersive.app.Injector
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class PopulateLearn {

    @Inject
    lateinit var app: Application

    init {
        Injector.get().inject(this)
    }

    fun populate(db: SupportSQLiteDatabase) {
        BufferedReader(InputStreamReader(app.assets.open("dump_tersive.sql"))).use { reader ->
            while (true) {
                val line = reader.readLine() ?: break
                db.execSQL(line)
            }
        }
    }
}
