package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt
import kotlin.text.toFloat
import kotlin.text.toInt
import kotlin.times

val SkyBlue = Color(0xFF87CEEB)
// Data class to represent a flight status
data class FlightStatusTest(
    val flightNumber: String,
    val departure: String,
    val departureCode: String,
    val departureTime: String,
    val departureActualTime: String,
    val destination: String,
    val destinationCode: String,
    val arrivalTime: String,
    val arrivalEstimatedTime: String,
    val status: String,
    val gate: String,
    val terminal: String,
    val altitude: Int,
    val speed: Int,
    val aircraft: String,
    val pilotName: String,
    val coPilotName: String,
    val flightDuration: String,
    val distanceKm: Int,
    val distanceTraveled: Int,
    val weatherDeparture: String,
    val weatherDestination: String,
    val temperatureDeparture: Int,
    val temperatureDestination: Int
)

enum class FlightTag(val color: Color, val text: String) {
    ONTIME(Color(0xFF4CAF50), "ON TIME"),
    DELAYED(Color(0xFFFFA000), "DELAYED"),
    BOARDING(Color(0xFF2196F3), "BOARDING"),
    INFLIGHT(Color(0xFF9C27B0), "IN FLIGHT"),
    LANDED(Color(0xFF795548), "LANDED"),
    CANCELLED(Color(0xFFE53935), "CANCELLED")
}

@Composable
fun FlightStatusScreen(
    navController: NavHostController,
    flightNumber: String = "FLY-1234"
) {
    // State for animations
    var isMainContentVisible by remember { mutableStateOf(false) }
    var isMapVisible by remember { mutableStateOf(false) }
    var isFlightInfoVisible by remember { mutableStateOf(false) }
    var isWeatherVisible by remember { mutableStateOf(false) }
    var isCrewVisible by remember { mutableStateOf(false) }

    // Mock flight data
    val flightStatus = remember {
        FlightStatusTest(
            flightNumber = flightNumber,
            departure = "New York",
            departureCode = "JFK",
            departureTime = "14:30",
            departureActualTime = "14:35",
            destination = "London",
            destinationCode = "LHR",
            arrivalTime = "03:45",
            arrivalEstimatedTime = "03:55",
            status = "IN FLIGHT",
            gate = "G12",
            terminal = "T5",
            altitude = 11000,
            speed = 837,
            aircraft = "Boeing 787-9",
            pilotName = "Capt. Sarah Johnson",
            coPilotName = "First Officer Michael Chen",
            flightDuration = "7h 15m",
            distanceKm = 5541,
            distanceTraveled = 3125,
            weatherDeparture = "Cloudy",
            weatherDestination = "Rainy",
            temperatureDeparture = 24,
            temperatureDestination = 12
        )
    }

    // Current time state
    var currentTime by remember { mutableStateOf("") }

    // Update time every minute
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = SimpleDateFormat("HH:mm, dd MMM yyyy", Locale.getDefault()).format(Date())
            delay(60000) // Update every minute
        }
    }

    // Sequential animations
    LaunchedEffect(Unit) {
        delay(300)
        isMainContentVisible = true
        delay(500)
        isMapVisible = true
        delay(300)
        isFlightInfoVisible = true
        delay(300)
        isWeatherVisible = true
        delay(300)
        isCrewVisible = true
    }

    // Main background with gradient
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
        // Security pattern background (matching other screens)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Draw security pattern lines
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

        // Main content column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top app bar
            FlightTopAppBar(
                textOne = "FLY",
                textTwo = "APP",
                navController = navController
            )

            // Flight status card
            AnimatedVisibility(
                visible = isMainContentVisible,
                enter = fadeIn(animationSpec = tween(800)) +
                        slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { it / 3 }
                        )
            ) {
                FlightStatusCard(
                    flightStatus = flightStatus,
                    currentTime = currentTime
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight map
            AnimatedVisibility(
                visible = isMapVisible,
                enter = fadeIn(animationSpec = tween(1000)) +
                        scaleIn(
                            animationSpec = tween(1000),
                            initialScale = 0.95f
                        )
            ) {
                FlightMapCard(flightStatus = flightStatus)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight info
            AnimatedVisibility(
                visible = isFlightInfoVisible,
                enter = fadeIn(animationSpec = tween(500))
            ) {
                FlightInfoCard(flightStatus = flightStatus)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weather info
            AnimatedVisibility(
                visible = isWeatherVisible,
                enter = fadeIn(animationSpec = tween(500))
            ) {
                WeatherCard(flightStatus = flightStatus)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Crew info
            AnimatedVisibility(
                visible = isCrewVisible,
                enter = fadeIn(animationSpec = tween(500))
            ) {
                CrewInfoCard(flightStatus = flightStatus)
            }

            // Buttons for actions
            Spacer(modifier = Modifier.height(24.dp))
            ActionButtons(navController = navController, flightNumber = flightNumber)

            // Bottom spacer for scrolling
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FlightStatusCard(
    flightStatus: FlightStatusTest,
    currentTime: String
) {
    val flightTagInfo = when (flightStatus.status) {
        "ON TIME" -> FlightTag.ONTIME
        "DELAYED" -> FlightTag.DELAYED
        "BOARDING" -> FlightTag.BOARDING
        "IN FLIGHT" -> FlightTag.INFLIGHT
        "LANDED" -> FlightTag.LANDED
        "CANCELLED" -> FlightTag.CANCELLED
        else -> FlightTag.ONTIME
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor,
                        Color(0xFFFFD700),
                        GoldColor
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with flight number and status tag
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FLIGHT STATUS",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldColor
                )

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = flightTagInfo.color
                    ),
                    modifier = Modifier.shadow(4.dp, RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = flightTagInfo.text,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = flightStatus.flightNumber,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "Last updated: $currentTime",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 2.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GoldColor.copy(alpha = 0.3f)
            )

            // Flight route info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // Departure
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = flightStatus.departureCode,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = flightStatus.departure,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.access_time),
                            contentDescription = "Departure time",
                            tint = GoldColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = flightStatus.departureTime,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    if (flightStatus.departureTime != flightStatus.departureActualTime) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Actual: ",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            Text(
                                text = flightStatus.departureActualTime,
                                fontSize = 12.sp,
                                color = flightTagInfo.color,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Flight path visualization
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = flightStatus.flightDuration,
                        fontSize = 12.sp,
                        color = GoldColor,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        // Circle for departure
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(GoldColor, CircleShape)
                        )

                        // Dotted line to show flight path
                        Canvas(
                            modifier = Modifier
                                .width(80.dp)
                                .height(2.dp)
                        ) {
                            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                            drawLine(
                                color = GoldColor,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 2f,
                                pathEffect = pathEffect
                            )
                        }

                        // Animated plane
                        FlightIcon()

                        // Remaining dotted line
                        Canvas(
                            modifier = Modifier
                                .width(80.dp)
                                .height(2.dp)
                        ) {
                            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                            drawLine(
                                color = GoldColor,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 2f,
                                pathEffect = pathEffect
                            )
                        }

                        // Circle for arrival
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(GoldColor, CircleShape)
                        )
                    }

                    // Progress text
                    Text(
                        text = "${(flightStatus.distanceTraveled * 100 / flightStatus.distanceKm)}% completed",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                // Destination
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = flightStatus.destinationCode,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = flightStatus.destination,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.access_time),
                            contentDescription = "Arrival time",
                            tint = GoldColor,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = flightStatus.arrivalTime,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    if (flightStatus.arrivalTime != flightStatus.arrivalEstimatedTime) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Est: ",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.6f)
                            )
                            Text(
                                text = flightStatus.arrivalEstimatedTime,
                                fontSize = 12.sp,
                                color = flightTagInfo.color,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight progress bar
            LinearProgressIndicator(
            progress = { flightStatus.distanceTraveled.toFloat() / flightStatus.distanceKm.toFloat() },
            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
            color = GoldColor,
            trackColor = GoldColor.copy(alpha = 0.2f),
            )

            // Gate and terminal info
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    title = "GATE",
                    value = flightStatus.gate
                )

                InfoItem(
                    title = "TERMINAL",
                    value = flightStatus.terminal
                )

                InfoItem(
                    title = "AIRCRAFT",
                    value = flightStatus.aircraft.split(" ")[0]
                )
            }
        }
    }
}

@Composable
fun FlightIcon() {
    val infiniteTransition = rememberInfiniteTransition(label = "plane_animation")
    val bobAnimation by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "plane_bob"
    )

    Icon(
        painter = painterResource(id = R.drawable.plane_path),
        contentDescription = "Flight",
        tint = GoldColor,
        modifier = Modifier
            .size(20.dp)
            .offset(y = bobAnimation.dp)
    )
}

@Composable
fun InfoItem(
    title: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            fontSize = 11.sp,
            color = GoldColor.copy(alpha = 0.8f)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
fun FlightMapCard(flightStatus: FlightStatusTest) {
    // Animated travel progress
    val progress = flightStatus.distanceTraveled.toFloat() / flightStatus.distanceKm.toFloat()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.7f),
                        GoldColor.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FLIGHT ROUTE",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldColor
                )

                Text(
                    text = "${flightStatus.distanceTraveled} / ${flightStatus.distanceKm} km",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Map visualization (simplified with gradient background)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF1a2a48),
                                Color(0xFF0e1526)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = GoldColor.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                // Grid lines
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                    val stroke = Stroke(width = 1f, pathEffect = pathEffect)

                    // Horizontal grid lines
                    for (i in 0..5) {
                        val y = size.height * i / 5
                        drawLine(
                            color = Color.White.copy(alpha = 0.1f),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                    }

                    // Vertical grid lines
                    for (i in 0..10) {
                        val x = size.width * i / 10
                        drawLine(
                            color = Color.White.copy(alpha = 0.1f),
                            start = Offset(x, 0f),
                            end = Offset(x, size.height),
                            strokeWidth = 1f
                        )
                    }

                    // Flight path
                    drawLine(
                        color = GoldColor.copy(alpha = 0.6f),
                        start = Offset(size.width * 0.1f, size.height * 0.6f),
                        end = Offset(size.width * 0.9f, size.height * 0.4f),
                        strokeWidth = 2f,
                        pathEffect = pathEffect
                    )
                }

                // Departure point
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.BottomStart)
                        .padding(start = 40.dp, bottom = 80.dp)
                        .background(GoldColor, CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                )

                // Arrival point
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.TopEnd)
                        .padding(end = 40.dp, top = 80.dp)
                        .background(GoldColor, CircleShape)
                        .border(1.dp, Color.White, CircleShape)
                )

                // Current position
                val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                val pulseAnimation by infiniteTransition.animateFloat(
                    initialValue = 0.8f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulse_animation"
                )

                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .scale(pulseAnimation)
                        .align(Alignment.Center)
                        .padding(start = (200 * progress).dp)
                        .background(Color.White, CircleShape)
                        .border(2.dp, GoldColor, CircleShape)
                )

                // City names
                Text(
                    text = flightStatus.departureCode,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 40.dp, bottom = 60.dp)
                )

                Text(
                    text = flightStatus.destinationCode,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 40.dp, top = 60.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Current flight details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoItem(
                    title = "ALTITUDE",
                    value = "${flightStatus.altitude} m"
                )

                InfoItem(
                    title = "GROUND SPEED",
                    value = "${flightStatus.speed} km/h"
                )

                InfoItem(
                    title = "PROGRESS",
                    value = "${(progress * 100).roundToInt()}%"
                )
            }
        }
    }
}

@Composable
fun FlightInfoCard(flightStatus: FlightStatusTest) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    // Animations
    val cardElevation by animateDpAsState(
        targetValue = if (isHovered) 16.dp else 8.dp,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(300)
    )

    val borderWidth by animateDpAsState(
        targetValue = if (isHovered) 2.dp else 1.dp,
        animationSpec = tween(200)
    )

    val pulseAnimation = rememberInfiniteTransition()
    val scale by pulseAnimation.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val gradientColors = if (isHovered) {
        listOf(
            GoldColor,
            GoldColor.copy(alpha = 0.7f),
            GoldColor.copy(alpha = 0.5f)
        )
    } else {
        listOf(
            GoldColor.copy(alpha = 0.7f),
            GoldColor.copy(alpha = 0.3f)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(cardElevation, RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                expanded = !expanded
            }
            .graphicsLayer {
                if (flightStatus.status == "On Time") {
                    this.scaleX = if (isHovered) scale else 1f
                    this.scaleY = if (isHovered) scale else 1f
                }
            },
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            width = borderWidth,
            brush = Brush.linearGradient(colors = gradientColors)
        ),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FLIGHT INFORMATION",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldColor
                )

                StatusIndicator(flightStatus.status)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Route visualization
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = flightStatus.departure,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = flightStatus.departureTime,
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }

                FlightPathIndicator()

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = flightStatus.arrivalTime,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = flightStatus.arrivalTime,
                        fontSize = 12.sp,
                        color = Color.LightGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = GoldColor.copy(alpha = 0.3f)
            )

            // Basic flight information
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconInfoItem(
                    painterResource(R.drawable.plane_path),
                    label = "Flight",
                    value = flightStatus.flightNumber
                )

                IconInfoItem(
                    painterResource(R.drawable.location),
                    label = "Gate",
                    value = flightStatus.gate
                )

                IconInfoItem(
                    painterResource(R.drawable.date_range),
                    label = "Duration",
                    value = flightStatus.flightDuration
                )
            }

            ExpandButton(
                expanded = expanded,
                rotation = rotation
            )

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Divider(
                        color = GoldColor.copy(alpha = 0.3f),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Expanded information
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            InfoRow("Aircraft", flightStatus.aircraft)
                            Spacer(modifier = Modifier.height(12.dp))
                            InfoRow("Distance", "${flightStatus.distanceKm} km")
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            InfoRow("Terminal", flightStatus.terminal)
                            Spacer(modifier = Modifier.height(12.dp))
                            InfoRow("Status", flightStatus.status)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun IconInfoItem(icon: Painter, label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = GoldColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.LightGray
        )
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun FlightPathIndicator() {
    val infiniteTransition = rememberInfiniteTransition()
    val planePosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = Modifier
            .width(120.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Flight path line
        HorizontalDivider(
            modifier = Modifier.align(Alignment.Center),
            thickness = 2.dp,
            color = SkyBlue.copy(alpha = 0.6f)
        )

        // Animated plane
        Icon(
            painterResource(R.drawable.plane_path),
            contentDescription = null,
            tint = GoldColor,
            modifier = Modifier
                .size(24.dp)
                .offset(x = (planePosition * 96).dp - 48.dp)
        )

        // Dots on the path
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(SkyBlue, CircleShape)
            )
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(SkyBlue, CircleShape)
            )
        }
    }
}

@Composable
fun StatusIndicator(status: String) {
    val statusColor = when (status) {
        "On Time" -> Color.Green
        "Delayed" -> Color(0xFFFFA500) // Orange
        "Cancelled" -> Color.Red
        else -> Color.Gray
    }

    val pulseAnimation = rememberInfiniteTransition()
    val alpha by pulseAnimation.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(statusColor.copy(alpha = alpha))
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = status,
            fontSize = 14.sp,
            color = statusColor
        )
    }
}
@Composable
fun ExpandButton(expanded: Boolean, rotation: Float) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = if (expanded) "Show Less" else "Show More",
            tint = GoldColor,
            modifier = Modifier
                .size(32.dp)
                .rotate(rotation)
        )
    }
}

@Composable
fun WeatherCard(flightStatus: FlightStatusTest) {
    var expandedState by remember { mutableStateOf(false) }
    val expandedTransition = animateFloatAsState(
        targetValue = if (expandedState) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "expand_transition"
    )

    // Animate card elevation when expanded
    val elevationAnimation by animateFloatAsState(
        targetValue = if (expandedState) 12f else 8f,
        animationSpec = tween(durationMillis = 300),
        label = "elevation_animation"
    )

    // Animate background color when expanded
    val backgroundColor = if (expandedState) {
        DarkNavyBlue.copy(alpha = 0.95f)
    } else {
        DarkNavyBlue.copy(alpha = 0.8f)
    }

    // Weather animation for icons
    val infiniteTransition = rememberInfiniteTransition(label = "weather_animation")
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_animation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(elevationAnimation.dp, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = if (expandedState) 0.9f else 0.7f),
                        GoldColor.copy(alpha = if (expandedState) 0.5f else 0.3f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { expandedState = !expandedState },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevationAnimation.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "WEATHER CONDITIONS",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldColor
                )

                Icon(
                    painter = painterResource(
                        id = if (expandedState) R.drawable.collapse else R.drawable.expand
                    ),
                    contentDescription = if (expandedState) "Collapse" else "Expand",
                    tint = GoldColor,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(expandedTransition.value * 180f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Departure weather
                WeatherItem(
                    city = flightStatus.departureCode,
                    condition = flightStatus.weatherDeparture,
                    temperature = flightStatus.temperatureDeparture,
                    iconScale = iconScale,
                    isExpanded = expandedState
                )

                // Animated divider
                val dividerWidth by animateFloatAsState(
                    targetValue = if (expandedState) 2f else 1f,
                    label = "divider_width"
                )
                HorizontalDivider(
                    modifier = Modifier
                        .height(100.dp)
                        .width(dividerWidth.dp),
                    color = GoldColor.copy(alpha = if (expandedState) 0.5f else 0.3f)
                )

                // Destination weather
                WeatherItem(
                    city = flightStatus.destinationCode,
                    condition = flightStatus.weatherDestination,
                    temperature = flightStatus.temperatureDestination,
                    iconScale = iconScale,
                    isExpanded = expandedState
                )
            }

            // Expanded content with additional weather information
            AnimatedVisibility(
                visible = expandedState,
                enter = fadeIn(animationSpec = tween(500)) +
                        slideInVertically(initialOffsetY = { it / 2 },
                            animationSpec = tween(500)),
                exit = fadeOut(animationSpec = tween(300))
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(
                        color = GoldColor.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Extended weather information for departure
                        ExtendedWeatherInfo(
                            city = flightStatus.departure,
                            humidity = "68%",
                            windSpeed = "12 km/h",
                            visibility = "10 km"
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Extended weather information for destination
                        ExtendedWeatherInfo(
                            city = flightStatus.destination,
                            humidity = "75%",
                            windSpeed = "8 km/h",
                            visibility = "5 km"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherItem(
    city: String,
    condition: String,
    temperature: Int,
    iconScale: Float = 1f,
    isExpanded: Boolean = false
) {
    // Pulse animation for temperature changes
    val temperatureColor by animateColorAsState(
        targetValue = when {
            temperature > 30 -> Color(0xFFFF5722) // Hot
            temperature < 5 -> Color(0xFF2196F3)  // Cold
            else -> Color.White                   // Normal
        },
        animationSpec = tween(500),
        label = "temperature_color"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clickable(enabled = false) {} // Make it look interactive but don't handle click
    ) {
        Text(
            text = city,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isExpanded) GoldColor else Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Weather icon based on condition with animation
        val iconRes = when (condition.lowercase()) {
            "sunny" -> R.drawable.sunny
            "cloudy" -> R.drawable.cloud_sun
            "rainy" -> R.drawable.cloud_rain
            "snowy" -> R.drawable.snowy
            else -> R.drawable.sunny
        }

        Box(
            modifier = Modifier
                .size(50.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GoldColor.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = condition,
                tint = GoldColor,
                modifier = Modifier
                    .size(40.dp)
                    .scale(iconScale)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = condition,
            fontSize = 14.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Animated temperature display
        Text(
            text = "$temperature°C",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = temperatureColor,
            modifier = Modifier
                .background(
                    color = DarkNavyBlue.copy(alpha = if (isExpanded) 0.7f else 0f),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(if (isExpanded) 8.dp else 0.dp)
        )
    }
}

@Composable
fun ExtendedWeatherInfo(
    city: String,
    humidity: String,
    windSpeed: String,
    visibility: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = city,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = GoldColor,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.water_drop),
                contentDescription = "Humidity",
                tint = Color(0xFF29B6F6),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = " Humidity: $humidity",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.wind),
                contentDescription = "Wind Speed",
                tint = Color(0xFF90CAF9),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = " Wind: $windSpeed",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.eye_password_show),
                contentDescription = "Visibility",
                tint = Color(0xFFFFB74D),
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = " Visibility: $visibility",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun CrewInfoCard(flightStatus: FlightStatusTest) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.7f),
                        GoldColor.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.8f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "CREW INFORMATION",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = GoldColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pilot information
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(GoldColor.copy(alpha = 0.2f), CircleShape)
                        .border(1.dp, GoldColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pilot),
                        contentDescription = "Pilot",
                        tint = GoldColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = "PILOT",
                        fontSize = 12.sp,
                        color = GoldColor
                    )
                    Text(
                        text = flightStatus.pilotName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Co-Pilot information
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(GoldColor.copy(alpha = 0.2f), CircleShape)
                        .border(1.dp, GoldColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.pilot),
                        contentDescription = "Co-Pilot",
                        tint = GoldColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(
                        text = "CO-PILOT",
                        fontSize = 12.sp,
                        color = GoldColor
                    )
                    Text(
                        text = flightStatus.coPilotName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ActionButtons(navController: NavHostController, flightNumber: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Share button
        Button(
            onClick = { /* Share functionality would go here */ },
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldColor
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Share,
                    contentDescription = "Share",
                    tint = DarkNavyBlue
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SHARE",
                    color = DarkNavyBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Notifications button
        Button(
            onClick = {
                navController.navigate(Screen.NotificationScreen.route + "/$flightNumber")
            },
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.notification_),
                    contentDescription = "Notifications",
                    tint = DarkNavyBlue
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ALERTS",
                    color = DarkNavyBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlightStatusScreenPreview() {
    FlightStatusScreen(
        navController = rememberNavController()
    )
}