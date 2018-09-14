package com.troy.tersive.mgr

import android.app.Application
import androidx.room.Room
import com.troy.tersive.model.db.TersiveDatabase
import org.dbtools.android.room.CloseableDatabaseWrapper
import org.dbtools.android.room.sqliteorg.SqliteOrgSQLiteOpenHelperFactory

class TersiveDatabaseManager(application: Application) :
    CloseableDatabaseWrapper<TersiveDatabase>(application) {

    val tersiveDb: TersiveDatabase get() = getDatabase()

    override fun createDatabase(): TersiveDatabase {
        return Room.databaseBuilder(
            application,
            TersiveDatabase::class.java,
            TersiveDatabaseManager.DATABASE_NAME
        )
            .openHelperFactory(SqliteOrgSQLiteOpenHelperFactory())
            .addMigrations(*TersiveDatabase.migrations)
            .addCallback(TersiveDatabase.Callback)
            .build()
    }

    companion object {
        const val DATABASE_NAME = "tersive.db"
    }
}
