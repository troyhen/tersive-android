package com.troy.tersive.model.repo

import androidx.annotation.WorkerThread
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.mgr.Prefs.Companion.NO_USER
import com.troy.tersive.mgr.TersiveDatabaseManager
import com.troy.tersive.mgr.UserDatabaseManager
import com.troy.tersive.model.data.HashUtil
import com.troy.tersive.model.db.user.entity.Learn
import com.troy.tersive.model.db.user.entity.User
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import org.lds.mobile.coroutine.CoroutineContextProvider
import org.threeten.bp.Clock
import org.threeten.bp.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@WorkerThread
@Singleton
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
        val username = prefs.username
        if (username != NO_USER) {
            GlobalScope.launch(cc.default) {
                onLogin(dbManager.userDb.userDao.findUser(username))
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
        prefs.username = foundUser?.email ?: NO_USER
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
            tersiveDatabaseManager.tersiveDb.tersiveDao.findUserList().forEachIndexed { index, tl ->
                val learn = Learn(
                    userIndex = newUser.index,
                    type = tl.type,
                    lvl4 = tl.lvl4,
                    kbd = tl.kbd,
                    sort1 = index + 1
                )
                userDb.learnDao.save(learn)
            }
            userDb.setTransactionSuccessful()
        } finally {
            userDb.endTransaction()
        }
        return true
    }
}
