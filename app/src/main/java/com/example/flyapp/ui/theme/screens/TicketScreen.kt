package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.DottedLine
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

@Composable
fun TicketScreen(
    navController: NavHostController,
    // Default values for preview
    flightNumber: String = "FA 2240",
    departureCity: String = "NEW YORK",
    destinationCity: String = "LONDON",
    departureAirport: String = "JFK",
    destinationAirport: String = "LHR",
    departureDate: String = "MAY 15, 2025",
    departureTime: String = "10:30",
    arrivalTime: String = "22:15",
    gate: String = "A22",
    terminal: String = "2",
    boardingTime: String = "09:45",
    selectedSeats: List<String> = listOf("15A", "15B")
) {
    // Animation states
    var isTicketVisible by remember { mutableStateOf(false) }
    var isBottomVisible by remember { mutableStateOf(false) }

    // The scroll state to enable scrolling
    val scrollState = rememberScrollState()

    // Calculate flight duration (for display purposes)
    val durationHours = 7  // For example JFK to LHR is about 7 hours
    val durationMinutes = 45

    // The current date for ticket generation
    val currentDate = remember { SimpleDateFormat("dd MMM yyyy", Locale.US).format(Date()) }

    // Launch animations sequentially
    LaunchedEffect(Unit) {
        delay(300)
        isTicketVisible = true
        delay(800)
        isBottomVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepBlue,     // Dark blue
                        MediumBlue,   // Medium blue
                        DarkNavyBlue  // Navy blue
                    )
                )
            )
    ) {
        // Security pattern background (matching payment screen)
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
                .verticalScroll(scrollState)
        ) {

            FlightTopAppBar(
                textOne = "BOARDING ",
                textTwo = "PASS",
                navController= navController,
            )

            // Main content area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success message
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn(animationSpec = tween(1000)) +
                            slideInVertically(
                                animationSpec = tween(1000, easing = FastOutSlowInEasing),
                                initialOffsetY = { -40 }
                            )
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Your tickets are ready!",
                            color = GoldColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Flight information and boarding passes",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Boarding Pass Ticket
                AnimatedVisibility(
                    visible = isTicketVisible,
                    enter = fadeIn(animationSpec = tween(800)) +
                            expandVertically(
                                animationSpec = tween(800, easing = FastOutSlowInEasing),
                                expandFrom = Alignment.Top
                            )
                ) {
                    BoardingPassCard(
                        flightNumber = flightNumber,
                        departureCity = departureCity,
                        destinationCity = destinationCity,
                        departureAirport = departureAirport,
                        destinationAirport = destinationAirport,
                        departureDate = departureDate,
                        departureTime = departureTime,
                        arrivalTime = arrivalTime,
                        gate = gate,
                        terminal = terminal,
                        boardingTime = boardingTime,
                        selectedSeats = selectedSeats,
                        durationHours = durationHours,
                        durationMinutes = durationMinutes,
                        currentDate = currentDate
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                AnimatedVisibility(
                    visible = isBottomVisible,
                    enter = fadeIn(animationSpec = tween(800, delayMillis = 500))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Share & Email buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            // Share button
                            Button(
                                onClick = { /* Share functionality */ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldColor.copy(alpha = 0.2f),
                                    contentColor = GoldColor
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "SHARE",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Email button
                            Button(
                                onClick = { /* Email functionality */ },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldColor.copy(alpha = 0.2f),
                                    contentColor = GoldColor
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email"
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "EMAIL",
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Return to home button
                        Button(
                            onClick = {
                                navController.navigate(Screen.HomeScreen.route) {
                                    popUpTo(0) // Clear back stack
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
                            Icon(
                                painterResource(R.drawable.airplane_up),
                                contentDescription = "Home",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "BOOK ANOTHER FLIGHT",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Check-in reminder text
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.7f))) {
                                    append("Check-in online ")
                                }
                                withStyle(style = SpanStyle(
                                    color = GoldColor,
                                    textDecoration = TextDecoration.Underline,
                                    fontWeight = FontWeight.Medium
                                )) {
                                    append("24 hours")
                                }
                                withStyle(style = SpanStyle(color = Color.White.copy(alpha = 0.7f))) {
                                    append(" before departure")
                                }
                            },
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Airport arrival tip
                        Text(
                            text = "Arrive at the airport at least 2 hours before departure",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun BoardingPassCard(
    flightNumber: String,
    departureCity: String,
    destinationCity: String,
    departureAirport: String,
    destinationAirport: String,
    departureDate: String,
    departureTime: String,
    arrivalTime: String,
    gate: String,
    terminal: String,
    boardingTime: String,
    selectedSeats: List<String>,
    durationHours: Int,
    durationMinutes: Int,
    currentDate: String
) {
    // Card states
    var isFlipped by remember { mutableStateOf(false) }

    // Animation values for card flip
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "card_flip"
    )

    // The flip animation
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isFlipped = !isFlipped }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
    ) {
        // Front of the boarding pass
        Image(
            painter = painterResource(id = R.drawable.earth_night),
            contentDescription = "Plane Ticket",
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Transparent)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    // Hide back when card is flipped
                    alpha = if (rotation < 90f) 1f else 0f
                }
        ) {
            BoardingPassFront(
                flightNumber = flightNumber,
                departureCity = departureCity,
                destinationCity = destinationCity,
                departureAirport = departureAirport,
                destinationAirport = destinationAirport,
                departureDate = departureDate,
                departureTime = departureTime,
                arrivalTime = arrivalTime,
                gate = gate,
                terminal = terminal,
                boardingTime = boardingTime,
                selectedSeats = selectedSeats,
                durationHours = durationHours,
                durationMinutes = durationMinutes
            )
        }

        // Back of the boarding pass (QR Code and additional info)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    // Hide front when card is not flipped
                    rotationY = 180f
                    alpha = if (rotation >= 90f) 1f else 0f
                }
        ) {
            BoardingPassBack(
                flightNumber = flightNumber,
                departureCity = departureCity,
                destinationCity = destinationCity,
                departureDate = departureDate,
                departureTime = departureTime,
                currentDate = currentDate,
                selectedSeats = selectedSeats
            )
        }
    }
}

@Composable
fun BoardingPassFront(
    flightNumber: String,
    departureCity: String,
    destinationCity: String,
    departureAirport: String,
    destinationAirport: String,
    departureDate: String,
    departureTime: String,
    arrivalTime: String,
    gate: String,
    terminal: String,
    boardingTime: String,
    selectedSeats: List<String>,
    durationHours: Int,
    durationMinutes: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
         ,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Top airline bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                GoldColor.copy(alpha = 0.7f),
                                GoldColor,
                                GoldColor.copy(alpha = 0.7f)
                            )
                        )
                    )
                    .padding(vertical = 10.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Airline logo and name
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painterResource(R.drawable.plane_ticket),
                        contentDescription = "Airline Logo",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "FLY AIRLINES",
                        color = DarkNavyBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp
                    )
                }

                // Flight number
                Text(
                    text = flightNumber,
                    color = DarkNavyBlue,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            // Main boarding pass content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                // Flight route header
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp)
                    ,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Departure city
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = departureCity,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = departureAirport,
                            color = GoldColor,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }

                    // Flight path icon
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.plane_path),
                            contentDescription = "Flight Path",
                            tint = GoldColor,
                            modifier = Modifier
                                .size(28.dp)
                        )

                        Text(
                            text = "${durationHours}h ${durationMinutes}m",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Destination city
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = destinationCity,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = destinationAirport,
                            color = GoldColor,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Flight date row
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Date",
                        tint = GoldColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = departureDate,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Flight times
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Departure time
                    Column {
                        Text(
                            text = "DEPARTURE",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = departureTime,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    // Arrival time
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "ARRIVAL",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = arrivalTime,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Divider with scissor effect
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .offset(
                            y = (-8).dp // Adjust the offset to create a scissor effect
                        )

                ) {
                   DottedLine(
                          lineHeight = 2.dp,
                          notchRadius = 16.dp,
                          backgroundColor = MediumBlue,
                       overflowBy = (40).dp,
                   )
//
//                    // Left scissors icon
//                    Icon(
//                        painter = painterResource(id = R.drawable.scissors),
//                        contentDescription = "Cut Here",
//                        modifier = Modifier
//                            .size(24.dp)
//                            .align(Alignment.CenterStart)
//                            .alpha(0.7f),
//                        tint = GoldColor
//                    )
//
//                    // Right scissors icon
//                    Icon(
//                        painter = painterResource(id = R.drawable.scissors),
//                        contentDescription = "Cut Here",
//                        modifier = Modifier
//                            .size(24.dp)
//                            .align(Alignment.CenterEnd)
//                            .alpha(0.7f)
//                            .rotate(180f),
//                        tint = GoldColor
//                    )
                }

                // Passenger and seat information
                Row(
                    modifier = Modifier
                        .fillMaxWidth().padding(start = 16.dp, end = 16.dp)
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Passenger count
                    Column(
                    ) {
                        Text(
                            text = "PASSENGER",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${selectedSeats.size} Adult${if (selectedSeats.size > 1) "s" else ""}",
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                    }

                    // Seat information
                    Column( modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                            horizontalAlignment = Alignment.End) {
                        Text(
                            text = "SEAT${if (selectedSeats.size > 1) "S" else ""}",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = selectedSeats.joinToString(", "),
                            color = GoldColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Gate, Terminal and Boarding Time
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Gate
                    Column {
                        Text(
                            text = "GATE",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = gate,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    // Terminal
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TERMINAL",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = terminal,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    // Boarding time
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "BOARDING",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = boardingTime,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Barcode section
                BarcodeCode128(
                    data = "PRODUCT12345",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp)
                        .height(120.dp)
                        .padding(vertical = 8.dp)
                )
                // Bottom text - Tap to flip
                Text(
                    text = "TAP CARD TO VIEW QR CODE",
                    color = GoldColor.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun BoardingPassBack(
    flightNumber: String,
    departureCity: String,
    destinationCity: String,
    departureDate: String,
    departureTime: String,
    currentDate: String,
    selectedSeats: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(R.drawable.plane_ticket),
                    contentDescription = "Ticket",
                    tint = GoldColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "FLY AIRLINES",
                    color = GoldColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
            }

            // QR Code
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.qrcode),
                    contentDescription = "QR Code",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight info summary
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "$departureCity to $destinationCity",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$departureDate at $departureTime",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Flight $flightNumber | ${selectedSeats.joinToString(", ")}",
                    color = GoldColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Divider with dashed line
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            ) {
                drawLine(
                    color = GoldColor.copy(alpha = 0.5f),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 2f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Additional info
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                InfoRow(title = "PASSENGER", value = "${selectedSeats.size} Adult${if (selectedSeats.size > 1) "s" else ""}")
                InfoRow(title = "ISSUED", value = currentDate)
                InfoRow(title = "BOOKING REF", value = generateBookingRef())
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Bottom text
            Text(
                text = "TAP CARD TO VIEW BOARDING PASS",
                color = GoldColor.copy(alpha = 0.7f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun InfoRow(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 12.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        )
    }
}

// Generate a random booking reference code
private fun generateBookingRef(): String {
    // In a real app, this would come from the API
    // For demo purposes, we'll use a fixed value
    return "FLY4382X"
}

@Preview(showBackground = true)
@Composable
fun TicketScreenPreview() {
    val navController = rememberNavController()

    TicketScreen(
        navController = navController,
        flightNumber = "FA 2240",
        departureCity = "NEW YORK",
        destinationCity = "LONDON",
        departureAirport = "JFK",
        destinationAirport = "LHR",
        departureDate = "MAY 15, 2025",
        departureTime = "10:30",
        arrivalTime = "22:15",
        gate = "A22",
        terminal = "2",
        boardingTime = "09:45",
        selectedSeats = listOf("15A", "15B")
    )
}

@Composable
fun BarcodeCode128(
    data: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.White,
    barColor: Color = Color.Black
) {
    val encodingPattern = remember(data) {
        generateCode128EncodingPattern(data)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(16.dp)

    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height - 20.dp.toPx()
            val moduleWidth = width / encodingPattern.size

            // Draw the barcode pattern
            for (i in encodingPattern.indices) {
                if (encodingPattern[i] == 1) {
                    drawRect(
                        color = barColor,
                        topLeft = Offset(i * moduleWidth, 0f),
                        size = Size(moduleWidth, height)
                    )
                }
            }
        }

        // Draw the data text below the barcode
        Text(
            text = data,
            color = barColor,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 8.dp)
        )
    }
}

// Generate Code 128 encoding pattern
// This is a simplified implementation for visualization purposes
private fun generateCode128EncodingPattern(data: String): List<Int> {
    val result = mutableListOf<Int>()

    // Start code (Code B)
    result.addAll(listOf(1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0))

    // Encode each character
    // This is a simplified implementation - real Code 128 uses specific patterns
    for (char in data) {
        val charCode = char.code
        // Generate a pseudo-realistic pattern for each character
        // In a real implementation, you would use actual Code 128 encoding tables
        for (i in 0 until 11) {
            result.add(if ((i + charCode) % 2 == 0) 1 else 0)
        }
    }

    // Add a simplified checksum
    val checksum = data.sumOf { it.code } % 103
    for (i in 0 until 11) {
        result.add(if ((i + checksum) % 3 == 0) 1 else 0)
    }

    // Stop code
    result.addAll(listOf(1, 1, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 0, 1))

    return result
}