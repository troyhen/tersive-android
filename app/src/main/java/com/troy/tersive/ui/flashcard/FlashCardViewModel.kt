package com.troy.tersive.ui.flashcard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.troy.tersive.app.App
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.model.data.Card
import com.troy.tersive.model.data.TersiveUtil
import com.troy.tersive.model.db.user.entity.Learn
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.model.repo.FlashCardRepo.Result.AGAIN
import com.troy.tersive.model.repo.FlashCardRepo.Result.EASY
import com.troy.tersive.model.repo.FlashCardRepo.Result.GOOD
import com.troy.tersive.model.repo.FlashCardRepo.Result.HARD
import com.troy.tersive.model.repo.UserRepo
import com.troy.tersive.ui.base.BaseViewModel
import com.troy.tersive.ui.nav.NavActivity
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FlashCardViewModel : BaseViewModel<Unit>() {

    private val inject = EntryPoints.get(App.app, Inject::class.java)
    private val flashCardRepo = inject.flashCardRepo
    private val prefs = inject.prefs
    private val tersiveUtil = inject.tersiveUtil
    private val userRepo = inject.userRepo

    private var card by mutableStateOf<Card?>(null)
    val cardFlags: Int get() = card?.learn?.flags ?: 0
    val cardQuestion: String
        get() {
            val card = card ?: return ""
            return when {
                card.front -> cardPhrases(card)
                else -> cardTersive(card)
            }
        }
    val cardAnswer: String
        get() {
            val card = card ?: return ""
            return when {
                card.front -> cardTersive(card)
                else -> cardPhrases(card)
            }
        }
    val isFront get() = card?.front == true

    var phraseType = FlashCardRepo.Type.ANY
    val showAnswer = mutableStateOf(false)
    val typeMode get() = prefs.typeMode

    fun autoSignIn() {
        if (!userRepo.isLoggedIn) {
            (App.currentActivity?.get() as? NavActivity)?.signIn()
        }
    }

    private fun cardTersive(card: Card) = if (typeMode) card.learn.tersive else tersiveUtil.optimizeHand(card.learn.tersive).toString()

    private fun cardPhrases(card: Card): String {
        return card.tersiveList.asSequence()
            .mapNotNull { it.phrase }
            .joinToString(", ") { it }
    }

    fun onLogin(user: FirebaseUser) = viewModelScope.launch(Dispatchers.IO) {
        userRepo.login(user)
        nextCard()
    }

    private fun nextCard() = viewModelScope.launch(Dispatchers.IO) {
        flashCardRepo.nextCard(phraseType, FlashCardRepo.Side.ANY)?.let {
            card = it
        }
    }

    fun updateCard(result: FlashCardRepo.Result) = viewModelScope.launch(Dispatchers.IO) {
        val card = card ?: return@launch
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

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface Inject {
        val flashCardRepo: FlashCardRepo
        val prefs: Prefs
        val tersiveUtil: TersiveUtil
        val userRepo: UserRepo
    }
}
