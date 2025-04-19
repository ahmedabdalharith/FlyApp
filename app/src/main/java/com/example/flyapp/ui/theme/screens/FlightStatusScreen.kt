package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.utils.formatDate
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Enum class for flight status
enum class FlightStatus(val label: String, val color: Color) {
    ON_TIME("On Time", Color(0xFF4CAF50)),
    DELAYED("Delayed", Color(0xFFFF9800)),
    BOARDING("Boarding", Color(0xFF03A9F4)),
    DEPARTED("Departed", Color(0xFF9C27B0)),
    ARRIVED("Arrived", Color(0xFF4CAF50)),
    CANCELLED("Cancelled", Color(0xFFE91E63)),
    SCHEDULED("Scheduled", Color(0xFF607D8B))
}

// Data class for flight information
data class FlightInfo(
    val flightNumber: String,
    val airline: String,
    val departure: FlightLocation,
    val arrival: FlightLocation,
    val status: FlightStatus,
    val aircraft: String? = null,
    val gate: String? = null,
    val terminal: String? = null,
    val delayMinutes: Int? = null,
    val baggageClaim: String? = null
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightStatusScreen(
    navController: NavHostController = rememberNavController()
) {
    // State variables
    var flightNumber by remember { mutableStateOf("") }
    var showFlightDetails by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var showNoResults by remember { mutableStateOf(false) }
    var flightDetails by remember { mutableStateOf<FlightInfo?>(null) }

    // Animation states
    var showContent by remember { mutableStateOf(false) }

    // Trigger content animation
    LaunchedEffect(key1 = true) {
        delay(300)
        showContent = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF001034),
                        Color(0xFF003045),
                        Color(0xFF004D40)
                    )
                )
            )
    ) {
        // Enhanced background animations
        ParticleEffectContainer()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Flight Status",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
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
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn() + slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = tween(durationMillis = 800)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Search section
                    SearchSection(
                        flightNumber = flightNumber,
                        onFlightNumberChange = { flightNumber = it },
                        onSearch = {
                            if (flightNumber.isNotEmpty()) {
                                isLoading = true
                                showNoResults = false
                                showFlightDetails = false

                                // Simulate API call
                                MainScope().launch {
                                    delay(1500) // Simulate network delay

                                    // Find flight info based on flight number
                                    flightDetails = getSampleFlights().find {
                                        it.flightNumber.equals(flightNumber, ignoreCase = true)
                                    }

                                    isLoading = false
                                    showFlightDetails = flightDetails != null
                                    showNoResults = flightDetails == null
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Content area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        when {
                            isLoading -> {
                                LoadingIndicator()
                            }
                            showNoResults -> {
                                NoResultsFound()
                            }
                            showFlightDetails && flightDetails != null -> {
                                FlightDetailsContent(flightDetails!!)
                            }
                            else -> {
                                RecentFlights()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchSection(
    flightNumber: String,
    onFlightNumberChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A2432).copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Track Your Flight",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter your flight number to check its current status",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = flightNumber,
                onValueChange = onFlightNumberChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Flight number (e.g., AA123)") },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.Gray,
                    errorTextColor = Color.Red,

                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    cursorColor = Color(0xFF4CAF50),
                    focusedBorderColor = Color(0xFF4CAF50),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),

                    focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                    unfocusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                )
                ,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                trailingIcon = {
                    IconButton(onClick = onSearch) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color(0xFF4CAF50)
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onSearch,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Track Flight", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = Color(0xFF4CAF50),
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Searching for flight...",
            color = Color.White,
            fontSize = 16.sp
        )
    }
}

@Composable
fun NoResultsFound() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = Color(0xFFE91E63),
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Flight Found",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We couldn't find any flight with this number. Please check the flight number and try again.",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FlightDetailsContent(flightInfo: FlightInfo) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            // Flight header
            FlightHeader(flightInfo)

            Spacer(modifier = Modifier.height(16.dp))

            // Flight status
            FlightStatusCard(flightInfo)

            Spacer(modifier = Modifier.height(16.dp))

            // Flight route visualization
            FlightRouteCard(flightInfo)

            Spacer(modifier = Modifier.height(16.dp))

            // Terminal and gate info
            if (flightInfo.terminal != null || flightInfo.gate != null) {
                TerminalGateCard(flightInfo)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Additional Info
            AdditionalInfoCard(flightInfo)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FlightHeader(flightInfo: FlightInfo) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A2432).copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
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
                    text = flightInfo.flightNumber,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                StatusBadge(flightInfo.status)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = flightInfo.airline,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )

            if (flightInfo.aircraft != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aircraft: ${flightInfo.aircraft}",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun StatusBadge(status: FlightStatus) {
    Surface(
        color = status.color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (status) {
                FlightStatus.ON_TIME -> Icons.Default.Check
                FlightStatus.DELAYED -> Icons.Default.Warning
                FlightStatus.CANCELLED -> Icons.Default.Close
                else -> Icons.Default.Check
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = status.color,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = status.label,
                color = status.color,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun FlightStatusCard(flightInfo: FlightInfo) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A2432).copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            if (flightInfo.status == FlightStatus.DELAYED && flightInfo.delayMinutes != null) {
                Surface(
                    color = FlightStatus.DELAYED.color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = FlightStatus.DELAYED.color,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Flight Delayed",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Text(
                                text = "Your flight is delayed by ${flightInfo.delayMinutes} minutes",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (flightInfo.status == FlightStatus.CANCELLED) {
                Surface(
                    color = FlightStatus.CANCELLED.color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = FlightStatus.CANCELLED.color,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Flight Cancelled",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Text(
                                text = "Please contact the airline for more information",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (flightInfo.status == FlightStatus.BOARDING) {
                Surface(
                    color = FlightStatus.BOARDING.color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource(R.drawable.airplane_up),
                            contentDescription = null,
                            tint = FlightStatus.BOARDING.color,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Now Boarding",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Text(
                                text = "Please proceed to gate ${flightInfo.gate ?: "immediately"}",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (flightInfo.status == FlightStatus.ARRIVED && flightInfo.baggageClaim != null) {
                Surface(
                    color = FlightStatus.ARRIVED.color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource(R.drawable.airplane_down),
                            contentDescription = null,
                            tint = FlightStatus.ARRIVED.color,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Flight Arrived",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Text(
                                text = "Baggage claim: ${flightInfo.baggageClaim}",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun FlightRouteCard(flightInfo: FlightInfo) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A2432).copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Flight Route",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Departure info
                LocationTimeInfo(
                    location = flightInfo.departure,
                    isArrival = false,
                    modifier = Modifier.weight(1f)
                )

                // Flight path visualization
                FlightPathVisual(
                    status = flightInfo.status,
                    modifier = Modifier.weight(1f)
                )

                // Arrival info
                LocationTimeInfo(
                    location = flightInfo.arrival,
                    isArrival = true,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun LocationTimeInfo(
    location: FlightLocation,
    isArrival: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = if (isArrival) Alignment.End else Alignment.Start
    ) {
        Text(
            text = location.airportCode,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = location.cityName,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = formatTime(location.time),
            color = Color(0xFF4CAF50),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = formatDate(location.time),
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
    }
}

@Composable
fun FlightPathVisual(
    status: FlightStatus,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Departure dot
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Color(0xFF4CAF50), CircleShape)
            )

            // Path line
            Divider(
                modifier = Modifier
                    .weight(1f)
                    .height(2.dp),
                color = when (status) {
                    FlightStatus.DEPARTED, FlightStatus.ARRIVED -> Color(0xFF4CAF50)
                    FlightStatus.DELAYED -> Color(0xFFFF9800)
                    FlightStatus.CANCELLED -> Color(0xFFE91E63)
                    else -> Color.White.copy(alpha = 0.5f)
                }
            )

            // Arrival dot
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        when (status) {
                            FlightStatus.ARRIVED -> Color(0xFF4CAF50)
                            else -> Color.White.copy(alpha = 0.5f)
                        },
                        CircleShape
                    )
            )
        }

        // Flight icon
        if (status != FlightStatus.CANCELLED) {
            Icon(
                painterResource(R.drawable.plane_path),
                contentDescription = null,
                tint = when (status) {
                    FlightStatus.DELAYED -> Color(0xFFFF9800)
                    else -> Color(0xFF03A9F4)
                },
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun TerminalGateCard(flightInfo: FlightInfo) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A2432).copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Terminal info
            if (flightInfo.terminal != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF03A9F4).copy(alpha = 0.2f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "T",
                                color = Color(0xFF03A9F4),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Terminal",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )

                    Text(
                        text = flightInfo.terminal,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Gate info
            if (flightInfo.gate != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFF9800).copy(alpha = 0.2f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "G",
                                color = Color(0xFFFF9800),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Gate",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )

                    Text(
                        text = flightInfo.gate,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun AdditionalInfoCard(flightInfo: FlightInfo) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A2432).copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Flight Details",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Duration info
            val durationMillis = flightInfo.arrival.time - flightInfo.departure.time
            val hours = durationMillis / (1000 * 60 * 60)
            val minutes = (durationMillis / (1000 * 60)) % 60
            DetailRow(
                label = "Departure Terminal",
                value = flightInfo.departure.terminal ?: "N/A"
            )

            DetailRow(
                label = "Departure Gate",
                value = flightInfo.departure.gate
            )

            DetailRow(
                label = "Arrival Terminal",
                value = flightInfo.arrival.terminal
            )

            DetailRow(
                label = "Arrival Gate",
                value = flightInfo.arrival.gate
            )

            if (flightInfo.baggageClaim != null) {
                DetailRow(
                    label = "Baggage Claim",
                    value = flightInfo.baggageClaim
                )
            }
        }
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
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
fun RecentFlights() {
    // Sample data for recent flights
    val recentFlights = getSampleFlights().take(5)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Recent Flights",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0A2432).copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (recentFlights.isEmpty()) {
                    Text(
                        text = "No recent flights found",
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        textAlign = TextAlign.Center
                    )
                } else {
                    recentFlights.forEachIndexed { index, flight ->
                        RecentFlightItem(flight)

                        if (index < recentFlights.size - 1) {
                            Divider(
                                color = Color.White.copy(alpha = 0.1f),
                                modifier = Modifier.padding(vertical = 12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecentFlightItem(flightInfo: FlightInfo) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = flightInfo.flightNumber,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "${flightInfo.departure.airportCode} → ${flightInfo.arrival.airportCode}",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )

            Text(
                text = formatDate((flightInfo.departure.time)),
                color = Color.White.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
        }

        StatusBadge(flightInfo.status)
    }
}

@Composable
fun ParticleEffectContainer() {
    // This would be a decorative background animation
    // Implementation would depend on a custom animation library
    // For this example, we'll just leave it as a placeholder
    Box(modifier = Modifier.fillMaxSize())
}

// Helper function to format time
fun formatTime(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}

// Sample data for testing
fun getSampleFlights(): List<FlightInfo> {
    val currentTime = System.currentTimeMillis()
    val oneHour = 60 * 60 * 1000L

    return listOf(
        FlightInfo(
            flightNumber = "AA123",
            airline = "American Airlines",

            departure = FlightLocation(
                airportCode = "JFK",
                cityName = "New York",
                time = currentTime + 2 * oneHour,
                terminal = "4",
                gate = "B12",
                airportName = "John F. Kennedy International Airport"
            ),
            arrival = FlightLocation(
                airportCode = "LAX",
                cityName = "Los Angeles",
                time = currentTime + 8 * oneHour,
                terminal = "5",
                gate = "C15",
                airportName = "Los Angeles International Airport"
            ),
            status = FlightStatus.SCHEDULED,
            aircraft = "Boeing 787-9",
            gate = "B12",
            terminal = "4"
        ),
        FlightInfo(
            flightNumber = "DL456",
            airline = "Delta Airlines",
            departure = FlightLocation(
                airportCode = "ATL",
                cityName = "Atlanta",
                time = currentTime - oneHour,
                terminal = "S",
                gate = "A22",
                airportName = "Hartsfield-Jackson Atlanta International Airport"
            ),
            arrival = FlightLocation(
                airportCode = "ORD",
                cityName = "Chicago",
                time = currentTime + oneHour,
                terminal = "3",
                gate = "G8",
                airportName = "O'Hare International Airport"
            ),
            status = FlightStatus.DEPARTED,
            aircraft = "Airbus A320",
            gate = "A22",
            terminal = "S"
        ),
        FlightInfo(
            flightNumber = "UA789",
            airline = "United Airlines",
            departure = FlightLocation(
                airportCode = "SFO",
                cityName = "San Francisco",
                time = currentTime - 2 * oneHour,
                terminal = "3",
                gate = "F12",
                airportName = "San Francisco International Airport"
            ),
            arrival = FlightLocation(
                airportCode = "SEA",
                cityName = "Seattle",
                time = currentTime - oneHour / 2,
                terminal = "N",
                gate = "D5",
                airportName = "Seattle-Tacoma International Airport"
            ),
            status = FlightStatus.ARRIVED,
            aircraft = "Boeing 737-800",
            gate = "D5",
            terminal = "N",
            baggageClaim = "Carousel 4"
        ),
        FlightInfo(
            flightNumber = "SW123",
            airline = "Southwest Airlines",
            departure = FlightLocation(
                airportCode = "MDW",
                cityName = "Chicago",
                time = currentTime + 3 * oneHour,
                terminal = "B",
                gate = "C10",
                airportName = "Chicago Midway International Airport"
            ),
            arrival = FlightLocation(
                airportCode = "LAS",
                cityName = "Las Vegas",
                time = currentTime + 5 * oneHour,
                terminal = "1",
                gate = "A1",
                airportName = "Harry Reid International Airport"
            ),
            status = FlightStatus.BOARDING,
            aircraft = "Boeing 737 MAX 8",
            gate = "C10",
            terminal = "B"
        ),
        FlightInfo(
            flightNumber = "AA456",
            airline = "American Airlines",
            departure = FlightLocation(
                airportCode = "ORD",
                cityName = "Chicago",
                time = currentTime + 4 * oneHour,
                terminal = "5",
                gate = "B8",
                airportName = "O'Hare International Airport"
            ),
            arrival = FlightLocation(
                airportCode = "MIA",
                cityName = "Miami",
                time = currentTime + 6 * oneHour,
                terminal = "D",
                gate = "D12",
                airportName = "Miami International Airport"
            ),
            status = FlightStatus.DELAYED,
            aircraft = "Boeing 777-200ER",
            gate = "B8",
            terminal = "5",
            delayMinutes = 30
        ),
        FlightInfo(
            flightNumber = "DL789",
            airline = "Delta Airlines",
            departure = FlightLocation(
                airportCode = "LAX",
                cityName = "Los Angeles",
                time = currentTime + 5 * oneHour,
                terminal = "2",
                gate = "C5",
                airportName = "Los Angeles International Airport"
            ),
            arrival = FlightLocation(
                airportCode = "JFK",
                cityName = "New York",
                time = currentTime + 8 * oneHour,
                terminal = "4",
                gate = "B10",
                airportName = "John F. Kennedy International Airport"
            ),
            status = FlightStatus.CANCELLED,
            aircraft = "Airbus A330-300",
            gate = "C5",
            terminal = "2"
        ),
        FlightInfo(
            flightNumber = "UA123",
            airline = "United Airlines",
            departure = FlightLocation(
                airportCode = "DEN",
                cityName = "Denver",
                time = currentTime + 2 * oneHour,
                terminal = "A",
                gate = "A12",
                airportName = "Denver International Airport"
            ),
            arrival = FlightLocation(
                airportCode = "SFO",
                cityName = "San Francisco",
                time = currentTime + 4 * oneHour,
                terminal = "3",
                gate = "F8",
                airportName = "San Francisco International Airport"
            ),
            status = FlightStatus.SCHEDULED,
            aircraft = "Boeing 787-8",
            gate = "A12",
            terminal = "A"
        )
    )
}
@Preview
@Composable
fun FlightStatusScreenPreview() {
    FlightStatusScreen(navController = rememberNavController())
}