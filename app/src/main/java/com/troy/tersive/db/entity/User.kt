package com.troy.tersive.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class User(
    @PrimaryKey
    val id: UUID,
    val index: Int,
    val email: String,
    val salt: Long,
    val passHash: Long
)
