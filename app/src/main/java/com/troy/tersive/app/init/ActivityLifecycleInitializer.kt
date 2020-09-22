package com.troy.tersive.app.init

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.troy.tersive.app.App
import java.lang.ref.WeakReference

class ActivityLifecycleInitializer : Initializer<Unit> {

    var starts = 0
    val inForeground get() = starts > 0     // true when the application is in the foreground
    private val startListeners = mutableListOf<StartListener>()

    init {
        activityLifecycleTracker = this
    }

    fun addStartListener(listener: StartListener) {
        synchronized(startListeners) {
            startListeners.add(listener)
        }
    }

    fun removeStartListener(listener: StartListener) {
        synchronized(startListeners) {
            startListeners.remove(listener)
        }
    }

    override fun create(context: Context) {
        val applicationContext = checkNotNull(context.applicationContext) { "Missing Application Context" } as Application
        registerListeners(applicationContext)
//        registerSessionPrefs(applicationContext)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()

    private fun registerListeners(applicationContext: Application) {
        applicationContext.registerActivityLifecycleCallbacks(object : EmptyLifecycleCallbacks() {

            override fun onActivityStarted(activity: Activity) {
                super.onActivityStarted(activity)
                starts++
                App.currentActivity = WeakReference(activity)
                synchronized(startListeners) {
                    startListeners.forEach {
                        it.onStart()
                    }
                }

            }

            override fun onActivityStopped(activity: Activity) {
                super.onActivityStopped(activity)
                starts--
            }
        })
    }

//    private fun registerSessionPrefs(applicationContext: Application) {
//        val injector = EntryPoints.get(applicationContext, ActivityLifecycleCallbacksInitializerInjector::class.java)
//        applicationContext.registerActivityLifecycleCallbacks(injector.getSessionPrefs())
//    }

    companion object {
        lateinit var activityLifecycleTracker: ActivityLifecycleInitializer
            private set
    }

    interface StartListener {
        fun onStart()
    }

//    @EntryPoint
//    @InstallIn(ApplicationComponent::class)
//    interface ActivityLifecycleCallbacksInitializerInjector {
//        fun getSessionPrefs(): SessionPrefs
//    }
}