package com.troy.tersive.ui.read

import androidx.lifecycle.MutableLiveData
import com.troy.tersive.model.data.WebDoc
import com.troy.tersive.model.repo.WebRepo
import com.troy.tersive.ui.base.BaseViewModel
import kotlinx.coroutines.experimental.launch
import org.lds.mobile.coroutine.CoroutineContextProvider
import javax.inject.Inject

class ReadViewModel @Inject constructor(
    cc: CoroutineContextProvider,
    private val webRepo: WebRepo
) : BaseViewModel(cc) {

    var webDoc: WebDoc? = null
        set(value) {
            field = value
            load()
        }

    val textLiveData = MutableLiveData<CharSequence>()

    private fun load() = launch {
        textLiveData.postValue(webDoc?.let { webRepo.load(it) })
    }
}
