package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.FlightTopAppBar
import kotlinx.coroutines.delay


// Flight status enum
enum class FlightStatusType {
    ON_TIME,
    BOARDING,
    DELAYED,
    LANDED,
    DEPARTED,
    CANCELED
}

@Composable
fun FlightStatusScreen(
    navController: NavHostController,
    flightNumber: String = "FLY-1234"
) {
    // State for animations
    var isPageLoaded by remember { mutableStateOf(false) }
    var isFlightDetailsVisible by remember { mutableStateOf(false) }
    var isRoutePathVisible by remember { mutableStateOf(false) }
    var isWeatherVisible by remember { mutableStateOf(false) }
    var selectedFlightIndex by remember { mutableIntStateOf(0) }
    var progressAnimation by remember { mutableFloatStateOf(0f) }

    // Sample flight data
    val flights = remember {
        listOf(
            FlightStatusMap(
                flightNumber = "FLY-1234",
                departure = "New York (JFK)",
                destination = "London (LHR)",
                departureTime = "14:30",
                arrivalTime = "02:45",
                date = "30 Apr 2025",
                status = FlightStatusType.BOARDING,
                gate = "B12",
                terminal = "Terminal 4",
                boardingTime = "13:45",
                progress = 0.05f
            ),
            FlightStatusMap(
                flightNumber = "FLY-5678",
                departure = "London (LHR)",
                destination = "Paris (CDG)",
                departureTime = "09:15",
                arrivalTime = "11:30",
                date = "2 May 2025",
                status = FlightStatusType.ON_TIME,
                gate = "D7",
                terminal = "Terminal 2",
                boardingTime = "08:30",
                progress = 0.0f
            ),
            FlightStatusMap(
                flightNumber = "FLY-9012",
                departure = "Paris (CDG)",
                destination = "Dubai (DXB)",
                departureTime = "22:45",
                arrivalTime = "06:30",
                date = "3 May 2025",
                status = FlightStatusType.DELAYED,
                gate = "F9",
                terminal = "Terminal 1",
                boardingTime = "21:45",
                progress = 0.0f
            ),
            FlightStatusMap(
                flightNumber = "FLY-3456",
                departure = "Los Angeles (LAX)",
                destination = "New York (JFK)",
                departureTime = "08:00",
                arrivalTime = "16:15",
                date = "1 May 2025",
                status = FlightStatusType.DEPARTED,
                gate = "C5",
                terminal = "Terminal 3",
                boardingTime = "07:15",
                progress = 0.65f
            )
        )
    }

    // Selected flight
    val selectedFlight = flights[selectedFlightIndex]

    // Animation for progress
    val animatedProgress by animateFloatAsState(
        targetValue = if (isFlightDetailsVisible) selectedFlight.progress else 0f,
        animationSpec = tween(1500, easing = LinearOutSlowInEasing),
        label = "progress"
    )

    // Animation effects
    LaunchedEffect(Unit) {
        delay(300)
        isPageLoaded = true
        delay(500)
        isFlightDetailsVisible = true
        delay(800)
        isRoutePathVisible = true
        delay(1200)
        isWeatherVisible = true
    }

    // Background with security pattern
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
        // Security pattern background
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
                navController = navController,
            )

            // Title and flight selector
            AnimatedVisibility(
                visible = isPageLoaded,
                enter = fadeIn(animationSpec = tween(800)) +
                        slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { it / 3 }
                        )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "FLIGHT STATUS",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldColor,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Track your flight status, departure and arrival times",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        letterSpacing = 0.5.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Flight selector cards
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        flights.forEachIndexed { index, flight ->
                            FlightSelectCard(
                                flight = flight,
                                isSelected = index == selectedFlightIndex,
                                onClick = {
                                    selectedFlightIndex = index
                                    isFlightDetailsVisible = false
                                    isRoutePathVisible = false
                                    isWeatherVisible = false

                                    // Re-trigger animations

                                }
                            )
                        }
                        LaunchedEffect(selectedFlightIndex) {
                            delay(300)
                            isFlightDetailsVisible = true
                            delay(300)
                            isRoutePathVisible = true
                            delay(300)
                            isWeatherVisible = true
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight details card
            AnimatedVisibility(
                visible = isFlightDetailsVisible,
                enter = fadeIn(animationSpec = tween(800)) +
                        slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { it / 3 }
                        )
            ) {
                FlightDetailsCard(
                    flight = selectedFlight,
                    animatedProgress = animatedProgress
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight route and status
            AnimatedVisibility(
                visible = isRoutePathVisible,
                enter = fadeIn(animationSpec = tween(800)) +
                        expandVertically(
                            animationSpec = tween(1000),
                            expandFrom = Alignment.Top
                        )
            ) {
                FlightRouteCard(
                    flight = selectedFlight,
                    animatedProgress = animatedProgress
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Weather and additional info
            AnimatedVisibility(
                visible = isWeatherVisible,
                enter = fadeIn(animationSpec = tween(800)) +
                        expandVertically(
                            animationSpec = tween(1000),
                            expandFrom = Alignment.Top
                        )
            ) {
                AdditionalInfoCard(flight = selectedFlight)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action buttons
            AnimatedVisibility(
                visible = isFlightDetailsVisible,
                enter = fadeIn(animationSpec = tween(1000)) +
                        expandVertically(
                            animationSpec = tween(1200),
                            expandFrom = Alignment.Top
                        )
            ) {
                ActionButtons(navController = navController)
            }

            // Bottom spacer for scrolling
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FlightSelectCard(
    flight: FlightStatusMap,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(180.dp)
            .shadow(if (isSelected) 8.dp else 4.dp, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = if (isSelected) {
                        listOf(GoldColor, Color(0xFFFFD700), GoldColor)
                    } else {
                        listOf(Color.White.copy(0.3f), Color.White.copy(0.1f))
                    }
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                DarkNavyBlue.copy(alpha = 0.9f)
            } else {
                DarkNavyBlue.copy(alpha = 0.5f)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Flight number
            Text(
                text = flight.flightNumber,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) GoldColor else Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Route
            Text(
                text = "${flight.departure.split(" ")[0]} → ${flight.destination.split(" ")[0]}",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date
            Text(
                text = flight.date,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Status indicator
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatusIndicator(status = flight.status)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = getStatusText(flight.status),
                    fontSize = 12.sp,
                    color = getStatusColor(flight.status)
                )
            }
        }
    }
}

@Composable
fun StatusIndicator(status: FlightStatusType) {
    val color = getStatusColor(status)

    Box(
        modifier = Modifier
            .size(8.dp)
            .background(color, CircleShape)
    )
}

@Composable
fun getStatusText(status: FlightStatusType): String {
    return when (status) {
        FlightStatusType.ON_TIME -> "On Time"
        FlightStatusType.BOARDING -> "Boarding"
        FlightStatusType.DELAYED -> "Delayed"
        FlightStatusType.LANDED -> "Landed"
        FlightStatusType.DEPARTED -> "In Air"
        FlightStatusType.CANCELED -> "Canceled"
    }
}

@Composable
fun getStatusColor(status: FlightStatusType): Color {
    return when (status) {
        FlightStatusType.ON_TIME -> Color(0xFF4CAF50) // Green
        FlightStatusType.BOARDING -> GoldColor // Gold
        FlightStatusType.DELAYED -> Color(0xFFFF9800) // Orange
        FlightStatusType.LANDED -> Color(0xFF2196F3) // Blue
        FlightStatusType.DEPARTED -> Color(0xFF9C27B0) // Purple
        FlightStatusType.CANCELED -> Color(0xFFF44336) // Red
    }
}

@Composable
fun FlightDetailsCard(
    flight: FlightStatusMap,
    animatedProgress: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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
            // Flight number and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.plane_path),
                        contentDescription = "Flight",
                        tint = GoldColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = flight.flightNumber,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldColor
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatusIndicator(status = flight.status)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = getStatusText(flight.status),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = getStatusColor(flight.status)
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GoldColor.copy(alpha = 0.3f)
            )

            // Departure and Arrival info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Departure column
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "DEPARTURE",
                        fontSize = 12.sp,
                        color = GoldColor.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = flight.departureTime,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = flight.departure,
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.date_range),
                            contentDescription = "Date",
                            tint = GoldColor.copy(alpha = 0.8f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = flight.date,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                // Flight time visual separator
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.plane_path),
                        contentDescription = "Flight path",
                        tint = GoldColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Calculate flight duration (placeholder)
                    val flightDuration = "5h 15m"
                    Text(
                        text = flightDuration,
                        fontSize = 12.sp,
                        color = GoldColor,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Arrival column
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "ARRIVAL",
                        fontSize = 12.sp,
                        color = GoldColor.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = flight.arrivalTime,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = flight.destination,
                        fontSize = 16.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.date_range),
                            contentDescription = "Date",
                            tint = GoldColor.copy(alpha = 0.8f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = flight.date,
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GoldColor.copy(alpha = 0.3f)
            )

            // Gate, Terminal, and Boarding Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Terminal
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "TERMINAL",
                        fontSize = 12.sp,
                        color = GoldColor.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = flight.terminal,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                // Gate
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "GATE",
                        fontSize = 12.sp,
                        color = GoldColor.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = flight.gate,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                // Boarding time
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "BOARDING",
                        fontSize = 12.sp,
                        color = GoldColor.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = flight.boardingTime,
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
fun FlightRouteCard(
    flight: FlightStatusMap,
    animatedProgress: Float
) {
    val density = LocalDensity.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "FLIGHT ROUTE",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = GoldColor,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Flight progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(horizontal = 16.dp)
            ) {
                // Draw flight route path
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .align(Alignment.Center)
                ) {
                    // Background line
                    drawLine(
                        color = Color.White.copy(alpha = 0.3f),
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 4f,
                        cap = StrokeCap.Round
                    )

                    // Progress line
                    drawLine(
                        color = GoldColor,
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width * animatedProgress, size.height / 2),
                        strokeWidth = 4f,
                        cap = StrokeCap.Round
                    )
                }

                // Departure city indicator
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(bottom = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(GoldColor, CircleShape)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = flight.departure.split(" ")[0],
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start
                    )
                }

                // Airplane position indicator
                val airplaneOffset = with(density) {
                    (density.density * animatedProgress - 12.dp.toPx()).coerceIn(0f, density.density - 24.dp.toPx())
                }

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterStart)
                        .padding(start = with(density) { airplaneOffset.toDp() })
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.myplane),
                        contentDescription = "Flight",
                        tint = GoldColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Destination city indicator
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(bottom = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color.White.copy(alpha = 0.6f), CircleShape)
                            .align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = flight.destination.split(" ")[0],
                        fontSize = 12.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End
                    )
                }
                Text(
                    text = "${(animatedProgress * 100).toInt()}%",
                    color = GoldColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight status info
            when (flight.status) {
                FlightStatusType.BOARDING -> {
                    StatusMessage(
                        icon = R.drawable.profile_ic,
                        title = "BOARDING IN PROGRESS",
                        message = "Please proceed to gate ${flight.gate} at ${flight.terminal}",
                        color = GoldColor
                    )
                }
                FlightStatusType.DELAYED -> {
                    StatusMessage(
                        icon = R.drawable.clock_circle,
                        title = "FLIGHT DELAYED",
                        message = "New estimated departure is 23:45. We apologize for the inconvenience.",
                        color = Color(0xFFFF9800)
                    )
                }
                FlightStatusType.DEPARTED -> {
                    StatusMessage(
                        icon = R.drawable.flight_takeoff,
                        title = "FLIGHT IN PROGRESS",
                        message = "Your flight departed at ${flight.departureTime} and is on schedule.",
                        color = Color(0xFF9C27B0)
                    )
                }
                FlightStatusType.ON_TIME -> {
                    StatusMessage(
                        icon = R.drawable.check_circle,
                        title = "FLIGHT ON TIME",
                        message = "Your flight is scheduled to depart at ${flight.departureTime}.",
                        color = Color(0xFF4CAF50)
                    )
                }
                FlightStatusType.LANDED -> {
                    StatusMessage(
                        icon = R.drawable.flight_land,
                        title = "FLIGHT LANDED",
                        message = "Your flight has landed at ${flight.destination}.",
                        color = Color(0xFF2196F3)
                    )
                }
                FlightStatusType.CANCELED -> {
                    StatusMessage(
                        icon = R.drawable.cancel,
                        title = "FLIGHT CANCELED",
                        message = "We apologize for the inconvenience. Please contact customer service.",
                        color = Color(0xFFF44336)
                    )
                }
            }
        }
    }
}

@Composable
fun StatusMessage(
    icon: Int,
    title: String,
    message: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = title,
            tint = color,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = title,
                color = color,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = message,
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun AdditionalInfoCard(flight: FlightStatusMap) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.1f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "DESTINATION WEATHER",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = GoldColor,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Weather info row
            WeatherInfo(
                destination = flight.destination.split(" ")[0],
                temperature = "21°C",
                condition = "Partly Cloudy",
                humidity = "65%",
                visibility = "10 km"
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = Color.White.copy(alpha = 0.1f)
            )

            // Flight information
            Text(
                text = "FLIGHT INFORMATION",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = GoldColor,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Aircraft info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "AIRCRAFT",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Boeing 787-9",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                Column {
                    Text(
                        text = "SEAT",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "12A (Window)",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                Column {
                    Text(
                        text = "CLASS",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Business",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = GoldColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // In-flight amenities
            Text(
                text = "IN-FLIGHT AMENITIES",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AmenityItem(icon = R.drawable.wifi_ic, text = "Wi-Fi")
                AmenityItem(icon = R.drawable.restaurant, text = "Meals")
                AmenityItem(icon = R.drawable.wifi_ic, text = "Entertainment")
                AmenityItem(icon = R.drawable.wifi_ic, text = "Power")
            }
        }
    }
}

@Composable
fun WeatherInfo(
    destination: String,
    temperature: String,
    condition: String,
    humidity: String,
    visibility: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Weather icon
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF64B5F6),
                            Color(0xFF2196F3)
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.cloud_sun),
                contentDescription = "Weather",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Weather details
        Column {
            Text(
                text = "$destination - $temperature",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = condition,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(
                    text = "Humidity: $humidity",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Visibility: $visibility",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun AmenityItem(icon: Int, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = GoldColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = text,
            fontSize = 10.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionButtonsMap(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Boarding pass button
        Button(
            onClick = { /* Navigate to boarding pass screen */ },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = GoldColor,
                contentColor = DeepBlue
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.qr),
                contentDescription = "Boarding Pass",
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "BOARDING PASS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Customer service button
        Button(
            onClick = { /* Navigate to customer service screen */ },
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DeepBlue,
                contentColor = GoldColor
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.profile_ic),
                contentDescription = "Customer Service",
                modifier = Modifier.size(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "CUSTOMER SERVICE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlightStatusScreenPreview() {
    FlightStatusScreen(navController = rememberNavController())
}