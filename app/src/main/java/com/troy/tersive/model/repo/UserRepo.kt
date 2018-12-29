package com.troy.tersive.model.repo

import androidx.annotation.WorkerThread
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.mgr.Prefs.Companion.NO_USER
import com.troy.tersive.model.data.HashUtil
import com.troy.tersive.model.db.tersive.TersiveDatabaseManager
import com.troy.tersive.model.db.user.UserDatabaseManager
import com.troy.tersive.model.db.user.entity.Learn
import com.troy.tersive.model.db.user.entity.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.lds.mobile.coroutine.CoroutineContextProvider
import org.threeten.bp.Clock
import org.threeten.bp.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@WorkerThread
class UserRepo @Inject constructor(
    private val cc: CoroutineContextProvider,
    private val clock: Clock,
    private val dbManager: UserDatabaseManager,
    private val hashUtil: HashUtil,
    private val prefs: Prefs,
    private val tersiveDatabaseManager: TersiveDatabaseManager,
    private val userDatabaseManager: UserDatabaseManager
) {
    val isLoggedIn get() = user != null
    var user: User? = null
        private set

    fun autoLogin() {
        if (isLoggedIn) return
        val userId = prefs.userId
        if (userId != NO_USER) {
            GlobalScope.launch(cc.default) {
                onLogin(dbManager.userDb.userDao.findUser(userId))
            }
        }
    }

    fun changePassword(oldPassword: String, password: String): Boolean {
        val user = user ?: return false
        val oldHash = hashUtil.hash(oldPassword)
        if (oldHash != user.passHash) return false
        val passHash = hashUtil.hash(password)
        val userDb = userDatabaseManager.userDb
        val newUser = user.copy(passHash = passHash)
        userDb.userDao.save(newUser)
        this.user = newUser
        return true
    }

    fun login(email: String, password: String): Boolean {
        val userDb = userDatabaseManager.userDb
        val foundUser = userDb.userDao.findUser(email) ?: return false
        val passHash = hashUtil.hash(password)
        return if (passHash == foundUser.passHash) {
            onLogin(foundUser)
            true
        } else false
    }

    fun logout() {
        onLogin(null)
    }

    private fun onLogin(foundUser: User?) {
        val now = LocalDateTime.now(clock)
        user = foundUser?.copy(lastLogin = now)
        prefs.userId = foundUser?.email ?: NO_USER
        if (user != null) userDatabaseManager.userDb.userDao.save(user!!)
    }

    fun register(email: String, password: String): Boolean {
        val userDb = userDatabaseManager.userDb
        userDb.beginTransaction()
        try {
            val oldUser = userDb.userDao.findUser(email)
            if (oldUser != null) return false
            val lastIndex = userDb.userDao.findNextIndex() ?: 0
            val passHash = hashUtil.hash(password)
            val newUser = User(UUID.randomUUID(), lastIndex + 1, email, passHash)
            userDb.userDao.save(newUser)
            onLogin(newUser)
            val sorts = arrayOf(1, 1, 1, 1)
            tersiveDatabaseManager.tersiveDb.tersiveDao.findUserList().forEach { tl ->
                val tersiveType = tl.type
                val sort = sorts[tersiveType] + 1
                sorts[tersiveType] = sort
                Learn(
                    userIndex = newUser.index,
                    flags = tersiveType or Learn.FRONT or Learn.SCRIPT,
                    tersive = tl.lvl4,
                    sort = sort
                ).also { userDb.learnDao.save(it) }
                Learn(
                    userIndex = newUser.index,
                    flags = tersiveType or Learn.BACK or Learn.SCRIPT,
                    tersive = tl.lvl4,
                    sort = sort
                ).also { userDb.learnDao.save(it) }
                Learn(
                    userIndex = newUser.index,
                    flags = tersiveType or Learn.FRONT or Learn.KEY,
                    tersive = tl.kbd,
                    sort = sort
                ).also { userDb.learnDao.save(it) }
                Learn(
                    userIndex = newUser.index,
                    flags = tersiveType or Learn.BACK or Learn.KEY,
                    tersive = tl.kbd,
                    sort = sort
                ).also { userDb.learnDao.save(it) }
            }
            userDb.setTransactionSuccessful()
        } finally {
            userDb.endTransaction()
        }
        return true
    }
}
