package com.troy.tersive.model.db.user.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Learn(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val flags: Int,
    val tersive: String,
    val sort: Int,
    val time: LocalDateTime? = null,
    val easy: Int = 0,
    val tries: Int = 0
) {
    companion object {
        const val WORD = 0
        const val PHRASE = 1
        const val NONRELIGIOUS = 0
        const val RELIGIOUS = 2
        const val FRONT = 0
        const val BACK = 4
        const val SCRIPT = 0
        const val KEY = 8
    }
}
