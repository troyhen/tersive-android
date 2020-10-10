package com.troy.tersive.ui.main

import androidx.compose.runtime.mutableStateOf
import com.troy.tersive.app.App
import com.troy.tersive.mgr.Prefs
import com.troy.tersive.model.repo.FirestoreRepo
import com.troy.tersive.ui.base.BaseViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

class MainViewModel : BaseViewModel<Unit>() {

    private val inject = EntryPoints.get(App.app, Inject::class.java)
    private val firestoreRepo = inject.firestoreRepo
    private val prefs = inject.prefs

    private val typeModeState = mutableStateOf(prefs.typeMode)
    var typeMode
        get() = typeModeState.value
        set(value) {
            prefs.typeMode = value
            typeModeState.value = value
        }

    @EntryPoint
    @InstallIn(ApplicationComponent::class)
    interface Inject {
        val firestoreRepo: FirestoreRepo
        val prefs: Prefs
    }
}
