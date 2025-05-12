package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.FlightTopAppBar
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.theme.DarkNavyBlue
import com.example.flyapp.ui.theme.theme.DeepBlue
import com.example.flyapp.ui.theme.theme.GoldColor
import com.example.flyapp.ui.theme.theme.MediumBlue
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navController: NavHostController) {
    // State for animated page indicator
    var currentPage by remember { mutableIntStateOf(0) }
    val pages = remember { listOf("Discover", "Plan", "Travel") }

    // Animation for page transitions
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000) // 4 seconds per page
            currentPage = (currentPage + 1) % pages.size
        }
    }

    // Animation for floating effect
    val infiniteTransition = rememberInfiniteTransition(label = "floating_animation")
    val elementGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "element_glow"
    )

    // Main background with gradient (similar to AirplaneSeatsScreen)
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
            )
    ) {
        // Security pattern background (from AirplaneSeatsScreen)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Draw security pattern lines (like passport security pattern)
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 3f), 0f)
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

            // Draw circular watermark
            val stroke = Stroke(width = 1f, pathEffect = pathEffect)
            for (i in 1..5) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.03f),
                    radius = canvasHeight / 3f * i / 5f,
                    center = Offset(canvasWidth / 2f, canvasHeight / 2f),
                    style = stroke
                )
            }
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Draw subtle background elements
            drawCircle(
                color = Color.White.copy(alpha = 0.03f),
                radius = canvasHeight / 2f,
                center = Offset(canvasWidth / 2f, canvasHeight / 2f)
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.02f),
                radius = canvasHeight / 3f,
                center = Offset(canvasWidth / 2f, canvasHeight / 2f)
            )
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            FlightTopAppBar(
                textOne = "FLY",
                textTwo = "APP",
                navController = navController,
                isBacked = false
            )

            // Top section - Content and animation
            Box(
                modifier = Modifier
                    .weight(0.6f)
                    .fillMaxWidth()
            ) {
                // Page content
                AnimatedOnboardingContent(
                    page = currentPage,
                    glowFactor = elementGlow,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp)
                )

                // Page indicator dots
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(pages.size) { page ->
                        val isSelected = page == currentPage
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(if (isSelected) 10.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) GoldColor else Color.White.copy(alpha = 0.5f)
                                )
                        )
                    }
                }
            }

            // Bottom section - Authentication options
            Column(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Authentication card (with gold accents like in AirplaneSeatsScreen)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(16.dp))
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    GoldColor.copy(alpha = 0.7f),
                                    GoldColor.copy(alpha = 0.3f),
                                    GoldColor.copy(alpha = 0.7f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = DarkNavyBlue.copy(alpha = 0.85f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Welcome to FlyApp",
                            color = GoldColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Sign in button (gold color like AirplaneSeatsScreen)
                        Button(
                            onClick = {
                                navController.navigate(Screen.LoginScreen.route) {
                                    popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldColor,
                                contentColor = DarkNavyBlue
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Sign In",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Create account button (darker blue with gold border)
                        Button(
                            onClick = {
                                navController.navigate(Screen.SignUpScreen.route) {
                                    popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .border(
                                    width = 1.dp,
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            GoldColor.copy(alpha = 0.7f),
                                            GoldColor.copy(alpha = 0.3f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MediumBlue
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Create Account",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldColor
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Guest option (with gold color like AirplaneSeatsScreen)
                        TextButton(
                            onClick = {
                                navController.navigate(Screen.MainScreen.route) {
                                    popUpTo(Screen.WelcomeScreen.route) { inclusive = true }
                                }
                            }
                        ) {
                            Text(
                                text = "Continue as Guest",
                                color = GoldColor.copy(alpha = 0.9f),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedOnboardingContent(page: Int, glowFactor: Float, modifier: Modifier = Modifier) {
    val content = when (page) {
        0 -> OnboardingPage(
            title = "Explore Global Destinations",
            description = "Discover unique travel experiences across the world",
            icon = painterResource(R.drawable.explore_ic)
        )
        1 -> OnboardingPage(
            title = "Plan Your Journey",
            description = "Create personalized travel itineraries with ease",
            icon = painterResource(R.drawable.location)
        )
        else -> OnboardingPage(
            title = "Travel With Confidence",
            description = "Enjoy seamless and secure access to global destinations",
            icon = painterResource(R.drawable.myplane)
        )
    }

    // Animation for icon floating
    val infiniteTransition = rememberInfiniteTransition(label = "icon_float")
    val iconOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_float_anim"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // Icon with floating animation (updated with gold color)
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .offset(y = iconOffset.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                GoldColor.copy(alpha = glowFactor),
                                GoldColor.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                GoldColor,
                                GoldColor.copy(alpha = 0.5f)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = content.icon,
                    contentDescription = content.title,
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title (with gold color)
            Text(
                text = content.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = GoldColor,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.SansSerif
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = content.description,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
    }
}

// Data class for onboarding page content
data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: Painter
)

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(rememberNavController())
}