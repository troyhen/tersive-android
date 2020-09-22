package com.troy.tersive.ui.intro

import com.troy.tersive.ui.base.BaseViewModel
import com.troy.tersive.ui.nav.IntroScreen

class IntroViewModel() : BaseViewModel<Unit>() {//Event>() {

    val webContext = WebContext()

    fun onBackPressed() {
        if (webContext.canGoBack()) {
            webContext.goBack()
        } else {
            IntroScreen.pop()
//            send(Finish)
        }
    }

//    sealed class Event {
//        object Finish : Event()
//    }
}
