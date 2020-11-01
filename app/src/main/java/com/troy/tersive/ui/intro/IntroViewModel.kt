package com.troy.tersive.ui.intro

import androidx.navigation.NavController
import com.troy.tersive.ui.base.BaseViewModel

class IntroViewModel : BaseViewModel<Unit>() {//Event>() {

    val webContext = WebContext()

    fun onBackPressed(navController: NavController) {
        if (webContext.canGoBack()) {
            webContext.goBack()
        } else {
            navController.popBackStack()
        }
    }

//    sealed class Event {
//        object Finish : Event()
//    }
}
