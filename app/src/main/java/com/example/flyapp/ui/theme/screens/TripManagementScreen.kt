package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
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
import com.example.flyapp.ui.theme.navigition.Screen
import kotlinx.coroutines.delay

data class Trip(
    val id: String,
    val flightNumber: String,
    val from: String,
    val to: String,
    val departureDate: String,
    val departureTime: String,
    val arrivalDate: String,
    val arrivalTime: String,
    val status: TripStatus,
    val passengers: List<String>,
    val flightClass: String,
    val seats: List<String>,
    val airline: String,
    val price: Double,
    val imageUrl: Int? = null
)

enum class TripStatus {
    UPCOMING, PAST, CANCELED
}

@Composable
fun TripManagementScreen(navController: NavHostController) {
    // Sample trips data
    val trips = remember {
        listOf(
            Trip(
                id = "TK1001",
                flightNumber = "TK1001",
                from = "New York (JFK)",
                to = "London (LHR)",
                departureDate = "May 15, 2025",
                departureTime = "08:30 AM",
                arrivalDate = "May 15, 2025",
                arrivalTime = "10:45 PM",
                status = TripStatus.UPCOMING,
                passengers = listOf("John Doe"),
                flightClass = "Business",
                seats = listOf("15A"),
                airline = "Turkish Airlines",
                price = 750.0,
                imageUrl = R.drawable.new_york
            ),
            Trip(
                id = "BA2530",
                flightNumber = "BA2530",
                from = "London (LHR)",
                to = "Paris (CDG)",
                departureDate = "June 02, 2025",
                departureTime = "14:15 PM",
                arrivalDate = "June 02, 2025",
                arrivalTime = "16:30 PM",
                status = TripStatus.UPCOMING,
                passengers = listOf("John Doe", "Jane Doe"),
                flightClass = "Economy",
                seats = listOf("22C", "22D"),
                airline = "British Airways",
                price = 240.0,
                imageUrl = R.drawable.paris
            ),
            Trip(
                id = "EK3801",
                flightNumber = "EK3801",
                from = "Paris (CDG)",
                to = "Dubai (DXB)",
                departureDate = "June 10, 2025",
                departureTime = "21:45 PM",
                arrivalDate = "June 11, 2025",
                arrivalTime = "07:30 AM",
                status = TripStatus.UPCOMING,
                passengers = listOf("John Doe"),
                flightClass = "First Class",
                seats = listOf("2A"),
                airline = "Emirates",
                price = 1450.0,
                imageUrl = R.drawable.dubai
            ),
            Trip(
                id = "LH7910",
                flightNumber = "LH7910",
                from = "Dubai (DXB)",
                to = "New York (JFK)",
                departureDate = "April 05, 2025",
                departureTime = "09:30 AM",
                arrivalDate = "April 05, 2025",
                arrivalTime = "15:45 PM",
                status = TripStatus.PAST,
                passengers = listOf("John Doe"),
                flightClass = "Business",
                seats = listOf("8F"),
                airline = "Lufthansa",
                price = 890.0,
                imageUrl = R.drawable.new_york
            ),
            Trip(
                id = "QR5240",
                flightNumber = "QR5240",
                from = "New York (JFK)",
                to = "Tokyo (HND)",
                departureDate = "March 12, 2025",
                departureTime = "23:10 PM",
                arrivalDate = "March 14, 2025",
                arrivalTime = "06:25 AM",
                status = TripStatus.PAST,
                passengers = listOf("John Doe", "Jane Doe"),
                flightClass = "Economy",
                seats = listOf("34A", "34B"),
                airline = "Qatar Airways",
                price = 1100.0,
                imageUrl = R.drawable.tokyo
            ),
            Trip(
                id = "SQ4321",
                flightNumber = "SQ4321",
                from = "London (LHR)",
                to = "Singapore (SIN)",
                departureDate = "February 28, 2025",
                departureTime = "20:45 PM",
                arrivalDate = "March 01, 2025",
                arrivalTime = "18:30 PM",
                status = TripStatus.CANCELED,
                passengers = listOf("John Doe"),
                flightClass = "First Class",
                seats = listOf("1D"),
                airline = "Singapore Airlines",
                price = 1750.0,
                imageUrl = R.drawable.tokyo
            )
        )
    }

    // Tab state
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Upcoming", "Past", "Canceled")

    // Animated header visibility
    var showHeader by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        showHeader = true
    }

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
            ).padding(bottom = 32.dp)

    ) {
        // Security pattern background (matching booking screen)
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

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Top header with animation
            AnimatedVisibility(
                visible = showHeader,
                enter = fadeIn(animationSpec = tween(800)) +
                        slideInVertically(
                            animationSpec = tween(800),
                            initialOffsetY = { -it }
                        )
            ) {
                FlightTopAppBar(
                    textOne = "MY",
                    textTwo = " TRIPS",
                    navController = navController,
                )

            }
            Text(
                text = "Manage your bookings",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
            // Tab row for filtering trips
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                GoldColor.copy(alpha = 0.7f),
                                GoldColor.copy(alpha = 0.3f)
                            )
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ),
                containerColor = DarkNavyBlue.copy(alpha = 0.7f),
                contentColor = GoldColor,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .padding(horizontal = 16.dp),
                        height = 2.dp,
                        color = GoldColor
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        selectedContentColor = GoldColor,
                        unselectedContentColor = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            // Filter trips based on tab selection
            val filteredTrips = when (selectedTabIndex) {
                0 -> trips.filter { it.status == TripStatus.UPCOMING }
                1 -> trips.filter { it.status == TripStatus.PAST }
                2 -> trips.filter { it.status == TripStatus.CANCELED }
                else -> trips
            }

            if (filteredTrips.isEmpty()) {
                EmptyTripsList(tabs[selectedTabIndex])
            } else {
                // Trip list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredTrips) { trip ->
                        TripCard(
                            trip = trip,
                            onTripClick = {
                                // Navigate to trip details
                                navController.navigate(Screen.TicketScreen.route)
                            }
                        )
                    }

                    // Bottom spacing
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // Add new trip FAB
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            Button(
                onClick = { //navController.navigate(Screen.BookingFlightScreen.route)
                     },
                modifier = Modifier
                    .shadow(8.dp, CircleShape)
                    .height(56.dp)
                    .width(200.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldColor,
                    contentColor = DarkNavyBlue
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Trip"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BOOK NEW TRIP",
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
@Composable
fun TripCard(
    trip: Trip,
    onTripClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showOptions by remember { mutableStateOf(false) }

    val cardAlpha = when (trip.status) {
        TripStatus.CANCELED -> 0.7f
        else -> 1f
    }

    val statusColor = when (trip.status) {
        TripStatus.UPCOMING -> Color(0xFF4CAF50) // Green
        TripStatus.PAST -> Color.Gray
        TripStatus.CANCELED -> Color(0xFFE57373) // Light red
    }

    val cardElevation by animateFloatAsState(
        targetValue = if (expanded) 12f else 4f,
        label = "cardElevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(cardElevation.dp, RoundedCornerShape(16.dp))
            .alpha(cardAlpha)
            .clickable { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation.dp),
        border = if (trip.status == TripStatus.UPCOMING) {
            androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.7f),
                        GoldColor.copy(alpha = 0.3f),
                        GoldColor.copy(alpha = 0.7f)
                    )
                )
            )
        } else {
            null
        }
    ) {
        Column {
            // Flight destination image
            if (trip.imageUrl != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Image(
                        painter = painterResource(id = trip.imageUrl),
                        contentDescription = "Destination ${trip.to}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Overlay gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        DarkNavyBlue.copy(alpha = 0.7f)
                                    ),
                                    startY = 0f,
                                    endY = 120f
                                )
                            )
                    )

                    // Status chip
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(DarkNavyBlue.copy(alpha = 0.8f))
                            .border(
                                width = 1.dp,
                                color = statusColor,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = trip.status.name,
                            color = statusColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Airline & Flight number
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(R.drawable.plane_path),
                                contentDescription = "Airline",
                                tint = GoldColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${trip.airline} • ${trip.flightNumber}",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // More options
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp)
                    ) {
                        IconButton(
                            onClick = { showOptions = true }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = Color.White
                            )
                        }

                        DropdownMenu(
                            expanded = showOptions,
                            onDismissRequest = { showOptions = false },
                            modifier = Modifier
                                .background(DarkNavyBlue)
                                .border(
                                    1.dp,
                                    GoldColor.copy(alpha = 0.5f),
                                    RoundedCornerShape(8.dp)
                                )
                        ) {
                            DropdownMenuItem(
                                text = { Text("View Details", color = Color.White) },
                                onClick = {
                                    showOptions = false
                                    onTripClick()
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Info,
                                        contentDescription = null,
                                        tint = GoldColor
                                    )
                                }
                            )

                            if (trip.status == TripStatus.UPCOMING) {
                                DropdownMenuItem(
                                    text = { Text("Check In", color = Color.White) },
                                    onClick = { showOptions = false },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = GoldColor
                                        )
                                    }
                                )

                                DropdownMenuItem(
                                    text = { Text("Cancel Booking", color = Color(0xFFE57373)) },
                                    onClick = { showOptions = false },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = null,
                                            tint = Color(0xFFE57373)
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Main card content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Flight route
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // From
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = trip.from.split("(")[0].trim(),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "(${trip.from.split("(")[1].replace(")", "")})",
                            color = GoldColor,
                            fontSize = 14.sp
                        )
                    }

                    // Flight icon
                    Box(
                        modifier = Modifier.width(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Dotted line
                        HorizontalDivider(
                            color = GoldColor.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp)
                        )

                        // Plane icon
                        Icon(
                            painterResource(R.drawable.myplane),
                            contentDescription = "Flight Direction",
                            tint = GoldColor,
                            modifier = Modifier
                                .size(24.dp)
                                .background(DarkNavyBlue, CircleShape)
                                .padding(4.dp)
                        )
                    }

                    // To
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = trip.to.split("(")[0].trim(),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            textAlign = TextAlign.End
                        )
                        Text(
                            text = "(${trip.to.split("(")[1].replace(")", "")})",
                            color = GoldColor,
                            fontSize = 14.sp,
                            textAlign = TextAlign.End
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date and time
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Departure time
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painterResource(R.drawable.airplane_up),
                                contentDescription = "Departure",
                                tint = GoldColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Departure",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }

                        Text(
                            text = trip.departureTime,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = trip.departureDate,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }

                    // Arrival time
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Arrival",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painterResource(R.drawable.airplane_down),
                                contentDescription = "Arrival",
                                tint = GoldColor,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = trip.arrivalTime,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End
                        )

                        Text(
                            text = trip.arrivalDate,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            textAlign = TextAlign.End
                        )
                    }
                }

                // Expanded details
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        HorizontalDivider(
                            color = GoldColor.copy(alpha = 0.3f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // Trip details
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left column
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                TripDetailItem(
                                    label = "Class",
                                    value = trip.flightClass
                                )

                                TripDetailItem(
                                    label = "Seat(s)",
                                    value = trip.seats.joinToString(", ")
                                )

                                TripDetailItem(
                                    label = "Passenger(s)",
                                    value = "${trip.passengers.size} person"
                                )
                            }

                            // Right column
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.End
                            ) {
                                TripDetailItem(
                                    label = "Price",
                                    value = "$${trip.price}",
                                    alignment = Alignment.End
                                )

                                TripDetailItem(
                                    label = "Passenger(s)",
                                    value = trip.passengers.joinToString(", "),
                                    alignment = Alignment.End
                                )
                            }
                        }

                        // Action buttons
                        if (trip.status == TripStatus.UPCOMING) {
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = { onTripClick() },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = GoldColor.copy(alpha = 0.8f),
                                        contentColor = DarkNavyBlue
                                    )
                                ) {
                                    Image(
                                        painterResource(R.drawable.plane_ticket),
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("View Ticket")
                                }

                                Button(
                                    onClick = {


                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF4CAF50),
                                        contentColor = Color.White
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Check In")
                                }
                            }
                        } else if (trip.status == TripStatus.PAST) {
                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { /* Book similar trip */ },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GoldColor.copy(alpha = 0.8f),
                                    contentColor = DarkNavyBlue
                                )
                            ) {
                                Icon(
                                    painterResource(R.drawable.plane_path),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Book Similar Trip")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TripDetailItem(
    label: String,
    value: String,
    alignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalAlignment = alignment
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp
        )

        Text(
            text = value,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun EmptyTripsList(status: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Empty state icon with animation
            val animatedScale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(1000),
                label = "emptyScale"
            )

            Icon(
                painterResource(R.drawable.myplane),
                contentDescription = "No Trips",
                tint = GoldColor.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(100.dp)
                    .scale(animatedScale)
                    .rotate(45f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No $status Trips",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when(status) {
                    "Upcoming" -> "You don't have any upcoming trips.\nBook a new trip to get started!"
                    "Past" -> "You don't have any past trips yet.\nYour travel history will appear here."
                    "Canceled" -> "You don't have any canceled trips.\nThat's actually a good thing!"
                    else -> "No trips found in this category."
                },
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (status == "Upcoming") {
                Button(
                    onClick = {

                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldColor,
                        contentColor = DarkNavyBlue
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "BOOK NEW TRIP",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun TripManagementScreenPreview() {
    TripManagementScreen(rememberNavController())
}

@Preview(showBackground = true)
@Composable
fun EmptyTripsListPreview() {
    EmptyTripsList("Upcoming")
}