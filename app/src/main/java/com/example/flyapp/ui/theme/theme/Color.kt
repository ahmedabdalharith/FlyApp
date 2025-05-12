package com.example.flyapp.ui.theme.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val BackgroundColor=Brush.verticalGradient(
    colors = listOf(Color(0xFF001034),
        Color(0xFF003045),
        Color(0xFF004D40)
    )
)
val BackgroundColor2=Brush.verticalGradient(
    colors = listOf( Color(0xFF001034),
        Color(0xFF0A1929),
        Color(0xFF0A2942)
    )
)

val TicketGradientSkyBlue = Brush.verticalGradient(
    colors = listOf(Color(0xFF81D4FA), Color(0xFF0288D1))
)

val TicketGradientDarkBlueGray = Brush.horizontalGradient(
    colors = listOf(Color(0xFF37474F), Color(0xFFB0BEC5))
)

val TicketGradientWarmOrange = Brush.verticalGradient(
    colors = listOf(Color(0xFFFFCC80), Color(0xFFFFA726))
)

val TicketGradientBlueViolet = Brush.linearGradient(
    colors = listOf(Color(0xFF3F51B5), Color(0xFF9C27B0))
)
val Gradient = Brush.verticalGradient(
colors = listOf(
Color(0xFFE8F1FC), // لون فاتح أزرق في الأعلى
Color(0xFFF5F7FB)  // لون أفتح قريب من الأبيض في الأسفل
)
)

val SaudiaBlue = Color(0xFF074D81)

// Secondary green color used for accents and highlights
val SaudiaGreen = Color(0xFF00A652)

// Gold color for premium and first class indicators
val GoldPremium = Color(0xFFDEB74A)

// Gradient backgrounds
val BlueGradient = listOf(
    Color(0xFF0D5C9C),
    Color(0xFF072D60)
)

// Secondary and accent colors
val SaudiaDarkBlue = Color(0xFF002D50)
val SaudiaLightBlue = Color(0xFF64B5F6)
val SaudiaAccentOrange = Color(0xFFFF6C00)

// Background colors
val BackgroundDark = Color(0xFF0A192F)
val BackgroundMedium = Color(0xFF0D3A5F)
val BackgroundLight = Color(0xFF0A578C)

// Text colors
val TextPrimary = Color.White
val TextSecondary = Color.White.copy(alpha = 0.7f)
val TextMuted = Color.White.copy(alpha = 0.5f)

// Status colors
val StatusSuccess = Color(0xFF4CAF50)
val StatusWarning = Color(0xFFFFC107)
val StatusError = Color(0xFFFF5252)

// Create a vertical gradient brush from dark blue to medium blue
val BlueDarkToMediumGradient = Brush.verticalGradient(
    colors = listOf(
        BackgroundDark,
        BackgroundMedium
    )
)

// Create a horizontal gradient brush for premium elements
val PremiumGradient = Brush.horizontalGradient(
    colors = listOf(
        GoldPremium.copy(alpha = 0.7f),
        GoldPremium,
        GoldPremium.copy(alpha = 0.7f)
    )
)

// Define the gold color used in the welcome screen
val GoldColor = Color(0xFFDAA520)
val DarkNavyBlue = Color(0xFF0A1A35)
val MediumBlue = Color(0xFF0E3B6F)
val DeepBlue = Color(0xFF082147)

