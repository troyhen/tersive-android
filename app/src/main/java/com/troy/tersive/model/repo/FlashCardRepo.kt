package com.troy.tersive.model.repo

import androidx.annotation.WorkerThread
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.mgr.TersiveDatabaseManager
import com.troy.tersive.mgr.UserDatabaseManager
import com.troy.tersive.model.data.Card
import com.troy.tersive.model.db.user.entity.Learn.Companion.BACK
import com.troy.tersive.model.db.user.entity.Learn.Companion.FRONT
import com.troy.tersive.model.db.user.entity.Learn.Companion.KEY
import com.troy.tersive.model.db.user.entity.Learn.Companion.NONRELIGIOUS
import com.troy.tersive.model.db.user.entity.Learn.Companion.PHRASE
import com.troy.tersive.model.db.user.entity.Learn.Companion.RELIGIOUS
import com.troy.tersive.model.db.user.entity.Learn.Companion.SCRIPT
import com.troy.tersive.model.db.user.entity.Learn.Companion.WORD
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
    private val prefs: Prefs,
    private val tdm: TersiveDatabaseManager,
    private val udm: UserDatabaseManager,
    private val userRepo: UserRepo
) {
    private val rnd = Random()

    fun moveCard(card: Card, delta: Int, delay: Duration, easy: Int, tries: Int) {
        val user = userRepo.user ?: return
        val now = LocalDateTime.now(clock)
        val time = now + delay
        val oldSort = card.learn.sort
        val newSort = oldSort + delta
        udm.userDb.learnDao.shiftSort(user.index, card.learn.flags, oldSort + 1, newSort)
        udm.userDb.learnDao.save(
            card.learn.copy(sort = newSort, time = time, easy = easy, tries = tries)
        )
    }

    fun nextCard(type: Type, side: Side): Card {
        val user = userRepo.user!!.index
        val index = rnd.nextInt(SESSION_COUNT)
        val time = clock.millis()
        val back = when (side) {
            Side.ANY -> rnd.nextBoolean()
            Side.FRONT_ONLY -> false
            Side.BACK_ONLY -> true
        }
        val phrase = when (type) {
            FlashCardRepo.Type.ANY,
            FlashCardRepo.Type.RELIGIOUS_ONLY -> rnd.nextBoolean()
            FlashCardRepo.Type.WORD_ONLY -> false
            FlashCardRepo.Type.PHRASE_ONLY -> true
        }
        val religious = type == FlashCardRepo.Type.RELIGIOUS_ONLY
        val key = prefs.typeMode
        val tersiveType =
            (if (phrase) PHRASE else WORD) or (if (religious) RELIGIOUS else NONRELIGIOUS)
        val flags = tersiveType or (if (back) BACK else FRONT) or (if (key) KEY else SCRIPT)
        val learn = udm.userDb.learnDao.findNext(user, flags, index, time)
        val tersiveList = if (key) tdm.tersiveDb.tersiveDao.findKbdMatches(
            learn.tersive,
            tersiveType
        ) else tdm.tersiveDb.tersiveDao.findLvl4Matches(learn.tersive, tersiveType)
        return Card(back.not(), index, learn, tersiveList)
    }

    companion object {
        const val SESSION_COUNT = 20
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
