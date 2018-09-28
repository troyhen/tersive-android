package com.troy.tersive.ui.main

import com.troy.tersive.mgr.Prefs
import com.troy.tersive.model.repo.UserRepo
import com.troy.tersive.ui.base.BaseViewModel
import org.lds.mobile.coroutine.CoroutineContextProvider
import javax.inject.Inject

class MainViewModel @Inject constructor(
    cc: CoroutineContextProvider,
    private val prefs: Prefs,
    userRepo: UserRepo
) : BaseViewModel(cc) {

    init {
        userRepo.autoLogin()
    }

    var typeMode
        get() = prefs.typeMode
        set(value) {
            prefs.typeMode = value
        }
}
