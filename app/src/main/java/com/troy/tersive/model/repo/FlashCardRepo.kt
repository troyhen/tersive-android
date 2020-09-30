package com.troy.tersive.model.repo

import androidx.annotation.WorkerThread
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.model.data.Card
import com.troy.tersive.model.db.tersive.TersiveDatabaseManager
import com.troy.tersive.model.db.user.UserDatabaseManager
import com.troy.tersive.model.db.user.entity.Learn.Companion.BACK
import com.troy.tersive.model.db.user.entity.Learn.Companion.FRONT
import com.troy.tersive.model.db.user.entity.Learn.Companion.KEY
import com.troy.tersive.model.db.user.entity.Learn.Companion.NONRELIGIOUS
import com.troy.tersive.model.db.user.entity.Learn.Companion.PHRASE
import com.troy.tersive.model.db.user.entity.Learn.Companion.RELIGIOUS
import com.troy.tersive.model.db.user.entity.Learn.Companion.SCRIPT
import com.troy.tersive.model.db.user.entity.Learn.Companion.WORD
import java.time.Clock
import java.time.Duration
import java.time.LocalDateTime
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@WorkerThread
class FlashCardRepo @Inject constructor(
    private val prefs: Prefs,
    private val tdm: TersiveDatabaseManager,
    private val udm: UserDatabaseManager,
    private val userRepo: UserRepo
) {
    private val rnd = Random()

    init {
        tdm.tersiveDb
    }

    suspend fun maxSort(type: Int) = udm.userDb.learnDao.findMaxSort(type)

    suspend fun moveCard(card: Card, delta: Int, delay: Duration, easy: Int, tries: Int) {
        val user = userRepo.userFlow.value ?: return
        val now = LocalDateTime.now()
        val time = now + delay
        val oldSort = card.learn.sort
        val newSort = oldSort + delta
        udm.userDb.learnDao.shiftSort(user.uid, card.learn.flags, oldSort + 1, newSort)
        udm.userDb.learnDao.save(
            card.learn.copy(sort = newSort, time = time, easy = easy, tries = tries)
        )
    }

    suspend fun nextCard(type: Type, side: Side): Card? {
        val userId = userRepo.userFlow.value?.uid ?: return null
        val index = rnd.nextInt(SESSION_COUNT)
        val time = Clock.systemDefaultZone().millis()
        val back = when (side) {
            Side.ANY -> rnd.nextBoolean()
            Side.FRONT_ONLY -> false
            Side.BACK_ONLY -> true
        }
        val phrase = when (type) {
            Type.ANY,
            Type.RELIGIOUS_ONLY -> rnd.nextBoolean()
            Type.WORD_ONLY -> false
            Type.PHRASE_ONLY -> true
        }
        val religious = type == Type.RELIGIOUS_ONLY
        val key = prefs.typeMode
        val tersiveType =
            (if (phrase) PHRASE else WORD) or (if (religious) RELIGIOUS else NONRELIGIOUS)
        val flags = tersiveType or (if (back) BACK else FRONT) or (if (key) KEY else SCRIPT)
        return udm.userDb.learnDao.findNext(userId, flags, index, time)?.let { learn ->
            val tersiveList = if (key) tdm.tersiveDb.tersiveDao.findKbdMatches(
                learn.tersive,
                tersiveType
            ) else tdm.tersiveDb.tersiveDao.findLvl4Matches(learn.tersive, tersiveType)
            Card(!back, index, learn, tersiveList)
        }
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
