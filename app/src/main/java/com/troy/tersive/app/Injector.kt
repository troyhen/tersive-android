package com.troy.tersive.app

import android.app.Application

object Injector {

    // AppComponent.init() will be called from Application, Service or Receiver and must be the same instance
    private var appComponent: AppComponent? = null

    fun init(application: Application) {
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder().appModule(AppModule(application)).build()
        }
    }

    fun get() = appComponent ?: error("appComponent is null. Call init() prior to calling get()")
}
