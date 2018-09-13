package com.troy.tersive.app

import com.troy.tersive.db.migrate.PopulateTersive
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class/*, ViewModelModule::class*/])
interface AppComponent {
    //: ActivityComponent, FragmentComponent {
    fun inject(it: App)

    fun inject(it: PopulateTersive)
//    fun inject(it: AsyncRunner)
//    fun inject(it: AudioItem)
//    fun inject(it: DefaultAnalytics)
//    fun inject(it: MyAudioApi)
//    fun inject(it: MediaService)
//    fun inject(it: ReminderBroadcastReceiver)
//    fun inject(it: SyncContentRunner)
//    fun inject(it: SyncRunner)
//    fun inject(it: SyncWorker)
//    fun inject(it: UsernamePreference)
}
