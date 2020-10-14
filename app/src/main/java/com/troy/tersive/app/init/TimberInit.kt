package com.troy.tersive.app.init

//import com.google.firebase.crashlytics.FirebaseCrashlytics
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.troy.tersive.BuildConfig
import timber.log.Timber
import java.lang.Integer.min

class TimberInit : Initializer<Unit> {
    override fun create(context: Context) {
//        val applicationContext = checkNotNull(context.applicationContext) { "Missing Application Context" }
//        val aboutPrefs = injector.getAboutPrefs()

        // Always register userId (even if FirebaseCrashlyticsTree is not planted)
//        FirebaseCrashlytics.getInstance().setUserId(aboutPrefs.getAppInstanceId())

        // Only enable logging when using a DEBUG build or when developer mode is set
        // in the Church Mobile Dev app
        if (BuildConfig.DEBUG /*|| prefs.developerMode*/) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(ReleaseTree())
        }

//        @Suppress("ConstantConditionIf") // value is constant from BuildConfig
//        if (FORCE_CRASHLYTICS || BuildConfig.BUILD_TYPE != "debug") {
//            // Log.e(...) will log a non-fatal crash in Crashlytics
//            Timber.plant(FirebaseCrashlyticsTree())
//        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()//listOf(PrefsInitializer::class.java)

//    companion object {
//        private const val FORCE_CRASHLYTICS = true
//    }
}

class DebugTree : Timber.DebugTree() {

    override fun createStackElementTag(element: StackTraceElement): String? {
        // add line number
        return super.createStackElementTag(element) + ":" + element.lineNumber
    }
}

class ReleaseTree : Timber.Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
        return when (priority) {
            Log.VERBOSE, Log.DEBUG, Log.INFO -> false
            else -> true
        }
    }

    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (message.length < MAX_LOG_LENGTH) {
            if (priority == Log.ASSERT) {
                Log.wtf(tag, message)
            } else {
                Log.println(priority, tag, message)
            }
            return
        }

        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = message.length
        while (i < length) {
            var newline = message.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = min(newline, i + MAX_LOG_LENGTH)
                val part = message.substring(i, end)
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, part)
                } else {
                    Log.println(priority, tag, part)
                }
                i = end
            } while (i < newline)
            i++
        }
    }

    companion object {
        const val MAX_LOG_LENGTH = 4000
    }
}

//class FirebaseCrashlyticsTree : Timber.Tree() {
//
//    private val crashlytics = FirebaseCrashlytics.getInstance()
//
//    override fun isLoggable(tag: String?, priority: Int): Boolean {
//        return priority == Log.ERROR
//    }
//
//    @SuppressLint("LogNotTimber")
//    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//        if (priority == Log.ERROR) {
//            if (t != null) {
//                crashlytics.log(message)
//                crashlytics.recordException(t)
//            } else {
//                crashlytics.recordException(TimberNonFatalCrashLogException(message))
//            }
//        }
//    }
//}