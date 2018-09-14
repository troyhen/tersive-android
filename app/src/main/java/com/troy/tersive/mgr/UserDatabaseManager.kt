package com.troy.tersive.mgr

import android.app.Application
import androidx.room.Room
import com.troy.tersive.model.db.UserDatabase
import org.dbtools.android.room.CloseableDatabaseWrapper
import org.dbtools.android.room.sqliteorg.SqliteOrgSQLiteOpenHelperFactory

class UserDatabaseManager(application: Application) :
    CloseableDatabaseWrapper<UserDatabase>(application) {

    val userDb: UserDatabase get() = getDatabase()

    override fun createDatabase(): UserDatabase {
        return Room.databaseBuilder(application, UserDatabase::class.java, DATABASE_NAME)
            .openHelperFactory(SqliteOrgSQLiteOpenHelperFactory())
            .addMigrations(*UserDatabase.migrations)
            .addCallback(UserDatabase.Callback)
            .build()
    }

    companion object {
        const val DATABASE_NAME = "user.db"
    }
}
