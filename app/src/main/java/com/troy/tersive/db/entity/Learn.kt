package com.troy.tersive.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Learn(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val userIndex: Int,
    val type: Int,
    val lvl: String,
    val kbd: String,
    val sort1: Int,
    val sort2: Int,
    val time1: Long = 0L,
    val time2: Long = 0L,
    val tries1: Int = 0,
    val tries2: Int = 0
)
