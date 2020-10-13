package com.troy.tersive.model.db.tersive

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity
data class Datum(
    @PrimaryKey
    val id: String,
    val value: String,
)

@Dao
interface DatumDao {

    @Query("select value from Datum where id = :key")
    suspend fun find(key: String): String?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(datum: Datum)
}
