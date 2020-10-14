package com.troy.tersive.model.db.tersive

import android.app.Application
import androidx.room.Room
import org.dbtools.android.room.CloseableDatabaseWrapper

class TersiveDatabaseManager(private val app: Application) : CloseableDatabaseWrapper<TersiveDatabase>(app) {

    val tersiveDb: TersiveDatabase get() = getDatabase()

    override fun createDatabase(): TersiveDatabase {
        return Room.databaseBuilder(app, TersiveDatabase::class.java, DATABASE_NAME)
            .addMigrations(*TersiveDatabase.migrations)
            .addCallback(TersiveDatabase.Callback)
            .build()
    }

    companion object {
        const val DATABASE_NAME = "tersive.db"
    }
}
