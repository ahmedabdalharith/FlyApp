package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.theme.DarkNavyBlue
import com.example.flyapp.ui.theme.theme.DeepBlue
import com.example.flyapp.ui.theme.theme.GoldColor
import com.example.flyapp.ui.theme.theme.MediumBlue
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val type: NotificationType,
    val isRead: Boolean = false,
    val relatedOfferId: String? = null,
    val actionable: Boolean = false
)

// Define notification types
enum class NotificationType {
    PROMO,
    PRICE_ALERT,
    TRAVEL_UPDATE,
    SYSTEM,
    BOOKING_CONFIRMATION
}

// Extension function to get color based on notification type
fun NotificationType.color(): Color {
    return when (this) {
        NotificationType.PROMO -> Color(0xFFF9A825)  // Gold/Yellow
        NotificationType.PRICE_ALERT -> Color(0xFF4CAF50)  // Green
        NotificationType.TRAVEL_UPDATE -> Color(0xFF2196F3)  // Blue
        NotificationType.SYSTEM -> Color(0xFF9E9E9E)  // Gray
        NotificationType.BOOKING_CONFIRMATION -> Color(0xFF8E24AA)  // Purple
    }
}

// Extension function to get display name
fun NotificationType.displayName(): String {
    return when (this) {
        NotificationType.PROMO -> "Promotion"
        NotificationType.PRICE_ALERT -> "Price Alert"
        NotificationType.TRAVEL_UPDATE -> "Travel Update"
        NotificationType.SYSTEM -> "System"
        NotificationType.BOOKING_CONFIRMATION -> "Booking Confirmation"
    }
}

@Composable
fun NotificationScreen(
    navController: NavController
) {
    // Sample notifications data
    val notifications = remember {
        mutableStateListOf(
            NotificationItem(
                id = "1",
                title = "Flash Sale: 48-Hour Offer",
                message = "Don't miss our NYC to London flash sale with 38% discount. Valid until May 2.",
                timestamp = System.currentTimeMillis() - (1000 * 60 * 30), // 30 minutes ago
                type = NotificationType.PROMO,
                relatedOfferId = "FLASH001",
                actionable = true
            ),
            NotificationItem(
                id = "2",
                title = "Price Drop Alert",
                message = "Prices for your saved route Chicago to Tokyo have dropped by 15%. Book now for the best deal!",
                timestamp = System.currentTimeMillis() - (1000 * 60 * 60 * 3), // 3 hours ago
                type = NotificationType.PRICE_ALERT,
                relatedOfferId = "FREQ001",
                actionable = true
            ),
            NotificationItem(
                id = "3",
                title = "Booking Confirmed",
                message = "Your booking #BK78945 from Boston to Paris has been confirmed. Check your email for details.",
                timestamp = System.currentTimeMillis() - (1000 * 60 * 60 * 12), // 12 hours ago
                type = NotificationType.BOOKING_CONFIRMATION,
                isRead = true
            ),
            NotificationItem(
                id = "4",
                title = "New Bundle Offers Available",
                message = "Check out our new flight + hotel bundles for your next vacation. Save up to 22% on selected destinations.",
                timestamp = System.currentTimeMillis() - (1000 * 60 * 60 * 24), // 1 day ago
                type = NotificationType.PROMO,
                relatedOfferId = "BUND001",
                actionable = true
            ),
            NotificationItem(
                id = "5",
                title = "Gate Change Notice",
                message = "Your upcoming flight LA to Miami has a gate change. New gate: B23. Please check airport displays for confirmation.",
                timestamp = System.currentTimeMillis() - (1000 * 60 * 60 * 48), // 2 days ago
                type = NotificationType.TRAVEL_UPDATE,
                isRead = true
            ),
            NotificationItem(
                id = "6",
                title = "App Update Available",
                message = "A new version of FlyApp is available with improved booking experience and bug fixes.",
                timestamp = System.currentTimeMillis() - (1000 * 60 * 60 * 72), // 3 days ago
                type = NotificationType.SYSTEM,
                isRead = true
            )
        )
    }

    // State for showing content animation
    var showContent by remember { mutableStateOf(false) }
    var showFilterDialog by remember { mutableStateOf(false) }

    // Animation for the background effect
    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val hologramGlow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hologram_glow"
    )

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
                        DeepBlue,
                        MediumBlue,
                        DarkNavyBlue
                    )
                )
            )
    ) {
        // Security pattern background (same as in OfferDetailsScreen)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Draw security pattern lines
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
            for (i in 1..5) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.03f),
                    radius = canvasHeight / 3f * i / 5f,
                    center = Offset(canvasWidth / 2f, canvasHeight / 2f),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 1f,
                        pathEffect = pathEffect
                    )
                )
            }
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            // Top app bar
            NotificationTopAppBar(
                navController = navController,
                onFilterClick = { showFilterDialog = true }
            )

            // Notifications content with animation
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(800)) + slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = tween(durationMillis = 800)
                )
            ) {
                if (notifications.isEmpty()) {
                    // Empty state
                    EmptyNotificationsView()
                } else {
                    // List of notifications
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(notifications) { notification ->
                            NotificationCard(
                                notification = notification,
                                onNotificationClick = { notificationId ->
                                    // Mark as read if not already read
                                    val index = notifications.indexOfFirst { it.id == notificationId }
                                    if (index != -1 && !notifications[index].isRead) {
                                        notifications[index] = notifications[index].copy(isRead = true)
                                    }

                                    // Navigate to related offer if applicable
                                    notification.relatedOfferId?.let { offerId ->
                                        navController.navigate(Screen.OfferDetailsScreen.route)
                                    }
                                },
                                onDeleteClick = { notificationId ->
                                    // Remove notification
                                    notifications.removeIf { it.id == notificationId }
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }

        // Clear all FAB
        if (notifications.isNotEmpty()) {
            FloatingActionButton(
                onClick = { notifications.clear() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = GoldColor,
                contentColor = DarkNavyBlue
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear All Notifications"
                )
            }
        }

        // Filter Dialog would be implemented here if shown
        if (showFilterDialog) {
            // Would implement filter dialog here
            // For now, just close it after a delay
            LaunchedEffect(key1 = showFilterDialog) {
                delay(100)
                showFilterDialog = false
            }
        }
    }
}

@Composable
fun NotificationTopAppBar(
    navController: NavController,
    onFilterClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button with gold accent (same style as OfferDetailsScreen)
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, GoldColor.copy(alpha = 0.8f), CircleShape)
                .background(DarkNavyBlue.copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = GoldColor
            )
        }

        // Screen title
        Text(
            text = "NOTIFICATIONS",
            color = GoldColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        // Filter button
        IconButton(
            onClick = onFilterClick,
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, GoldColor.copy(alpha = 0.8f), CircleShape)
                .background(DarkNavyBlue.copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(
                painterResource(R.drawable.filter_ic),
                contentDescription = "Filter",
                tint = GoldColor
            )
        }
    }
}

@Composable
fun NotificationCard(
    notification: NotificationItem,
    onNotificationClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    val formatter = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault())
    val timeString = formatter.format(Date(notification.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = if (notification.isRead) 0.3f else 0.7f),
                        GoldColor.copy(alpha = if (notification.isRead) 0.1f else 0.3f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onNotificationClick(notification.id) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead)
                DarkNavyBlue.copy(alpha = 0.7f)
            else
                DarkNavyBlue.copy(alpha = 0.9f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Notification Type Indicator
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(notification.type.color(), CircleShape)
                    .align(Alignment.Top)
                    .padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Notification Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Title row with type badge
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        color = if (notification.isRead) Color.White.copy(alpha = 0.8f) else GoldColor,
                        fontSize = 16.sp,
                        fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    // Type indicator pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(notification.type.color().copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = notification.type.displayName(),
                            color = notification.type.color(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Message
                Text(
                    text = notification.message,
                    color = Color.White.copy(alpha = if (notification.isRead) 0.6f else 0.8f),
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Time and action indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timeString,
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 12.sp
                    )

                    if (notification.actionable) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null,
                                tint = GoldColor.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp).rotate(90f)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "View Details",
                                color = GoldColor.copy(alpha = 0.7f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Related offer indicator if applicable
                notification.relatedOfferId?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(color = GoldColor.copy(alpha = 0.3f), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plane_ticket),
                            contentDescription = null,
                            tint = GoldColor.copy(alpha = 0.7f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Related offer: $it",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Delete button
            IconButton(
                onClick = { onDeleteClick(notification.id) },
                modifier = Modifier
                    .size(32.dp)
                    .border(1.dp, GoldColor.copy(alpha = 0.4f), CircleShape)
                    .background(Color.Transparent)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete",
                    tint = GoldColor.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyNotificationsView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.notification_),
            contentDescription = null,
            tint = GoldColor.copy(alpha = 0.7f),
            modifier = Modifier.size(80.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Notifications",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You don't have any notifications at the moment. Check back later for updates on your flights, special offers, and more.",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NotificationScreen(
            navController = rememberNavController()
        )
    }
}