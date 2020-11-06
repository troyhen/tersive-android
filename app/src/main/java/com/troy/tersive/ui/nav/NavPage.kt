package com.troy.tersive.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.staticAmbientOf
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.troy.tersive.model.data.CardType
import com.troy.tersive.ui.admin.AdminMenuPage
import com.troy.tersive.ui.flashcard.FlashCardPage
import com.troy.tersive.ui.intro.IntroPage
import com.troy.tersive.ui.main.MainPage

enum class Screen(val route: String, val root: String = route) {
    ADMIN("admin"),
    CARD("card/{type}", "card"),
    INTRO("intro"),
    MAIN("main");

    companion object {
        fun path(screen: Screen) = screen.root

        fun path(screen: Screen, vararg args: Any): String {
            return when (screen) {
                CARD -> "${screen.root}/${args[0]}"
                else -> path(screen)
            }
        }
    }
}

val NavControllerAmbient = staticAmbientOf<NavController>()

@Composable
fun NavPage() {
    val navController = rememberNavController()
    Providers(NavControllerAmbient provides navController) {
        NavHost(navController = navController, startDestination = Screen.MAIN.route) {
            composable(Screen.ADMIN.route) { AdminMenuPage() }
            composable(
                route = Screen.CARD.route,
                arguments = listOf(navArgument("type") { type = NavType.EnumType(CardType::class.java) })
            ) { backStackEntry ->
                FlashCardPage(backStackEntry.arguments?.get("type") as? CardType ?: CardType.ANY)
            }
            composable(Screen.INTRO.route) { IntroPage() }
            composable(Screen.MAIN.route) { MainPage() }
        }
    }
}
