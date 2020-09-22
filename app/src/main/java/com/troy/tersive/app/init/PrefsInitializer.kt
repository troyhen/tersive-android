package com.troy.tersive.app.init

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.startup.Initializer
import com.troy.tersive.R
import com.troy.tersive.mgr.PrefsManager

class PrefsInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        val applicationContext = checkNotNull(context.applicationContext) { "Missing Application Context" }
//        val injector = EntryPoints.get(applicationContext, PrefsInitializerInjector::class.java)
        PrefsManager.init(applicationContext)
//        injector.getPrefs().setMobileDevPrefs()
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

//    @EntryPoint
//    @InstallIn(ApplicationComponent::class)
//    interface PrefsInitializerInjector {
//        fun getPrefs(): Prefs
//    }
}