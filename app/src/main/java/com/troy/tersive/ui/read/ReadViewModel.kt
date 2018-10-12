package com.troy.tersive.ui.read

import androidx.lifecycle.MutableLiveData
import com.troy.tersive.model.data.WebDoc
import com.troy.tersive.model.repo.WebRepo
import com.troy.tersive.ui.base.BaseViewModel
import kotlinx.coroutines.experimental.launch
import org.lds.mobile.coroutine.CoroutineContextProvider
import org.lds.mobile.livedata.SingleLiveEvent
import javax.inject.Inject

class ReadViewModel @Inject constructor(
    cc: CoroutineContextProvider,
    private val webRepo: WebRepo
) : BaseViewModel(cc) {

    var webUrl: String? = null
        set(value) {
            field = value
            load()
        }

    val textLiveData = MutableLiveData<String>()

    val onClickEvent = SingleLiveEvent<WebDoc>()

    fun onClick(item: WebDoc) {
        onClickEvent.value = item
    }

    private fun load() = launch {
        textLiveData.postValue(webUrl?.let { webRepo.load(it) })
    }
}
