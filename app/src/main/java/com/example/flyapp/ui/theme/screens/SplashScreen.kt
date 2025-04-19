package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.ParticleEffectBackground
import com.example.flyapp.ui.theme.navigition.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    // Animation states
    var startAnimation by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }

    // Scale animation for the logo
    val scaleAnimation by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "scale"
    )

    // Alpha animation for the tagline
    val alphaAnimation by animateFloatAsState(
        targetValue = if (showTagline) 1f else 0f,
        animationSpec = tween(
            durationMillis = 800,
            easing = FastOutSlowInEasing
        ),
        label = "alpha"
    )

    // Handle the navigation after splash screen delay
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(1000)
        showTagline = true
        delay(2000)
        // Navigate to the main screen after splash animation completes
        // Replace "home_screen" with your actual home screen route
        navController.navigate(Screen.WelcomeScreen.route) {
            popUpTo(Screen.SplashScreen.route) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF001034),
                        Color(0xFF003045),
                        Color(0xFF004D40)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Reuse the particle effect from the app
        ParticleEffectBackground()

        // Centered content with logo and app name
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            // Logo with pulsing effect
            LogoWithPulseEffect(scaleAnimation)

            Spacer(modifier = Modifier.height(24.dp))

            // App Name
            Text(
                text = "FlyApp",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline with fade-in effect
            Text(
                text = "Your journey begins here",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                modifier = Modifier.alpha(alphaAnimation)
            )
        }

        // Version text at the bottom
        Text(
            text = "Version 1.0.0",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}

@Composable
fun LogoWithPulseEffect(scale: Float) {
    // Animation for the outer glow
    val infiniteTransition = rememberInfiniteTransition(label = "logo_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.scale(scale)
    ) {
        // Outer pulse glow
        Canvas(modifier = Modifier.size(140.dp)) {
            drawCircle(
                color = Color(0xFF64B5F6).copy(alpha = pulseAlpha),
                radius = size.minDimension / 2
            )
        }

        // Inner circle with logo
        Box(
            modifier = Modifier
                .size(260.dp)
                .background(Color(0xFF0D47A1).copy(alpha = 0.8f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            // Replace R.drawable.app_logo with your actual app logo resource
            Image(
                painter = painterResource(R.drawable.logo2),
                contentDescription = "App Logo",
                modifier = Modifier.size(200.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(rememberNavController())
}