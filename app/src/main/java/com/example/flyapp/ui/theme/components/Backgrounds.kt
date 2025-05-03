package com.example.flyapp.ui.theme.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.flyapp.R

//
//@Composable
//fun ParticleEffectBackground() {
//    val particles = remember {
//        List(100) {
//            Particle(
//                x = Math.random().toFloat(),
//                y = Math.random().toFloat(),
//                radius = (1 + Math.random() * 2).toFloat(),
//                speed = (0.0005 + Math.random() * 0.001).toFloat()
//            )
//        }
//    }
//
//    val infiniteTransition = rememberInfiniteTransition(label = "particle_animation")
//    val time by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1000f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(20000, easing = LinearEasing)
//        ),
//        label = "star_time"
//    )
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Image(
//            painter = painterResource(id = R.drawable.earth_night),
//            contentDescription = "Logo",
//            modifier = Modifier
//                .fillMaxSize(),
//            contentScale= ContentScale.Crop
//
//        )
//        Canvas(modifier = Modifier.fillMaxSize()) {
//            particles.forEach { particle ->
//                val x = (particle.x + particle.speed * time) % 1f
//                val alpha = (0.3f + Math.random().toFloat() * 0.7f)
//
//                drawCircle(
//                    color = Color.White.copy(alpha = alpha),
//                    radius = particle.radius.dp.toPx(),
//                    center = Offset(x * size.width, particle.y * size.height)
//                )
//            }
//        }
//    }
//}
//@Composable
//fun ParticleEffectBackground() {
//    // Animation for floating particles
//    val infiniteTransition = rememberInfiniteTransition(label = "particles")
//    val particleOffset1 by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(20000, easing = FastOutSlowInEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "particle1"
//    )
//
//    val particleOffset2 by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(15000, easing = FastOutSlowInEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "particle2"
//    )
//
//    val particleOffset3 by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(25000, easing = FastOutSlowInEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "particle3"
//    )
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        // Particle 1
//        Box(
//            modifier = Modifier
//                .size(200.dp)
//                .offset(
//                    x = (particleOffset1 * 100).dp,
//                    y = (particleOffset2 * 200).dp
//                )
//                .alpha(0.2f)
//                .blur(50.dp)
//                .background(
//                    Color(0xFF64B5F6),
//                    CircleShape
//                )
//        )
//
//        // Particle 2
//        Box(
//            modifier = Modifier
//                .size(300.dp)
//                .offset(
//                    x = -(particleOffset2 * 150).dp,
//                    y = (particleOffset3 * 150).dp
//                )
//                .alpha(0.15f)
//                .blur(70.dp)
//                .background(
//                    Color(0xFF4CAF50),
//                    CircleShape
//                )
//        )
//
//        // Particle 3
//        Box(
//            modifier = Modifier
//                .size(250.dp)
//                .offset(
//                    x = (particleOffset3 * 150).dp,
//                    y = -(particleOffset1 * 100).dp
//                )
//                .alpha(0.1f)
//                .blur(60.dp)
//                .background(
//                    Color(0xFFFFD700),
//                    CircleShape
//                )
//        )
//    }
//}

@Composable
fun ParticleEffectBackground(){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF001034),
                        Color(0xFF003045),
                        Color(0xFF004D40)
                    )
                )
            )
    )
}