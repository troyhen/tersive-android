package com.troy.tersive.model.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import com.troy.tersive.model.db.BaseDao
import com.troy.tersive.model.db.user.entity.User

@Dao
interface UserDao : BaseDao<User> {
    @Query("select max(`index`) from User")
    fun findNextIndex(): Int?

    @Query("select * from User where email = :email")
    fun findUser(email: String): User?
}
