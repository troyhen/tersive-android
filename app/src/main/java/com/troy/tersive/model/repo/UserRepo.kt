package com.troy.tersive.model.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.troy.tersive.model.db.tersive.TersiveDatabaseManager
import com.troy.tersive.model.db.user.UserDatabaseManager
import com.troy.tersive.model.db.user.entity.Learn
import com.troy.tersive.model.prefs.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class UserRepo(
    private val prefs: Prefs,
    private val tersiveDatabaseManager: TersiveDatabaseManager,
    private val userDatabaseManager: UserDatabaseManager
) {
    private val auth = FirebaseAuth.getInstance()
    val userFlow = MutableStateFlow(auth.currentUser)
    val isLoggedIn get() = userFlow.value != null
    val isLoggedInFlow get() = userFlow.map { it != null }

    init {
        auth.addAuthStateListener { auth ->
            userFlow.value = auth.currentUser
        }
    }
//    fun autoLogin() {
//        if (isLoggedIn) return
//        //todo is this possible with Firebase?
//    }

    suspend fun login(user: FirebaseUser) {
        prefs.userId = user.uid
        val userDb = userDatabaseManager.userDb
        if (userDb.learnDao.countUser(user.uid) == 0) {
            initUser(user)
        }
    }

    fun logout() {
        auth.signOut()
    }

    private suspend fun initUser(newUser: FirebaseUser) = withContext(Dispatchers.IO) {
        val userDb = userDatabaseManager.userDb
        userDb.runInTransaction {
            val sorts = arrayOf(1, 1, 1, 1)
            runBlocking(Dispatchers.IO) { tersiveDatabaseManager.tersiveDb.tersiveDao.findUserList() }.forEach { tl ->
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
        }
    }
}
