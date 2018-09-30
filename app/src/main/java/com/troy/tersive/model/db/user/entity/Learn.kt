package com.troy.tersive.model.db.user.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity
data class Learn(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userIndex: Int,
    val type: Int,
    val lvl4: String,
    val kbd: String,
    val sort1: Int,
    val sort2: Int = sort1,
    val time1: LocalDateTime? = null,
    val time2: LocalDateTime? = null,
    val easy1: Int = 0,
    val easy2: Int = 0,
    val tries1: Int = 0,
    val tries2: Int = 0
)
