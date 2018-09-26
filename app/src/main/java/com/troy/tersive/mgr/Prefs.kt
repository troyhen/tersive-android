package com.troy.tersive.mgr

import org.lds.mobile.prefs.PrefsContainer
import org.lds.mobile.prefs.PrefsManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Prefs @Inject constructor() : PrefsContainer(PrefsManager.PROTECTED_NAMESPACE) {
    var username by SharedPref<String?>(null)
}
