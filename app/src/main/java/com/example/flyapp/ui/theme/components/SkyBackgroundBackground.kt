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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.cos
import kotlin.math.sin

data class Cloud(
    val x: Float,
    val y: Float,
    val scale: Float,
    val speed: Float,
    val opacity: Float
)

@Composable
fun SkyBackgroundEffect(
    skyTopColor: Color = Color(0xFF64B5F6),  // Light blue
    skyBottomColor: Color = Color(0xFFBBDEFB),  // Very light blue
    cloudColor: Color = Color.White,
    sunColor: Color = Color(0xFFFFEB3B)  // Yellow
) {
    // Create some random clouds
    val clouds = remember {
        List(15) {
            Cloud(
                x = Math.random().toFloat() * 1.2f - 0.1f,  // Some clouds start outside the screen
                y = 0.1f + Math.random().toFloat() * 0.5f,  // Upper part of the sky
                scale = 0.05f + Math.random().toFloat() * 0.15f,
                speed = (0.00005f + Math.random().toFloat() * 0.0001f),
                opacity = 0.6f + Math.random().toFloat() * 0.4f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "sky_animation")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing)
        ),
        label = "cloud_movement"
    )

    // Sun position animation
    val sunAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(180000, easing = LinearEasing)
        ),
        label = "sun_movement"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw sky gradient
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(skyTopColor, skyBottomColor)
                )
            )

            // Draw sun
            val sunRadius = size.width * 0.08f
            val sunCenterX = size.width * 0.5f + cos(Math.toRadians(sunAngle.toDouble()).toFloat()) * size.width * 0.4f
            val sunCenterY = size.height * 0.5f - sin(Math.toRadians(sunAngle.toDouble()).toFloat()) * size.height * 0.4f

            drawCircle(
                color = sunColor,
                radius = sunRadius,
                center = Offset(sunCenterX, sunCenterY)
            )

            // Draw sun rays
            val rayCount = 12
            val rayLength = sunRadius * 0.5f
            for (i in 0 until rayCount) {
                val angle = i * (360f / rayCount)
                val startX = sunCenterX + cos(Math.toRadians(angle.toDouble()).toFloat()) * sunRadius
                val startY = sunCenterY + sin(Math.toRadians(angle.toDouble()).toFloat()) * sunRadius
                val endX = sunCenterX + cos(Math.toRadians(angle.toDouble()).toFloat()) * (sunRadius + rayLength)
                val endY = sunCenterY + sin(Math.toRadians(angle.toDouble()).toFloat()) * (sunRadius + rayLength)

                drawLine(
                    color = sunColor.copy(alpha = 0.7f),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 8f
                )
            }

            // Draw clouds
            clouds.forEach { cloud ->
                val cloudX = (cloud.x + cloud.speed * time) % 1.2f
                drawCloud(
                    cloudX * size.width,
                    cloud.y * size.height,
                    cloud.scale * size.width,
                    cloudColor.copy(alpha = cloud.opacity)
                )
            }
        }
    }
}

// Helper function to draw a fluffy cloud
private fun DrawScope.drawCloud(x: Float, y: Float, scale: Float, color: Color) {
    val cloudPath = Path().apply {
        // Main cloud body
        addOval(androidx.compose.ui.geometry.Rect(
            left = x - scale * 1.0f,
            top = y - scale * 0.5f,
            right = x + scale * 1.0f,
            bottom = y + scale * 0.5f
        ))

        // Cloud puffs
        addOval(androidx.compose.ui.geometry.Rect(
            left = x - scale * 0.7f,
            top = y - scale * 0.9f,
            right = x + scale * 0.3f,
            bottom = y - scale * 0.1f
        ))

        addOval(androidx.compose.ui.geometry.Rect(
            left = x - scale * 0.3f,
            top = y - scale * 1.1f,
            right = x + scale * 0.7f,
            bottom = y - scale * 0.1f
        ))

        addOval(androidx.compose.ui.geometry.Rect(
            left = x + scale * 0.4f,
            top = y - scale * 0.8f,
            right = x + scale * 1.4f,
            bottom = y + scale * 0.2f
        ))
    }

    drawPath(
        path = cloudPath,
        color = color
    )
}