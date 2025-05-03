package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
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

// Seat status definitions
enum class SeatStatus {
    AVAILABLE,
    SELECTED,
    OCCUPIED,
    FIRST_CLASS,
    BUSINESS_CLASS,
    ECONOMY_CLASS
}

data class Seat(
    val id: String,
    var status: SeatStatus,
    var type: SeatStatus = SeatStatus.ECONOMY_CLASS
)
// Make sure your FlightStatus data class includes a progress property
data class FlightStatusMap(
    val flightNumber: String,
    val departure: String,
    val destination: String,
    val departureTime: String,
    val arrivalTime: String,
    val date: String,
    val status: FlightStatusType,
    val gate: String,
    val terminal: String,
    val boardingTime: String,
    val progress: Float = 0f  // Make sure this line exists
)
@Composable
fun AirplaneSeatsScreen(
    navController: NavHostController,
    flightNumber: String = "FLY-1234",
    departure: String = "New York (JFK)",
    destination: String = "London (LHR)",
    date: String = "30 Apr 2025",
    time: String = "14:30"
) {
    // State for selected seats
    val selectedSeats = remember { mutableStateListOf<String>() }

    // State for price calculation
    var totalPrice by remember { mutableLongStateOf(0L) }

    // Airplane seating data
    val seats = remember {
        val seatList = mutableListOf<Seat>()

        // Generate 30 rows of seats
        for (row in 1..30) {
            val seatType = when (row) {
                in 1..2 -> SeatStatus.FIRST_CLASS // Rows 1-2 are first class
                in 3..7 -> SeatStatus.BUSINESS_CLASS // Rows 3-7 are business class
                else -> SeatStatus.ECONOMY_CLASS // The rest are economy
            }

            // Seats A-F (6 per row)
            for (column in 'A'..'F') {
                val seatId = "$row$column"

                // Randomly mark some seats as occupied
                val isPreoccupied = when {
                    row <= 2 -> Math.random() < 0.7 // 70% of first class seats are taken
                    row <= 7 -> Math.random() < 0.6 // 60% of business class seats are taken
                    else -> Math.random() < 0.4 // 40% of economy seats are taken
                }

                val status = if (isPreoccupied) SeatStatus.OCCUPIED else SeatStatus.AVAILABLE
                seatList.add(Seat(id = seatId, status = status, type = seatType))
            }
        }
        seatList
    }

    // Seat legend animation
    var showLegend by remember { mutableStateOf(false) }

    // Animation for plane diagram
    var isPlaneVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        delay(300)
        isPlaneVisible = true
        delay(500)
        showLegend = true
    }

    // Calculate total price whenever selected seats change
    LaunchedEffect(selectedSeats) {
        totalPrice = selectedSeats.sumOf { seatId ->
            val row = seatId.substring(0, seatId.length - 1).toInt()
            when {
                row <= 2 -> 450L // First class
                row <= 7 -> 250L // Business class
                else -> 120L // Economy class
            }
        }
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
                navController= navController,
            )

            // Flight info card
            AnimatedVisibility(
                visible = isPlaneVisible,
                enter = fadeIn(animationSpec = tween(800)) +
                        slideInVertically(
                            animationSpec = tween(1000),
                            initialOffsetY = { it / 3 }
                        )
            ) {
                FlightInfoCard(
                    flightNumber = flightNumber,
                    departure = departure,
                    destination = destination,
                    date = date,
                    time = time
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Airplane diagram
            AnimatedVisibility(
                visible = isPlaneVisible,
                enter = fadeIn(animationSpec = tween(1000)) +
                        scaleIn(
                            animationSpec = tween(1000),
                            initialScale = 0.95f
                        )
            ) {
                AirplaneDiagram(
                    seats = seats,
                    selectedSeats = selectedSeats,
                    onSeatSelected = { seat ->
                        val seatIndex = seats.indexOfFirst { it.id == seat.id }
                        if (seatIndex != -1) {
                            when (seats[seatIndex].status) {
                                SeatStatus.AVAILABLE -> {
                                    seats[seatIndex] = seats[seatIndex].copy(status = SeatStatus.SELECTED)
                                    selectedSeats.add(seat.id)
                                }
                                SeatStatus.SELECTED -> {
                                    seats[seatIndex] = seats[seatIndex].copy(status = SeatStatus.AVAILABLE)
                                    selectedSeats.remove(seat.id)
                                }
                                else -> { /* Seat is occupied, do nothing */ }
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Seat legend
            AnimatedVisibility(
                visible = showLegend,
                enter = fadeIn(animationSpec = tween(500)) + expandVertically(tween(700))
            ) {
                SeatLegend()
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Selected seats summary
            AnimatedVisibility(
                visible = selectedSeats.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(300)) + expandVertically(tween(500))
            ) {
                SelectedSeatsCard(
                    selectedSeats = selectedSeats,
                    totalPrice = totalPrice
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Continue button
            AnimatedVisibility(
                visible = selectedSeats.isNotEmpty(),
                enter = fadeIn(animationSpec = tween(500)) + expandVertically(tween(700))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate(
                                Screen.PaymentScreen.route + "?selectedSeats=${selectedSeats.joinToString(",")}"
                            )
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
                            text = "CONTINUE TO PAYMENT",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "You can select up to 6 seats per booking",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Bottom spacer for scrolling
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun FlightInfoCard(
    flightNumber: String,
    departure: String,
    destination: String,
    date: String,
    time: String
) {
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
            // Flight number
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SELECT YOUR SEATS",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldColor
                )

                Text(
                    text = flightNumber,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GoldColor.copy(alpha = 0.3f)
            )

            // Route info
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
                        text = departure,
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
                        text = destination,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GoldColor.copy(alpha = 0.3f)
            )

            // Date and time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.date_range),
                        contentDescription = "Date",
                        tint = GoldColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = date,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.access_time),
                        contentDescription = "Time",
                        tint = GoldColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = time,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun AirplaneDiagram(
    seats: List<Seat>,
    selectedSeats: List<String>,
    onSeatSelected: (Seat) -> Unit
) {
    val density = LocalDensity.current

    // Animation for airplane diagram
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 1000),
        label = "airplane_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Airplane nose
        Box(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                GoldColor.copy(alpha = 0.3f),
                                DarkNavyBlue.copy(alpha = 0.6f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                GoldColor.copy(alpha = 0.7f),
                                GoldColor.copy(alpha = 0.1f)
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 80.dp, topEnd = 80.dp)
                    ),
            ) {
                // Cockpit indicator
                Text(
                    text = "COCKPIT",
                    color = GoldColor.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 16.dp),
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Main cabin
        Box(
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
                    shape = RoundedCornerShape(8.dp)
                )
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF001034).copy(alpha = 0.7f))
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // First Class Section
                Text(
                    text = "FIRST CLASS",
                    fontSize = 12.sp,
                    color = GoldColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // First Class seats (rows 1-2)
                SeatRows(
                    seats = seats.filter { it.id.startsWith("1") || it.id.startsWith("2") },
                    selectedSeats = selectedSeats,
                    onSeatSelected = onSeatSelected
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(0.8f),
                    color = GoldColor.copy(alpha = 0.3f)
                )

                // Business Class Section
                Text(
                    text = "BUSINESS CLASS",
                    fontSize = 12.sp,
                    color = GoldColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Business Class seats (rows 3-7)
                SeatRows(
                    seats = seats.filter {
                        val row = it.id.substring(0, it.id.length - 1).toInt()
                        row in 3..7
                    },
                    selectedSeats = selectedSeats,
                    onSeatSelected = onSeatSelected
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
                        .fillMaxWidth(0.8f),
                    color = GoldColor.copy(alpha = 0.3f)
                )

                // Economy Class Section
                Text(
                    text = "ECONOMY CLASS",
                    fontSize = 12.sp,
                    color = GoldColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Economy Class seats (rows 8-30)
                SeatRows(
                    seats = seats.filter {
                        val row = it.id.substring(0, it.id.length - 1).toInt()
                        row > 7
                    },
                    selectedSeats = selectedSeats,
                    onSeatSelected = onSeatSelected
                )
            }
        }

        // Airplane tail
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(60.dp)
                    .clip(RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DarkNavyBlue.copy(alpha = 0.6f),
                                GoldColor.copy(alpha = 0.3f)
                            )
                        )
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                GoldColor.copy(alpha = 0.1f),
                                GoldColor.copy(alpha = 0.7f)
                            )
                        ),
                        shape = RoundedCornerShape(bottomStart = 60.dp, bottomEnd = 60.dp)
                    ),
            ) {
                // Exit indicator
                Text(
                    text = "EXIT",
                    color = GoldColor.copy(alpha = 0.8f),
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(bottom = 16.dp),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SeatRows(
    seats: List<Seat>,
    selectedSeats: List<String>,
    onSeatSelected: (Seat) -> Unit
) {
    val groupedSeats = seats.groupBy { it.id.substring(0, it.id.length - 1) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        groupedSeats.forEach { (row, seatsInRow) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Row number
                Text(
                    text = row,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.width(20.dp),
                    textAlign = TextAlign.Center
                )

                // Left seats (A-C)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    seatsInRow
                        .filter { it.id.endsWith("A") || it.id.endsWith("B") || it.id.endsWith("C") }
                        .sortedBy { it.id.last() }
                        .forEach { seat ->
                            SeatItem(
                                seat = seat,
                                isSelected = selectedSeats.contains(seat.id),
                                onSelect = { onSeatSelected(seat) }
                            )
                        }
                }

                // Aisle
                Box(modifier = Modifier.width(24.dp))

                // Right seats (D-F)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    seatsInRow
                        .filter { it.id.endsWith("D") || it.id.endsWith("E") || it.id.endsWith("F") }
                        .sortedBy { it.id.last() }
                        .forEach { seat ->
                            SeatItem(
                                seat = seat,
                                isSelected = selectedSeats.contains(seat.id),
                                onSelect = { onSeatSelected(seat) }
                            )
                        }
                }

                // Row number (right side)
                Text(
                    text = row,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.width(20.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun SeatItem(
    seat: Seat,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val seatColor = when {
        isSelected -> GoldColor
        seat.status == SeatStatus.OCCUPIED -> Color.Gray.copy(alpha = 0.5f)
        seat.type == SeatStatus.FIRST_CLASS -> Color(0xFF9C27B0).copy(alpha = 0.7f) // Purple for first class
        seat.type == SeatStatus.BUSINESS_CLASS -> Color(0xFF2196F3).copy(alpha = 0.7f) // Blue for business class
        else -> Color(0xFF4CAF50).copy(alpha = 0.7f) // Green for economy
    }

    val seatSize = when (seat.type) {
        SeatStatus.FIRST_CLASS -> 28.dp // Larger seats for first class
        SeatStatus.BUSINESS_CLASS -> 24.dp // Medium seats for business class
        else -> 20.dp // Standard size for economy
    }

    Box(
        modifier = Modifier
            .size(seatSize)
            .border(
                width = 1.dp,
                color = if (isSelected) GoldColor else seatColor.copy(alpha = 0.8f),
                shape = RoundedCornerShape(4.dp)
            )
            .background(
                color = if (seat.status == SeatStatus.OCCUPIED)
                    Color.DarkGray.copy(alpha = 0.3f)
                else
                    seatColor.copy(alpha = if (isSelected) 0.9f else 0.3f),
                shape = RoundedCornerShape(4.dp)
            ).clickable(
                enabled = seat.status != SeatStatus.OCCUPIED,
                onClick = onSelect
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = seat.id.last().toString(),
            fontSize = if (seat.type == SeatStatus.FIRST_CLASS) 12.sp else 10.sp,
            color = if (isSelected || seat.status == SeatStatus.AVAILABLE) Color.White else Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SeatLegend() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.7f)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(listOf(GoldColor.copy(alpha = 0.5f), GoldColor.copy(alpha = 0.2f)))
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "SEAT LEGEND",
                color = GoldColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Legend item for available seat
                LegendItem(
                    color = Color(0xFF4CAF50).copy(alpha = 0.7f),
                    label = "Economy",
                    price = "€120"
                )

                // Legend item for business class
                LegendItem(
                    color = Color(0xFF2196F3).copy(alpha = 0.7f),
                    label = "Business",
                    price = "€250"
                )

                // Legend item for first class
                LegendItem(
                    color = Color(0xFF9C27B0).copy(alpha = 0.7f),
                    label = "First Class",
                    price = "€450"
                )

                // Legend item for selected seat
                LegendItem(
                    color = GoldColor,
                    label = "Selected",
                    price = ""
                )

                // Legend item for occupied seat
                LegendItem(
                    color = Color.Gray.copy(alpha = 0.5f),
                    label = "Occupied",
                    price = ""
                )
            }
        }
    }
}

@Composable
fun LegendItem(
    color: Color,
    label: String,
    price: String = ""
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Seat box
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(color = color.copy(alpha = 0.3f), shape = RoundedCornerShape(4.dp))
                .border(1.dp, color.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
        )

        // Label
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        // Price (if any)
        if (price.isNotEmpty()) {
            Text(
                text = price,
                fontSize = 10.sp,
                color = GoldColor.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun SelectedSeatsCard(
    selectedSeats: List<String>,
    totalPrice: Long
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.8f)
        ),
        border = CardDefaults.outlinedCardBorder().copy(
            brush = Brush.linearGradient(listOf(GoldColor.copy(alpha = 0.7f), GoldColor.copy(alpha = 0.3f)))
        ),
        shape = RoundedCornerShape(12.dp)
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painterResource(R.drawable.seat),
                        contentDescription = "Selected seats",
                        tint = GoldColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SELECTED SEATS (${selectedSeats.size})",
                        color = GoldColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "€$totalPrice",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GoldColor.copy(alpha = 0.3f)
            )

            // Display selected seats by category
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

            // First class seats section
            if (firstClassSeats.isNotEmpty()) {
                SeatCategorySection(
                    title = "First Class",
                    seats = firstClassSeats,
                    pricePerSeat = 450
                )
            }

            // Business class seats section
            if (businessClassSeats.isNotEmpty()) {
                SeatCategorySection(
                    title = "Business Class",
                    seats = businessClassSeats,
                    pricePerSeat = 250
                )
            }

            // Economy class seats section
            if (economyClassSeats.isNotEmpty()) {
                SeatCategorySection(
                    title = "Economy Class",
                    seats = economyClassSeats,
                    pricePerSeat = 120
                )
            }

            // Total amount row
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = GoldColor.copy(alpha = 0.3f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "TOTAL AMOUNT",
                    color = GoldColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "€$totalPrice",
                    color = GoldColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Note about price
            Text(
                text = "Prices include all taxes and fees",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun SeatCategorySection(
    title: String,
    seats: List<String>,
    pricePerSeat: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Seats
            Text(
                text = seats.joinToString(", "),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )

            // Price calculation
            Text(
                text = "${seats.size} × €$pricePerSeat = €${seats.size * pricePerSeat}",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AirplaneSeatsScreenPreview() {
    val navController = rememberNavController()
    AirplaneSeatsScreen(navController = navController)
}