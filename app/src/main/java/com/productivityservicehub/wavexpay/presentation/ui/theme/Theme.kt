package com.productivityservicehub.wavexpay.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF1E3A8A),
    secondary = Color(0xFFFF6B35),
    tertiary = Color(0xFF4A90E2),
    background = Color(0xFF0A1A3A),
    surface = Color(0xFF1E3A8A)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1E3A8A),
    secondary = Color(0xFFFF6B35),
    tertiary = Color(0xFF4A90E2),
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFFFFFFF)
)


@Composable
fun WaveXPayTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}