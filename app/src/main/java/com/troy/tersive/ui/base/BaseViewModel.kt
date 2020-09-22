package com.troy.tersive.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

open class BaseViewModel<T> : ViewModel() {

    private val _eventChannel = Channel<T>()
    val eventChannel: ReceiveChannel<T> = _eventChannel

    protected fun send(event: T) = viewModelScope.launch {
        _eventChannel.send(event)
    }
}
