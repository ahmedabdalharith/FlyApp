package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import kotlinx.coroutines.launch

data class ProfileMenuItem(
    val icon: ImageVector,
    val title: String,
    val description: String? = null,
    val badge: String? = null,
    val route: String
)

data class TripHistoryItem(
    val id: String,
    val destination: String,
    val date: String,
    val flightNumber: String,
    val status: TripStatus,
    val imageResId: Int
)

enum class TripStatus {
    COMPLETED, UPCOMING, CANCELED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController) {
    // User data
    val userName = "Alex Johnson"
    val userEmail = "alex.johnson@example.com"
    val membershipLevel = "Gold Member"
    val loyaltyPoints = 12750

    // Profile menu items
    val menuItems = listOf(
        ProfileMenuItem(
            icon = Icons.Outlined.Info,
            title = "My Bookings",
            description = "View and manage your bookings",
            badge = "3",
            route = "myBookings"
        ),
        ProfileMenuItem(
            icon = Icons.Outlined.Favorite,
            title = "Saved Trips",
            description = "Access your saved destinations",
            route = "savedTrips"
        ),
        ProfileMenuItem(
            icon = Icons.Outlined.Info,
            title = "Payment Methods",
            description = "Manage your cards and payment options",
            route = "paymentMethods"
        ),
        ProfileMenuItem(
            icon = Icons.Outlined.Settings,
            title = "Account Settings",
            description = "Update your profile and preferences",
            route = "accountSettings"
        ),
        ProfileMenuItem(
            icon = Icons.Outlined.Info,
            title = "Support",
            description = "Get help with your account",
            route = "support"
        ),
        ProfileMenuItem(
            icon = Icons.Outlined.Info,
            title = "About",
            description = "App information and legal",
            route = "about"
        )
    )

    // Trip history
    val tripHistory = listOf(
        TripHistoryItem(
            id = "trip1",
            destination = "Paris, France",
            date = "Mar 15, 2025",
            flightNumber = "FL4532",
            status = TripStatus.UPCOMING,
            imageResId = R.drawable.paris
        ),
        TripHistoryItem(
            id = "trip2",
            destination = "Tokyo, Japan",
            date = "Feb 8, 2025",
            flightNumber = "FL1298",
            status = TripStatus.COMPLETED,
            imageResId = R.drawable.tokyo
        ),
        TripHistoryItem(
            id = "trip3",
            destination = "New York, USA",
            date = "Dec 20, 2024",
            flightNumber = "FL7801",
            status = TripStatus.COMPLETED,
            imageResId = R.drawable.new_york
        )
    )

    // Animation states for staggered UI appearance
    var showHeader by remember { mutableStateOf(false) }
    var showMembershipCard by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showTripHistory by remember { mutableStateOf(false) }

    // Alpha animations for staggered appearance
    val headerAlpha by animateFloatAsState(
        targetValue = if (showHeader) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "header_alpha"
    )

    val membershipCardAlpha by animateFloatAsState(
        targetValue = if (showMembershipCard) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "membership_card_alpha"
    )

    val menuAlpha by animateFloatAsState(
        targetValue = if (showMenu) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "menu_alpha"
    )

    val tripHistoryAlpha by animateFloatAsState(
        targetValue = if (showTripHistory) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "trip_history_alpha"
    )

    // Trigger animations sequentially
    LaunchedEffect(key1 = true) {
        showHeader = true
        delay(300)
        showMembershipCard = true
        delay(200)
        showMenu = true
        delay(200)
        showTripHistory = true
    }

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
        // Background particle effects
        ParticleEffectBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    modifier = Modifier.alpha(headerAlpha),
                    title = {
                        Text(
                            text = "My Profile",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        // Edit profile button with animation
                        val editButtonTransition = rememberInfiniteTransition(label = "edit_button")
                        val editButtonScale by editButtonTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1500, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "edit_button_scale"
                        )

                        IconButton(
                            onClick = { /* Navigate to edit profile */ },
                            modifier = Modifier
                                .scale(editButtonScale)
                                .padding(end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                // Profile header section
                item {
                    ProfileHeader(
                        userName = userName,
                        userEmail = userEmail,
                        alpha = headerAlpha
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Membership card
                item {
                    MembershipCard(
                        membershipLevel = membershipLevel,
                        points = loyaltyPoints,
                        alpha = membershipCardAlpha
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Section title for menu
                item {
                    SectionTitle(
                        title = "Account",
                        alpha = menuAlpha
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Menu items
                items(menuItems) { menuItem ->
                    MenuItem(
                        item = menuItem,
                        alpha = menuAlpha,
                        onClick = { navController.navigate(menuItem.route) }
                    )
                }

                // Section title for trip history
                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    SectionTitle(
                        title = "Recent Trips",
                        actionText = "View All",
                        alpha = tripHistoryAlpha,
                        onActionClick = { navController.navigate("allTrips") }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Trip history items
                items(tripHistory) { trip ->
                    TripHistoryItem(
                        trip = trip,
                        alpha = tripHistoryAlpha,
                        onClick = { navController.navigate("tripDetails/${trip.id}") }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Log out button
                item {
                    Spacer(modifier = Modifier.height(24.dp))

                    LogoutButton(
                        alpha = tripHistoryAlpha,
                        onClick = { /* Handle logout */ }
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(
    userName: String,
    userEmail: String,
    alpha: Float
) {
    // Animation properties for avatar
    val infiniteTransition = rememberInfiniteTransition(label = "avatar_animation")
    val avatarScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "avatar_scale"
    )

    val glowTransition = rememberInfiniteTransition(label = "avatar_glow")
    val glowAlpha by glowTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar with glow effect
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            // Glow effect
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(avatarScale * 1.2f)
                    .alpha(glowAlpha)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF4CAF50).copy(alpha = 0.5f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            // Avatar container with border
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .scale(avatarScale)
                    .shadow(8.dp, CircleShape)
                    .background(Color(0xFF1A3546), CircleShape)
                    .border(
                        width = 3.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF4CAF50),
                                Color(0xFF1565C0)
                            )
                        ),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Default profile icon
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        // User name with enhanced typography
        Text(
            text = userName,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(4.dp))

        // User email
        Text(
            text = userEmail,
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun MembershipCard(
    membershipLevel: String,
    points: Int,
    alpha: Float
) {
    // Card animation properties
    val infiniteTransition = rememberInfiniteTransition(label = "card_animation")
    val cardScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_scale"
    )

    // Enhanced glow effect
    val glowTransition = rememberInfiniteTransition(label = "card_glow")
    val glowAlpha by glowTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .alpha(alpha)
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
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .scale(cardScale)
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .clickable { /* Show membership details */ },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF1A3546),
                            Color(0xFF003045)
                        )
                    )
                )
        ) {
            // Background decorative elements
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Decorative circles in background
                repeat(3) { index ->
                    val circleTransition = rememberInfiniteTransition(label = "circle_${index}")
                    val circleAlpha by circleTransition.animateFloat(
                        initialValue = 0.1f,
                        targetValue = 0.3f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(
                                durationMillis = 3000 + (index * 500),
                                easing = FastOutSlowInEasing
                            ),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "circle_alpha_${index}"
                    )

                    Box(
                        modifier = Modifier
                            .size(80.dp + (index * 40).dp)
                            .offset(
                                x = (index * 30).dp,
                                y = (index * 10).dp
                            )
                            .alpha(circleAlpha)
                            .background(
                                Color(0xFF4CAF50),
                                CircleShape
                            )
                    )
                }
            }

            // Card content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "FA",
                            color = Color(0xFF1A3546),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    // Level badge with pulsing animation
                    val badgeTransition = rememberInfiniteTransition(label = "badge_pulse")
                    val badgeScale by badgeTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = 1.1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "badge_scale"
                    )

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFFD700).copy(alpha = 0.8f),
                        modifier = Modifier.scale(badgeScale)
                    ) {
                        Text(
                            text = membershipLevel,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = Color(0xFF1A3546),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Points display
                Text(
                    text = "Loyalty Points",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = points.toString(),
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Points animation indicator
                    val pointsTransition = rememberInfiniteTransition(label = "points_animation")
                    val pointsAlpha by pointsTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1000, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "points_alpha"
                    )

                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Points increasing",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .alpha(pointsAlpha)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Benefits text
                Text(
                    text = "Tap to view your exclusive benefits",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun SectionTitle(
    title: String,
    actionText: String? = null,
    alpha: Float,
    onActionClick: (() -> Unit)? = null
) {
    // Animation for the action text if present
    val actionTextTransition = if (actionText != null) {
        rememberInfiniteTransition(label = "action_text")
    } else null

    val textAlpha by actionTextTransition?.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "text_alpha"
    ) ?: remember { mutableStateOf(1f) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(alpha)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title with decorative element
        Row(verticalAlignment = Alignment.CenterVertically) {
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

        // Action text if provided
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                fontSize = 14.sp,
                color = Color(0xFF4CAF50).copy(alpha = textAlpha),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { onActionClick() }
            )
        }
    }
}

@Composable
fun MenuItem(
    item: ProfileMenuItem,
    alpha: Float,
    onClick: () -> Unit
) {
    val cardTransition = rememberInfiniteTransition(label = "menu_item")
    val cardElevation by cardTransition.animateFloat(
        initialValue = 2f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .alpha(alpha)
            .shadow(cardElevation.dp, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A3546).copy(alpha = 0.9f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with subtle animation
            val iconTransition = rememberInfiniteTransition(label = "icon_animation")
            val iconScale by iconTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "icon_scale"
            )

            // Icon container
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .scale(iconScale)
                    .background(
                        Color(0xFF4CAF50).copy(alpha = 0.2f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = Color(0xFF4CAF50)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Title and description
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                item.description?.let {
                    Text(
                        text = it,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            // Badge if present
            item.badge?.let { badgeText ->
                val badgeTransition = rememberInfiniteTransition(label = "badge_animation")
                val badgeScale by badgeTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "badge_scale"
                )

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .scale(badgeScale)
                        .background(Color(0xFF4CAF50), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badgeText,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Arrow icon
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Navigate",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun TripHistoryItem(
    trip: TripHistoryItem,
    alpha: Float,
    onClick: () -> Unit
) {
    val cardTransition = rememberInfiniteTransition(label = "trip_card")
    val cardElevation by cardTransition.animateFloat(
        initialValue = 2f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_elevation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 16.dp)
            .alpha(alpha)
            .shadow(cardElevation.dp, RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A3546).copy(alpha = 0.9f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Destination image
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(id = trip.imageResId),
                    contentDescription = trip.destination,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Status indicator
                val statusColor = when(trip.status) {
                    TripStatus.COMPLETED -> Color(0xFF4CAF50)
                    TripStatus.UPCOMING -> Color(0xFF2196F3)
                    TripStatus.CANCELED -> Color(0xFFE53935)
                }

                // Animated status indicator
                val statusTransition = rememberInfiniteTransition(label = "status_animation")
                val statusAlpha by statusTransition.animateFloat(
                    initialValue = 0.7f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1000, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "status_alpha"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .align(Alignment.BottomCenter)
                        .alpha(statusAlpha)
                        .background(statusColor.copy(alpha = 0.7f))
                ) {
                    Text(
                        text = trip.status.name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                }
            }

            // Trip details
// Trip details
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Destination and date
                Column {
                    Text(
                        text = trip.destination,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = trip.date,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }

                // Flight info with plane icon
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Flight",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = trip.flightNumber,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // Right arrow
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "View Details",
                    tint = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun LogoutButton(
    alpha: Float,
    onClick: () -> Unit
) {
    // Button animation
    val buttonTransition = rememberInfiniteTransition(label = "logout_button")
    val buttonScale by buttonTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_scale"
    )

    val scope = rememberCoroutineScope()
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Logout button
    Button(
        onClick = { showConfirmDialog = true },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .alpha(alpha)
            .scale(buttonScale),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1A3546)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Logout",
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Log Out",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }

    // Confirmation dialog
    AnimatedVisibility(
        visible = showConfirmDialog,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Log Out") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            showConfirmDialog = false
                            delay(300)
                            onClick()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("Cancel")
                }
            },
            containerColor = Color(0xFF1A3546),
            titleContentColor = Color.White,
            textContentColor = Color.White.copy(alpha = 0.9f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    ProfileScreen(navController = navController)
}