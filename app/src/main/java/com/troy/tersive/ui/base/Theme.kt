package com.troy.tersive.ui.base

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkPalette = darkColors(
    primary = colorPrimary,
    primaryVariant = colorPrimaryDark,
    onPrimary = material_white,
    secondary = material_blue_500,
    //secondaryVariant = material_blue_500_dark,
    onSecondary = material_white,
    error = colorError,
    onError = material_black,
    surface = material_black,
    onSurface = material_white,
    background = material_black,
    onBackground = material_white
)

private val LightPalette = lightColors(
    primary = colorPrimary,
    primaryVariant = colorPrimaryDark,
    onPrimary = material_white,
    secondary = material_blue_700,
    secondaryVariant = material_blue_700_dark,
    onSecondary = material_white,
    error = colorError,
    onError = material_white,
    surface = material_white,
    onSurface = material_black,
    background = appBackground,
    onBackground = material_black
)

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkPalette else LightPalette
    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}