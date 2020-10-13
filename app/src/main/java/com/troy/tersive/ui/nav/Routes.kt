package com.troy.tersive.ui.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.troy.tersive.model.repo.FlashCardRepo
import com.troy.tersive.ui.admin.AdminMenuPage
import com.troy.tersive.ui.flashcard.FlashCardPage
import com.troy.tersive.ui.intro.IntroPage
import com.troy.tersive.ui.main.MainPage

private val backstack = BackStack<Screen>(MainScreen) { _, screen ->
    screen.clear()
}

sealed class Screen : ViewModelStoreOwner {

    private val viewModelStore = ViewModelStore()

    /**
     * Clear the ViewModels used by this screen
     */
    fun clear() {
        viewModelStore.clear()
    }

    /**
     * Pop the current screen, but not the root screen.
     * @return true if a screen was popped
     */
    fun pop() = backstack.pop()

    /**
     * Pop all screens back to [screen]. If [screen] is not found in the backstack no screens are popped.
     * @return true if one or more screens were popped
     */
    fun popTo(screen: Screen) = backstack.popTo(screen)

    /**
     * Compose the current screen
     */
    @Composable
    abstract fun compose()

    /**
     * Get this screen's [ViewModelStore]
     */
    override fun getViewModelStore() = viewModelStore

    companion object {
        /**
         * Get the current screen
         */
        fun current(): Screen = backstack.current
    }
}

object MainScreen : Screen() {
    @Composable
    override fun compose() = MainPage()

    fun goAdminMenu() = backstack.push(AdminMenuScreen)
    fun goIntro() = backstack.push(IntroScreen)
    fun goFlashCards(phraseType: FlashCardRepo.Type) = backstack.push(FlashCardsScreen(phraseType))
//    fun goReading() = backstack.push(ReadingScreen)
//    fun goWriting() = backstack.push(WritingScreen)
}

object IntroScreen : Screen() {
    @Composable
    override fun compose() = IntroPage()
}

data class FlashCardsScreen(val phraseType: FlashCardRepo.Type) : Screen() {
    @Composable
    override fun compose() = FlashCardPage(phraseType)
}

object AdminMenuScreen : Screen() {
    @Composable
    override fun compose() = AdminMenuPage()
}

//object LoginScreen : Screen() {
//    @Composable
//    override fun compose() = LoginPage()
//}

//object ReadingScreen : Screen()
//
//object WritingScreen : Screen()
