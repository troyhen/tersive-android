package com.troy.tersive.model.repo

import androidx.annotation.WorkerThread
import com.troy.tersive.mgr.TersiveDatabaseManager
import com.troy.tersive.mgr.UserDatabaseManager
import com.troy.tersive.model.data.Card
import org.threeten.bp.Clock
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
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

    fun moveCard(card: Card, delta: Int, delay: Duration, easy: Int, tries: Int) {
        val user = userRepo.user ?: return
        val now = LocalDateTime.now(clock)
        val time = now + delay
        if (card.front) {
            val oldSort = card.learn.sort1
            val newSort = oldSort + delta
            udm.userDb.learnDao.shiftSort1(user.index, oldSort + 1, newSort)
            udm.userDb.learnDao.save(
                card.learn.copy(
                    sort1 = newSort,
                    time1 = time,
                    easy1 = easy,
                    tries1 = tries
                )
            )
        } else {
            val oldSort = card.learn.sort2
            val newSort = oldSort + delta
            udm.userDb.learnDao.shiftSort2(user.index, oldSort + 1, newSort)
            udm.userDb.learnDao.save(
                card.learn.copy(
                    sort2 = newSort,
                    time2 = time,
                    easy2 = easy,
                    tries2 = tries
                )
            )
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
        val tersiveList = tdm.tersiveDb.tersiveDao.findMatches(learn.lvl4, learn.kbd, learn.type)
        return Card(front, index, learn, tersiveList)
    }

    fun shiftCard(card: Card, result: Result) {
        val user = userRepo.user!!.index
        val newIndex = card.index + result.sortAdd
        val now = LocalDateTime.now(clock)
        val newTime = now + result.timeAdd
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

    enum class Result(val sortAdd: Int, val timeAdd: Duration) {
        EASY(160, Duration.ofDays(4)),
        GOOD(80, Duration.ofDays(1)),
        HARD(40, Duration.ofHours(2)),
        AGAIN(20, Duration.ofMinutes(15))
    }
}
