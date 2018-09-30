package com.troy.tersive.ui.flashcard

import androidx.lifecycle.MutableLiveData
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.model.data.Card
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.model.repo.FlashCardRepo.Result.AGAIN
import com.troy.tersive.model.repo.FlashCardRepo.Result.EASY
import com.troy.tersive.model.repo.FlashCardRepo.Result.GOOD
import com.troy.tersive.model.repo.FlashCardRepo.Result.HARD
import com.troy.tersive.model.repo.UserRepo
import com.troy.tersive.ui.base.BaseViewModel
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.launch
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
    var phraseMode = FlashCardRepo.Type.ANY
    val typeMode get() = prefs.typeMode

    fun nextCard() = launch {
        val card = flashCardRepo.nextCard(phraseMode, FlashCardRepo.Side.ANY)
        cardLiveData.postValue(card)
    }

    fun updateCard(result: FlashCardRepo.Result) = launch {
        val card = cardLiveData.value ?: return@launch
        val step = FlashCardRepo.SESSION_COUNT
        val easy = when {
            result != EASY -> 0
            card.front -> card.learn.easy1 + 1
            else -> card.learn.easy2 + 1
        }
        val delta = when (result) {
            EASY -> step * 4 * easy
            GOOD -> step * 3
            HARD -> step * 2
            AGAIN -> step * 1
        }
        val tries = 1 + if (card.front) card.learn.tries1 else card.learn.tries2
        flashCardRepo.moveCard(card, delta, result.timeAdd, easy, tries)
        nextCard()
    }
}
