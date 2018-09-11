package com.troy.tersive.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Learn(
    @PrimaryKey
    val id: UUID,
    val userIndex: Int,
    val type: String,
    val lvl: String,
    val kbd: String,
    val priority1: Int,
    val priority2: Int,
    val tries1: Int,
    val tries2: Int
)
