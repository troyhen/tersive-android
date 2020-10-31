package com.troy.tersive.ui.main

import androidx.compose.runtime.mutableStateOf
import com.troy.tersive.model.prefs.Prefs
import com.troy.tersive.model.repo.FirestoreRepo
import com.troy.tersive.ui.base.BaseViewModel

class MainViewModel(val firestoreRepo: FirestoreRepo, val prefs: Prefs) : BaseViewModel<Unit>() {

    private val typeModeState = mutableStateOf(prefs.typeMode)
    var typeMode
        get() = typeModeState.value
        set(value) {
            prefs.typeMode = value
            typeModeState.value = value
        }
}
