package com.example.civicapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    primaryContainer = PrimaryLight,
    secondary = Secondary,
    secondaryContainer = SecondaryLight,
    tertiary = Info,
    background = Background,
    surface = White,
    surfaceVariant = LightGray,
    error = Error,
    onPrimary = White,
    onSecondary = White,
    onBackground = DarkGray,
    onSurface = DarkGray,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    primaryContainer = Primary,
    secondary = SecondaryLight,
    secondaryContainer = Secondary,
    tertiary = Info,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2C2C2C),
    error = Error,
    onPrimary = Color(0xFF0066CC),
    onSecondary = Secondary,
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
)

@Composable
fun CivicAppTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}