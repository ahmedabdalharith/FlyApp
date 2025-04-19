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
