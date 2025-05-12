package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.theme.DarkNavyBlue
import com.example.flyapp.ui.theme.theme.DeepBlue
import com.example.flyapp.ui.theme.theme.GoldColor
import com.example.flyapp.ui.theme.theme.MediumBlue
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random


data class Particle(
    var position: Offset,
    var velocity: Offset,
    var radius: Float,
    var alpha: Float,
    var color: Color
)

@Composable
fun SplashScreen(navController: NavHostController) {
    val animationState = remember {
        SplashAnimationState(
            logoScale = Animatable(0.3f),
            logoAlpha = Animatable(0f),
            textAlpha = Animatable(0f),
            glowScale = Animatable(0f),
            particleAnimatable = Animatable(0f)
        )
    }


    var showExplosion by remember { mutableStateOf(false) }
    var particleAnimationProgress by remember { mutableFloatStateOf(0f) }


    val particles = remember {
        List(80) {
            Particle(
                position = Offset.Zero,
                velocity = Offset.Zero,
                radius = Random.nextFloat() * 5f + 2f,
                alpha = 1f,
                color = when {
                    it % 3 == 0 -> GoldColor
                    it % 3 == 1 -> Color.White
                    else -> Color.White.copy(alpha = 0.7f)
                }
            )
        }
    }


    LaunchedEffect(animationState.particleAnimatable.value) {
        particleAnimationProgress = animationState.particleAnimatable.value
    }


    LaunchedEffect(key1 = true) {

        animationState.logoAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
        )


        animationState.logoScale.animateTo(
            targetValue = 1.3f,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )


        showExplosion = true


        animationState.logoScale.animateTo(
            targetValue = 1.6f,
            animationSpec = tween(durationMillis = 600, easing = LinearEasing)
        )


        animationState.logoScale.animateTo(
            targetValue = 3.5f,
            animationSpec = tween(durationMillis = 800, easing = FastOutLinearInEasing)
        )


        animationState.textAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 600, delayMillis = 300)
        )


        animationState.particleAnimatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )


        animationState.glowScale.animateTo(
            targetValue = 1.1f,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        )


//        animationState.logoScale.animateTo(
//            targetValue = 150f, // تقليل لتحسين الأداء
//            animationSpec = tween(durationMillis = 700, easing = FastOutLinearInEasing)
//        )


        delay(3000 - 2900)
        navController.navigate(Screen.WelcomeScreen.route) {
            popUpTo(Screen.SplashScreen.route) { inclusive = true }
        }
    }


    val infiniteTransition = rememberInfiniteTransition(label = "pulse_animation")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_effect"
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepBlue,
                        MediumBlue,
                        DarkNavyBlue
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {

        SecurityPatternBackground()


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // شعار متحرك مع تأثير توهج
            LogoWithEffects(
                logoScale = animationState.logoScale.value,
                logoAlpha = animationState.logoAlpha.value,
                glowScale = animationState.glowScale.value,
                pulse = pulse,
                showExplosion = showExplosion,
                particles = particles,
                particleAnimationProgress = particleAnimationProgress
            )

            Spacer(modifier = Modifier.height(32.dp))

            // اسم التطبيق مع تحريك
            AppNameWithTagline(textAlpha = animationState.textAlpha.value)
        }
    }
}

@Composable
private fun SecurityPatternBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // رسم خطوط نمط الأمان
        val pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 3f), 0f)

        // خطوط متقاطعة
        drawLine(
            color = Color.White.copy(alpha = 0.05f),
            start = Offset(0f, 0f),
            end = Offset(canvasWidth, canvasHeight),
            strokeWidth = 1f,
            pathEffect = pathEffect
        )

        drawLine(
            color = Color.White.copy(alpha = 0.05f),
            start = Offset(canvasWidth, 0f),
            end = Offset(0f, canvasHeight),
            strokeWidth = 1f,
            pathEffect = pathEffect
        )

        // رسم علامة مائية دائرية
        val stroke = Stroke(width = 1f, pathEffect = pathEffect)
        for (i in 1..4) {
            drawCircle(
                color = Color.White.copy(alpha = 0.03f),
                radius = canvasHeight / 3f * i / 4f,
                center = Offset(canvasWidth / 2f, canvasHeight / 2f),
                style = stroke
            )
        }

        // دوائر خلفية إضافية خفيفة
        drawCircle(
            color = Color.White.copy(alpha = 0.02f),
            radius = canvasHeight / 2f,
            center = Offset(canvasWidth / 2f, canvasHeight / 2f)
        )
    }
}

@Composable
private fun LogoWithEffects(
    logoScale: Float,
    logoAlpha: Float,
    glowScale: Float,
    pulse: Float,
    showExplosion: Boolean,
    particles: List<Particle>,
    particleAnimationProgress: Float
) {
    Box(
        modifier = Modifier.size(180.dp),
        contentAlignment = Alignment.Center
    ) {
        // خلفية توهج
        Box(
            modifier = Modifier
                .size(160.dp)
                .scale(glowScale)
                .alpha(logoAlpha * 0.7f)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GoldColor.copy(alpha = 0.7f),
                            GoldColor.copy(alpha = 0.0f)
                        )
                    )
                )
        )

        // الشعار
        Image(
            painter = painterResource(id = R.drawable.front_plane),
            contentDescription = "FlyApp Logo",
            modifier = Modifier
                .size(100.dp)
                .scale(logoScale * pulse)
                .alpha(logoAlpha)
        )

        // تأثير جزيئات الانفجار
        if (showExplosion) {
            ExplosionParticles(
                particles = particles,
                particleAnimationProgress = particleAnimationProgress
            )
        }
    }
}

@Composable
private fun ExplosionParticles(
    particles: List<Particle>,
    particleAnimationProgress: Float
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)


        particles.forEachIndexed { index, particle ->
            if (particle.position == Offset.Zero) {
                // حساب زوايا عشوائية للتشتت
                val angle = Random.nextDouble(0.0, 2 * Math.PI).toFloat()
                val speed = Random.nextFloat() * 6f + 2f

                particle.position = center
                particle.velocity = Offset(
                    cos(angle) * speed,
                    sin(angle) * speed
                )
            }

            // حساب موضع الجزيء بناءً على تقدم التحريك
            val currentPosition = Offset(
                particle.position.x + particle.velocity.x * particleAnimationProgress * size.width * 0.12f,
                particle.position.y + particle.velocity.y * particleAnimationProgress * size.height * 0.12f
            )

            // حساب الشفافية بناءً على تقدم التحريك (تلاشي مع مرور الوقت)
            val currentAlpha = particle.alpha * (1f - particleAnimationProgress * 0.8f)

            // رسم الجزيء
            drawCircle(
                color = particle.color.copy(alpha = currentAlpha),
                radius = particle.radius * (1f - particleAnimationProgress * 0.4f),
                center = currentPosition
            )
        }
    }
}

@Composable
private fun AppNameWithTagline(textAlpha: Float) {
    // اسم التطبيق مع تحريك
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.alpha(textAlpha)
    ) {
        Text(
            text = "FLY",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "APP",
            color = GoldColor,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    // شعار التطبيق
    Text(
        text = "Explore the world with ease",
        color = Color.White.copy(alpha = 0.8f),
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center,
        modifier = Modifier.alpha(textAlpha)
    )
}

private class SplashAnimationState(
    val logoScale: Animatable<Float, AnimationVector1D>,
    val logoAlpha: Animatable<Float, AnimationVector1D>,
    val textAlpha: Animatable<Float, AnimationVector1D>,
    val glowScale: Animatable<Float, AnimationVector1D>,
    val particleAnimatable: Animatable<Float, AnimationVector1D>
)

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}