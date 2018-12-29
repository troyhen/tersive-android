package com.troy.tersive.ui.flashcard

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.model.data.Card
import com.troy.tersive.model.db.user.entity.Learn
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.model.repo.FlashCardRepo.Result.AGAIN
import com.troy.tersive.model.repo.FlashCardRepo.Result.EASY
import com.troy.tersive.model.repo.FlashCardRepo.Result.GOOD
import com.troy.tersive.model.repo.FlashCardRepo.Result.HARD
import com.troy.tersive.model.repo.UserRepo
import com.troy.tersive.ui.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.lds.mobile.coroutine.CoroutineContextProvider
import javax.inject.Inject

class FlashCardViewModel @Inject constructor(
    cc: CoroutineContextProvider,
    private val flashCardRepo: FlashCardRepo,
    private val prefs: Prefs,
    private val userRepo: UserRepo
) : BaseViewModel(cc), CoroutineScope {

    val cardLiveData = MutableLiveData<Card>()
    val needLogin get() = !userRepo.isLoggedIn
    var phraseType = FlashCardRepo.Type.ANY
    val typeMode get() = prefs.typeMode

    fun onLogin(user: FirebaseUser) = launch {
        userRepo.login(user)
        nextCard()
    }

    fun nextCard() = launch {
        flashCardRepo.nextCard(phraseType, FlashCardRepo.Side.ANY)?.let {
            cardLiveData.postValue(it)
        }
    }

    fun updateCard(result: FlashCardRepo.Result) = launch {
        val card = cardLiveData.value ?: return@launch
        val step = FlashCardRepo.SESSION_COUNT
        val easy = if (result != EASY) 0 else card.learn.easy + 1
        val delta = when (result) {
            EASY -> {
                val maxDelta =
                    flashCardRepo.maxSort(card.learn.flags and (Learn.PHRASE or Learn.RELIGIOUS)) - card.learn.sort
                if (easy > 3) {
                    maxDelta
                } else {
                    (step * 4 * easy).coerceIn(step, maxDelta)
                }
            }
            GOOD -> step * 3
            HARD -> step * 2
            AGAIN -> step * 1
        }
        val tries = 1 + card.learn.tries
        flashCardRepo.moveCard(card, delta, result.timeAdd, easy, tries)
        nextCard()
    }
}
