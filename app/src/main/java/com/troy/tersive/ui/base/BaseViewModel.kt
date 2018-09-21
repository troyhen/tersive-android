package com.troy.tersive.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Job
import org.lds.mobile.coroutine.CoroutineContextProvider

open class BaseViewModel(cc: CoroutineContextProvider) : ViewModel(), CoroutineScope {

    private val job = Job() // create a job as a parent for coroutines
    private val backgroundContext = cc.commonPool
    override val coroutineContext get() = backgroundContext + job // actual context to use with coroutines

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
