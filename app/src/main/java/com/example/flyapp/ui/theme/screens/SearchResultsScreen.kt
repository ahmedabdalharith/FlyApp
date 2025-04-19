package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.navigition.Screen
import kotlinx.coroutines.delay

@Composable
fun SearchResultsScreen(navController: NavController) {
    // State variables
    var isLoading by remember { mutableStateOf(true) }
    var flights by remember { mutableStateOf<List<FlightData>>(emptyList()) }
    var filterPrice by remember { mutableStateOf(PriceFilter.ALL) }

    // Animation for background effect
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val animatedColor by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "color"
    )

    // Simulate loading data
    LaunchedEffect(key1 = true) {
        delay(2000) // Simulate network delay
        flights = generateDummyFlights()
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(
                            red = 0.1f,
                            green = 0.2f + animatedColor * 0.1f,
                            blue = 0.4f + animatedColor * 0.1f
                        ),
                        Color(
                            red = 0.05f,
                            green = 0.1f + animatedColor * 0.1f,
                            blue = 0.3f + animatedColor * 0.1f
                        )
                    )
                )
            )
    ) {
        // Animated background particles
        ParticleBackground()

        // Main content
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top app bar
            SearchResultsTopBar(navController = navController)

            // Main content area with loading state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    // Loading indicator
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF64B5F6),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Searching for the best flights...",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                } else {
                    // Filter section and flight results
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Filter section
                        FilterSection(
                            selectedFilter = filterPrice,
                            onFilterSelected = { filterPrice = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Results count
                        Text(
                            text = "${flights.size} flights found",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // Flight list
                        AnimatedVisibility(
                            visible = !isLoading,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            LazyColumn(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(flights.filter {
                                    when (filterPrice) {
                                        PriceFilter.ALL -> true
                                        PriceFilter.UNDER_200 -> it.price < 200
                                        PriceFilter.UNDER_400 -> it.price < 400
                                        PriceFilter.UNDER_600 -> it.price < 600
                                    }
                                }) { flight ->
                                    FlightCard(
                                        flight = flight,
                                        onSelectFlight = {
                                            // Navigate to flight detail screen
                                            navController.navigate(Screen.FlightDetailsScreen.route)
                                        }
                                    )
                                }

                                // Add some bottom padding
                                item {
                                    Spacer(modifier = Modifier.height(80.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultsTopBar(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF0A192F).copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        // Title
        Text(
            text = "Flight Results",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        // Filter button
        IconButton(
            onClick = { /* Open detailed filters */ },
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF0A192F).copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.filter_ic),
                contentDescription = "Filters",
                tint = Color.White
            )
        }
    }
}

@Composable
fun FilterSection(
    selectedFilter: PriceFilter,
    onFilterSelected: (PriceFilter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0A192F).copy(alpha = 0.7f))
            .padding(12.dp)
    ) {
        Text(
            text = "Filter by Price",
            color = Color.White,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PriceFilter.entries.forEach { filter ->
                FilterChip(
                    filter = filter,
                    isSelected = filter == selectedFilter,
                    onSelect = { onFilterSelected(filter) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun FilterChip(
    filter: PriceFilter,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(
                if (isSelected) Color(0xFF0288D1) else Color(0xFF0A192F).copy(alpha = 0.5f)
            )
            .clickable(onClick = onSelect)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = filter.displayText,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FlightCard(
    flight: FlightData,
    onSelectFlight: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelectFlight),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A192F).copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Airline info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Airline logo placeholder
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF64B5F6).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.airplane_up),
                            contentDescription = null,
                            tint = Color(0xFF64B5F6),
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = flight.airline,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }

                // Flight rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < flight.rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = null,
                            tint = if (index < flight.rating) Color(0xFFFFD700) else Color.Gray.copy(alpha = 0.5f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight route and time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Departure
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = flight.departureTime,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = flight.departureCode,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }

                // Flight path visualization
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF64B5F6), CircleShape)
                        )

                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(1.dp)
                                .background(Color.White.copy(alpha = 0.5f))
                        )

                        Icon(
                            painter = painterResource(R.drawable.airplane_up),
                            contentDescription = null,
                            tint = Color(0xFF64B5F6),
                            modifier = Modifier.size(16.dp)
                        )

                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .height(1.dp)
                                .background(Color.White.copy(alpha = 0.5f))
                        )

                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFF64B5F6), CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = flight.duration,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                // Arrival
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = flight.arrivalTime,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = flight.arrivalCode,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = Color.White.copy(alpha = 0.1f))

            Spacer(modifier = Modifier.height(16.dp))

            // Price and book button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Price
                Column {
                    Text(
                        text = "Total Price",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "$${flight.price}",
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }

                // Book button
                Button(
                    onClick = onSelectFlight,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0288D1)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Select",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Data model for flight search results
data class FlightData(
    val id: String,
    val airline: String,
    val departureTime: String,
    val arrivalTime: String,
    val departureCode: String,
    val arrivalCode: String,
    val duration: String,
    val price: Int,
    val rating: Int
)

// Price filter options
enum class PriceFilter(val displayText: String) {
    ALL("All"),
    UNDER_200("< $200"),
    UNDER_400("< $400"),
    UNDER_600("< $600")
}

// Generate dummy flight data for preview
private fun generateDummyFlights(): List<FlightData> {
    return listOf(
        FlightData(
            id = "FL001",
            airline = "SkyBlue Airlines",
            departureTime = "08:45 AM",
            arrivalTime = "11:30 AM",
            departureCode = "NYC",
            arrivalCode = "LAX",
            duration = "2h 45m",
            price = 199,
            rating = 4
        ),
        FlightData(
            id = "FL002",
            airline = "Global Airways",
            departureTime = "10:15 AM",
            arrivalTime = "01:25 PM",
            departureCode = "NYC",
            arrivalCode = "LAX",
            duration = "3h 10m",
            price = 259,
            rating = 5
        ),
        FlightData(
            id = "FL003",
            airline = "Eagle Express",
            departureTime = "12:30 PM",
            arrivalTime = "03:45 PM",
            departureCode = "NYC",
            arrivalCode = "LAX",
            duration = "3h 15m",
            price = 189,
            rating = 3
        ),
        FlightData(
            id = "FL004",
            airline = "Pacific Flights",
            departureTime = "02:45 PM",
            arrivalTime = "05:30 PM",
            departureCode = "NYC",
            arrivalCode = "LAX",
            duration = "2h 45m",
            price = 349,
            rating = 4
        ),
        FlightData(
            id = "FL005",
            airline = "Star Alliance",
            departureTime = "05:00 PM",
            arrivalTime = "08:10 PM",
            departureCode = "NYC",
            arrivalCode = "LAX",
            duration = "3h 10m",
            price = 279,
            rating = 5
        ),
        FlightData(
            id = "FL006",
            airline = "BlueJet Airways",
            departureTime = "07:30 PM",
            arrivalTime = "10:15 PM",
            departureCode = "NYC",
            arrivalCode = "LAX",
            duration = "2h 45m",
            price = 229,
            rating = 4
        ),
        FlightData(
            id = "FL007",
            airline = "Summit Air",
            departureTime = "09:45 PM",
            arrivalTime = "12:35 AM",
            departureCode = "NYC",
            arrivalCode = "LAX",
            duration = "2h 50m",
            price = 179,
            rating = 3
        )
    )
}

@Preview(showBackground = true)
@Composable
fun SearchResultsScreenPreview() {
    MaterialTheme {
        Surface {
            SearchResultsScreen(rememberNavController())
        }
    }
}