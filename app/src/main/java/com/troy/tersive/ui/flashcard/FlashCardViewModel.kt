package com.troy.tersive.ui.flashcard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.troy.tersive.model.data.Card
import com.troy.tersive.model.repo.FlashCardRepo
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import org.lds.mobile.coroutine.CoroutineContextProvider
import javax.inject.Inject

class FlashCardViewModel @Inject constructor(
    private val flashCardRepo: FlashCardRepo,
    cc: CoroutineContextProvider
) : ViewModel(), CoroutineScope {

    private val job = Job() // create a job as a parent for coroutines
    private val backgroundContext = cc.commonPool
    override val coroutineContext get() = backgroundContext + job // actual context to use with coroutines
    val cardLiveData = MutableLiveData<Card>()

    fun nextCard() {
        launch {
            val card = flashCardRepo.nextCard(FlashCardRepo.Type.ANY, FlashCardRepo.Side.ANY)
            cardLiveData.postValue(card)
        }
    }
}
