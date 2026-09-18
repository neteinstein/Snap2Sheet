package org.neteinstein.snap2sheet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.neteinstein.snap2sheet.domain.model.AppTheme

/**
 * The design pairs three Google Fonts: Space Grotesk (headings/wordmark), Manrope (body/UI) and
 * Space Mono (amounts, document numbers, NIFs). None are bundled yet — until real font files are
 * added under `composeApp/src/commonMain/composeResources/font/` and loaded with
 * `FontFamily(Font(Res.font.xxx))`, these fall back to the platform default so the app still
 * matches the design's weight/size rhythm even though the exact typeface differs.
 */
object FaturaFonts {
    val Heading: FontFamily = FontFamily.Default
    val Body: FontFamily = FontFamily.Default
    val Mono: FontFamily = FontFamily.Monospace
}

private val FaturaTypography = Typography(
    headlineMedium = TextStyle(fontFamily = FaturaFonts.Heading, fontWeight = FontWeight.Bold, fontSize = 28.sp, letterSpacing = (-0.2).sp),
    headlineSmall = TextStyle(fontFamily = FaturaFonts.Heading, fontWeight = FontWeight.Bold, fontSize = 23.sp),
    titleLarge = TextStyle(fontFamily = FaturaFonts.Heading, fontWeight = FontWeight.Bold, fontSize = 21.sp),
    titleMedium = TextStyle(fontFamily = FaturaFonts.Heading, fontWeight = FontWeight.Bold, fontSize = 18.sp),
    titleSmall = TextStyle(fontFamily = FaturaFonts.Body, fontWeight = FontWeight.Bold, fontSize = 15.sp),
    bodyLarge = TextStyle(fontFamily = FaturaFonts.Body, fontWeight = FontWeight.Normal, fontSize = 15.sp, lineHeight = 21.sp),
    bodyMedium = TextStyle(fontFamily = FaturaFonts.Body, fontWeight = FontWeight.Medium, fontSize = 14.sp),
    bodySmall = TextStyle(fontFamily = FaturaFonts.Body, fontWeight = FontWeight.Medium, fontSize = 12.5.sp),
    labelLarge = TextStyle(fontFamily = FaturaFonts.Body, fontWeight = FontWeight.Bold, fontSize = 16.sp),
    labelMedium = TextStyle(fontFamily = FaturaFonts.Body, fontWeight = FontWeight.Bold, fontSize = 12.5.sp),
    labelSmall = TextStyle(fontFamily = FaturaFonts.Body, fontWeight = FontWeight.Bold, fontSize = 11.sp),
)

private val LightColorScheme = lightColorScheme(
    primary = FaturaColors.Accent,
    onPrimary = FaturaColors.Surface,
    primaryContainer = FaturaColors.AccentSoft,
    onPrimaryContainer = FaturaColors.Accent,
    secondary = FaturaColors.Success,
    background = FaturaColors.Background,
    onBackground = FaturaColors.Ink,
    surface = FaturaColors.Surface,
    onSurface = FaturaColors.Ink,
    surfaceVariant = FaturaColors.SurfaceMuted,
    onSurfaceVariant = FaturaColors.Muted,
    outline = FaturaColors.Border,
    outlineVariant = FaturaColors.Divider,
    error = FaturaColors.Danger,
    errorContainer = FaturaColors.DangerSoft,
)

private val DarkColorScheme = darkColorScheme(
    primary = FaturaColors.Accent,
    onPrimary = FaturaColors.Surface,
    primaryContainer = Color(0xFF1B2A44),
    onPrimaryContainer = Color(0xFFAFC6EE),
    secondary = FaturaColors.Success,
    background = Color(0xFF101215),
    onBackground = Color(0xFFE7E9ED),
    surface = Color(0xFF181B20),
    onSurface = Color(0xFFE7E9ED),
    surfaceVariant = Color(0xFF20242B),
    onSurfaceVariant = Color(0xFFA6ACB8),
    outline = Color(0xFF31363F),
    outlineVariant = Color(0xFF262A31),
    error = FaturaColors.Danger,
    errorContainer = Color(0xFF3A1A17),
)

@Composable
fun FaturaTheme(
    theme: AppTheme = AppTheme.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (theme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.SYSTEM -> isSystemInDarkTheme()
    }
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = FaturaTypography,
        content = content
    )
}
