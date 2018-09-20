package com.troy.tersive.ui.flashcard

import androidx.lifecycle.MutableLiveData
import com.troy.tersive.model.data.Card
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.ui.BaseViewModel
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.launch
import org.lds.mobile.coroutine.CoroutineContextProvider
import javax.inject.Inject

class FlashCardViewModel @Inject constructor(
    private val flashCardRepo: FlashCardRepo,
    cc: CoroutineContextProvider
) : BaseViewModel(cc), CoroutineScope {

    val cardLiveData = MutableLiveData<Card>()

    fun nextCard() {
        launch {
            val card = flashCardRepo.nextCard(FlashCardRepo.Type.ANY, FlashCardRepo.Side.ANY)
            cardLiveData.postValue(card)
        }
    }
}
