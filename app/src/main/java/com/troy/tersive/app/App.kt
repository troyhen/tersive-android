package com.troy.tersive.app

import android.app.Application
import android.preference.PreferenceManager
import com.idescout.sql.SqlScoutServer
import com.troy.tersive.BuildConfig
import com.troy.tersive.R
import org.lds.mobile.devtools.initStetho
import org.lds.mobile.devtools.installLeakCanary
import org.lds.mobile.devtools.isLeakCanaryInAnalyzerProcess
import org.lds.mobile.log.CrashlyticsTree
import org.lds.mobile.log.DebugTree
import org.lds.mobile.log.ReleaseTree
import org.lds.mobile.prefs.PrefsManager
import timber.log.Timber

class App : Application() {

    init {
        PrefsManager.init(this)
        Injector.init(this)
    }

    override fun onCreate() {
        super.onCreate()

        // Leak Canary
        if (isLeakCanaryInAnalyzerProcess()) {
            // This process is dedicated to LeakCanary for heap analysis.
            // Do not init the app in this process!
            return
        }
        installLeakCanary()

//        // Needs to be done prior to injection
//        AndroidThreeTen.init(this)
//        val jobManager = JobManager.create(this)

        Injector.get().inject(this)

        // logging should be done before upgradeApp()
        setupLogging()

//        FacebookSdk.sdkInitialize(applicationContext)

//        registerLifecycleCallbacks()
//        registerExceptionLogging()

//        jobManager.addJobCreator(jobCreator)

//        NotifyChannel.registerAllChannels(this)

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
//        if (oauthConfig.useOAuth) {
//            oauthConfig.updateIfNeeded()
//            registerActivityLifecycleCallbacks(OauthConfigurationActivityLifecycleCallback(oauthConfig, ldsAccountLogger))
//        }
//        analytics.upload()
//        registerActivityLifecycleCallbacks(sessionPrefs)

        // tools
        initStetho()
//        initCrashFix()

        SqlScoutServer.create(this, packageName)
    }

    private fun setupLogging() {
//        // Always register Crashltyics (even if CrashlyticsTree is not planted)
//        Fabric.with(this, Crashlytics())

        val tree = if (BuildConfig.DEBUG) {
            DebugTree()
        } else {
            ReleaseTree()
        }
        Timber.plant(tree)

        @Suppress("ConstantConditionIf") // value is constant from BuildConfig
        if (FORCE_CRASHLYTICS || BuildConfig.BUILD_TYPE != "debug") {
            // Log.e(...) will log a non-fatal crash in Crashlytics
            Timber.plant(CrashlyticsTree())
        }
    }

    companion object {
        private const val FORCE_CRASHLYTICS = true
    }
}
