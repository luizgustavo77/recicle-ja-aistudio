package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme =
  darkColorScheme(
    primary = RecicleGreenDarkPrimary,
    onPrimary = RecicleGreenOnContainer,
    primaryContainer = RecicleGreenDarkContainer,
    onPrimaryContainer = RecicleGreenDarkPrimary,
    secondary = ReciclePetrolDarkSecondary,
    onSecondary = ReciclePetrolOnContainer,
    secondaryContainer = ReciclePetrolSecondary,
    onSecondaryContainer = ReciclePetrolContainer,
    tertiary = RecicleAmberDarkTertiary,
    onTertiary = RecicleAmberOnContainer,
    background = RecicleDarkBackground,
    onBackground = RecicleSurfaceVariant,
    surface = RecicleDarkSurface,
    onSurface = RecicleSurfaceVariant,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = RecicleGreenPrimary,
    onPrimary = RecicleGreenOnPrimary,
    primaryContainer = RecicleGreenContainer,
    onPrimaryContainer = RecicleGreenOnContainer,
    secondary = ReciclePetrolSecondary,
    onSecondary = ReciclePetrolOnSecondary,
    secondaryContainer = ReciclePetrolContainer,
    onSecondaryContainer = ReciclePetrolOnContainer,
    tertiary = RecicleAmberTertiary,
    onTertiary = RecicleAmberOnTertiary,
    tertiaryContainer = RecicleAmberContainer,
    onTertiaryContainer = RecicleAmberOnContainer,
    background = RecicleBackground,
    onBackground = RecicleOnBackground,
    surface = RecicleSurface,
    onSurface = RecicleOnSurface,
    surfaceVariant = RecicleSurfaceVariant,
    onSurfaceVariant = RecicleOnSurfaceVariant,
    outline = RecicleOutline,
    outlineVariant = RecicleOutlineVariant,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve brand identity by default
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

