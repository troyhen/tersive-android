package com.troy.tersive.ui.flashcard

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.asFontFamily
import androidx.compose.ui.text.font.font
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.troy.tersive.R
import com.troy.tersive.app.App
import com.troy.tersive.model.data.Card
import com.troy.tersive.model.data.CardResult
import com.troy.tersive.model.data.CardResult.AGAIN
import com.troy.tersive.model.data.CardResult.EASY
import com.troy.tersive.model.data.CardResult.GOOD
import com.troy.tersive.model.data.CardResult.HARD
import com.troy.tersive.model.data.CardSide
import com.troy.tersive.model.data.CardType
import com.troy.tersive.model.data.TersiveUtil
import com.troy.tersive.model.db.user.entity.Learn
import com.troy.tersive.model.prefs.Prefs
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.model.repo.UserRepo
import com.troy.tersive.ui.base.BaseViewModel
import com.troy.tersive.ui.nav.NavActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

class FlashCardViewModel(
    private val flashCardRepo: FlashCardRepo,
    private val prefs: Prefs,
    private val tersiveUtil: TersiveUtil,
    private val userRepo: UserRepo,
) : BaseViewModel<Unit>() {

    private val isLoggedInFlow get() = userRepo.isLoggedInFlow
    private val cardIndex = MutableStateFlow(1)
    private var cardTypeFlow = MutableStateFlow(CardType.ANY)
    var cardType: CardType
        get() = cardTypeFlow.value
        set(value) {
            cardTypeFlow.value = value
        }

    private var card: Card? = null
    val cardFlow = combine(isLoggedInFlow, cardTypeFlow, cardIndex) { isLoggedIn, cardType, _ ->
        if (isLoggedIn) {
            showAnswer.value = false
            flashCardRepo.nextCard(cardType, CardSide.ANY)
        } else null
    }.flowOn(Dispatchers.IO).onEach {
        card = it
    }

    val showAnswer = mutableStateOf(false)
    val typeMode get() = prefs.typeMode

    fun answerFont(card: Card): FontFamily {
        return when {
            !card.front -> phraseFont
            prefs.typeMode -> keyFont
            else -> tersiveFont
        }
    }

    @Composable
    fun answerStyle(card: Card): TextStyle {
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

    fun cardAnswer(card: Card, typeMode: Boolean): String {
        return when {
            card.front -> cardTersive(card, typeMode)
            else -> cardPhrases(card)
        }
    }

    private fun cardPhrases(card: Card): String {
        return card.tersiveList.asSequence()
            .mapNotNull { it.phrase }
            .joinToString(", ") { it }
    }

    fun cardQuestion(card: Card, typeMode: Boolean): String {
        return when {
            card.front -> cardPhrases(card)
            else -> cardTersive(card, typeMode)
        }
    }

    private fun cardTersive(card: Card, typeMode: Boolean) = if (typeMode) card.learn.tersive else tersiveUtil.optimizeHand(card.learn.tersive).toString()

    fun questionFont(card: Card): FontFamily {
        return when {
            card.front -> phraseFont
            prefs.typeMode -> keyFont
            else -> tersiveFont
        }
    }

    @Composable
    fun questionStyle(card: Card): TextStyle {
        return when {
            card.front -> MaterialTheme.typography.h6
            prefs.typeMode -> MaterialTheme.typography.h6
            else -> MaterialTheme.typography.h1
        }
    }

    fun updateCard(card: Card, result: CardResult) = viewModelScope.launch(Dispatchers.IO) {
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
        cardIndex.value++
    }

    companion object {
        private val keyFont = FontFamily.Monospace
        private val phraseFont = FontFamily.Default
        private val tersiveFont = font(R.font.tersive_script).asFontFamily()
    }
}

object FlashCardViewModelFactory : ViewModelProvider.Factory {
    private val koin = GlobalContext.get()
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = FlashCardViewModel(koin.get(), koin.get(), koin.get(), koin.get()) as T
}
