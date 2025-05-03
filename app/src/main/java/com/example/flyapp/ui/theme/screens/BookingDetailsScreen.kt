package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
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
import kotlinx.coroutines.delay
data class PassengerDetails(
    val name: String,
    val passportNumber: String,
    val nationality: String
)

data class BookingInfo(
    val bookingReference: String,
    val flightNumber: String,
    val departure: String,
    val destination: String,
    val date: String,
    val time: String,
    val gate: String,
    val boardingTime: String,
    val passengers: List<PassengerDetails>,
    val selectedSeats: List<String>
)

@Composable
fun BookingDetailsScreen(
    navController: NavHostController,
    selectedSeats: List<String> = listOf("1A", "1B", "2C")
) {
    // Sample booking information
    val booking = remember {
        BookingInfo(
            bookingReference = "FLY-8X42JK",
            flightNumber = "FLY-1234",
            departure = "New York (JFK)",
            destination = "London (LHR)",
            date = "30 Apr 2025",
            time = "14:30",
            gate = "B12",
            boardingTime = "13:45",
            passengers = listOf(
                PassengerDetails(
                    name = "John Smith",
                    passportNumber = "X12345678",
                    nationality = "United States"
                ),
                PassengerDetails(
                    name = "Jane Smith",
                    passportNumber = "X87654321",
                    nationality = "United States"
                )
            ),
            selectedSeats = selectedSeats
        )
    }

    // Animation states
    var isHeaderVisible by remember { mutableStateOf(false) }
    var isFlightInfoVisible by remember { mutableStateOf(false) }
    var isPassengerInfoVisible by remember { mutableStateOf(false) }
    var isSeatInfoVisible by remember { mutableStateOf(false) }
    var isActionButtonVisible by remember { mutableStateOf(false) }

    // Launch animations in sequence
    LaunchedEffect(key1 = Unit) {
        isHeaderVisible = true
        delay(300)
        isFlightInfoVisible = true
        delay(200)
        isPassengerInfoVisible = true
        delay(200)
        isSeatInfoVisible = true
        delay(200)
        isActionButtonVisible = true
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
            val stroke = androidx.compose.ui.graphics.drawscope.Stroke(width = 1f, pathEffect = pathEffect)
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
                textOne = "Booking",
                textTwo = "Details",
                navController = navController,
            )
            // Success Header
            AnimatedVisibility(
                visible = isHeaderVisible,
                enter = fadeIn(animationSpec = tween(800)) +
                        slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { -it }
                        )
            ) {
                BookingSuccessHeader()
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight details card
            AnimatedVisibility(
                visible = isFlightInfoVisible,
                enter = fadeIn(animationSpec = tween(800)) +
                        slideInHorizontally(
                            animationSpec = tween(1000),
                            initialOffsetX = { -it }
                        )
            ) {
                FlightDetailsCard(booking)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Passenger details
            AnimatedVisibility(
                visible = isPassengerInfoVisible,
                enter = fadeIn(animationSpec = tween(800)) +
                        slideInHorizontally(
                            animationSpec = tween(1000),
                            initialOffsetX = { it }
                        )
            ) {
                PassengerDetailsCard(booking.passengers)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Seat information
            AnimatedVisibility(
                visible = isSeatInfoVisible,
                enter = fadeIn(animationSpec = tween(800)) +
                        slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { it / 2 }
                        )
            ) {
                SeatInformationCard(booking.selectedSeats)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Share and Download Buttons
            AnimatedVisibility(
                visible = isActionButtonVisible,
                enter = fadeIn(animationSpec = tween(500)) +
                        slideInVertically(
                            animationSpec = tween(700),
                            initialOffsetY = { it }
                        )
            ) {
                ActionButtons(navController)
            }

            // Bottom spacer for scrolling
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun BookingSuccessHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Success icon with circle background
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            GoldColor.copy(alpha = 0.7f),
                            GoldColor.copy(alpha = 0.0f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(GoldColor.copy(alpha = 0.2f), CircleShape)
                    .border(2.dp, GoldColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = GoldColor,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Success text
        Text(
            text = "BOOKING CONFIRMED",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = GoldColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Booking reference
        Text(
            text = "Booking Reference: FLY-8X42JK",
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Confirmation message
        Text(
            text = "Your booking details have been sent to your email",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FlightDetailsCard(booking: BookingInfo) {
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
                        GoldColor.copy(alpha = 0.3f),
                        GoldColor.copy(alpha = 0.7f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape =  RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Card header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FLIGHT DETAILS",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldColor
                )

                Text(
                    text = booking.flightNumber,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GoldColor.copy(alpha = 0.3f)
            )

            // Flight route
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Departure
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "FROM",
                        fontSize = 12.sp,
                        color = GoldColor.copy(alpha = 0.8f)
                    )
                    Text(
                        text = booking.departure,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                // Flight path icon
                Icon(
                    painter = painterResource(id = R.drawable.plane_path),
                    contentDescription = "Flight path",
                    tint = GoldColor,
                    modifier = Modifier.size(24.dp)
                )

                // Destination
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "TO",
                        fontSize = 12.sp,
                        color = GoldColor.copy(alpha = 0.8f)
                    )
                    Text(
                        text = booking.destination,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date, time, gate and boarding
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Date and time column
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Date row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.date_range),
                            contentDescription = "Date",
                            tint = GoldColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = booking.date,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Time row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.access_time),
                            contentDescription = "Time",
                            tint = GoldColor,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = booking.time,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }

                // Gate and boarding column
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    // Gate row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "GATE",
                            fontSize = 12.sp,
                            color = GoldColor.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = booking.gate,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Boarding time row
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "BOARDING",
                            fontSize = 12.sp,
                            color = GoldColor.copy(alpha = 0.8f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = booking.boardingTime,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PassengerDetailsCard(passengers: List<PassengerDetails>) {
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
                        GoldColor.copy(alpha = 0.3f),
                        GoldColor.copy(alpha = 0.7f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Card header
            Text(
                text = "PASSENGER DETAILS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = GoldColor
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GoldColor.copy(alpha = 0.3f)
            )

            // Passenger list
            passengers.forEachIndexed { index, passenger ->
                PassengerItem(passenger)

                // Add divider between passengers (but not after the last one)
                if (index < passengers.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = GoldColor.copy(alpha = 0.1f)
                    )
                }
            }
        }
    }
}

@Composable
fun PassengerItem(passenger: PassengerDetails) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Passenger name
        Text(
            text = passenger.name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Passport details
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Passport number
            Column {
                Text(
                    text = "PASSPORT",
                    fontSize = 12.sp,
                    color = GoldColor.copy(alpha = 0.8f)
                )
                Text(
                    text = passenger.passportNumber,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            // Nationality
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "NATIONALITY",
                    fontSize = 12.sp,
                    color = GoldColor.copy(alpha = 0.8f)
                )
                Text(
                    text = passenger.nationality,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun SeatInformationCard(selectedSeats: List<String>) {
    // Calculate fares based on seat types
    val firstClassSeats = selectedSeats.filter {
        val row = it.substring(0, it.length - 1).toInt()
        row <= 2
    }.sorted()

    val businessClassSeats = selectedSeats.filter {
        val row = it.substring(0, it.length - 1).toInt()
        row in 3..7
    }.sorted()

    val economyClassSeats = selectedSeats.filter {
        val row = it.substring(0, it.length - 1).toInt()
        row > 7
    }.sorted()

    // Calculate total price
    val totalPrice = (firstClassSeats.size * 450) + (businessClassSeats.size * 250) + (economyClassSeats.size * 120)

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
                        GoldColor.copy(alpha = 0.3f),
                        GoldColor.copy(alpha = 0.7f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Card header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(R.drawable.seat),
                        contentDescription = "Seats",
                        tint = GoldColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SEAT INFORMATION",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldColor
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GoldColor.copy(alpha = 0.3f)
            )

            // Seat class sections
            if (firstClassSeats.isNotEmpty()) {
                SeatClassSection(
                    title = "First Class",
                    seats = firstClassSeats,
                    pricePerSeat = 450
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (businessClassSeats.isNotEmpty()) {
                SeatClassSection(
                    title = "Business Class",
                    seats = businessClassSeats,
                    pricePerSeat = 250
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (economyClassSeats.isNotEmpty()) {
                SeatClassSection(
                    title = "Economy Class",
                    seats = economyClassSeats,
                    pricePerSeat = 120
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Total price
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GoldColor.copy(alpha = 0.3f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "TOTAL PAID",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldColor
                )

                Text(
                    text = "€$totalPrice",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldColor
                )
            }
        }
    }
}

@Composable
fun SeatClassSection(title: String, seats: List<String>, pricePerSeat: Int) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Seat numbers
            Text(
                text = seats.joinToString(", "),
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            // Price calculation
            Text(
                text = "${seats.size} × €$pricePerSeat = €${seats.size * pricePerSeat}",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun ActionButtons(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Share boarding pass button
        Button(
            onClick = { /* Share functionality */ },
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
                Icons.Default.Share,
                contentDescription = "Share",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "SHARE BOARDING PASS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Download boarding pass button (outlined style)
        Button(
            onClick = { /* Download functionality */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = GoldColor
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.download),
                contentDescription = "Download",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "DOWNLOAD BOARDING PASS",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Return to home button
        Button(
            onClick = { navController.navigate(Screen.HomeScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkNavyBlue.copy(alpha = 0.8f),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "RETURN TO HOME",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun BookingDetailsScreenPreview() {
    val navController = rememberNavController()
    BookingDetailsScreen(navController = navController)
}

