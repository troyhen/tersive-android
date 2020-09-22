package com.troy.tersive.model.db.user

import android.app.Application
import androidx.room.Room
import org.dbtools.android.room.CloseableDatabaseWrapper
import org.dbtools.android.room.sqliteorg.SqliteOrgSQLiteOpenHelperFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDatabaseManager @Inject constructor(private val app: Application) :
    CloseableDatabaseWrapper<UserDatabase>(app) {

    val userDb: UserDatabase get() = getDatabase()

    override fun createDatabase(): UserDatabase {
        return Room.databaseBuilder(app, UserDatabase::class.java, DATABASE_NAME)
            .openHelperFactory(SqliteOrgSQLiteOpenHelperFactory())
            .addMigrations(*UserDatabase.migrations)
            .addCallback(UserDatabase.Callback)
            .build()
    }

    companion object {
        const val DATABASE_NAME = "user.db"
    }
}
