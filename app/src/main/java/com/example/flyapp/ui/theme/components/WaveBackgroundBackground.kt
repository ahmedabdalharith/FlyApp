package com.example.flyapp.ui.theme.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import kotlin.math.sin

@Composable
fun WaveBackgroundEffect(
    primaryColor: Color = Color(0xFF4285F4),
    secondaryColor: Color = Color(0xFF34A853),
    waveCount: Int = 3
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_animation")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing)
        ),
        label = "wave_progress"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val waveHeight = height / 8
            val waves = (0 until waveCount).map { waveIndex ->
                // Create different phase shifts for each wave
                val phaseShift = waveIndex * 0.3f
                val animationOffset = (animatedProgress + phaseShift) % 1f

                // Create a path for each wave
                val path = Path().apply {
                    moveTo(0f, height)

                    // Draw the wavy line
                    for (x in 0..width.toInt() step 5) {
                        val waveAmplitude = waveHeight * (1f - waveIndex * 0.2f)
                        val y = height - (waveIndex * height / (waveCount * 1.5f)) -
                                waveAmplitude * sin((x / width * 4 * Math.PI + animationOffset * Math.PI * 2).toFloat())
                        lineTo(x.toFloat(), y.toFloat())
                    }

                    // Complete the path to create a filled shape
                    lineTo(width, height)
                    close()
                }

                // Create gradient colors that vary based on wave index
                val colors = listOf(
                    primaryColor.copy(alpha = 0.7f - (waveIndex * 0.15f)),
                    secondaryColor.copy(alpha = 0.6f - (waveIndex * 0.15f))
                )

                Pair(path, colors)
            }

            // Draw each wave
            waves.forEach { (path, colors) ->
                drawPath(
                    path = path,
                    brush = Brush.linearGradient(
                        colors = colors,
                        start = Offset(0f, 0f),
                        end = Offset(width, height)
                    )
                )
            }
        }
    }
}