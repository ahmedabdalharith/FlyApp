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
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.ParticleEffectBackground
import com.example.flyapp.ui.theme.navigition.Screen
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

data class DestinationItem(
    val id: String,
    val city: String,
    val country: String,
    val imageResId: Int,
    val price: Double,
    val rating: Float,
    val isFeatured: Boolean = false
)

data class OfferItem(
    val id: String,
    val title: String,
    val description: String,
    val imageResId: Int,
    val discount: Int
)

enum class BookingType {
    FLIGHTS, HOTELS, CARS, TRAINS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    // User info
    val userName = "Ahmed"

    // Current Date
    val dateFormat = SimpleDateFormat("EEEE, dd MMM", Locale.getDefault())
    val currentDate = dateFormat.format(Date())

    // Animation states for staggered UI appearance
    var showHeaderSection by remember { mutableStateOf(false) }
    var showSearchSection by remember { mutableStateOf(false) }
    var showBookingTypes by remember { mutableStateOf(false) }
    var showMainContent by remember { mutableStateOf(false) }

    // Alpha animations for staggered appearance
    val headerAlpha by animateFloatAsState(
        targetValue = if (showHeaderSection) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "header_alpha"
    )

    val searchAlpha by animateFloatAsState(
        targetValue = if (showSearchSection) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "search_alpha"
    )

    val bookingTypesAlpha by animateFloatAsState(
        targetValue = if (showBookingTypes) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "booking_types_alpha"
    )

    val mainContentAlpha by animateFloatAsState(
        targetValue = if (showMainContent) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "main_content_alpha"
    )

    // Trigger animations sequentially
    LaunchedEffect(key1 = true) {
        showHeaderSection = true
        delay(300)
        showSearchSection = true
        delay(200)
        showBookingTypes = true
        delay(200)
        showMainContent = true
    }

    // Search query
    var searchQuery by remember { mutableStateOf("") }

    // Selected booking type
    var selectedBookingType by remember { mutableStateOf(BookingType.FLIGHTS) }

    // Sample destinations
    val popularDestinations = listOf(
        DestinationItem(
            id = "dest1",
            city = "New York",
            country = "USA",
            imageResId = R.drawable.new_york,
            price = 299.0,
            rating = 4.8f
        ),
        DestinationItem(
            id = "dest2",
            city = "Paris",
            country = "France",
            imageResId = R.drawable.paris,
            price = 349.0,
            rating = 4.7f
        ),
        DestinationItem(
            id = "dest3",
            city = "Tokyo",
            country = "Japan",
            imageResId = R.drawable.tokyo,
            price = 499.0,
            rating = 4.9f
        ),
        DestinationItem(
            id = "dest4",
            city = "Sydney",
            country = "Australia",
            imageResId = R.drawable.sydney,
            price = 599.0,
            rating = 4.6f
        )
    )

    // Sample offers
    val specialOffers = listOf(
        OfferItem(
            id = "offer1",
            title = "Summer Sale",
            description = "Get 20% off on selected flights",
            imageResId = R.drawable.summer_offer,
            discount = 20
        ),
        OfferItem(
            id = "offer2",
            title = "Weekend Getaway",
            description = "Special deals for quick weekend trips",
            imageResId = R.drawable.summer_offer,
            discount = 15
        ),
        OfferItem(
            id = "offer3",
            title = "Family Package",
            description = "Special discounts for family travel",
            imageResId = R.drawable.summer_offer,
            discount = 25
        )
    )

    // Featured destination
    val featuredDestination = DestinationItem(
        id = "featured1",
        city = "Bali",
        country = "Indonesia",
        imageResId = R.drawable.emirates_logo,
        price = 449.0,
        rating = 4.9f,
        isFeatured = true
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF001034),  // Matching SplashScreen gradient
                        Color(0xFF003045),
                        Color(0xFF004D40)
                    )
                )
            )
    ) {
        // Enhanced background animations - use ParticleEffectContainer from SplashScreen for consistency
        ParticleEffectBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    modifier = Modifier.alpha(headerAlpha),
                    title = {
                        Column {
                            Text(
                                text = "Hello, $userName 👋",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = currentDate,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    },
                    actions = {
                        // Notification badge with animation
                        Box {
                            IconButton(onClick = {
                                navController.navigate(Screen.NotificationScreen.route)
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.White
                                )
                            }

                            // Notification indicator dot with pulse animation
                            val infiniteTransition = rememberInfiniteTransition(label = "notification_pulse")
                            val pulseScale by infiniteTransition.animateFloat(
                                initialValue = 0.8f,
                                targetValue = 1.2f,
                                animationSpec = infiniteRepeatable(
                                    animation = tween(1000, easing = FastOutSlowInEasing),
                                    repeatMode = RepeatMode.Reverse
                                ),
                                label = "notification_dot"
                            )

                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .scale(pulseScale)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-2).dp, y = 2.dp)
                                    .background(Color(0xFF4CAF50), CircleShape)
                            )
                        }

                        // User profile with glow effect
                        val glowTransition = rememberInfiniteTransition(label = "profile_glow")
                        val glowAlpha by glowTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 0.5f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(2000, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "profile_glow_alpha"
                        )

                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                        ) {
                            // Glow effect
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .alpha(glowAlpha)
                                    .background(
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                Color(0xFF4CAF50),
                                                Color.Transparent
                                            )
                                        ),
                                        shape = CircleShape
                                    )
                            )

                            // Profile avatar
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .clickable(onClick = {
                                        navController.navigate(Screen.ProfileScreen.route)
                                    })
                                    .background(Color(0xFF1A3546)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = Color.White
                                )
                            }
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
                    .verticalScroll(rememberScrollState())
            ) {
                // Search section with enhanced style and animations
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .alpha(searchAlpha)
                ) {
                    // Animated container glow
                    val glowTransition = rememberInfiniteTransition(label = "search_glow")
                    val glowAlpha by glowTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 0.5f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(2000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "search_glow_alpha"
                    )

                    // Outer glow
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .alpha(glowAlpha)
                            .shadow(16.dp, RoundedCornerShape(12.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF4CAF50).copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                    )

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(12.dp)),
                        placeholder = { Text("Search destinations", color = Color.White.copy(alpha = 0.7f)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedPlaceholderColor = Color.White.copy(alpha = 0.7f),
                            unfocusedPlaceholderColor = Color.White.copy(alpha = 0.7f),
                            focusedBorderColor = Color(0xFF4CAF50),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                            focusedContainerColor = Color(0xFF1A3546).copy(alpha = 0.8f),
                            unfocusedContainerColor = Color(0xFF1A3546).copy(alpha = 0.8f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Booking type selector with enhanced animation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(bookingTypesAlpha)
                ) {
                    // Add subtle background glow
                    val glowColorAnimation = rememberInfiniteTransition(label = "glow_color")
                    val glowOpacity by glowColorAnimation.animateFloat(
                        initialValue = 0f,
                        targetValue = 0.15f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(3000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "glow_opacity"
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(horizontal = 16.dp)
                            .alpha(glowOpacity)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF4CAF50).copy(alpha = 0.3f),
                                        Color.Transparent,
                                        Color.Transparent
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                    )

                    BookingTypeSelector(
                        selectedType = selectedBookingType,
                        onTypeSelected = { selectedBookingType = it }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Content container with fade-in animation
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(mainContentAlpha)
                ) {
                    // Quick action buttons for flights with enhanced style
                    if (selectedBookingType == BookingType.FLIGHTS) {
                        EnhancedQuickActions(navController)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Featured destination with enhanced styling
                    EnhancedFeaturedDestination(
                        destination = featuredDestination,
                        onViewClicked = { navController.navigate(Screen.BookingFlightScreen.route) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Popular destinations with enhanced section title
                    EnhancedSectionTitle(
                        title = "Popular Destinations",
                        actionText = "View All",
                        onActionClick = {
                            navController.navigate(Screen.AllDestinationsScreen.route)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(popularDestinations) { destination ->
                            EnhancedDestinationCard(
                                destination = destination,
                                onClick = {
                                    navController.navigate(Screen.BookingFlightScreen.route)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Special offers with enhanced section title
                    EnhancedSectionTitle(
                        title = "Special Offers",
                        actionText = "See All",
                        onActionClick = {
                            navController.navigate(Screen.AllOffersScreen.route)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(specialOffers) { offer ->
                            EnhancedOfferCard(
                                offer = offer,
                                onClick = {
                                    navController.navigate(Screen.OfferDetailsScreen.route)
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun EnhancedQuickActions(navController: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        EnhancedQuickActionButton(
            text = "Book Flight",
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate(Screen.BookingFlightScreen.route) }
        )

        EnhancedQuickActionButton(
            text = "Flight Status",
            modifier = Modifier.weight(1f),
            onClick = {
                navController.navigate(Screen.FlightStatusScreen.route)
            }
        )
    }
}

@Composable
fun EnhancedQuickActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Enhanced animation system
    val infiniteTransition = rememberInfiniteTransition(label = "button_animation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_pulse"
    )

    // Button glow effect
    val glowTransition = rememberInfiniteTransition(label = "glow_transition")
    val glowAlpha by glowTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Glow effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .scale(scale * 1.2f)
                .alpha(glowAlpha)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4CAF50).copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
        )

        Button(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .scale(scale)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(12.dp),
                    ambientColor = Color(0xFF4CAF50),
                    spotColor = Color(0xFF4CAF50)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1A3546)
            )
        ) {
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun EnhancedFeaturedDestination(
    destination: DestinationItem,
    onViewClicked: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "featured_animation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "featured_scale"
    )

    // Enhanced glow effect
    val glowTransition = rememberInfiniteTransition(label = "featured_glow")
    val glowAlpha by glowTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "featured_glow_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Outer glow effect
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(y = 4.dp)
                .alpha(glowAlpha)
                .scale(1.05f)
                .shadow(20.dp, RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4CAF50).copy(alpha = 0.3f),
                            Color(0xFF1565C0).copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .scale(scale)
                .shadow(10.dp, RoundedCornerShape(16.dp))
                .clickable { onViewClicked() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Background image
                Image(
                    painter = painterResource(id = destination.imageResId),
                    contentDescription = destination.city,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient overlay with animated color
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color(0xFF003045).copy(alpha = 0.8f)
                                ),
                                startY = 0f,
                                endY = 180f * 2
                            )
                        )
                )

                // Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    // Badge with animation
                    val badgeTransition = rememberInfiniteTransition(label = "featured_badge")
                    val badgeAlpha by badgeTransition.animateFloat(
                        initialValue = 0.7f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1500, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "badge_alpha"
                    )

                    Surface(
                        color = Color(0xFF4CAF50).copy(alpha = badgeAlpha),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = "Featured Destination",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "${destination.city}, ${destination.country}",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Starting from $${destination.price.toInt()}",
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )

                        // Enhanced button with glow effect
                        val buttonGlowTransition = rememberInfiniteTransition(label = "button_glow")
                        val buttonGlowAlpha by buttonGlowTransition.animateFloat(
                            initialValue = 0.5f,
                            targetValue = 0.9f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1500, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "button_glow_alpha"
                        )

                        Box {
                            // Glow effect
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .alpha(buttonGlowAlpha)
                                    .shadow(12.dp, RoundedCornerShape(8.dp))
                                    .background(
                                        Color(0xFF4CAF50).copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                            )

                            Button(
                                onClick = onViewClicked,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50)
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text("Explore")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedSectionTitle(
    title: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    // Animation for the "View All" text
    val infiniteTransition = rememberInfiniteTransition(label = "action_text")
    val textAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "text_alpha"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title with decorative element
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Decorative accent
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(24.dp)
                    .background(Color(0xFF4CAF50), RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Text(
            text = actionText,
            fontSize = 14.sp,
            color = Color(0xFF4CAF50).copy(alpha = textAlpha),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.clickable { onActionClick() }
        )
    }
}

@Composable
fun EnhancedDestinationCard(
    destination: DestinationItem,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "card_hover")
    val elevationAnim by infiniteTransition.animateFloat(
        initialValue = 2f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_elevation"
    )

    // Add subtle border glow
    // Add subtle border glow
    val borderGlowTransition = rememberInfiniteTransition(label = "border_glow")
    val borderGlowAlpha by borderGlowTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border_glow_alpha"
    )

    Box(
        modifier = Modifier
            .width(180.dp)
            .height(220.dp)
    ) {
        // Glow effect
        Box(
            modifier = Modifier
                .matchParentSize()
                .scale(1.05f)
                .alpha(borderGlowAlpha)
                .shadow(elevationAnim.dp, RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF4CAF50).copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        Card(
            modifier = Modifier
                .fillMaxSize()
                .shadow(elevationAnim.dp, RoundedCornerShape(16.dp))
                .clickable { onClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1A3546).copy(alpha = 0.9f))
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Destination image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                ) {
                    Image(
                        painter = painterResource(id = destination.imageResId),
                        contentDescription = destination.city,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Price tag with animation
                    val priceTagTransition = rememberInfiniteTransition(label = "price_tag")
                    val priceTagScale by priceTagTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1500, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "price_tag_scale"
                    )

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .scale(priceTagScale)
                            .shadow(4.dp, RoundedCornerShape(8.dp))
                            .background(
                                Color(0xFF4CAF50),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "$${destination.price.toInt()}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                // Destination info
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = destination.city,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = destination.country,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rating with stars animation
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Animated rating stars
                        val starCount = 5
                        val filledStars = (destination.rating / 5f * starCount).roundToInt()

                        repeat(starCount) { index ->
                            val isFilled = index < filledStars

                            val starTransition = rememberInfiniteTransition(label = "star_${index}")
                            val starScale by if (isFilled) {
                                starTransition.animateFloat(
                                    initialValue = 1f,
                                    targetValue = 1.2f,
                                    animationSpec = infiniteRepeatable(
                                        animation = tween(
                                            durationMillis = 1000 + (index * 200),
                                            easing = FastOutSlowInEasing
                                        ),
                                        repeatMode = RepeatMode.Reverse
                                    ),
                                    label = "star_scale_${index}"
                                )
                            } else {
                                remember { mutableStateOf(1f) }
                            }

                            Icon(
                                painter = painterResource(
                                    id = if (isFilled) R.drawable.ic_star_filled else R.drawable.ic_star_outline
                                ),
                                contentDescription = null,
                                tint = if (isFilled) Color(0xFFFFC107) else Color.Gray.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .size(14.dp)
                                    .scale(starScale)
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = destination.rating.toString(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EnhancedOfferCard(
    offer: OfferItem,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "offer_animation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offer_scale"
    )

    // Discount badge animation
    val badgeTransition = rememberInfiniteTransition(label = "badge_animation")
    val badgeRotation by badgeTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "badge_rotation"
    )

    Box(
        modifier = Modifier
            .width(280.dp)
            .height(140.dp)
    ) {
        // Card with shadow and scale animation
        Card(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale)
                .shadow(8.dp, RoundedCornerShape(16.dp))
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A3546).copy(alpha = 0.9f)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Left side - offer details
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Title and description
                        Column {
                            Text(
                                text = offer.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = offer.description,
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.7f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Call to action button with glow effect
                        val buttonGlowTransition = rememberInfiniteTransition(label = "button_glow")
                        val buttonGlowAlpha by buttonGlowTransition.animateFloat(
                            initialValue = 0.5f,
                            targetValue = 0.9f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1500, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "button_glow_alpha"
                        )

                        Box {
                            // Glow effect
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .alpha(buttonGlowAlpha)
                                    .shadow(8.dp, RoundedCornerShape(8.dp))
                                    .background(
                                        Color(0xFF4CAF50).copy(alpha = 0.3f),
                                        RoundedCornerShape(8.dp)
                                    )
                            )

                            Button(
                                onClick = onClick,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50)
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Text("View Deal")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Right side - offer image with discount badge
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = painterResource(id = offer.imageResId),
                            contentDescription = offer.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Discount badge
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 8.dp, y = (-4).dp)
                                .rotate(badgeRotation)
                                .background(Color(0xFFFF5722), CircleShape)
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${offer.discount}%",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BookingTypeSelector(
    selectedType: BookingType,
    onTypeSelected: (BookingType) -> Unit
) {
    // Scroll state for lazy row
    val rowScrollState = rememberScrollState()

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(BookingType.entries.toTypedArray()) { type ->
            BookingTypeItem(
                type = type,
                isSelected = type == selectedType,
                onSelect = { onTypeSelected(type) }
            )
        }
    }
}

@Composable
fun BookingTypeItem(
    type: BookingType,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    // Animation properties
    val backgroundScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.9f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "background_scale"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.7f,
        animationSpec = tween(300),
        label = "content_alpha"
    )

    // Icon and text based on type
    val (icon, text) = when (type) {
        BookingType.FLIGHTS -> Pair(R.drawable.plane_path, "Flights")
        BookingType.HOTELS -> Pair(R.drawable.hotel, "Hotels")
        BookingType.CARS -> Pair(R.drawable.car_ic, "Cars")
        BookingType.TRAINS -> Pair(R.drawable.train_ic, "Trains")
    }

    // Background glow for selected item
    val infiniteTransition = rememberInfiniteTransition(label = "glow_animation")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = Modifier
            .width(80.dp)
            .clickable { onSelect() },
        contentAlignment = Alignment.Center
    ) {
        // Glow effect for selected item
        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .scale(backgroundScale * 1.5f)
                    .alpha(glowAlpha)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF4CAF50).copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(backgroundScale)
                .alpha(contentAlpha)
        ) {
            // Icon with container
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .shadow(
                        elevation = if (isSelected) 8.dp else 4.dp,
                        shape = CircleShape
                    )
                    .background(
                        color = if (isSelected) Color(0xFF4CAF50) else Color(0xFF1A3546),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = text,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController())
}