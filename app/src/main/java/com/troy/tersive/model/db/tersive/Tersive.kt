package com.troy.tersive.model.db.tersive

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Tersive(
    @PrimaryKey
    val id: Long,
    val phrase: String,
    val lvl1: String?,
    val lvl2: String?,
    val lvl3: String?,
    val lvl4: String?,
    val kbd: String?,
    val words: Int,
    val frequency: Int,
    val sort: Int?,
    val type: Int
)
