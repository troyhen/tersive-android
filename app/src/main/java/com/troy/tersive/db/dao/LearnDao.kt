package com.troy.tersive.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.troy.tersive.db.entity.Learn

@Dao
interface LearnDao : BaseDao<Learn> {

    @Query("select * from Learn where userIndex = :userIndex and (time1 = 0 or time1 >= :time) order by sort1 limit 1 offset :index ")
    fun findNext1(userIndex: Int, index: Int, time: Long): LiveData<Learn>

    @Query("select * from Learn where userIndex = :userIndex and (time2 = 0 or time2 >= :time) order by sort2 limit 1 offset :index ")
    fun findNext2(userIndex: Int, index: Int, time: Long): LiveData<Learn>

    @Query("update Learn set sort1 = sort1 - 1 where userIndex = :userIndex and sort1 between :from and :to")
    fun shiftSort1(userIndex: Int, from: Int, to: Int)

    @Query("update Learn set sort2 = sort2 - 1 where userIndex = :userIndex and sort2 between :from and :to")
    fun shiftSort2(userIndex: Int, from: Int, to: Int)
}
