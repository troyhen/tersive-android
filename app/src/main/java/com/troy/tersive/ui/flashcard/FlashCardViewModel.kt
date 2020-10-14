package com.troy.tersive.ui.flashcard

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.asFontFamily
import androidx.compose.ui.text.font.font
import androidx.lifecycle.viewModelScope
import com.troy.tersive.R
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FlashCardViewModel(
    private val flashCardRepo: FlashCardRepo,
    private val prefs: Prefs,
    private val tersiveUtil: TersiveUtil,
    private val userRepo: UserRepo,
) : BaseViewModel<Unit>() {

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
    val questionFont: FontFamily
        get() {
            val card = card ?: return FontFamily.Default
            return when {
                card.front -> phraseFont
                prefs.typeMode -> keyFont
                else -> tersiveFont
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
    val answerFont: FontFamily
        get() {
            val card = card ?: return FontFamily.Default
            return when {
                !card.front -> phraseFont
                prefs.typeMode -> keyFont
                else -> tersiveFont
            }
        }

    val isFront get() = card?.front == true

    var phraseType = FlashCardRepo.Type.ANY
    val showAnswer = mutableStateOf(false)
    val typeMode get() = prefs.typeMode
    private val keyFont = FontFamily.Monospace
    private val phraseFont = FontFamily.Default
    private val tersiveFont = font(R.font.tersive_script).asFontFamily()

    init {
        if (userRepo.isLoggedIn) {
            nextCard()
        }
    }

    @Composable
    fun answerStyle(): TextStyle {
        val card = card ?: return MaterialTheme.typography.subtitle1
        return when {
            !card.front -> MaterialTheme.typography.h6
            prefs.typeMode -> MaterialTheme.typography.h6
            else -> MaterialTheme.typography.h1
        }
    }

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

//    fun onLogin(user: FirebaseUser) = viewModelScope.launch(Dispatchers.IO) {
//        userRepo.login(user)
//        nextCard()
//    }

    private fun nextCard() = viewModelScope.launch(Dispatchers.Main) {
        showAnswer.value = false
        withContext(Dispatchers.IO) { flashCardRepo.nextCard(phraseType, FlashCardRepo.Side.ANY) }?.let {
            card = it
        }
    }

    @Composable
    fun questionStyle(): TextStyle {
        val card = card ?: return MaterialTheme.typography.subtitle1
        return when {
            card.front -> MaterialTheme.typography.h6
            prefs.typeMode -> MaterialTheme.typography.h6
            else -> MaterialTheme.typography.h1
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
        var timeAdd = result.timeAdd
        if (result == EASY) {
            repeat(easy - 1) {
                timeAdd += timeAdd
            }
        }
        val tries = 1 + card.learn.tries
        flashCardRepo.moveCard(card, delta, timeAdd, easy, tries)
        nextCard()
    }
}
