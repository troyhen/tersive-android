package com.troy.tersive.ui.flashcard

import androidx.lifecycle.MutableLiveData
import com.troy.tersive.model.data.Card
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.model.repo.UserRepo
import com.troy.tersive.ui.base.BaseViewModel
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.launch
import org.lds.mobile.coroutine.CoroutineContextProvider
import javax.inject.Inject

class FlashCardViewModel @Inject constructor(
    cc: CoroutineContextProvider,
    private val flashCardRepo: FlashCardRepo,
    private val userRepo: UserRepo
) : BaseViewModel(cc), CoroutineScope {

    val needLogin get() = !userRepo.isLoggedIn
    val cardLiveData = MutableLiveData<Card>()
    var phraseMode = FlashCardRepo.Type.ANY

    fun nextCard() {
        launch {
            val card = flashCardRepo.nextCard(phraseMode, FlashCardRepo.Side.ANY)
            cardLiveData.postValue(card)
        }
    }
}
