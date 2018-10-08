package com.troy.tersive.app

import com.troy.tersive.model.db.tersive.PopulateTersive
import com.troy.tersive.ui.base.ViewModelModule
import com.troy.tersive.ui.flashcard.FlashCardActivity
import com.troy.tersive.ui.intro.IntroActivity
import com.troy.tersive.ui.main.MainActivity
import com.troy.tersive.ui.user.LoginActivity
import com.troy.tersive.ui.user.RegisterActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(it: App)
    fun inject(it: FlashCardActivity)
    fun inject(it: IntroActivity)
    fun inject(it: LoginActivity)
    fun inject(it: MainActivity)
    fun inject(it: PopulateTersive)
    fun inject(it: RegisterActivity)
}
