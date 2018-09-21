package com.troy.tersive.ui.user

import com.troy.tersive.model.repo.UserRepo
import com.troy.tersive.ui.base.BaseViewModel
import kotlinx.coroutines.experimental.launch
import org.lds.mobile.coroutine.CoroutineContextProvider
import org.lds.mobile.livedata.SingleLiveEvent
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    cc: CoroutineContextProvider,
    private val userRepo: UserRepo
) : BaseViewModel(cc) {

    val registerEvent = SingleLiveEvent<RegisterResult>()

    fun register(username: String?, password1: String?, password2: String?) = launch {
        if (username == null || password1 == null || password2 == null) {
            registerEvent.postValue(RegisterResult.MISSING_FIELD)
            return@launch
        }
        if (password1 == password2) {
            val success = userRepo.register(username, password1)
            registerEvent.postValue(if (success) RegisterResult.SUCCESS else RegisterResult.DUPLICATE_USERNAME)
        } else {
            registerEvent.postValue(RegisterResult.PASSWORD_MISMATCH)
        }
    }

    enum class RegisterResult {
        SUCCESS, DUPLICATE_USERNAME, PASSWORD_MISMATCH, MISSING_FIELD
    }
}
