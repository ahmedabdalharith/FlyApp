//package com.example.flyapp.ui.theme.screens
//
//import android.view.MotionEvent
//import androidx.compose.animation.AnimatedContent
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.Crossfade
//import androidx.compose.animation.animateContentSize
//import androidx.compose.animation.core.Animatable
//import androidx.compose.animation.core.FastOutSlowInEasing
//import androidx.compose.animation.core.LinearEasing
//import androidx.compose.animation.core.RepeatMode
//import androidx.compose.animation.core.Spring
//import androidx.compose.animation.core.animateDpAsState
//import androidx.compose.animation.core.animateFloat
//import androidx.compose.animation.core.infiniteRepeatable
//import androidx.compose.animation.core.rememberInfiniteTransition
//import androidx.compose.animation.core.spring
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.slideInVertically
//import androidx.compose.animation.slideOutVertically
//import androidx.compose.animation.togetherWith
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.interaction.MutableInteractionSource
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScope
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowForward
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material.icons.filled.Place
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.icons.filled.Settings
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Divider
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FilledTonalButton
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.LinearProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Tab
//import androidx.compose.material3.TabRow
//import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableFloatStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.composed
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.graphicsLayer
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.input.pointer.pointerInteropFilter
//import androidx.compose.ui.platform.LocalDensity
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.window.Dialog
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import com.example.flyapp.R
//import kotlinx.coroutines.delay
//import java.text.SimpleDateFormat
//import java.util.Calendar
//import java.util.Date
//import java.util.Locale
//
//
//// Data class for flight information
//data class FlightTrip(
//    val id: String,
//    val airline: String,
//    val flightNumber: String,
//    val departureCity: String,
//    val departureCode: String,
//    val departureTime: Date,
//    val arrivalCity: String,
//    val arrivalCode: String,
//    val arrivalTime: Date,
//    val status: TripStatus,
//    val price: Double,
//    val terminal: String = "T2",
//    val gate: String = "B12",
//    val seat: String = "14C",
//    val baggageAllowance: String = "1 x 23kg",
//    val mealOption: String = "Standard"
//)
//
//// Enum for trip status
//enum class TripStatus {
//    UPCOMING,
//    IN_PROGRESS,
//    COMPLETED,
//    CANCELLED
//}
//
//// Color mapping for different trip statuses
//val statusColors = mapOf(
//    TripStatus.UPCOMING to Color(0xFF4CAF50),
//    TripStatus.IN_PROGRESS to Color(0xFF2196F3),
//    TripStatus.COMPLETED to Color(0xFF9E9E9E),
//    TripStatus.CANCELLED to Color(0xFFE91E63)
//)
//
//// Status label text mapping
//val statusLabels = mapOf(
//    TripStatus.UPCOMING to "Upcoming",
//    TripStatus.IN_PROGRESS to "In Progress",
//    TripStatus.COMPLETED to "Completed",
//    TripStatus.CANCELLED to "Cancelled"
//)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TripManagementScreen(mainNavController: NavHostController) {
//    val flights = remember {
//        mutableStateOf(getSampleFlights())
//    }
//
//    var selectedTab by remember { mutableStateOf(0) }
//    val tabs = listOf("All Trips", "Upcoming", "Past")
//
//    // Track if details dialog is visible
//    var showDetailsDialog by remember { mutableStateOf(false) }
//    var selectedFlight by remember { mutableStateOf<FlightTrip?>(null) }
//
//    // Track if manage dialog is visible
//    var showManageDialog by remember { mutableStateOf(false) }
//
//    // Animation for title
//    val titleScale = remember { Animatable(0.8f) }
//    LaunchedEffect(Unit) {
//        titleScale.animateTo(
//            targetValue = 1f,
//            animationSpec = spring(
//                dampingRatio = Spring.DampingRatioMediumBouncy,
//                stiffness = Spring.StiffnessMedium
//            )
//        )
//    }
//
//    // Show details dialog if needed
//    if (showDetailsDialog && selectedFlight != null) {
//        FlightDetailsDialog(
//            flight = selectedFlight!!,
//            onDismiss = { showDetailsDialog = false }
//        )
//    }
//
//    // Show manage dialog if needed
//    if (showManageDialog && selectedFlight != null) {
//        FlightManageDialog(
//            flight = selectedFlight!!,
//            onDismiss = { showManageDialog = false }
//        )
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        "My Trips",
//                        modifier = Modifier.graphicsLayer {
//                            scaleX = titleScale.value
//                            scaleY = titleScale.value
//                        }
//                    )
//                },
//                actions = {
//                    // Search button with animation
//                    var searchScale by remember { mutableFloatStateOf(1f) }
//                    IconButton(
//                        onClick = { /* TODO: Implement search */ },
//                        modifier = Modifier.scale(searchScale)
//                    ) {
//                        Icon(
//                            Icons.Default.Search,
//                            contentDescription = "Search",
//                            modifier = Modifier.pointerInput(
//                                onPressed = { searchScale = 0.8f },
//                                onReleased = { searchScale = 1f }
//                            )
//                        )
//                    }
//
//                    // Filter button with animation
//                    var filterScale by remember { mutableFloatStateOf(1f) }
//                    IconButton(
//                        onClick = { /* TODO: Implement filter */ },
//                        modifier = Modifier.scale(filterScale)
//                    ) {
//                        Icon(
//                            painter = painterResource(R.drawable.filter_ic),
//                            contentDescription = "Filter",
//                            modifier = Modifier.pointerInput(
//                                onPressed = { filterScale = 0.8f },
//                                onReleased = { filterScale = 1f }
//                            )
//                        )
//                    }
//                }
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//        ) {
//            // Animated Tab selection
//            val density = LocalDensity.current
//            val tabWidthPx = with(density) { (screenWidth / tabs.size).toPx() }
//            val indicatorOffset = remember { Animatable(0f) }
//
//            LaunchedEffect(selectedTab) {
//                indicatorOffset.animateTo(
//                    targetValue = tabWidthPx * selectedTab,
//                    animationSpec = spring(
//                        dampingRatio = Spring.DampingRatioLowBouncy,
//                        stiffness = Spring.StiffnessLow
//                    )
//                )
//            }
//
//            // Tabs with custom indicator animation
//            TabRow(
//                selectedTabIndex = selectedTab,
//                modifier = Modifier.fillMaxWidth(),
//                indicator = { tabPositions ->
//                    Box(
//                        Modifier
//                            .tabIndicatorOffset(tabPositions[selectedTab])
//                            .height(3.dp)
//                            .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
//                            .background(MaterialTheme.colorScheme.primary)
//                    )
//                }
//            ) {
//                tabs.forEachIndexed { index, title ->
//                    Tab(
//                        selected = selectedTab == index,
//                        onClick = { selectedTab = index },
//                        text = {
//                            Text(
//                                text = title,
//                                modifier = Modifier.graphicsLayer {
//                                    alpha = if (selectedTab == index) 1f else 0.7f
//                                    scaleX = if (selectedTab == index) 1.1f else 1f
//                                    scaleY = if (selectedTab == index) 1.1f else 1f
//                                }
//                            )
//                        }
//                    )
//                }
//            }
//
//            // Flight listings with animations
//            val filteredFlights = when (selectedTab) {
//                1 -> flights.value.filter { it.status == TripStatus.UPCOMING || it.status == TripStatus.IN_PROGRESS }
//                2 -> flights.value.filter { it.status == TripStatus.COMPLETED || it.status == TripStatus.CANCELLED }
//                else -> flights.value
//            }
//
//            // Crossfade animation when switching tabs
//            Crossfade(
//                targetState = selectedTab,
//                animationSpec = tween(300)
//            ) { tab ->
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(horizontal = 16.dp)
//                ) {
//                    itemsIndexed(
//                        items = filteredFlights,
//                        key = { _, flight -> flight.id }
//                    ) { index, flight ->
//                        // Animate each card entry with staggered delay
//                        var visible by remember { mutableStateOf(false) }
//                        LaunchedEffect(tab, flight.id) {
//                            delay(index * 100L)
//                            visible = true
//                        }
//
//                        AnimatedVisibility(
//                            visible = visible,
//                            enter = slideInVertically(
//                                initialOffsetY = { it * 2 },
//                                animationSpec = spring(
//                                    dampingRatio = Spring.DampingRatioLowBouncy,
//                                    stiffness = Spring.StiffnessMedium
//                                )
//                            ) + fadeIn(),
//                            exit = slideOutVertically() + fadeOut()
//                        ) {
//                            FlightCardTrip(flightTrip = flight,
//                                onDetailsClick = {
//                                    selectedFlight = flight
//                                    showDetailsDialog = true
//                                },
//                                onManageClick = {
//                                    selectedFlight = flight
//                                    showManageDialog = true
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun FlightCardTrip(flightTrip: FlightTrip,
//                   onDetailsClick: () -> Unit,
//                   onManageClick: () -> Unit) {
//    val dateFormatter = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
//    val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
//
//    // Card animation states
//    var expanded by remember { mutableStateOf(false) }
//    val cardElevation by animateDpAsState(
//        targetValue = if (expanded) 8.dp else 4.dp,
//        animationSpec = spring(
//            dampingRatio = Spring.DampingRatioMediumBouncy,
//            stiffness = Spring.StiffnessLow
//        ),
//        label = "cardElevation"
//    )
//
//    // Animated plane position
//    val infiniteTransition = rememberInfiniteTransition(label = "planeMovement")
//    val planePosition by infiniteTransition.animateFloat(
//        initialValue = 0f,
//        targetValue = 1f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(2000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "planePosition"
//    )
//
//    // Animated pulse for status indicator
//    val statusPulse by infiniteTransition.animateFloat(
//        initialValue = 0.8f,
//        targetValue = 1.2f,
//        animationSpec = infiniteRepeatable(
//            animation = tween(1000, easing = FastOutSlowInEasing),
//            repeatMode = RepeatMode.Reverse
//        ),
//        label = "statusPulse"
//    )
//
//    val pulseScale = when (flightTrip.status) {
//        TripStatus.IN_PROGRESS -> statusPulse
//        TripStatus.UPCOMING -> if (expanded) statusPulse else 1f
//        else -> 1f
//    }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp)
//            .clickable { expanded = !expanded },
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .animateContentSize(
//                    animationSpec = spring(
//                        dampingRatio = Spring.DampingRatioMediumBouncy,
//                        stiffness = Spring.StiffnessLow
//                    )
//                )
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = flightTrip.airline,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp
//                )
//
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(4.dp)
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(8.dp)
//                            .clip(CircleShape)
//                            .background(statusColors[flightTrip.status] ?: Color.Gray)
//                            .scale(pulseScale)
//                    )
//                    Text(
//                        text = statusLabels[flightTrip.status] ?: "Unknown",
//                        color = statusColors[flightTrip.status] ?: Color.Gray,
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                // Departure information
//                Column(
//                    horizontalAlignment = Alignment.Start
//                ) {
//                    Text(
//                        text = flightTrip.departureCode,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 24.sp
//                    )
//                    Text(
//                        text = flightTrip.departureCity,
//                        color = Color.Gray,
//                        fontSize = 14.sp
//                    )
//                    Text(
//                        text = timeFormatter.format(flightTrip.departureTime),
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//
//                // Flight path visualization with animated plane
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center,
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(
//                        text = "Flight ${flightTrip.flightNumber}",
//                        fontSize = 12.sp,
//                        color = Color.Gray
//                    )
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier.padding(vertical = 4.dp)
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .size(8.dp)
//                                .clip(CircleShape)
//                                .background(MaterialTheme.colorScheme.primary)
//                        )
//                        Box(
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            HorizontalDivider(
//                                modifier = Modifier.fillMaxWidth(),
//                                color = MaterialTheme.colorScheme.primary
//                            )
//                            // Animated airplane
//                            Icon(
//                                painterResource(R.drawable.plane_path) ,
//                                contentDescription = null,
//                                tint = MaterialTheme.colorScheme.primary,
//                                modifier = Modifier
//                                    .size(24.dp)
//                                    .graphicsLayer {
//                                        translationX = planePosition * (this.size.width - 24f)
//                                    }
//                            )
//                        }
//                        Box(
//                            modifier = Modifier
//                                .size(8.dp)
//                                .clip(CircleShape)
//                                .background(MaterialTheme.colorScheme.primary)
//                        )
//                    }
//                    Text(
//                        text = dateFormatter.format(flightTrip.departureTime),
//                        fontSize = 12.sp,
//                        color = Color.Gray
//                    )
//                }
//
//                // Arrival information
//                Column(
//                    horizontalAlignment = Alignment.End
//                ) {
//                    Text(
//                        text = flightTrip.arrivalCode,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 24.sp
//                    )
//                    Text(
//                        text = flightTrip.arrivalCity,
//                        color = Color.Gray,
//                        fontSize = 14.sp
//                    )
//                    Text(
//                        text = timeFormatter.format(flightTrip.arrivalTime),
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            // Expandable content with animation
//            if (expanded) {
//                HorizontalDivider()
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // Flight details section that appears when expanded
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = 8.dp)
//                ) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//                        DetailItem(title = "Terminal", value = "T2")
//                        DetailItem(title = "Gate", value = "B12")
//                        DetailItem(title = "Seat", value = "14C")
//                    }
//
//                    Spacer(modifier = Modifier.height(12.dp))
//
//                    // Progress indicator for upcoming flights
//                    if (flightTrip.status == TripStatus.UPCOMING || flightTrip.status == TripStatus.IN_PROGRESS) {
//                        val progress = when (flightTrip.status) {
//                            TripStatus.UPCOMING -> 0f
//                            TripStatus.IN_PROGRESS -> 0.5f
//                            else -> 1f
//                        }
//
//                        Column {
//                            Text(
//                                text = "Flight Progress",
//                                fontSize = 14.sp,
//                                fontWeight = FontWeight.Medium,
//                                modifier = Modifier.padding(bottom = 4.dp)
//                            )
//
//                            LinearProgressIndicator(
//                                progress = { progress },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .height(6.dp)
//                                    .clip(RoundedCornerShape(3.dp)),
//                                color = MaterialTheme.colorScheme.primary,
//                                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
//                            )
//                        }
//
//                        Spacer(modifier = Modifier.height(12.dp))
//                    }
//                }
//
//                HorizontalDivider()
//
//                Spacer(modifier = Modifier.height(12.dp))
//            }
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "$${String.format("%.2f", flightTrip.price)}",
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 18.sp,
//                    color = MaterialTheme.colorScheme.primary
//                )
//
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    FilledTonalButton(
//                        onClick =onDetailsClick,
//                        modifier = Modifier.height(40.dp)
//                    ) {
//                        AnimatedContent(
//                            targetState = expanded,
//                            transitionSpec = {
//                                fadeIn() + slideInVertically() togetherWith
//                                        fadeOut() + slideOutVertically()
//                            },
//                            label = "detailsButtonText"
//                        ) { isExpanded ->
//                            Text(if (isExpanded) "Hide" else "Details")
//                        }
//                    }
//
//                    Button(
//                        onClick = onManageClick,
//                        modifier = Modifier.height(40.dp)
//                    ) {
//                        Text("Manage")
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun DetailItem(title: String, value: String) {
//    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//        Text(
//            text = title,
//            fontSize = 12.sp,
//            color = Color.Gray
//        )
//        Text(
//            text = value,
//            fontSize = 16.sp,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}
//
//// Extension function for pointer input animations
//@OptIn(ExperimentalComposeUiApi::class)
//fun Modifier.pointerInput(
//    onPressed: () -> Unit,
//    onReleased: () -> Unit
//): Modifier = composed {
//    var isPressed by remember { mutableStateOf(false) }
//
//    Modifier.clickable(
//        interactionSource = remember { MutableInteractionSource() },
//        indication = null
//    ) {
//        // No action on click as this is just for visual feedback
//    }.composed {
//        LaunchedEffect(isPressed) {
//            if (isPressed) onPressed() else onReleased()
//        }
//
//        this.pointerInteropFilter { event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    isPressed = true
//                    true
//                }
//                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
//                    isPressed = false
//                    true
//                }
//                else -> false
//            }
//        }
//    }
//}
//
//// For demo/preview purposes
//val screenWidth = 360.dp
//
//// Sample data for preview
//fun getSampleFlights(): List<FlightTrip> {
//    val calendar = Calendar.getInstance()
//
//    // Create upcoming flight
//    val upcomingDeparture = calendar.clone() as Calendar
//    upcomingDeparture.add(Calendar.DAY_OF_MONTH, 5)
//    upcomingDeparture.set(Calendar.HOUR_OF_DAY, 10)
//    upcomingDeparture.set(Calendar.MINUTE, 30)
//
//    val upcomingArrival = upcomingDeparture.clone() as Calendar
//    upcomingArrival.add(Calendar.HOUR_OF_DAY, 3)
//
//    // Create in-progress flight
//    val inProgressDeparture = calendar.clone() as Calendar
//    inProgressDeparture.add(Calendar.HOUR_OF_DAY, -2)
//
//    val inProgressArrival = inProgressDeparture.clone() as Calendar
//    inProgressArrival.add(Calendar.HOUR_OF_DAY, 4)
//
//    // Create completed flight
//    val completedDeparture = calendar.clone() as Calendar
//    completedDeparture.add(Calendar.DAY_OF_MONTH, -10)
//    completedDeparture.set(Calendar.HOUR_OF_DAY, 14)
//    completedDeparture.set(Calendar.MINUTE, 15)
//
//    val completedArrival = completedDeparture.clone() as Calendar
//    completedArrival.add(Calendar.HOUR_OF_DAY, 2)
//    completedArrival.add(Calendar.MINUTE, 30)
//
//    // Create cancelled flight
//    val cancelledDeparture = calendar.clone() as Calendar
//    cancelledDeparture.add(Calendar.DAY_OF_MONTH, -3)
//    cancelledDeparture.set(Calendar.HOUR_OF_DAY, 7)
//    cancelledDeparture.set(Calendar.MINUTE, 45)
//
//    val cancelledArrival = cancelledDeparture.clone() as Calendar
//    cancelledArrival.add(Calendar.HOUR_OF_DAY, 1)
//    cancelledArrival.add(Calendar.MINUTE, 30)
//
//    return listOf(
//        FlightTrip(
//            id = "FL001",
//            airline = "SkyWays Airlines",
//            flightNumber = "SW1234",
//            departureCity = "New York",
//            departureCode = "JFK",
//            departureTime = upcomingDeparture.time,
//            arrivalCity = "Los Angeles",
//            arrivalCode = "LAX",
//            arrivalTime = upcomingArrival.time,
//            status = TripStatus.UPCOMING,
//            price = 329.99
//        ),
//        FlightTrip(
//            id = "FL002",
//            airline = "Global Airways",
//            flightNumber = "GA7890",
//            departureCity = "Chicago",
//            departureCode = "ORD",
//            departureTime = inProgressDeparture.time,
//            arrivalCity = "Miami",
//            arrivalCode = "MIA",
//            arrivalTime = inProgressArrival.time,
//            status = TripStatus.IN_PROGRESS,
//            price = 249.50
//        ),
//        FlightTrip(
//            id = "FL003",
//            airline = "Pacific Express",
//            flightNumber = "PE4567",
//            departureCity = "San Francisco",
//            departureCode = "SFO",
//            departureTime = completedDeparture.time,
//            arrivalCity = "Seattle",
//            arrivalCode = "SEA",
//            arrivalTime = completedArrival.time,
//            status = TripStatus.COMPLETED,
//            price = 199.99
//        ),
//        FlightTrip(
//            id = "FL004",
//            airline = "Mountain Air",
//            flightNumber = "MA2468",
//            departureCity = "Denver",
//            departureCode = "DEN",
//            departureTime = cancelledDeparture.time,
//            arrivalCity = "Phoenix",
//            arrivalCode = "PHX",
//            arrivalTime = cancelledArrival.time,
//            status = TripStatus.CANCELLED,
//            price = 179.75
//        )
//    )
//}
//@Composable
//fun FlightDetailsDialog(flight: FlightTrip, onDismiss: () -> Unit) {
//    val dateFormatter = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
//    val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
//
//    Dialog(onDismissRequest = onDismiss) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            shape = RoundedCornerShape(16.dp),
//            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(24.dp)
//            ) {
//                // Dialog header
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "Flight Details",
//                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    IconButton(onClick = onDismiss) {
//                        Icon(Icons.Default.Close, contentDescription = "Close")
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Airline information
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Icon(
//                        painter = painterResource(R.drawable.plane_path),
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.size(32.dp)
//                    )
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Column {
//                        Text(
//                            text = flight.airline,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 18.sp
//                        )
//                        Text(
//                            text = "Flight ${flight.flightNumber}",
//                            color = Color.Gray
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // Flight route details
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Column(horizontalAlignment = Alignment.Start) {
//                        Text(
//                            text = flight.departureCode,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 32.sp
//                        )
//                        Text(
//                            text = flight.departureCity,
//                            color = Color.Gray
//                        )
//                        Text(
//                            text = dateFormatter.format(flight.departureTime),
//                            fontSize = 14.sp
//                        )
//                        Text(
//                            text = timeFormatter.format(flight.departureTime),
//                            fontWeight = FontWeight.Medium,
//                            fontSize = 14.sp
//                        )
//                    }
//
//                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                        Icon(
//                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
//                            contentDescription = null,
//                            tint = MaterialTheme.colorScheme.primary
//                        )
//                        Text(
//                            text = calculateFlightDuration(flight.departureTime, flight.arrivalTime),
//                            fontSize = 12.sp,
//                            color = Color.Gray
//                        )
//                    }
//
//                    Column(horizontalAlignment = Alignment.End) {
//                        Text(
//                            text = flight.arrivalCode,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 32.sp
//                        )
//                        Text(
//                            text = flight.arrivalCity,
//                            color = Color.Gray
//                        )
//                        Text(
//                            text = dateFormatter.format(flight.arrivalTime),
//                            fontSize = 14.sp
//                        )
//                        Text(
//                            text = timeFormatter.format(flight.arrivalTime),
//                            fontWeight = FontWeight.Medium,
//                            fontSize = 14.sp
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//                Divider()
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Flight details
//                DetailItemWithIcon(
//                    icon = Icons.Default.Place,
//                    title = "Terminal",
//                    value = flight.terminal
//                )
//                DetailItemWithIcon(
//                    icon = Icons.Default.LocationOn,
//                    title = "Gate",
//                    value = flight.gate
//                )
//                DetailItemWithIcon(
//                    icon = Icons.Default.Close,
//                    title = "Seat",
//                    value = flight.seat
//                )
//                DetailItemWithIcon(
//                    icon = Icons.Default.LocationOn,
//                    title = "Baggage",
//                    value = flight.baggageAllowance
//                )
//                DetailItemWithIcon(
//                    icon = Icons.Default.Settings,
//                    title = "Meal",
//                    value = flight.mealOption
//                )
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // Status information
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .background(
//                            color = statusColors[flight.status]?.copy(alpha = 0.1f) ?: Color.Gray.copy(alpha = 0.1f),
//                            shape = RoundedCornerShape(8.dp)
//                        )
//                        .padding(12.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Box(
//                        modifier = Modifier
//                            .size(12.dp)
//                            .clip(CircleShape)
//                            .background(statusColors[flight.status] ?: Color.Gray)
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = "Flight Status: ${statusLabels[flight.status]}",
//                        fontWeight = FontWeight.Medium,
//                        color = statusColors[flight.status] ?: Color.Gray
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(24.dp))
//
//                // Bottom action buttons
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    OutlinedButton(
//                        onClick = onDismiss,
//                        modifier = Modifier.padding(end = 8.dp)
//                    ) {
//                        Text("Close")
//                    }
//
//                    Button(
//                        onClick = { /* TODO: Implement download boarding pass */ }
//                    ) {
//                        Text("Boarding Pass")
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun DetailItemWithIcon(icon: ImageVector, title: String, value: String) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null,
//            tint = MaterialTheme.colorScheme.primary,
//            modifier = Modifier.size(24.dp)
//        )
//
//        Spacer(modifier = Modifier.width(16.dp))
//
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = title,
//                fontSize = 14.sp,
//                color = Color.Gray
//            )
//
//            Text(
//                text = value,
//                fontWeight = FontWeight.Medium,
//                fontSize = 14.sp
//            )
//        }
//    }
//}
//
//private fun ColumnScope.calculateFlightDuration(departureTime: Date, arrivalTime: Date): String {
//    // حساب الفرق بالمللي ثانية
//    val diff = arrivalTime.time - departureTime.time
//
//    // تحويل إلى ساعات ودقائق
//    val hours = diff / (1000 * 60 * 60)
//    val minutes = (diff % (1000 * 60 * 60)) / (1000 * 60)
//
//    return when {
//        hours > 0 && minutes > 0 -> "${hours}h ${minutes}m"
//        hours > 0 -> "${hours}h"
//        else -> "${minutes}m"
//    }
//}
//
//@Composable
//fun FlightManageDialog(flight: FlightTrip, onDismiss: () -> Unit) {
//    var selectedTab by remember { mutableStateOf(0) }
//    val tabs = listOf("Change Seat", "Meal Options", "Add Baggage")
//
//    Dialog(onDismissRequest = onDismiss) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .heightIn(max = 560.dp)
//                .padding(16.dp),
//            shape = RoundedCornerShape(16.dp),
//            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(vertical = 16.dp)
//            ) {
//                // Dialog header
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 24.dp),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "Manage Booking",
//                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = FontWeight.Bold
//                    )
//
//                    IconButton(onClick = onDismiss) {
//                        Icon(Icons.Default.Close, contentDescription = "Close")
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                // Flight summary
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 24.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Column {
//                        Text(
//                            text = "${flight.flightNumber}: ${flight.departureCode} → ${flight.arrivalCode}",
//                            fontWeight = FontWeight.Medium
//                        )
//                        Text(
//                            text = flight.airline,
//                            color = Color.Gray,
//                            fontSize = 14.sp
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Tab selection
//                TabRow(
//                    selectedTabIndex = selectedTab,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    tabs.forEachIndexed { index, title ->
//                        Tab(
//                            selected = selectedTab == index,
//                            onClick = { selectedTab = index },
//                            text = { Text(title) }
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Content based on selected tab
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .weight(1f)
//                        .padding(horizontal = 24.dp)
//                ) {
//                    when (selectedTab) {
//                        0 -> SeatSelectionContent(flight.seat)
//                        1 -> MealOptionsContent(flight.mealOption)
//                        2 -> BaggageContent(flight.baggageAllowance)
//                    }
//                }
//
//                HorizontalDivider(
//                    modifier = Modifier.padding(vertical = 16.dp),
//                )
//
//                // Bottom action buttons
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 24.dp),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    OutlinedButton(
//                        onClick = onDismiss,
//                        modifier = Modifier.padding(end = 8.dp)
//                    ) {
//                        Text("Cancel")
//                    }
//
//                    Button(
//                        onClick = { /* TODO: Save changes */ }
//                    ) {
//                        Text("Save Changes")
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun SeatSelectionContent(currentSeat: String) {
//    var selectedSeat by remember { mutableStateOf(currentSeat) }
//    val seatRows = 10
//    val seatsPerRow = 6
//    val seatTypes = listOf("A", "B", "C", "D", "E", "F")
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = "Select your preferred seat",
//            fontWeight = FontWeight.Bold,
//            fontSize = 16.sp,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // Seat map legend
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 12.dp),
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Box(
//                    modifier = Modifier
//                        .size(16.dp)
//                        .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(4.dp))
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Text("Available", fontSize = 12.sp)
//            }
//
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Box(
//                    modifier = Modifier
//                        .size(16.dp)
//                        .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(4.dp))
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Text("Selected", fontSize = 12.sp)
//            }
//
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Box(
//                    modifier = Modifier
//                        .size(16.dp)
//                        .background(Color.Gray, shape = RoundedCornerShape(4.dp))
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Text("Occupied", fontSize = 12.sp)
//            }
//        }
//
//        // Airplane layout
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 8.dp)
//        ) {
//            Column(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                // Airplane cockpit
//                Box(
//                    modifier = Modifier
//                        .width(80.dp)
//                        .height(40.dp)
//                        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
//                        .background(Color.LightGray)
//                )
//
//                // Cabin
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth(0.9f)
//                        .height(300.dp)
//                        .background(Color.LightGray)
//                ) {
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(horizontal = 12.dp, vertical = 8.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        items(seatRows) { row ->
//                            val rowNumber = row + 1
//
//                            Row(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 4.dp),
//                                horizontalArrangement = Arrangement.SpaceBetween
//                            ) {
//                                Text(
//                                    text = rowNumber.toString(),
//                                    fontSize = 10.sp,
//                                    modifier = Modifier.width(15.dp),
//                                    textAlign = TextAlign.Center
//                                )
//
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween
//                                ) {
//                                    // Left seats (A, B, C)
//                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
//                                        for (i in 0 until 3) {
//                                            val seatCode = "${rowNumber}${seatTypes[i]}"
//                                            val isOccupied = isRandomlyOccupied(seatCode)
//                                            val isSelected = seatCode == selectedSeat
//
//                                            SeatItem(
//                                                seatCode = seatCode,
//                                                isOccupied = isOccupied,
//                                                isSelected = isSelected,
//                                                onSeatSelected = { if (!isOccupied) selectedSeat = it }
//                                            )
//                                        }
//                                    }
//
//                                    Spacer(modifier = Modifier.width(20.dp))
//
//                                    // Right seats (D, E, F)
//                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
//                                        for (i in 3 until 6) {
//                                            val seatCode = "${rowNumber}${seatTypes[i]}"
//                                            val isOccupied = isRandomlyOccupied(seatCode)
//                                            val isSelected = seatCode == selectedSeat
//
//                                            SeatItem(
//                                                seatCode = seatCode,
//                                                isOccupied = isOccupied,
//                                                isSelected = isSelected,
//                                                onSeatSelected = { if (!isOccupied) selectedSeat = it }
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Current selection
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                    shape = RoundedCornerShape(8.dp)
//                )
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = "Selected Seat:",
//                fontWeight = FontWeight.Medium
//            )
//            Text(
//                text = selectedSeat,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.primary
//            )
//        }
//    }
//}
//
//@Composable
//fun SeatItem(
//    seatCode: String,
//    isOccupied: Boolean,
//    isSelected: Boolean,
//    onSeatSelected: (String) -> Unit
//) {
//    val backgroundColor = when {
//        isSelected -> MaterialTheme.colorScheme.primary
//        isOccupied -> Color.Gray
//        else -> Color(0xFFE0E0E0)
//    }
//
//    val textColor = when {
//        isSelected -> Color.White
//        else -> Color.Black.copy(alpha = 0.7f)
//    }
//
//    Box(
//        modifier = Modifier
//            .size(30.dp)
//            .clip(RoundedCornerShape(4.dp))
//            .background(backgroundColor)
//            .clickable(enabled = !isOccupied) { onSeatSelected(seatCode) },
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = seatCode.takeLast(1),
//            fontSize = 12.sp,
//            color = textColor,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}
//
//// Helper function to simulate occupied seats
//private fun isRandomlyOccupied(seatCode: String): Boolean {
//    return seatCode.hashCode() % 3 == 0
//}
//
//@Composable
//fun MealOptionsContent(currentMeal: String) {
//    var selectedMeal by remember { mutableStateOf(currentMeal) }
//    val mealOptions = listOf(
//        MealOption("Standard", "Regular meal with meat, vegetable, and dessert", R.drawable.plane_path, 0.0),
//        MealOption("Vegetarian", "Plant-based meal with fresh vegetables and fruit", R.drawable.plane_path, 5.99),
//        MealOption("Vegan", "100% plant-based meal without any animal products", R.drawable.plane_path, 7.99),
//        MealOption("Gluten-Free", "Special meal without gluten-containing ingredients", R.drawable.plane_path, 6.99),
//        MealOption("Low Calorie", "Light meal with fewer calories", R.drawable.plane_path, 4.99),
//        MealOption("Premium", "Gourmet meal with premium ingredients", R.drawable.plane_path, 14.99)
//    )
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = "Select your meal preference",
//            fontWeight = FontWeight.Bold,
//            fontSize = 16.sp,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        LazyColumn(
//            modifier = Modifier.fillMaxWidth(),
//            verticalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            items(mealOptions.size) { index ->
//                val meal = mealOptions[index]
//                val isSelected = meal.name == selectedMeal
//
//                MealOptionItem(
//                    meal = meal,
//                    isSelected = isSelected,
//                    onMealSelected = { selectedMeal = meal.name }
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Current selection
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                    shape = RoundedCornerShape(8.dp)
//                )
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = "Selected Meal:",
//                fontWeight = FontWeight.Medium
//            )
//            Text(
//                text = selectedMeal,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.primary
//            )
//        }
//    }
//}
//
//data class MealOption(
//    val name: String,
//    val description: String,
//    val imageRes: Int,
//    val additionalPrice: Double
//)
//
//@Composable
//fun MealOptionItem(
//    meal: MealOption,
//    isSelected: Boolean,
//    onMealSelected: () -> Unit
//) {
//    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
//    val backgroundColor = if (isSelected)
//        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else
//        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onMealSelected() },
//        shape = RoundedCornerShape(12.dp),
//        border = BorderStroke(
//            width = if (isSelected) 2.dp else 0.dp,
//            color = borderColor
//        ),
//        colors = CardDefaults.cardColors(
//            containerColor = backgroundColor
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            // Meal icon
//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    painter = painterResource(id = meal.imageRes),
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.size(24.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            // Meal details
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(
//                    text = meal.name,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp
//                )
//                Text(
//                    text = meal.description,
//                    fontSize = 14.sp,
//                    color = Color.Gray,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//
//            // Price
//            if (meal.additionalPrice > 0) {
//                Text(
//                    text = "+$${String.format("%.2f", meal.additionalPrice)}",
//                    fontWeight = FontWeight.Bold,
//                    color = MaterialTheme.colorScheme.primary
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun BaggageContent(currentBaggage: String) {
//    var selectedBaggage by remember { mutableStateOf(currentBaggage) }
//    val baggageOptions = listOf(
//        BaggageOption("1 x 23kg", "Standard allowance", 0.0),
//        BaggageOption("2 x 23kg", "Additional baggage", 35.0),
//        BaggageOption("3 x 23kg", "Extra baggage", 70.0),
//        BaggageOption("1 x 32kg", "Heavy baggage", 50.0)
//    )
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(
//            text = "Select your baggage allowance",
//            fontWeight = FontWeight.Bold,
//            fontSize = 16.sp,
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//
//        // Baggage options
//        Column(
//            modifier = Modifier.fillMaxWidth(),
//            verticalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            baggageOptions.forEach { baggage ->
//                val isSelected = baggage.allowance == selectedBaggage
//
//                BaggageOptionItem(
//                    baggage = baggage,
//                    isSelected = isSelected,
//                    onBaggageSelected = { selectedBaggage = baggage.allowance }
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Baggage policy note
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    color = Color(0xFFFFF9C4),
//                    shape = RoundedCornerShape(8.dp)
//                )
//                .padding(12.dp)
//        ) {
//            Text(
//                text = "Baggage Policy",
//                fontWeight = FontWeight.Bold,
//                fontSize = 14.sp
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = "Carry-on baggage: 1 piece up to 7kg\n" +
//                        "Maximum dimensions per checked bag: 158cm (length + width + height)\n" +
//                        "Oversized or overweight items may incur additional fees",
//                fontSize = 12.sp,
//                lineHeight = 16.sp
//            )
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Current selection
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(
//                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
//                    shape = RoundedCornerShape(8.dp)
//                )
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Text(
//                text = "Selected Baggage:",
//                fontWeight = FontWeight.Medium
//            )
//            Text(
//                text = selectedBaggage,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.primary
//            )
//        }
//    }
//}
//
//data class BaggageOption(
//    val allowance: String,
//    val description: String,
//    val additionalPrice: Double
//)
//
//@Composable
//fun BaggageOptionItem(
//    baggage: BaggageOption,
//    isSelected: Boolean,
//    onBaggageSelected: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onBaggageSelected() },
//        shape = RoundedCornerShape(12.dp),
//        border = BorderStroke(
//            width = if (isSelected) 2.dp else 1.dp,
//            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray
//        ),
//        colors = CardDefaults.cardColors(
//            containerColor = if (isSelected)
//                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else
//                MaterialTheme.colorScheme.surface
//        )
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                // Baggage icon
//                Icon(
//                    painter = painterResource(R.drawable.plane_path), // Replace with actual baggage icon
//                    contentDescription = null,
//                    modifier = Modifier.size(36.dp),
//                    tint = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
//                )
//
//                Spacer(modifier = Modifier.width(16.dp))
//
//                // Baggage details
//                Column {
//                    Text(
//                        text = baggage.allowance,
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 16.sp
//                    )
//                    Text(
//                        text = baggage.description,
//                        fontSize = 14.sp,
//                        color = Color.Gray
//                    )
//                }
//            }
//
//            // Price info
//            Column(horizontalAlignment = Alignment.End) {
//                if (baggage.additionalPrice > 0) {
//                    Text(
//                        text = "+$${String.format("%.2f", baggage.additionalPrice)}",
//                        fontWeight = FontWeight.Bold,
//                        color = MaterialTheme.colorScheme.primary
//                    )
//                } else {
//                    Text(
//                        text = "Included",
//                        fontSize = 14.sp,
//                        color = Color(0xFF4CAF50)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun TripManagementScreenPreview() {
//    MaterialTheme {
//        TripManagementScreen(rememberNavController())
//    }
//}