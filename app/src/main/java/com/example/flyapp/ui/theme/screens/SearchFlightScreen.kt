package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.FlightTopAppBar
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.utils.formatDate

// Define the gold color used in the welcome screen
val GoldColor = Color(0xFFDAA520)
val DarkNavyBlue = Color(0xFF0A1A35)
val MediumBlue = Color(0xFF0E3B6F)
val DeepBlue = Color(0xFF082147)

@Composable
fun SearchFlightScreen(navController: NavController) {
    // State variables
    var fromCity by remember { mutableStateOf("") }
    var toCity by remember { mutableStateOf("") }
    var departureDate by remember { mutableStateOf("") }
    var returnDate by remember { mutableStateOf("") }
    var passengerCount by remember { mutableStateOf("1") }
    var showDepartureDatePicker by remember { mutableStateOf(false) }
    var showReturnDatePicker by remember { mutableStateOf(false) }
    var flightMode by remember { mutableStateOf(FlightMode.ROUND_TRIP) }
    var classType by remember { mutableStateOf(ClassType.FIRST_CLASS) }
    var showClassTypeMenu by remember { mutableStateOf(false) }

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

    // Hologram animation (from welcome screen)
    val hologramGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hologram_glow"
    )

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
        // Security pattern background (from welcome screen)
        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
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
            modifier = Modifier.fillMaxSize()
        ) {
            // Top app bar
            FlightTopAppBar(
                textOne ="SEA",
                textTwo = "RCH",
                navController= navController,
            )

            // Main content area
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title with passport style
                Text(
                    text = "SEARCH YOUR FLIGHT",
                    color = GoldColor,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Trip type selector with gold border
                TripTypeSelector(
                    flightMode = flightMode,
                    onSearchModeChange = { flightMode = it }
                )

                // Card for search form with gold border
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(16.dp))
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    GoldColor,       // Gold
                                    Color(0xFFFFD700), // Gold
                                    GoldColor        // Gold
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
                                        tint = GoldColor
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                    unfocusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                    focusedLabelColor = GoldColor,
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = GoldColor,
                                    unfocusedBorderColor = GoldColor.copy(alpha = 0.5f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                            // Swap button
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(GoldColor)
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
                                    tint = DarkNavyBlue,
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
                                        painterResource(R.drawable.airplane_down),
                                        contentDescription = "To city",
                                        tint = GoldColor
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                    unfocusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                    focusedLabelColor = GoldColor,
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = GoldColor,
                                    unfocusedBorderColor = GoldColor.copy(alpha = 0.5f)
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
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                    unfocusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                    focusedLabelColor = GoldColor,
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = GoldColor,
                                    unfocusedBorderColor = GoldColor.copy(alpha = 0.5f)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    IconButton(onClick = { showDepartureDatePicker = true }) {
                                        Icon(
                                            painterResource(R.drawable.date_range),
                                            contentDescription = "Select departure date",
                                            tint = GoldColor
                                        )
                                    }
                                }
                            )

                            // Return date field (visible only for round trip)
                            if (flightMode == FlightMode.ROUND_TRIP) {
                                OutlinedTextField(
                                    value = returnDate,
                                    onValueChange = { returnDate = it },
                                    modifier = Modifier.weight(1f),
                                    label = { Text("Return") },
                                    readOnly = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                        unfocusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                        focusedLabelColor = GoldColor,
                                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White,
                                        focusedBorderColor = GoldColor,
                                        unfocusedBorderColor = GoldColor.copy(alpha = 0.5f)
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    trailingIcon = {
                                        IconButton(onClick = { showReturnDatePicker = true }) {
                                            Icon(
                                                painterResource(R.drawable.date_range),
                                                contentDescription = "Select return date",
                                                tint = GoldColor
                                            )
                                        }
                                    }
                                )
                            }
                        }

                        // Passenger count
                      Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                      ) {
                          OutlinedTextField(
                              value = passengerCount,
                              onValueChange = { passengerCount = it },
                              modifier = Modifier.weight(.5f),
                              label = { Text("Passengers") },
                              leadingIcon = {
                                  Icon(
                                      imageVector = Icons.Default.Person,
                                      contentDescription = "Passengers",
                                      tint = GoldColor
                                  )
                              },
                              colors = OutlinedTextFieldDefaults.colors(
                                  focusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                  unfocusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                  focusedLabelColor = GoldColor,
                                  unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                  focusedTextColor = Color.White,
                                  unfocusedTextColor = Color.White,
                                  focusedBorderColor = GoldColor,
                                  unfocusedBorderColor = GoldColor.copy(alpha = 0.5f)
                              ),
                              shape = RoundedCornerShape(12.dp),

                          )
                          OutlinedTextField(
                              value = classType.displayName,
                              onValueChange = { classType.displayName = it },
                              modifier = Modifier.weight(1f),
                              label = { Text("Class Type") },
                              leadingIcon = {
                                  Image(
                                      painterResource(classType.icon),
                                      contentDescription = "seat",
                                  )
                              },
                              trailingIcon = {
                                    IconButton(onClick = {
                                        showClassTypeMenu= !showClassTypeMenu
                                    }) {
                                        Icon(
                                            Icons.Default.ArrowDropDown,
                                            contentDescription = "Select class type",
                                            tint = GoldColor
                                        )
                                    }
                                  ClassTypeMenu(
                                        classType = classType,
                                        expanded = showClassTypeMenu,
                                        onDismissRequest = { showClassTypeMenu = false },
                                        onTypeSelected = { selectedType ->
                                            classType = selectedType
                                        }
                                  )
                              },
                              colors = OutlinedTextFieldDefaults.colors(
                                  cursorColor = GoldColor,
                                  focusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                  unfocusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                  focusedLabelColor = GoldColor,
                                  unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                  focusedTextColor = Color.White,
                                  unfocusedTextColor = Color.White,
                                  focusedBorderColor = GoldColor,
                                  unfocusedBorderColor = GoldColor.copy(alpha = 0.5f)
                              ),
                              shape = RoundedCornerShape(12.dp)
                          )
                      }
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
                                containerColor = GoldColor,
                                contentColor = DarkNavyBlue
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
                                text = "SEARCH FLIGHTS",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }

                // Popular destinations section
                Text(
                    text = "POPULAR DESTINATIONS",
                    color = GoldColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
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
fun TripTypeSelector(flightMode: FlightMode, onSearchModeChange: (FlightMode) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, GoldColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .background(DarkNavyBlue.copy(alpha = 0.7f))
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FlightMode.entries.forEach { mode ->
            val isSelected = mode == flightMode
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) GoldColor else Color.Transparent
                    )
                    .clickable { onSearchModeChange(mode) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mode.displayName,
                    color = if (isSelected) DarkNavyBlue else GoldColor.copy(alpha = 0.9f),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp,
                    letterSpacing = 0.5.sp
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
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldColor,
                    contentColor = DarkNavyBlue
                )
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = GoldColor
                ),
                modifier = Modifier.border(1.dp, GoldColor, RoundedCornerShape(4.dp))
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
            .clickable(onClick = onClick)
            .border(1.dp, GoldColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
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
                    color = GoldColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

enum class FlightMode(val displayName: String) {
    ONE_WAY("ONE WAY"),
    ROUND_TRIP("ROUND TRIP"),
    MULTI_CITY("MULTI-CITY")
}
enum class ClassType(var displayName: String,var icon: Int) {
    ECONOMY("Economy", R.drawable.seat_green),
    BUSINESS("Business", R.drawable.seat_blue),
    FIRST_CLASS("First Class", R.drawable.seat_red),
    PREMIUM_ECONOMY("Premium Economy", R.drawable.seat_gold);
}

@Composable
fun PassportDataField(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = GoldColor.copy(alpha = 0.7f),
            fontSize = 10.sp,
            letterSpacing = 1.sp
        )

        Text(
            text = value,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookingFlightScreenPreview() {
    MaterialTheme {
        Surface {
            SearchFlightScreen(rememberNavController())
        }
    }
}

@Composable
fun ClassTypeMenu(
    classType: ClassType,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onTypeSelected: (ClassType) -> Unit = {}
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .width(IntrinsicSize.Max)
            .background(
                color = DarkNavyBlue,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 8.dp),
            properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Text(
            text = "Select Class Type",
            style = MaterialTheme.typography.labelLarge,
            color = GoldColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
             color = GoldColor.copy(alpha = 0.3f)
        )

        ClassType.entries.forEach { type ->
            DropdownMenuItem(
                text = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(type.icon),
                            contentDescription = type.displayName,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = type.displayName,
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                onClick = {
                    onTypeSelected(type)
                    onDismissRequest()
                },
                colors = MenuDefaults.itemColors(
                    textColor = Color.White,
                    leadingIconColor = GoldColor
                ),
                modifier = Modifier
                    .background(
                        color = if (classType == type)
                            GoldColor.copy(alpha = 0.15f)
                        else
                            DarkNavyBlue
                    )
                    .fillMaxWidth()
            )

            if (type != ClassType.entries.last()) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                     color = GoldColor.copy(alpha = 0.2f)
                )
            }
        }
    }
}