package com.troy.tersive.model.db.tersive

import androidx.room.Dao
import androidx.room.Query
import androidx.sqlite.db.SupportSQLiteDatabase
import com.troy.tersive.model.data.TersiveLearn
import com.troy.tersive.model.db.BaseDao

@Dao
interface TersiveDao : BaseDao<Tersive> {

    @Query("select * from Tersive order by id")
    suspend fun findAll(): List<Tersive>

    @Query("select * from Tersive where lvl4 = :lvl4 and type = :type order by length(phrase), phrase")
    suspend fun findLvl4Matches(lvl4: String, type: Int): List<Tersive>

    @Query("select * from Tersive where kbd = :kbd and type = :type order by length(phrase), phrase")
    suspend fun findKbdMatches(kbd: String, type: Int): List<Tersive>

    @Query(
        """select lvl4, kbd, (words > 1) + (frequency < 0) * 2 as type
		from Tersive
		where words > 0 and lvl4 is not null and kbd is not null
		group by lvl4, kbd, type
		order by max(frequency) desc, kbd, lvl4"""
    )
    suspend fun findUserList(): List<TersiveLearn>

    companion object {
        suspend fun onCreate(db: SupportSQLiteDatabase) {
            PopulateTersive().populate(db)
        }

        suspend fun onOpen(db: SupportSQLiteDatabase) {
            if (BaseDao.isTableEmpty(db, "Tersive")) {
                onCreate(db)
            }
        }
    }
}
