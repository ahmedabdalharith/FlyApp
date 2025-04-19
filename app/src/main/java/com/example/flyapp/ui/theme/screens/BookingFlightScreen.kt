package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.utils.formatDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BookingFlightScreen(navController: NavController) {
    // State variables
    var fromCity by remember { mutableStateOf("") }
    var toCity by remember { mutableStateOf("") }
    var departureDate by remember { mutableStateOf("") }
    var returnDate by remember { mutableStateOf("") }
    var passengerCount by remember { mutableStateOf("1") }
    var showDepartureDatePicker by remember { mutableStateOf(false) }
    var showReturnDatePicker by remember { mutableStateOf(false) }
    var searchMode by remember { mutableStateOf(SearchMode.ROUND_TRIP) }

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
            TopAppBar(navController = navController)

            // Main content area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                Text(
                    text = "Book Your Flight",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Trip type selector
                TripTypeSelector(
                    searchMode = searchMode,
                    onSearchModeChange = { searchMode = it }
                )

                // Card for search form
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0A192F).copy(alpha = 0.85f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // From and To fields
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // From city field
                            OutlinedTextField(
                                value = fromCity,
                                onValueChange = { fromCity = it },
                                modifier = Modifier.weight(1f),
                                label = { Text("From") },
                                leadingIcon = {
                                    Icon(
                                        painterResource(R.drawable.airplane_up),
                                        contentDescription = "From city",
                                        tint = Color(0xFF64B5F6)
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF001F3F).copy(alpha = 0.7f),
                                    unfocusedContainerColor = Color(0xFF001F3F).copy(alpha = 0.7f),
                                    focusedLabelColor = Color(0xFF64B5F6),
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // Swap button
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0288D1))
                                    .clickable {
                                        val temp = fromCity
                                        fromCity = toCity
                                        toCity = temp
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id =R.drawable.reload_ic),
                                    contentDescription = "Swap cities",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            // To city field
                            OutlinedTextField(
                                value = toCity,
                                onValueChange = { toCity = it },
                                modifier = Modifier.weight(1f),
                                label = { Text("To") },
                                leadingIcon = {
                                    Icon(
                                        painterResource(R.drawable.airplane_up),
                                        contentDescription = "To city",
                                        tint = Color(0xFF64B5F6)
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF001F3F).copy(alpha = 0.7f),
                                    unfocusedContainerColor = Color(0xFF001F3F).copy(alpha = 0.7f),
                                    focusedLabelColor = Color(0xFF64B5F6),
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Date selection fields
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Departure date field
                            OutlinedTextField(
                                value = departureDate,
                                onValueChange = { departureDate = it },
                                modifier = Modifier.weight(1f),
                                label = { Text("Departure") },
                                readOnly = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color(0xFF001F3F).copy(alpha = 0.7f),
                                    unfocusedContainerColor = Color(0xFF001F3F).copy(alpha = 0.7f),
                                    focusedLabelColor = Color(0xFF64B5F6),
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    IconButton(onClick = { showDepartureDatePicker = true }) {
                                        Icon(
                                            painterResource(R.drawable.date_range),
                                            contentDescription = "Select departure date",
                                            tint = Color(0xFF64B5F6)
                                        )
                                    }
                                }
                            )

                            // Return date field (visible only for round trip)
                            if (searchMode == SearchMode.ROUND_TRIP) {
                                OutlinedTextField(
                                    value = returnDate,
                                    onValueChange = { returnDate = it },
                                    modifier = Modifier.weight(1f),
                                    label = { Text("Return") },
                                    readOnly = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color(0xFF001F3F).copy(alpha = 0.7f),
                                        unfocusedContainerColor = Color(0xFF001F3F).copy(alpha = 0.7f),
                                        focusedLabelColor = Color(0xFF64B5F6),
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    trailingIcon = {
                                        IconButton(onClick = { showReturnDatePicker = true }) {
                                            Icon(
                                                painterResource(R.drawable.date_range),
                                                contentDescription = "Select return date",
                                                tint = Color(0xFF64B5F6)
                                            )
                                        }
                                    }
                                )
                            }
                        }

                        // Passenger count
                        OutlinedTextField(
                            value = passengerCount,
                            onValueChange = { passengerCount = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Passengers") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Passengers",
                                    tint = Color(0xFF64B5F6)
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF001F3F).copy(alpha = 0.7f),
                                unfocusedContainerColor = Color(0xFF001F3F).copy(alpha = 0.7f),
                                focusedLabelColor = Color(0xFF64B5F6),
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Search button
                        Button(
                            onClick = {
                                // Navigate to flight search results
                                navController.navigate(Screen.SearchResultsScreen.route)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0288D1)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Search Flights",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Popular destinations section
                Text(
                    text = "Popular Destinations",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                )

                // Horizontal row of destination cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PopularDestinationCard(
                        cityName = "Dubai",
                        price = "$199",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            toCity = "Dubai"
                        }
                    )
                    PopularDestinationCard(
                        cityName = "London",
                        price = "$299",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            toCity = "London"
                        }
                    )
                    PopularDestinationCard(
                        cityName = "New York",
                        price = "$399",
                        modifier = Modifier.weight(1f),
                        onClick = {
                            toCity = "New York"
                        }
                    )
                }
            }
        }

        // Date pickers
        if (showDepartureDatePicker) {
            DatePickerDialogWithConfirmation(
                onDateSelected = { selectedDate ->
                    departureDate = formatDate(selectedDate)
                    showDepartureDatePicker = false
                },
                onDismiss = { showDepartureDatePicker = false }
            )
        }

        if (showReturnDatePicker) {
            DatePickerDialogWithConfirmation(
                onDateSelected = { selectedDate ->
                    returnDate = formatDate(selectedDate)
                    showReturnDatePicker = false
                },
                onDismiss = { showReturnDatePicker = false }
            )
        }
    }
}

@Composable
fun TopAppBar(navController: NavController) {
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

        // App name or logo
        Text(
            text = "FlyApp",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        // Placeholder for alignment
        Box(modifier = Modifier.size(40.dp))
    }
}

@Composable
fun ParticleBackground() {
    // Animation for floating particles
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val particleOffset1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "particle1"
    )

    val particleOffset2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(15000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "particle2"
    )

    val particleOffset3 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(25000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "particle3"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Particle 1
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(
                    x = (particleOffset1 * 100).dp,
                    y = (particleOffset2 * 200).dp
                )
                .alpha(0.2f)
                .blur(50.dp)
                .background(
                    Color(0xFF64B5F6),
                    CircleShape
                )
        )

        // Particle 2
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(
                    x = -(particleOffset2 * 150).dp,
                    y = (particleOffset3 * 150).dp
                )
                .alpha(0.15f)
                .blur(70.dp)
                .background(
                    Color(0xFF4CAF50),
                    CircleShape
                )
        )

        // Particle 3
        Box(
            modifier = Modifier
                .size(250.dp)
                .offset(
                    x = (particleOffset3 * 150).dp,
                    y = -(particleOffset1 * 100).dp
                )
                .alpha(0.1f)
                .blur(60.dp)
                .background(
                    Color(0xFFFFD700),
                    CircleShape
                )
        )
    }
}

@Composable
fun TripTypeSelector(searchMode: SearchMode, onSearchModeChange: (SearchMode) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0A192F).copy(alpha = 0.7f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SearchMode.entries.forEach { mode ->
            val isSelected = mode == searchMode
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) Color(0xFF0288D1) else Color.Transparent
                    )
                    .clickable { onSearchModeChange(mode) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mode.displayName,
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogWithConfirmation(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
fun PopularDestinationCard(
    cityName: String,
    price: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A192F).copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = cityName,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "From",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )

                Text(
                    text = price,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

enum class SearchMode(val displayName: String) {
    ONE_WAY("One Way"),
    ROUND_TRIP("Round Trip"),
    MULTI_CITY("Multi-City")
}


@Preview(showBackground = true)
@Composable
fun BookingFlightScreenPreview() {
    MaterialTheme {
        Surface {
            BookingFlightScreen(rememberNavController())
        }
    }
}