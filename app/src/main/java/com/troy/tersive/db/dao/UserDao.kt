package com.troy.tersive.db.dao

import androidx.room.Dao
import com.troy.tersive.db.entity.User

@Dao
interface UserDao : BaseDao<User> {
}
