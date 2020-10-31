package com.troy.tersive.app.init

import android.content.Context
import androidx.preference.PreferenceManager
import androidx.startup.Initializer
import com.troy.tersive.R
import com.troy.tersive.model.prefs.PrefsManager

class PrefsInit : Initializer<Unit> {

    override fun create(context: Context) {
        val applicationContext = checkNotNull(context.applicationContext) { "Missing Application Context" }
        PrefsManager.init(applicationContext)
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}