package com.troy.tersive.model.db.user.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime
import java.util.UUID

@Entity
data class User(
    @PrimaryKey
    val id: UUID,
    val index: Int,
    val email: String,
    val passHash: String,
    val lastLogin: LocalDateTime? = null
)
