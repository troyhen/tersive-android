package com.troy.tersive.ui.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.troy.tersive.model.prefs.Prefs
import com.troy.tersive.model.repo.FirestoreRepo
import com.troy.tersive.ui.base.BaseViewModel
import org.koin.core.context.GlobalContext

class MainViewModel(val firestoreRepo: FirestoreRepo, val prefs: Prefs) : BaseViewModel<Unit>() {

    private val typeModeState = mutableStateOf(prefs.typeMode)
    var typeMode
        get() = typeModeState.value
        set(value) {
            prefs.typeMode = value
            typeModeState.value = value
        }
}

object MainViewModelFactory : ViewModelProvider.Factory {
    private val koin = GlobalContext.get()
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainViewModel(koin.get(), koin.get()) as T
}
