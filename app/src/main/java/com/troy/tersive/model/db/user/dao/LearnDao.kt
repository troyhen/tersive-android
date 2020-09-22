package com.troy.tersive.model.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import com.troy.tersive.model.db.BaseDao
import com.troy.tersive.model.db.user.entity.Learn

@Dao
interface LearnDao : BaseDao<Learn> {

    @Query("select count(*) from Learn where userId = :userId")
    suspend fun countUser(userId: String): Int

    @Query("select max(sort) from Learn where flags = :flags")
    suspend fun findMaxSort(flags: Int): Int

    @Query(
        """
        select *
        from Learn
        where userId = :userId
            and flags = :flags
            and (time is null or time >= :time)
        order by sort
        limit 1
        offset :index"""
    )
    suspend fun findNext(userId: String, flags: Int, index: Int, time: Long): Learn?

    @Query(
        """
        update Learn
        set sort = sort - 1
        where userId = :userId
            and flags = :flags
            and sort between :from and :to"""
    )
    suspend fun shiftSort(userId: String, flags: Int, from: Int, to: Int)
}
