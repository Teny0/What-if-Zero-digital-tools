package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Amber60,
    onPrimary = SlateDark,
    primaryContainer = AmberDark,
    onPrimaryContainer = Amber80,
    secondary = Terracotta60,
    onSecondary = Color.White,
    secondaryContainer = Terracotta40,
    onSecondaryContainer = Terracotta80,
    tertiary = Amber80,
    background = SlateDark,
    onBackground = Color(0xFFF1F5F9),
    surface = SlateNavy,
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = PaperCardDark,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

private val LightColorScheme = lightColorScheme(
    primary = Amber40,
    onPrimary = Color.White,
    primaryContainer = Amber80,
    onPrimaryContainer = AmberDark,
    secondary = Terracotta40,
    onSecondary = Color.White,
    secondaryContainer = Terracotta80,
    onSecondaryContainer = Terracotta40,
    tertiary = Amber60,
    background = PaperLight,
    onBackground = SlateDark,
    surface = Color.White,
    onSurface = SlateNavy,
    surfaceVariant = PaperCard,
    onSurfaceVariant = Color(0xFF4B5563),
    outline = PaperBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep distinctive warm brand theme by default
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
