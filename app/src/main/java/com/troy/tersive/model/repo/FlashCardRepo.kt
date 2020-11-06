package com.troy.tersive.model.repo

import com.troy.tersive.model.data.Card
import com.troy.tersive.model.data.CardSide
import com.troy.tersive.model.data.CardType
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
import com.troy.tersive.model.prefs.Prefs
import java.time.Duration
import java.time.LocalDateTime
import java.util.Random

class FlashCardRepo(
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

    suspend fun nextCard(type: CardType, side: CardSide): Card? {
        val userId = userRepo.userFlow.value?.uid ?: return null
        val index = rnd.nextInt(SESSION_COUNT)
        val time = LocalDateTime.now()
        val back = when (side) {
            CardSide.ANY -> rnd.nextBoolean()
            CardSide.FRONT -> false
            CardSide.BACK -> true
        }
        val phrase = when (type) {
            CardType.ANY,
            CardType.RELIGIOUS -> rnd.nextBoolean()
            CardType.WORD -> false
            CardType.PHRASE -> true
        }
        val religious = type == CardType.RELIGIOUS
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

}
