package com.troy.tersive.ui.user

import com.troy.tersive.model.repo.UserRepo
import com.troy.tersive.ui.base.BaseViewModel
import kotlinx.coroutines.experimental.launch
import org.lds.mobile.coroutine.CoroutineContextProvider
import org.lds.mobile.livedata.SingleLiveEvent
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    cc: CoroutineContextProvider,
    private val userRepo: UserRepo
) : BaseViewModel(cc) {

    val loginEvent = SingleLiveEvent<Boolean>()

    fun login(username: String, password: String) = launch {
        loginEvent.postValue(userRepo.login(username, password))
    }
}
