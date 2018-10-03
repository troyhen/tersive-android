package com.troy.tersive.model.db.user.dao

import androidx.room.Dao
import androidx.room.Query
import com.troy.tersive.model.db.BaseDao
import com.troy.tersive.model.db.user.entity.Learn

@Dao
interface LearnDao : BaseDao<Learn> {

    @Query(
        """
        select *
        from Learn
        where userIndex = :userIndex
            and flags = :flags
            and (time is null or time >= :time)
        order by sort
        limit 1
        offset :index"""
    )
    fun findNext(userIndex: Int, flags: Int, index: Int, time: Long): Learn

    @Query(
        """
        update Learn
        set sort = sort - 1
        where userIndex = :userIndex
            and flags = :flags
            and sort between :from and :to"""
    )
    fun shiftSort(userIndex: Int, flags: Int, from: Int, to: Int)
}
