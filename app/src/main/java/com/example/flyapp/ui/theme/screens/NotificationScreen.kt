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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.ui.theme.components.ParticleEffectBackground
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.flyapp.R

// Data class for notification items
data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: Date,
    var isRead: Boolean = false
)

// Enum for notification types
enum class NotificationType {
    BOOKING_CONFIRMATION, FLIGHT_UPDATE, SPECIAL_OFFER, GATE_CHANGE, SYSTEM_ALERT
}

// Enum for notification filters
enum class NotificationFilter {
    ALL, UNREAD, BOOKINGS, UPDATES, OFFERS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(navController: NavHostController) {
    // State for notifications
    val notifications = remember {
        mutableStateListOf(
            NotificationItem(
                id = "notif1",
                title = "Flight Booking Confirmed",
                message = "Your flight to New York on 25 May has been confirmed. Booking reference: YTR789.",
                type = NotificationType.BOOKING_CONFIRMATION,
                timestamp = Date(System.currentTimeMillis() - (3 * 60 * 60 * 1000)), // 3 hours ago
                isRead = false
            ),
            NotificationItem(
                id = "notif2",
                title = "Gate Change Alert",
                message = "Your flight AI302 gate has changed from G12 to G18. Please proceed to the new gate.",
                type = NotificationType.GATE_CHANGE,
                timestamp = Date(System.currentTimeMillis() - (8 * 60 * 60 * 1000)), // 8 hours ago
                isRead = false
            ),
            NotificationItem(
                id = "notif3",
                title = "Special Summer Offer",
                message = "Enjoy 20% off on all international flights booked this weekend. Use code SUMMER20.",
                type = NotificationType.SPECIAL_OFFER,
                timestamp = Date(System.currentTimeMillis() - (1 * 24 * 60 * 60 * 1000)), // 1 day ago
                isRead = true
            ),
            NotificationItem(
                id = "notif4",
                title = "Flight Schedule Update",
                message = "Your flight to Paris on 30 May has been rescheduled to depart at 14:30 instead of 13:45.",
                type = NotificationType.FLIGHT_UPDATE,
                timestamp = Date(System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000)), // 2 days ago
                isRead = true
            ),
            NotificationItem(
                id = "notif5",
                title = "Important: Check-in Available",
                message = "Online check-in for your flight to Tokyo is now available. Check in now to select your preferred seat.",
                type = NotificationType.SYSTEM_ALERT,
                timestamp = Date(System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000)), // 3 days ago
                isRead = false
            ),
            NotificationItem(
                id = "notif6",
                title = "Weekend Getaway Deals",
                message = "Discover amazing weekend getaway deals starting from just $99. Limited time offer!",
                type = NotificationType.SPECIAL_OFFER,
                timestamp = Date(System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000)), // 5 days ago
                isRead = true
            ),
            NotificationItem(
                id = "notif7",
                title = "Flight Delay Notification",
                message = "Your flight SQ421 has been delayed by 45 minutes. New departure time: 18:15.",
                type = NotificationType.FLIGHT_UPDATE,
                timestamp = Date(System.currentTimeMillis() - (6 * 24 * 60 * 60 * 1000)), // 6 days ago
                isRead = true
            )
        )
    }

    // Animation states for staggered UI appearance
    var showHeaderSection by remember { mutableStateOf(false) }
    var showFiltersSection by remember { mutableStateOf(false) }
    var showNotificationsList by remember { mutableStateOf(false) }

    // Alpha animations for staggered appearance
    val headerAlpha by animateFloatAsState(
        targetValue = if (showHeaderSection) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "header_alpha"
    )

    val filtersAlpha by animateFloatAsState(
        targetValue = if (showFiltersSection) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "filters_alpha"
    )

    val listAlpha by animateFloatAsState(
        targetValue = if (showNotificationsList) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "list_alpha"
    )

    // Current selected filter
    var selectedFilter by remember { mutableStateOf(NotificationFilter.ALL) }

    // Filtered notifications
    val filteredNotifications = remember(notifications, selectedFilter) {
        when (selectedFilter) {
            NotificationFilter.ALL -> notifications
            NotificationFilter.UNREAD -> notifications.filter { !it.isRead }
            NotificationFilter.BOOKINGS -> notifications.filter {
                it.type == NotificationType.BOOKING_CONFIRMATION
            }
            NotificationFilter.UPDATES -> notifications.filter {
                it.type == NotificationType.FLIGHT_UPDATE || it.type == NotificationType.GATE_CHANGE
            }
            NotificationFilter.OFFERS -> notifications.filter {
                it.type == NotificationType.SPECIAL_OFFER
            }
        }
    }

    // Unread count
    val unreadCount = notifications.count { !it.isRead }

    // Coroutine scope for animation delays
    val coroutineScope = rememberCoroutineScope()

    // Function to mark notification as read
    fun markAsRead(notification: NotificationItem) {
        val index = notifications.indexOf(notification)
        if (index != -1) {
            notifications[index] = notification.copy(isRead = true)
        }
    }

    // Function to delete notification
    fun deleteNotification(notification: NotificationItem) {
        notifications.remove(notification)
    }

    // Function to mark all as read
    fun markAllAsRead() {
        val updatedList = notifications.map { it.copy(isRead = true) }
        notifications.clear()
        notifications.addAll(updatedList)
    }

    // Trigger animations sequentially
    LaunchedEffect(key1 = true) {
        showHeaderSection = true
        delay(300)
        showFiltersSection = true
        delay(200)
        showNotificationsList = true
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
                                text = "Notifications",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "$unreadCount unread messages",
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
                        // Mark all as read button with animation
                        val markAllBtnTransition = rememberInfiniteTransition(label = "mark_all_btn")
                        val markAllBtnAlpha by markAllBtnTransition.animateFloat(
                            initialValue = 0.7f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1500, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "mark_all_btn_alpha"
                        )

                        IconButton(
                            onClick = { markAllAsRead() },
                            modifier = Modifier
                                .alpha(if (unreadCount > 0) markAllBtnAlpha else 0.5f)
                                .padding(end = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.email_read),
                                contentDescription = "Mark all as read",
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
            ) {
                // Notification filters
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .alpha(filtersAlpha)
                ) {
                    NotificationFilters(
                        selectedFilter = selectedFilter,
                        onFilterSelected = { selectedFilter = it },
                        unreadCount = unreadCount
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Notification list with animation
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .alpha(listAlpha)
                ) {
                    if (filteredNotifications.isEmpty()) {
                        EmptyNotificationsMessage(selectedFilter)
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp
                            )
                        ) {
                            items(filteredNotifications) { notification ->
                                // State for animation visibility
                                var isVisible by remember { mutableStateOf(false) }
                                val index = filteredNotifications.indexOf(notification)

                                // Delayed animation for staggered appearance
                                LaunchedEffect(showNotificationsList) {
                                    if (showNotificationsList) {
                                        delay(100L * index)
                                        isVisible = true
                                    }
                                }

                                // Fixed AnimatedVisibility implementation
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp)
                                ) {
                                    filteredNotifications.forEachIndexed { index, notification ->
                                        AnimatedVisibility(
                                            visible = isVisible,
                                            enter = fadeIn(tween(500))
                                        ) {
                                            NotificationCard(
                                                notification = notification,
                                                onNotificationClick = {
                                                    markAsRead(notification)
                                                    when (notification.type) {
                                                        NotificationType.BOOKING_CONFIRMATION -> {
                                                            // Navigate to booking details
                                                        }
                                                        NotificationType.SPECIAL_OFFER -> {
                                                            // Navigate to offers screen
                                                        }
                                                        else -> {
                                                            // Handle other types
                                                        }
                                                    }
                                                },
                                                onDeleteClick = { deleteNotification(notification) }
                                            )

                                            if (index < filteredNotifications.lastIndex) {
                                                Spacer(modifier = Modifier.height(8.dp))
                                            }
                                        }
                                    }
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
fun NotificationFilters(
    selectedFilter: NotificationFilter,
    onFilterSelected: (NotificationFilter) -> Unit,
    unreadCount: Int
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp)
    ) {
        items(NotificationFilter.entries.toTypedArray()) { filter ->
            FilterChip(
                filter = filter,
                isSelected = filter == selectedFilter,
                unreadCount = if (filter == NotificationFilter.UNREAD) unreadCount else null,
                onSelect = { onFilterSelected(filter) }
            )
        }
    }
}

@Composable
fun FilterChip(
    filter: NotificationFilter,
    isSelected: Boolean,
    unreadCount: Int? = null,
    onSelect: () -> Unit
) {
    // Animation properties
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.95f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "scale_anim"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "chip_animation")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.7f,
        animationSpec = tween(300),
        label = "content_alpha"
    )

    val filterText = when (filter) {
        NotificationFilter.ALL -> "All"
        NotificationFilter.UNREAD -> "Unread"
        NotificationFilter.BOOKINGS -> "Bookings"
        NotificationFilter.UPDATES -> "Updates"
        NotificationFilter.OFFERS -> "Offers"
    }

    Box(
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        // Glow effect for selected chip
        if (isSelected) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .scale(scale * 1.2f)
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

        FilledTonalButton(
            onClick = onSelect,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.scale(scale),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = if (isSelected) Color(0xFF4CAF50) else Color(0xFF1A3546).copy(alpha = 0.8f),
                contentColor = Color.White
            )
        ) {
            Text(
                text = if (unreadCount != null && unreadCount > 0) "$filterText ($unreadCount)" else filterText,
                color = Color.White,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.alpha(contentAlpha)
            )
        }
    }
}

@Composable
fun NotificationCard(
    notification: NotificationItem,
    onNotificationClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    // Animation properties
    val infiniteTransition = rememberInfiniteTransition(label = "card_animation")
    val unreadPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (notification.isRead) 1f else 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "unread_pulse"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = if (notification.isRead) 0f else 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Glow effect for unread notifications
        Box(
            modifier = Modifier
                .matchParentSize()
                .scale(unreadPulse * 1.05f)
                .alpha(glowAlpha)
                .shadow(8.dp, RoundedCornerShape(16.dp))
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
                .fillMaxWidth()
                .scale(unreadPulse)
                .shadow(if (notification.isRead) 4.dp else 8.dp, RoundedCornerShape(16.dp))
                .clickable { onNotificationClick() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A3546).copy(alpha = 0.9f)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Notification type icon with animation
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(end = 16.dp, top = 4.dp)
                ) {
                    val (icon, iconColor) = getNotificationTypeIcon(notification.type)

                    val iconTransition = rememberInfiniteTransition(label = "icon_animation")
                    val iconScale by iconTransition.animateFloat(
                        initialValue = 1f,
                        targetValue = if (notification.isRead) 1f else 1.1f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1500, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "icon_scale"
                    )

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .scale(iconScale)
                            .shadow(4.dp, CircleShape)
                            .background(
                                color = Color(0xFF1A3546),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = icon,
                            contentDescription = null,
                            tint = iconColor,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }

                    // Unread indicator
                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 2.dp, y = (-2).dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        )
                    }
                }

                // Notification content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = notification.title,
                        fontSize = 16.sp,
                        fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = notification.message,
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val dateFormat = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
                    Text(
                        text = dateFormat.format(notification.timestamp),
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.5f)
                    )
                }

                // Delete action
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .size(36.dp)
                        .alpha(0.7f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete notification",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyNotificationsMessage(filter: NotificationFilter) {
    val message = when (filter) {
        NotificationFilter.ALL -> "No notifications yet"
        NotificationFilter.UNREAD -> "No unread notifications"
        NotificationFilter.BOOKINGS -> "No booking notifications"
        NotificationFilter.UPDATES -> "No flight updates"
        NotificationFilter.OFFERS -> "No special offers"
    }

    val subMessage = when (filter) {
        NotificationFilter.ALL -> "We'll notify you when there's something important"
        NotificationFilter.UNREAD -> "All notifications have been read"
        NotificationFilter.BOOKINGS -> "Book a flight to receive booking confirmations"
        NotificationFilter.UPDATES -> "Your flights are currently on schedule"
        NotificationFilter.OFFERS -> "Check back soon for special deals"
    }

    val emptyIcon = Icons.Outlined.Notifications

    // Animation for empty state
    val infiniteTransition = rememberInfiniteTransition(label = "empty_animation")
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "icon_scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon with glow effect
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Glow effect
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(iconScale * 1.2f)
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

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .scale(iconScale)
                    .shadow(8.dp, CircleShape)
                    .background(
                        color = Color(0xFF1A3546),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = emptyIcon,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = message,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = subMessage,
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun getNotificationTypeIcon(type: NotificationType): Pair<Painter, Color> {
    return when (type) {
        NotificationType.BOOKING_CONFIRMATION -> Pair(
            painterResource(R.drawable.plane_ticket),
            Color(0xFF4CAF50) // Green
        )
        NotificationType.FLIGHT_UPDATE -> Pair(
            painterResource(R.drawable.plane_ticket),
            Color(0xFF2196F3) // Blue
        )
        NotificationType.SPECIAL_OFFER -> Pair(
            painterResource(R.drawable.summer_offer),
            Color(0xFFFF9800) // Orange
        )
        NotificationType.GATE_CHANGE -> Pair(
            painterResource(R.drawable.check_circle),
            Color(0xFF9C27B0) // Purple
        )
        NotificationType.SYSTEM_ALERT -> Pair(
            painterResource(R.drawable.error),
            Color(0xFFF44336) // Red
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    NotificationScreen(rememberNavController())
}