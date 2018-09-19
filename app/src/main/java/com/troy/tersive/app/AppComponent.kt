package com.troy.tersive.app

import com.troy.tersive.model.db.tersive.PopulateTersive
import com.troy.tersive.model.db.user.PopulateLearn
import com.troy.tersive.ui.ViewModelModule
import com.troy.tersive.ui.flashcard.FlashCardActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(it: App)
    fun inject(it: FlashCardActivity)
    fun inject(it: PopulateLearn)
    fun inject(it: PopulateTersive)
}
