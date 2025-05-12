package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.example.flyapp.ui.theme.theme.GoldColor
import com.example.flyapp.ui.theme.theme.MediumBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun SearchResultsScreen(navController: NavController) {
    // State variables
    var isLoading by remember { mutableStateOf(true) }
    var flightResults by remember { mutableStateOf<List<FlightResult>>(emptyList()) }
    val favorites = remember { mutableStateListOf<String>() }
    var showFilterSheet by remember { mutableStateOf(false) }
    var priceRangeFilter by remember { mutableFloatStateOf(2000f) }
    val selectedAirlines = remember { mutableStateListOf<String>() }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var sortOrder by remember { mutableStateOf(SortOrder.PRICE_LOW_TO_HIGH) }

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

    // Load mock flight data
    LaunchedEffect(key1 = Unit) {
        delay(1500) // Simulate network delay
        flightResults = generateMockFlightResults()
        isLoading = false
    }

    // Apply filters and sorting
    val filteredResults = remember(flightResults, priceRangeFilter, selectedAirlines, sortOrder) {
        var results = flightResults.filter { it.price <= priceRangeFilter }

        if (selectedAirlines.isNotEmpty()) {
            results = results.filter { selectedAirlines.contains(it.airline) }
        }

        when (sortOrder) {
            SortOrder.PRICE_LOW_TO_HIGH -> results.sortedBy { it.price }
            SortOrder.PRICE_HIGH_TO_LOW -> results.sortedByDescending { it.price }
            SortOrder.DURATION_SHORTEST -> results.sortedBy { it.durationMinutes }
            SortOrder.DEPARTURE_EARLIEST -> results.sortedBy { it.departureTime }
        }
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
        // Security pattern background (like in SearchFlightScreen)
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

        // Main content
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top app bar
            FlightTopAppBar(
                textOne = "SEA",
                textTwo = "RCH",
                navController = navController
            )

            // Filter and sort bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Sort indicator
                Text(
                    text = "Sorted by: ${sortOrder.displayName}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Row {
                    IconButton(
                        onClick = { showFilterSheet = true },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(GoldColor.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            painterResource(R.drawable.filter_ic),
                            contentDescription = "Filter",
                            tint = GoldColor
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            sortOrder = when(sortOrder) {
                                SortOrder.PRICE_LOW_TO_HIGH -> SortOrder.PRICE_HIGH_TO_LOW
                                SortOrder.PRICE_HIGH_TO_LOW -> SortOrder.DURATION_SHORTEST
                                SortOrder.DURATION_SHORTEST -> SortOrder.DEPARTURE_EARLIEST
                                SortOrder.DEPARTURE_EARLIEST -> SortOrder.PRICE_LOW_TO_HIGH
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(GoldColor.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            painterResource(R.drawable.sort_from_top_to_bottom),
                            contentDescription = "Sort",
                            tint = GoldColor
                        )
                    }
                }
            }



            // Loading state
            AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(
                            color = GoldColor,
                            modifier = Modifier.size(60.dp),
                            strokeWidth = 4.dp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Searching for the best flights...",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            // Results content
            AnimatedVisibility(
                visible = !isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Results count
                    Text(
                        text = "${filteredResults.size} flights found",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )

                    // Results list
                    if (filteredResults.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(id = R.drawable.airplane_up),
                                    contentDescription = "No results",
                                    modifier = Modifier.size(80.dp),
                                    alpha = 0.5f
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "No flights found matching your criteria",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "Try adjusting your filters",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
                        ) {
                            items(filteredResults) { flight ->
                                EnhancedFlightResultCard(
                                    flight = flight,
                                    isFavorite = favorites.contains(flight.id),
                                    onFavoriteClick = { flightId ->
                                        if (favorites.contains(flightId)) {
                                            favorites.remove(flightId)
                                        } else {
                                            favorites.add(flightId)
                                        }
                                    },
                                    onCardClick = {
                                        // Navigate to flight details screen
                                        navController.navigate(Screen.FlightDetailsScreen.route)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Filter bottom sheet
        if (showFilterSheet) {
            FilterBottomSheet(
                onDismiss = { showFilterSheet = false },
                currentPriceRange = priceRangeFilter,
                onPriceRangeChange = { priceRangeFilter = it },
                airlines = remember { listOf("Emirates", "Etihad Airways", "Qatar Airways", "British Airways", "Singapore Airlines") },
                selectedAirlines = selectedAirlines,
                onAirlineSelected = { airline ->
                    if (selectedAirlines.contains(airline)) {
                        selectedAirlines.remove(airline)
                    } else {
                        selectedAirlines.add(airline)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    onDismiss: () -> Unit,
    currentPriceRange: Float,
    onPriceRangeChange: (Float) -> Unit,
    airlines: List<String>,
    selectedAirlines: List<String>,
    onAirlineSelected: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DarkNavyBlue,
        contentColor = Color.White,
        dragHandle = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(GoldColor.copy(alpha = 0.7f))
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "FILTER OPTIONS",
                color = GoldColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Price range filter
            Text(
                text = "Price Range",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$0",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )

                Slider(
                    value = currentPriceRange,
                    onValueChange = onPriceRangeChange,
                    valueRange = 100f..2000f,
                    steps = 19,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = GoldColor,
                        activeTrackColor = GoldColor,
                        inactiveTrackColor = GoldColor.copy(alpha = 0.3f)
                    )
                )

                Text(
                    text = "$${currentPriceRange.toInt()}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = GoldColor.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(16.dp))

            // Airlines filter
            Text(
                text = "Airlines",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            airlines.forEach { airline ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onAirlineSelected(airline) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = selectedAirlines.contains(airline),
                        onCheckedChange = { onAirlineSelected(airline) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = GoldColor,
                            uncheckedColor = GoldColor.copy(alpha = 0.5f),
                            checkmarkColor = DarkNavyBlue
                        )
                    )

                    Text(
                        text = airline,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Apply button
            Button(
                onClick = onDismiss,
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
                    text = "APPLY FILTERS",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun EnhancedFlightResultCard(
    flight: FlightResult,
    isFavorite: Boolean,
    onFavoriteClick: (String) -> Unit,
    onCardClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                onCardClick()
                isExpanded = !isExpanded
            })
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(20.dp), spotColor = GoldColor.copy(alpha = 0.4f))
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.3f),
                        GoldColor.copy(alpha = 0.8f),
                        GoldColor.copy(alpha = 0.3f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                ),
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.95f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp,
            focusedElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = if (isExpanded) 16.dp else 16.dp
                )
        ) {
            // Airline and favorite row with shimmer effect for the airline logo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .shimmerEffect()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.emirates_logo),
                            contentDescription = flight.airline,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = flight.airline,
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = flight.flightNumber,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }

                FavoriteButton(
                    isFavorite = isFavorite,
                    onClick = { onFavoriteClick(flight.id) }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Flight route visualization with enhanced visuals
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Departure details
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = flight.departureTime,
                        color = GoldColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = flight.fromAirportCode,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = flight.fromCity,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Enhanced flight path visualization
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = formatDuration(flight.durationMinutes),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    FlightPathAnimation(flight.stops > 0)

                    // Connection info with enhanced styling
                    Text(
                        text = if (flight.stops == 0) "Direct" else "${flight.stops} stop${if (flight.stops > 1) "s" else ""}",
                        color = if (flight.stops == 0) GoldColor else Color.White.copy(alpha = 0.7f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = if (flight.stops == 0) FontWeight.Medium else FontWeight.Normal,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Arrival details
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = flight.arrivalTime,
                        color = GoldColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = flight.toAirportCode,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = flight.toCity,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Price and class info with premium visual styling
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Class type with visual indicator
                ClassTypeTag(flight.classType)

                // Price with enhanced styling
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "$",
                            color = GoldColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                        Text(
                            text = flight.price.toString(),
                            color = GoldColor,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Text(
                        text = "per passenger",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }

            // Expandable content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(
                        modifier = Modifier.padding(bottom = 16.dp),
                        thickness = 1.dp,
                        color = GoldColor.copy(alpha = 0.3f)
                    )

                    // Flight details
                    FlightDetailRow(
                        icon = painterResource(R.drawable.date_range),
                        label = "Date",
                        value = "May 15, 2023" // This would come from flight data
                    )

                    FlightDetailRow(
                        icon = painterResource(R.drawable.baggage),
                        label = "Baggage",
                        value = "2 × 23kg"
                    )

                    FlightDetailRow(
                        icon = painterResource(R.drawable.restaurant),
                        label = "Meal",
                        value = "Included"
                    )

                    FlightDetailRow(
                        icon = painterResource(R.drawable.seat_green),
                        label = "Seat Selection",
                        value = "Free"
                    )

                    // Book now button
                    Button(
                        onClick = { /* Booking logic */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldColor,
                            contentColor = DarkNavyBlue
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 8.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Text(
                            text = "Book Now",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FlightDetailRow(
    icon: Painter,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = GoldColor.copy(alpha = 0.8f),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
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
fun ClassTypeTag(classType: String) {
    val (backgroundColor, borderColor, iconRes) = when (classType) {
        "Economy" -> Triple(
            Color(0xFF2E7D32).copy(alpha = 0.2f),
            Color(0xFF2E7D32).copy(alpha = 0.8f),
            R.drawable.seat_green
        )
        "Business" -> Triple(
            Color(0xFF1565C0).copy(alpha = 0.2f),
            Color(0xFF1565C0).copy(alpha = 0.8f),
            R.drawable.seat_blue
        )
        "First Class" -> Triple(
            Color(0xFFC62828).copy(alpha = 0.2f),
            Color(0xFFC62828).copy(alpha = 0.8f),
            R.drawable.seat_red
        )
        else -> Triple(
            GoldColor.copy(alpha = 0.2f),
            GoldColor.copy(alpha = 0.8f),
            R.drawable.seat_gold
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = classType,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = classType,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    IconButton(
        onClick = {
            onClick()
            coroutineScope.launch {
                scale.animateTo(
                    targetValue = 0.8f,
                    animationSpec = tween(100)
                )
                scale.animateTo(
                    targetValue = 1.2f,
                    animationSpec = tween(100)
                )
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(100)
                )
            }
        },
        modifier = Modifier
            .size(40.dp)
            .scale(scale.value)
            .background(
                color = if (isFavorite) GoldColor.copy(alpha = 0.2f) else Color.Transparent,
                shape = CircleShape
            )
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
            tint = if (isFavorite) GoldColor else GoldColor.copy(alpha = 0.7f),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun FlightPathAnimation(hasStops: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "flight path")
    val planePosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "plane position"
    )

    Box(modifier = Modifier.height(30.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 4.dp)
        ) {
            // Departure point
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(GoldColor)
            )

            Box(modifier = Modifier.weight(1f)) {
                // Dotted line
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)

                    drawLine(
                        color = GoldColor.copy(alpha = 0.5f),
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 2f,
                        pathEffect = pathEffect
                    )

                    // Draw stops if needed
                    if (hasStops) {
                        val stopPosition = size.width / 2
                        drawCircle(
                            color = GoldColor.copy(alpha = 0.7f),
                            radius = 4f,
                            center = Offset(stopPosition, size.height / 2)
                        )
                    }
                }

                // Animated plane
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 2.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.airplane_up),
                        contentDescription = null,
                        tint = GoldColor,
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterStart)
                            .graphicsLayer {
                                translationX = planePosition * (this.size.width - 16.dp.toPx())
                                rotationZ = 45f
                            }
                    )
                }
            }

            // Arrival point
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(GoldColor)
            )
        }
    }
}

// Shimmer effect extension
fun Modifier.shimmerEffect(): Modifier = composed {
    val shimmerColors = listOf(
        DarkNavyBlue.copy(alpha = 0.3f),
        DarkNavyBlue.copy(alpha = 0.5f),
        DarkNavyBlue.copy(alpha = 0.8f),
        DarkNavyBlue.copy(alpha = 0.5f),
        DarkNavyBlue.copy(alpha = 0.3f),
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer animation"
    )

    background(
        brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim - 1000f, 0f),
            end = Offset(translateAnim, 0f),
        )
    )
}
data class FlightResult(
    val id: String,
    val airline: String,
    val flightNumber: String,
    val fromCity: String,
    val fromAirportCode: String,
    val toCity: String,
    val toAirportCode: String,
    val departureTime: String,
    val arrivalTime: String,
    val durationMinutes: Int,
    val stops: Int,
    val price: Int,
    val classType: String
)

enum class SortOrder(val displayName: String) {
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    PRICE_HIGH_TO_LOW("Price: High to Low"),
    DURATION_SHORTEST("Shortest Duration"),
    DEPARTURE_EARLIEST("Earliest Departure")
}

// Helper function to format flight duration
fun formatDuration(minutes: Int): String {
    val hours = minutes / 60
    val mins = minutes % 60
    return "${hours}h ${mins}m"
}

// Generate mock flight data
// Generate mock flight data
fun generateMockFlightResults(): List<FlightResult> {
    val airlines = listOf("Emirates", "Etihad Airways", "Qatar Airways", "British Airways", "Singapore Airlines")
    val fromCities = listOf("Seattle", "New York", "London", "Dubai", "Singapore")
    val toCities = listOf("Paris", "Tokyo", "Sydney", "Dubai", "New York")
    val fromCodes = listOf("SEA", "JFK", "LHR", "DXB", "SIN")
    val toCodes = listOf("CDG", "HND", "SYD", "DXB", "JFK")
    val classTypes = listOf("Economy", "Business", "First Class", "Premium Economy")

    val results = mutableListOf<FlightResult>()

    for (i in 1..12) {
        val airlineIndex = Random.nextInt(airlines.size)
        val fromIndex = Random.nextInt(fromCities.size)
        val toIndex = Random.nextInt(toCities.size)
        val classIndex = Random.nextInt(classTypes.size)

        val stops = Random.nextInt(3)
        val durationMinutes = 120 + Random.nextInt(600) // 2-12 hours
        val price = 200 + Random.nextInt(1800) // $200-$2000

        // Generate departure time (between 00:00 and 23:59)
        val depHour = Random.nextInt(24).toString().padStart(2, '0')
        val depMinute = Random.nextInt(60).toString().padStart(2, '0')
        val departureTime = "$depHour:$depMinute"

        // Calculate arrival time based on departure time and duration
        val depTimeMinutes = depHour.toInt() * 60 + depMinute.toInt()
        val arrTimeMinutes = (depTimeMinutes + durationMinutes) % (24 * 60)
        val arrHour = (arrTimeMinutes / 60).toString().padStart(2, '0')
        val arrMinute = (arrTimeMinutes % 60).toString().padStart(2, '0')
        val arrivalTime = "$arrHour:$arrMinute"

        // Generate flight number (2 letters + 3-4 digits)
        val airlineCode = airlines[airlineIndex].take(2).uppercase()
        val flightNum = Random.nextInt(1000, 9999)
        val flightNumber = "$airlineCode$flightNum"

        results.add(
            FlightResult(
                id = "flight_$i",
                airline = airlines[airlineIndex],
                flightNumber = flightNumber,
                fromCity = fromCities[fromIndex],
                fromAirportCode = fromCodes[fromIndex],
                toCity = toCities[toIndex],
                toAirportCode = toCodes[toIndex],
                departureTime = departureTime,
                arrivalTime = arrivalTime,
                durationMinutes = durationMinutes,
                stops = stops,
                price = price,
                classType = classTypes[classIndex]
            )
        )
    }

    return results
}

@Preview
@Composable
fun SearchResultsScreenPreview() {
    Surface {
        SearchResultsScreen(navController = rememberNavController())
    }
}