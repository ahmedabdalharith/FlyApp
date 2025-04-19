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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.flyapp.ui.theme.screens.Particle


@Composable
fun ParticleEffectBackground() {
    val particles = remember {
        List(100) {
            Particle(
                x = Math.random().toFloat(),
                y = Math.random().toFloat(),
                radius = (1 + Math.random() * 2).toFloat(),
                speed = (0.0005 + Math.random() * 0.001).toFloat()
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "particle_animation")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing)
        ),
        label = "star_time"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { particle ->
                val x = (particle.x + particle.speed * time) % 1f
                val alpha = (0.3f + Math.random().toFloat() * 0.7f)

                drawCircle(
                    color = Color.White.copy(alpha = alpha),
                    radius = particle.radius.dp.toPx(),
                    center = Offset(x * size.width, particle.y * size.height)
                )
            }
        }
    }
}