
package com.example.flyapp.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.FlightTopAppBar
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.theme.DarkNavyBlue
import com.example.flyapp.ui.theme.theme.DeepBlue
import com.example.flyapp.ui.theme.theme.FlyAppTheme
import com.example.flyapp.ui.theme.theme.GoldColor
import com.example.flyapp.ui.theme.theme.MediumBlue
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

val AccentCyan = Color(0xFF61E8E1)

@Composable
fun FlightDetailsScreen(
    flightId: String ="FLY1234",
    navController: NavController
) {
    // Find the flight based on the ID (in a real app, this would fetch from a repository)
    val allFlights = remember {
        // Reuse the sample data, in a real app this would come from a repository
        listOf(
            FlightSearchResult(
                id = "FLY1234",
                airline = Airline("Sky Airways", "SA", 4.5f, R.drawable.airline_logo_placeholder),
                flightNumber = "SA 1234",
                departureCity = "New York",
                departureCode = "JFK",
                departureTime = "08:30",
                departureDate = "15 May 2025",
                arrivalCity = "London",
                arrivalCode = "LHR",
                arrivalTime = "20:45",
                arrivalDate = "15 May 2025",
                duration = "7h 15m",
                durationMinutes = 435,
                stops = 0,
                price = 549.99,
                currency = "USD",
                status = FlightStatus.ON_TIME
            ),
            FlightSearchResult(
                id = "FLY5678",
                airline = Airline("Star Flights", "SF", 4.3f, R.drawable.airline_logo_placeholder),
                flightNumber = "SF 5678",
                departureCity = "New York",
                departureCode = "JFK",
                departureTime = "19:45",
                departureDate = "15 May 2025",
                arrivalCity = "London",
                arrivalCode = "LHR",
                arrivalTime = "07:55",
                arrivalDate = "16 May 2025",
                duration = "8h 10m",
                durationMinutes = 490,
                stops = 1,
                stopCities = listOf("Dublin"),
                price = 459.99,
                currency = "USD",
                status = FlightStatus.SCHEDULED
            )
            // Add more sample data as needed
        )
    }

    val flight = remember(flightId) {
        allFlights.find { it.id == flightId } ?: allFlights.first()
    }

    // Animation for loading
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(key1 = true) {
        delay(500)
        isLoading = false
    }

    // Animation for flight path
    val infiniteTransition = rememberInfiniteTransition(label = "flight_path")
    val flightPathProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "flight_path"
    )

    // Pulsing animation for status indicators
    val pulseAnimation by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // State to track expanded sections
    var showAmenities by remember { mutableStateOf(false) }
    var showBaggageInfo by remember { mutableStateOf(false) }
    var showWeatherInfo by remember { mutableStateOf(false) }

    // Formatter for price display
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    formatter.currency = java.util.Currency.getInstance(flight.currency)

    // Background with gradient and security pattern
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

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top app bar
            FlightTopAppBar(
                textOne = "FLIGHT",
                textTwo = " DETAILS",
                navController = navController,
            )

            // Flight status display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .border(1.dp, GoldColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .background(DarkNavyBlue.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Flight number and date
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = flight.flightNumber,
                            color = GoldColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = flight.departureDate,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Flight path visualization
                    DetailedFlightPathView(
                        flight = flight,
                        pathProgress = flightPathProgress,
                        pulseAnimation = pulseAnimation
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Flight time and duration
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        // Departure time and terminal
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = flight.departureTime,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Terminal 4",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }

                        // Duration
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painterResource(R.drawable.access_time),
                                    contentDescription = "Duration",
                                    tint = GoldColor,
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = flight.duration,
                                    color = GoldColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            // Stops info
                            if (flight.stops > 0) {
                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "${flight.stops} stop",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 12.sp
                                )
                            } else {
                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "Direct",
                                    color = AccentCyan,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        // Arrival time and terminal
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = flight.arrivalTime,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Terminal 5",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Status pill
                    Spacer(modifier = Modifier.height(16.dp))

                    val statusColor = when (flight.status) {
                        FlightStatus.ON_TIME -> AccentCyan
                        FlightStatus.DELAYED -> Color(0xFFF39C12)  // Amber
                        FlightStatus.CANCELLED -> Color(0xFFE74C3C)  // Red
                        FlightStatus.BOARDING -> GoldColor
                        else -> Color.White.copy(alpha = 0.7f)
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(statusColor.copy(alpha = 0.2f))
                            .border(1.dp, statusColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = flight.status.displayName(),
                            color = statusColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Airline information
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MediumBlue
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Airline logo
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(DarkNavyBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        if (flight.airline.logoRes != null) {
                            androidx.compose.foundation.Image(
                                painter = painterResource(id = flight.airline.logoRes),
                                contentDescription = "Airline Logo",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(36.dp)
                            )
                        } else {
                            Text(
                                text = flight.airline.code,
                                color = GoldColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = flight.airline.name,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Airline rating
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val fullStars = flight.airline.rating.toInt()
                            val hasHalfStar = flight.airline.rating - fullStars >= 0.5f
                            // Display rating stars
                            for (i in 1..5) {
                                val icon = if (i <= fullStars) {
                                    Icons.Default.Star
                                } else if (i == fullStars + 1 && hasHalfStar) {
                                    Icons.Default.Star
                                } else {
                                    Icons.Outlined.Star
                                }

                                val tint = if (i <= fullStars || (i == fullStars + 1 && hasHalfStar)) {
                                    GoldColor
                                } else {
                                    GoldColor.copy(alpha = 0.3f)
                                }

                                Icon(
                                    imageVector = icon,
                                    contentDescription = "Rating Star",
                                    tint = tint,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Text(
                                text = "${flight.airline.rating}/5",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }

            // Price and booking section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MediumBlue
                ),
                shape = RoundedCornerShape(16.dp)
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
                        Column {
                            Text(
                                text = "Price",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )

                            Text(
                                text = formatter.format(flight.price),
                                color = GoldColor,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "per person",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 12.sp
                            )
                        }

                        Button(
                            onClick = {
                                navController.navigate(Screen.AirplaneSeatsScreen.route)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GoldColor
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "SELECT SEATS",
                                color = DeepBlue,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Expandable information sections
            // 1. Amenities Section
            ExpandableInfoSection(
                title = "Flight Amenities",
                icon = painterResource(R.drawable.entertainment),
                isExpanded = showAmenities,
                onToggle = { showAmenities = !showAmenities }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    AmenityItem(
                        icon = painterResource(R.drawable.wifi_ic),
                        title = "Wi-Fi",
                        description = "Available for purchase"
                    )

                    AmenityItem(
                        icon = painterResource(R.drawable.seat_green),
                        title = "Extra Legroom",
                        description = "31 inch seat pitch"
                    )

                    AmenityItem(
                        icon = painterResource(R.drawable.restaurant_plate),
                        title = "Meals",
                        description = "Complimentary meals and beverages"
                    )

                    AmenityItem(
                        icon = painterResource(R.drawable.entertainment),
                        title = "Entertainment",
                        description = "Personal screens with movies and TV shows"
                    )
                }
            }

            // 2. Baggage Information
            ExpandableInfoSection(
                title = "Baggage Information",
                icon = painterResource(R.drawable.baggage),
                isExpanded = showBaggageInfo,
                onToggle = { showBaggageInfo = !showBaggageInfo }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Carry-on Baggage",
                        color = GoldColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "1 personal item (under the seat) + 1 cabin bag (overhead bin)",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Text(
                        text = "Max weight: 7kg (15lbs) per item",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Checked Baggage",
                        color = GoldColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "1 checked bag included in your fare",
                        color = Color.White,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Text(
                        text = "Max weight: 23kg (50lbs)",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { /* Add extra baggage */ },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = AccentCyan
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentCyan)
                    ) {
                        Text("Add Extra Baggage")
                    }
                }
            }

            // 3. Weather Information
            ExpandableInfoSection(
                title = "Weather at Destination",
                icon = painterResource(R.drawable.cloud_sun),
                isExpanded = showWeatherInfo,
                onToggle = { showWeatherInfo = !showWeatherInfo }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // This would typically fetch real weather data
                    // Showing placeholder weather for London
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = flight.arrivalCity,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = flight.arrivalDate,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Weather icon
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(DeepBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painterResource(R.drawable.wind),
                                contentDescription = "Weather",
                                tint = GoldColor,
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "16°C / 61°F",
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Partly Cloudy",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 16.sp
                            )

                            Text(
                                text = "Wind: 10 km/h | Humidity: 65%",
                                color = Color.White.copy(alpha = 0.5f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Weather forecast is for arrival date and may change. Check again before departure.",
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            // Add some bottom spacing
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun DetailedFlightPathView(
    flight: FlightSearchResult,
    pathProgress: Float,
    pulseAnimation: Float
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        // Draw the flight path
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val padding = 48.dp.toPx()

            // Start and end points
            val startX = padding
            val endX = width - padding
            val centerY = height / 2

            // Draw dashed path line
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
            drawLine(
                color = Color.White.copy(alpha = 0.3f),
                start = Offset(startX, centerY),
                end = Offset(endX, centerY),
                strokeWidth = 2f,
                pathEffect = pathEffect
            )

            // Draw solid completed path based on progress
            val progressEndX = startX + (endX - startX) * pathProgress
            drawLine(
                color = GoldColor,
                start = Offset(startX, centerY),
                end = Offset(progressEndX, centerY),
                strokeWidth = 2f,
                cap = StrokeCap.Round
            )

            // Calculate plane position
            val planeX = startX + (endX - startX) * pathProgress

            // If there are stops, draw them
            if (flight.stops > 0) {
                val stopPositions = mutableListOf<Float>()
                val segmentSize = (endX - startX) / (flight.stops + 1)

                for (i in 1..flight.stops) {
                    val stopX = startX + segmentSize * i
                    stopPositions.add(stopX)

                    // Draw stop circle
                    val stopColor = if (planeX >= stopX) GoldColor else Color.White.copy(alpha = 0.5f)
                    drawCircle(
                        color = stopColor,
                        radius = 6f,
                        center = Offset(stopX, centerY)
                    )
                }
            }
        }

        // Start point indicator
        Box(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.CenterStart)
                .padding(start = 48.dp)
                .clip(CircleShape)
                .background(GoldColor)
        )

        // End point indicator
        Box(
            modifier = Modifier
                .size(16.dp)
                .align(Alignment.CenterEnd)
                .padding(end = 48.dp)
                .clip(CircleShape)
                .background(
                    if (pathProgress >= 0.99f) GoldColor else Color.White.copy(alpha = 0.5f)
                )
        )
        Layout(
            content = {
                Icon(
                    painterResource(R.drawable.plane_path),
                    contentDescription = "Airplane",
                    tint = AccentCyan,
                    modifier = Modifier
                        .size(24.dp)
                        .scale(pulseAnimation)
                )
            }
        ) { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints) }
            val totalWidth = constraints.maxWidth
            val availableWidth = totalWidth - 96.dp.roundToPx() // Accounting for padding
            val planePosition = (availableWidth * pathProgress).toInt()

            layout(totalWidth, constraints.maxHeight) {
                placeables.forEach { placeable ->
                    placeable.place(
                        x = 48.dp.roundToPx() + planePosition - placeable.width / 2,
                        y = (constraints.maxHeight - placeable.height) / 2
                    )
                }
            }
        }

        // City labels
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 32.dp)
        ) {
            Text(
                text = flight.departureCode,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = flight.departureCity,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 32.dp)
        ) {
            Text(
                text = flight.arrivalCode,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = flight.arrivalCity,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // If there are stops, show them below the line
        if (flight.stops > 0) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                val availableWidth = maxWidth - 96.dp
                val segmentSize = availableWidth / (flight.stops + 1)

                for (i in 0 until flight.stops) {
                    val stopPosition = 48.dp + segmentSize * (i + 1)

                    Text(
                        text = flight.stopCities[i],
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .offset(x = stopPosition - (segmentSize / 2))
                            .padding(bottom = 16.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun ExpandableInfoSection(
    title: String,
    icon: Painter,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable (() -> Unit)
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onToggle() },
        colors = CardDefaults.cardColors(
            containerColor = MediumBlue
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header row always visible
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Icon(
                    imageVector = if (isExpanded)
                        Icons.Default.Info
                    else
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = GoldColor,
                    modifier = Modifier
                        .size(24.dp)
                        .rotate(if (isExpanded) 0f else -90f)
                )
            }

            // Expandable content
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = GoldColor.copy(alpha = 0.3f)
                    )

                    content()
                }
            }
        }
    }
}

@Composable
fun AmenityItem(
    icon: Painter,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )


        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = description,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}

data class Airline(
    val name: String,
    val code: String,
    val rating: Float,
    val logoRes: Int? = null
)

data class FlightSearchResult(
    val id: String,
    val airline: Airline,
    val flightNumber: String,
    val departureCity: String,
    val departureCode: String,
    val departureTime: String,
    val departureDate: String,
    val arrivalCity: String,
    val arrivalCode: String,
    val arrivalTime: String,
    val arrivalDate: String,
    val duration: String,
    val durationMinutes: Int,
    val stops: Int,
    val stopCities: List<String> = emptyList(),
    val price: Double,
    val currency: String,
    val status: FlightStatus,
    val amenities: List<String> = listOf("Wi-Fi", "Power Outlets", "Entertainment"),
    val baggageAllowance: String = "1 × 23kg",
    val aircraft: String = "Boeing 777-300ER",
    val isFavorite: Boolean = false
)
enum class FlightStatus {
    ON_TIME,
    DELAYED,
    CANCELLED,
    BOARDING,
    DEPARTED,
    ARRIVED,
    SCHEDULED,
    IN_FLIGHT,
    LANDED,
    DIVERTED;

    fun displayName(): String {
        return when (this) {
            ON_TIME -> "On Time"
            DELAYED -> "Delayed"
            CANCELLED -> "Cancelled"
            BOARDING -> "Boarding"
            DEPARTED -> "Departed"
            ARRIVED -> "Arrived"
            SCHEDULED -> "Scheduled"
            IN_FLIGHT -> "In Flight"
            LANDED -> "Landed"
            DIVERTED -> "Diverted"
        }
    }
}


@Preview
@Composable
fun FlightDetailsScreenPreview() {
    FlyAppTheme {
        FlightDetailsScreen(
            navController = rememberNavController(),
            flightId = "FLY1234"
        )
    }
}