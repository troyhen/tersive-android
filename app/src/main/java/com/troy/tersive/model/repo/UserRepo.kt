package com.troy.tersive.model.repo

import androidx.annotation.WorkerThread
import com.troy.tersive.mgr.UserDatabaseManager
import com.troy.tersive.model.data.HashUtil
import com.troy.tersive.model.db.user.entity.User
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@WorkerThread
class UserRepo @Inject constructor(
    private val hashUtil: HashUtil,
    private val userDatabaseManager: UserDatabaseManager
) {

    var user: User? = null
        private set

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
            user = foundUser
            true
        } else false
    }

    fun logout() {
        user = null
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
            user = newUser
            userDb.setTransactionSuccessful()
        } finally {
            userDb.endTransaction()
        }
        return true
    }
}
