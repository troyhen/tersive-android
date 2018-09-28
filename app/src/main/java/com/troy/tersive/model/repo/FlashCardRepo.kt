package com.troy.tersive.model.repo

import androidx.annotation.WorkerThread
import com.troy.tersive.mgr.TersiveDatabaseManager
import com.troy.tersive.mgr.UserDatabaseManager
import com.troy.tersive.model.data.Card
import org.threeten.bp.Clock
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@WorkerThread
class FlashCardRepo @Inject constructor(
    private val clock: Clock,
    private val tdm: TersiveDatabaseManager,
    private val udm: UserDatabaseManager,
    private val userRepo: UserRepo
) {
    private val rnd = Random()

    fun moveCard(card: Card, delta: Int, tries: Int) {
        val user = userRepo.user ?: return
        if (card.front) {
            val oldSort = card.learn.sort1
            val newSort = oldSort + delta
            udm.userDb.learnDao.shiftSort1(user.index, oldSort + 1, newSort)
            udm.userDb.learnDao.save(card.learn.copy(sort1 = newSort, tries1 = tries))
        } else {
            val oldSort = card.learn.sort2
            val newSort = oldSort + delta
            udm.userDb.learnDao.shiftSort2(user.index, oldSort + 1, newSort)
            udm.userDb.learnDao.save(card.learn.copy(sort2 = newSort, tries2 = tries))
        }
    }

    fun nextCard(type: Type, side: Side): Card {
        val user = userRepo.user!!.index
        val index = rnd.nextInt(SESSION_COUNT)
        val time = clock.millis()
        val front = when (side) {
            Side.ANY -> rnd.nextBoolean()
            Side.FRONT_ONLY -> false
            Side.BACK_ONLY -> true
        }
        val learn = if (front) {
            when (type) {
                Type.ANY -> udm.userDb.learnDao.findNext1(user, index, time)
                Type.WORD_ONLY -> udm.userDb.learnDao.findNextTyped1(user, WORD_TYPE, index, time)
                Type.PHRASE_ONLY -> udm.userDb.learnDao.findNextTyped1(
                    user,
                    PHRASE_TYPE,
                    index,
                    time
                )
                Type.RELIGIOUS_ONLY -> udm.userDb.learnDao.findNextReligious1(user, index, time)
            }
        } else {
            when (type) {
                Type.ANY -> udm.userDb.learnDao.findNext2(user, index, time)
                Type.WORD_ONLY -> udm.userDb.learnDao.findNextTyped2(user, WORD_TYPE, index, time)
                Type.PHRASE_ONLY -> udm.userDb.learnDao.findNextTyped2(
                    user,
                    PHRASE_TYPE,
                    index,
                    time
                )
                Type.RELIGIOUS_ONLY -> udm.userDb.learnDao.findNextReligious2(user, index, time)
            }
        }
        val tersiveList = tdm.tersiveDb.tersiveDao.findMatches(learn.lvl4, learn.kbd)
        return Card(front, index, learn, tersiveList)
    }

    fun shiftCard(card: Card, result: Result) {
        val user = userRepo.user!!.index
        val newIndex = card.index + result.sortAdd
        val newTime = clock.millis() + result.timeAdd
        if (card.front) {
            udm.userDb.learnDao.shiftSort1(user, card.index, newIndex)
            udm.userDb.learnDao.save(card.learn.copy(sort1 = newIndex, time1 = newTime))
        } else {
            udm.userDb.learnDao.shiftSort2(user, card.index, newIndex)
            udm.userDb.learnDao.save(card.learn.copy(sort2 = newIndex, time2 = newTime))
        }
    }

    companion object {
        const val SESSION_COUNT = 20
        const val MINUTES = 60 * 1000
        const val HOURS = 60 * MINUTES
        const val DAYS = 24 * HOURS
        const val WORD_TYPE = 0
        const val PHRASE_TYPE = 1
        const val RELIGIOUS_WORD_TYPE = 2
        const val RELIGIOUS_PHRASE_TYPE = 3
    }

    enum class Side {
        ANY, FRONT_ONLY, BACK_ONLY
    }

    enum class Type {
        ANY, WORD_ONLY, PHRASE_ONLY, RELIGIOUS_ONLY
    }

    enum class Result(val sortAdd: Int, val timeAdd: Int) {
        EASY(160, 7 * DAYS), GOOD(80, 1 * DAYS), HARD(40, 1 * HOURS), AGAIN(20, 10 * MINUTES)
    }
}
