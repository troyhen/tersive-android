package com.troy.tersive.app

import android.app.Activity
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.lang.ref.WeakReference

@HiltAndroidApp
class App : Application() {

    init {
        app = this
    }


    companion object {
        @Suppress("MemberNameEqualsClassName")
        lateinit var app: Application
            private set

        var currentActivity: WeakReference<Activity>? = null
    }
}
