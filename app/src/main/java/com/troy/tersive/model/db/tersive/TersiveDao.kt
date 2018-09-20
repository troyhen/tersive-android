package com.troy.tersive.model.db.tersive

import androidx.room.Dao
import androidx.room.Query
import androidx.sqlite.db.SupportSQLiteDatabase
import com.troy.tersive.model.data.TersiveLearn
import com.troy.tersive.model.db.BaseDao

@Dao
interface TersiveDao : BaseDao<Tersive> {

    @Query("select * from Tersive where lvl1 = :lvl1 and kbd = :kbd")
    fun find(lvl1: String, kbd: String): Tersive

    @Query(
        """select lvl4, kbd, (words > 1) + (frequency < 0) * 2 as type
		from Tersive
		where words > 0 and lvl4 is not null and kbd is not null
		group by lvl4, kbd, type
		order by max(frequency) desc, kbd, lvl4"""
    )
    fun findUserList(): List<TersiveLearn>

    companion object {
        fun onCreate(db: SupportSQLiteDatabase) {
            PopulateTersive().populate(db)
        }

        fun onOpen(db: SupportSQLiteDatabase) {
            if (BaseDao.countRecords(db, "Tersive") == 0L) {
                onCreate(db)
            }
        }
    }
}
