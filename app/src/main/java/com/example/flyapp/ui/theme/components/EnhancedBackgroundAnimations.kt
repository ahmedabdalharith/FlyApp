package com.example.flyapp.ui.theme.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.random.Random

@Composable
fun EnhancedBackgroundAnimations() {
    // Track component size to determine particle count
    var size by remember { mutableStateOf(IntSize.Zero) }
    // Number of particles based on screen size
    val particles = remember { List(50) { ParticleState() } }
    // Animate particles
    val infiniteTransition = rememberInfiniteTransition(label = "particle_animation")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particle_progress"
    )

    // Create a shimmer effect in the background
    val shimmerColors = listOf(
        Color.White.copy(alpha = 0f),
        Color.White.copy(alpha = 0.05f),
        Color.White.copy(alpha = 0.1f),
        Color.White.copy(alpha = 0.05f),
        Color.White.copy(alpha = 0f)
    )

    val shimmerTransition = rememberInfiniteTransition(label = "shimmer_transition")
    val shimmerTranslateAnim by shimmerTransition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { size = it.size }
    ) {
        // Shimmer effect
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = shimmerColors,
                        start = Offset(shimmerTranslateAnim, 0f),
                        end = Offset(shimmerTranslateAnim + 500f, 500f)
                    )
                )
        )

        // Particles
        particles.forEach { particle ->
            val xPos = size.width * particle.xPos
            val yPos = size.height * (particle.yPos - animatedProgress * particle.speed)
            val adjustedYPos = if (yPos < 0) {
                size.height + yPos % size.height
            } else {
                yPos % size.height
            }

            // Animated particle properties
            val particleColor = Color(
                red = particle.color.red,
                green = particle.color.green,
                blue = particle.color.blue,
                alpha = particle.opacity * 0.3f
            )

            Box(
                modifier = Modifier
                    .size(particle.size.dp)
                    .offset(
                        x = xPos.dp,
                        y = adjustedYPos.dp
                    )
                    .alpha(particle.opacity)
                    .background(
                        color = particleColor,
                        shape = CircleShape
                    )
                    .blur(radius = (particle.size / 3).dp)
            )
        }

        // Animated wave
        val waveTransition = rememberInfiniteTransition(label = "wave_animation")
        val wavePhase by waveTransition.animateFloat(
            initialValue = 0f,
            targetValue = 2f * Math.PI.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(10000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "wave_phase"
        )

        // First wave
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .offset(y = (size.height * 0.6f).dp)
                .alpha(0.15f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF2196F3),
                            Color.Transparent
                        ),
                        startY = (50f * kotlin.math.sin(wavePhase)).toFloat(),
                        endY = 200f
                    )
                )
        )

        // Second wave (offset phase)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .offset(y = (size.height * 0.7f).dp)
                .alpha(0.1f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4CAF50),
                            Color.Transparent
                        ),
                        startY = (30f * kotlin.math.sin(wavePhase + Math.PI.toFloat())).toFloat(),
                        endY = 150f
                    )
                )
        )
    }
}
// Class to represent particle state
class ParticleState {
    val size = Random.nextInt(2, 6).toFloat()
    val opacity = Random.nextFloat() * 0.5f + 0.1f
    val xPos = Random.nextFloat()
    val yPos = Random.nextFloat()
    val speed = Random.nextFloat() * 0.5f + 0.1f
    val color = when(Random.nextInt(3)) {
        0 -> Color(0xFF2196F3) // Blue
        1 -> Color(0xFF4CAF50) // Green
        else -> Color.White
    }
}
