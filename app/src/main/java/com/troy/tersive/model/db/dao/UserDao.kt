package com.troy.tersive.model.db.dao

import androidx.room.Dao
import com.troy.tersive.model.db.entity.User

@Dao
interface UserDao : BaseDao<User> {
}
