package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.ParticleEffectBackground
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Data classes for booking details
data class FlightBooking(
    val bookingReference: String,
    val flightNumber: String,
    val departure: FlightLocation,
    val arrival: FlightLocation,
    val passenger: PassengerInfo,
    val seatInfo: SeatInfo,
    val status: BookingStatus,
    val bookingDate: Date
)

data class FlightLocation(
    val airportCode: String,
    val airportName: String,
    val cityName: String,
    val terminal: String,
    val gate: String,
    val time: Long
)

data class PassengerInfo(
    val name: String,
    val passportNumber: String,
    val ticketClass: TicketClass
)

data class SeatInfo(
    val seatNumber: String,
    val hasExtraLegroom: Boolean = false,
    val hasWindowSeat: Boolean = false
)

enum class BookingStatus {
    CONFIRMED, CHECKED_IN, IN_PROGRESS, COMPLETED, CANCELLED
}

enum class TicketClass {
    ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailsScreen(navController: NavHostController, bookingReference: String = "YTR789") {
    // Sample booking data (in a real app, this would be fetched from a repository)
    val booking = remember {
        FlightBooking(
            bookingReference = bookingReference,
            flightNumber = "AI302",
            departure = FlightLocation(
                airportCode = "JFK",
                airportName = "John F. Kennedy International Airport",
                cityName = "New York",
                terminal = "B",
                gate = "G18", // Changed from G12 as per notification
                time = 100000000L
            ),
            arrival = FlightLocation(
                airportCode = "LAX",
                airportName = "Los Angeles International Airport",
                cityName = "Los Angeles",
                terminal = "5",
                gate = "53B",
                time = 10300000L

            ),
            passenger = PassengerInfo(
                name = "Alex Johnson",
                passportNumber = "P123456789",
                ticketClass = TicketClass.BUSINESS
            ),
            seatInfo = SeatInfo(
                seatNumber = "12A",
                hasWindowSeat = true,
                hasExtraLegroom = true
            ),
            status = BookingStatus.CONFIRMED,
            bookingDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .parse("2025-04-15")!!
        )
    }

    // Animation states
    var showHeader by remember { mutableStateOf(false) }
    var showFlightInfo by remember { mutableStateOf(false) }
    var showPassengerInfo by remember { mutableStateOf(false) }
    var showTicketActions by remember { mutableStateOf(false) }

    // Alpha animations for staggered appearance
    val headerAlpha by animateFloatAsState(
        targetValue = if (showHeader) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "header_alpha"
    )

    val flightInfoAlpha by animateFloatAsState(
        targetValue = if (showFlightInfo) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "flight_info_alpha"
    )

    val passengerInfoAlpha by animateFloatAsState(
        targetValue = if (showPassengerInfo) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "passenger_info_alpha"
    )

    val actionsAlpha by animateFloatAsState(
        targetValue = if (showTicketActions) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "actions_alpha"
    )

    // Trigger animations sequentially
    LaunchedEffect(key1 = true) {
        showHeader = true
        delay(300)
        showFlightInfo = true
        delay(300)
        showPassengerInfo = true
        delay(300)
        showTicketActions = true
    }

    // Scroll state
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF001034),  // Matching HomeScreen gradient
                        Color(0xFF003045),
                        Color(0xFF004D40)
                    )
                )
            )
    ) {
        // Enhanced background animations
        ParticleEffectBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    modifier = Modifier.alpha(headerAlpha),
                    title = {
                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = "Booking Details",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Ref: ${booking.bookingReference}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        // Share button
                        IconButton(
                            onClick = { /* Share functionality */ },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share ticket",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                // Flight Card
                AnimatedVisibility(
                    visible = showFlightInfo,
                    enter = fadeIn(tween(600)) + slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(durationMillis = 600)
                    )
                ) {
                    FlightCard(booking = booking)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Passenger Information Card
                AnimatedVisibility(
                    visible = showPassengerInfo,
                    enter = fadeIn(tween(600)) + slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(durationMillis = 600)
                    )
                ) {
                    PassengerInfoCard(passenger = booking.passenger, seatInfo = booking.seatInfo)
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Ticket Actions
                AnimatedVisibility(
                    visible = showTicketActions,
                    enter = fadeIn(tween(600)) + slideInVertically(
                        initialOffsetY = { it / 2 },
                        animationSpec = tween(durationMillis = 600)
                    )
                ) {
                    TicketActionsCard(status = booking.status)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun FlightCard(booking: FlightBooking) {
    // Animation for card pulsing effect
    val infiniteTransition = rememberInfiniteTransition(label = "flight_card_animation")
    val cardScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_scale"
    )

    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dayFormat = SimpleDateFormat("dd MMM", Locale.getDefault())

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(cardScale)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A3546).copy(alpha = 0.9f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Flight header with status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painterResource(R.drawable.plane_ticket),
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = booking.flightNumber,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    StatusBadge(status = booking.status)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Flight route
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Departure info
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = booking.departure.airportCode,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = booking.departure.cityName,
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = dateFormat.format(booking.departure.time),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                        Text(
                            text = dayFormat.format(booking.departure.time),
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }

                    // Flight path visualization
                    FlightPathIndicator()

                    // Arrival info
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = booking.arrival.airportCode,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = booking.arrival.cityName,
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = dateFormat.format(booking.arrival.time),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50),
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = dayFormat.format(booking.arrival.time),
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Terminal and gate info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Departure terminal/gate
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "TERMINAL",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                        Text(
                            text = booking.departure.terminal,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "GATE",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                        GateIndicator(gate = booking.departure.gate)
                    }

                    // Arrival terminal/gate
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "TERMINAL",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.5f),
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = booking.arrival.terminal,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "GATE",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.5f),
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            GateIndicator(
                                gate = booking.arrival.gate,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FlightPathIndicator() {
    // Animated plane position
    val infiniteTransition = rememberInfiniteTransition(label = "flight_path_animation")
    val planeOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "plane_offset"
    )

    Box(
        modifier = Modifier
            .width(100.dp)
            .padding(vertical = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Dotted line
        Divider(
            color = Color.White.copy(alpha = 0.3f),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        // Plane icon
        Icon(
            painter = painterResource(R.drawable.plane_ticket),
            contentDescription = null,
            tint = Color(0xFF4CAF50),
            modifier = Modifier
                .size(24.dp)
                .offset(x = (planeOffset * 70 - 35).dp, y = (-6).dp)
        )
    }
}

@Composable
fun GateIndicator(gate: String, modifier: Modifier = Modifier) {
    // Pulsing animation for the gate indicator
    val infiniteTransition = rememberInfiniteTransition(label = "gate_animation")
    val gateAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gate_alpha"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .alpha(gateAlpha)
                .background(Color(0xFF4CAF50), CircleShape)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = gate,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun StatusBadge(status: BookingStatus) {
    val (statusText, statusColor) = when (status) {
        BookingStatus.CONFIRMED -> Pair("Confirmed", Color(0xFF4CAF50)) // Green
        BookingStatus.CHECKED_IN -> Pair("Checked In", Color(0xFF2196F3)) // Blue
        BookingStatus.IN_PROGRESS -> Pair("In Progress", Color(0xFFFF9800)) // Orange
        BookingStatus.COMPLETED -> Pair("Completed", Color(0xFF9C27B0)) // Purple
        BookingStatus.CANCELLED -> Pair("Cancelled", Color(0xFFF44336)) // Red
    }

    // Pulsing animation for status
    val infiniteTransition = rememberInfiniteTransition(label = "status_animation")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = Modifier
            .alpha(pulseAlpha)
            .clip(RoundedCornerShape(16.dp))
            .background(statusColor.copy(alpha = 0.2f))
            .border(1.dp, statusColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = statusText,
            color = statusColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PassengerInfoCard(passenger: PassengerInfo, seatInfo: SeatInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A3546).copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Section title
            Text(
                text = "Passenger Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Passenger name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Passenger",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Text(
                    text = passenger.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(12.dp))

            // Passport
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Passport/ID",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Text(
                    text = passenger.passportNumber,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(12.dp))

            // Class
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Class",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                val classColor = when (passenger.ticketClass) {
                    TicketClass.FIRST -> Color(0xFFAD1457) // Dark Pink
                    TicketClass.BUSINESS -> Color(0xFF4CAF50) // Green
                    TicketClass.PREMIUM_ECONOMY -> Color(0xFF2196F3) // Blue
                    TicketClass.ECONOMY -> Color(0xFFFF9800) // Orange
                }

                val className = when (passenger.ticketClass) {
                    TicketClass.FIRST -> "First Class"
                    TicketClass.BUSINESS -> "Business Class"
                    TicketClass.PREMIUM_ECONOMY -> "Premium Economy"
                    TicketClass.ECONOMY -> "Economy"
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(classColor.copy(alpha = 0.2f))
                        .border(1.dp, classColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = className,
                        color = classColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color.White.copy(alpha = 0.1f))
            Spacer(modifier = Modifier.height(12.dp))

            // Seat
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Seat",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (seatInfo.hasWindowSeat) {
                        SeatFeatureBadge(
                            text = "Window",
                            color = Color(0xFF2196F3) // Blue
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    if (seatInfo.hasExtraLegroom) {
                        SeatFeatureBadge(
                            text = "Extra Legroom",
                            color = Color(0xFF9C27B0) // Purple
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    Text(
                        text = seatInfo.seatNumber,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun SeatFeatureBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TicketActionsCard(status: BookingStatus) {
    // Animation for button pulsing
    val infiniteTransition = rememberInfiniteTransition(label = "button_animation")
    val buttonScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A3546).copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Download boarding pass button
            Button(
                onClick = { /* Download boarding pass */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .scale(buttonScale),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    painterResource(R.drawable.download),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Completing the TicketActionsCard function
                Text(
                text = "Download Boarding Pass",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Check-in button (conditional based on status)
            if (status == BookingStatus.CONFIRMED) {
                Button(
                    onClick = { /* Check-in functionality */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Check-in Now",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (status == BookingStatus.CHECKED_IN) {
                Text(
                    text = "You are checked in successfully",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // View flight details button
            Button(
                onClick = { /* View flight details */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1A3546)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "View Flight Details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingDetailsScreenPreview() {
    BookingDetailsScreen(navController = rememberNavController())
}
