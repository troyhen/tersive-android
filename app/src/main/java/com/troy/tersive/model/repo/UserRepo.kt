package com.troy.tersive.model.repo

import androidx.annotation.WorkerThread
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.model.db.tersive.TersiveDatabaseManager
import com.troy.tersive.model.db.user.UserDatabaseManager
import com.troy.tersive.model.db.user.entity.Learn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@WorkerThread
class UserRepo @Inject constructor(
    private val prefs: Prefs,
    private val tersiveDatabaseManager: TersiveDatabaseManager,
    private val userDatabaseManager: UserDatabaseManager
) {
    private val auth by lazy { FirebaseAuth.getInstance() }
    val isLoggedIn get() = user != null
    val user get() = auth.currentUser

    fun autoLogin() {
        if (isLoggedIn) return
        //todo is this possible with Firebase?
    }

    fun login(user: FirebaseUser) {
        prefs.userId = user.uid
        val userDb = userDatabaseManager.userDb
        if (userDb.learnDao.countUser(user.uid) == 0) {
            initUser(user)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun initUser(newUser: FirebaseUser) {
        val userDb = userDatabaseManager.userDb
        userDb.beginTransaction()
        try {
            val sorts = arrayOf(1, 1, 1, 1)
            tersiveDatabaseManager.tersiveDb.tersiveDao.findUserList().forEach { tl ->
                val tersiveType = tl.type
                val sort = sorts[tersiveType] + 1
                sorts[tersiveType] = sort
                Learn(
                    userId = newUser.uid,
                    flags = tersiveType or Learn.FRONT or Learn.SCRIPT,
                    tersive = tl.lvl4,
                    sort = sort
                ).also { userDb.learnDao.save(it) }
                Learn(
                    userId = newUser.uid,
                    flags = tersiveType or Learn.BACK or Learn.SCRIPT,
                    tersive = tl.lvl4,
                    sort = sort
                ).also { userDb.learnDao.save(it) }
                Learn(
                    userId = newUser.uid,
                    flags = tersiveType or Learn.FRONT or Learn.KEY,
                    tersive = tl.kbd,
                    sort = sort
                ).also { userDb.learnDao.save(it) }
                Learn(
                    userId = newUser.uid,
                    flags = tersiveType or Learn.BACK or Learn.KEY,
                    tersive = tl.kbd,
                    sort = sort
                ).also { userDb.learnDao.save(it) }
            }
            userDb.setTransactionSuccessful()
        } finally {
            userDb.endTransaction()
        }
    }
}
