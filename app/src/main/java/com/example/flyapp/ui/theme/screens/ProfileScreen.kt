package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.FlightTopAppBar
import com.example.flyapp.ui.theme.navigition.Screen
import kotlinx.coroutines.delay

// User Profile data class
data class UserProfile(
    val id: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val membershipLevel: MembershipLevel,
    val loyaltyPoints: Int,
    val isVerified: Boolean,
    val joinDate: String
)

// Membership Level enum
enum class MembershipLevel(val displayName: String, val color: Color) {
    BRONZE("Bronze", Color(0xFFCD7F32)),
    SILVER("Silver", Color(0xFFC0C0C0)),
    GOLD("Gold", Color(0xFFFFD700)),
    PLATINUM("Platinum", Color(0xFFE5E4E2))
}

@Composable
fun ProfileScreen(
    navController: NavController
) {
    // Sample user profile data
    val userProfile = remember {
        UserProfile(
            id = "USR12345",
            name = "Ahmed Ibrahim",
            email = "ahmed.alharth01@gmail.com",
            phoneNumber = "+201065751305",
            membershipLevel = MembershipLevel.GOLD,
            loyaltyPoints = 4750,
            isVerified = true,
            joinDate = "Sep 2024"
        )
    }

    // Toggle states
    var notificationsEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }

    // Animation state
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
                        DeepBlue,
                        MediumBlue,
                        DarkNavyBlue
                    )
                )
            )
    ) {
        // Security pattern background
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
            FlightTopAppBar(
                textOne = "My",
                textTwo = "Profile",
                navController = navController,
            )

            // Content with animation
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(initialAlpha = 0.3f) + slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = androidx.compose.animation.core.tween(durationMillis = 800)
                )
            ) {
                // Scrollable content
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    // Profile header
                    item {
                        ProfileHeader(userProfile)
                    }

                    // Membership card
                    item {
                        MembershipCard(userProfile)
                    }

                    // Section: Account
                    item {
                        SectionHeader(title = "Account")

                        ProfileMenuItem(
                            icon = painterResource(R.drawable.check_circle),
                            title = "Personal Information",
                            onClick = {
                                // Navigate to edit profile screen
                        navController.navigate(Screen.ProfileEditorScreen.route)
                            }
                        )

                        ProfileMenuItem(
                            icon = painterResource(R.drawable.creditcard),
                            title = "Payment Methods",
                            onClick = {
                                // Navigate to payment methods screen
                                navController.navigate(Screen.PaymentScreen.route)
                            }
                        )

                    }

                    // Section: Settings
                    item {
                        ProfileMenuItem(
                            icon = painterResource(R.drawable.settings),
                            title = "Settings",
                            onClick = {
                                // Navigate to app settings screen
                                navController.navigate(Screen.SettingsScreen.route)
                            }
                        )
                    }

                    // Section: Support
                    item {
                        SectionHeader(title = "Support")

                        ProfileMenuItem(
                            icon = painterResource(R.drawable.help_ic),
                            title = "Help Center",
                            onClick = {
                                // Navigate to help center screen
                                navController.navigate(Screen.HelpCenterScreen.route)
                            }
                        )

                        ProfileMenuItem(
                            icon = painterResource(R.drawable.logout),
                            title = "Sign Out",
                            textColor = Color(0xFFFF6B6B),
                            onClick = {
                                // Sign out logic
                                navController.navigate(Screen.LoginScreen.route) {
                                    popUpTo(Screen.MainScreen.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // Bottom spacer
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}
@Composable
fun ProfileHeader(
    profile: UserProfile
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile image
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .border(2.dp, GoldColor, CircleShape)
                .background(MediumBlue),
            contentAlignment = Alignment.Center
        ) {
            // Using placeholders here - would typically use Coil or Glide for real images
            Text(
                text = profile.name.first().toString(),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = GoldColor
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        // User name
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = profile.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit Profile",
                tint = GoldColor,
                modifier = Modifier
                    .size(32.dp)
                    .padding(4.dp)
                    .clip(CircleShape)
            )
        }


        // Email
        Text(
            text = profile.email,
            fontSize = 16.sp,
            color = Color.White.copy(alpha = 0.8f)
        )

        // Phone number
        Text(
            text = profile.phoneNumber,
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Verification status
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (profile.isVerified) "Verified Account" else "Unverified",
                fontSize = 14.sp,
                color = if (profile.isVerified) Color(0xFF4CAF50) else Color(0xFFFF9800)
            )

            Spacer(modifier = Modifier.width(4.dp))

            if (profile.isVerified) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Verified",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Member since
        Text(
            text = "Member since ${profile.joinDate}",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun MembershipCard(profile: UserProfile) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .shadow(8.dp, RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        profile.membershipLevel.color.copy(alpha = 0.8f),
                        profile.membershipLevel.color.copy(alpha = 0.4f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Membership level
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${profile.membershipLevel.displayName} Member",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = profile.membershipLevel.color
                )

                Icon(
                    painterResource(R.drawable.check_circle),
                    contentDescription = null,
                    tint = profile.membershipLevel.color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Loyalty points
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Loyalty Points:",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${profile.loyaltyPoints}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Next level progress
            if (profile.membershipLevel != MembershipLevel.PLATINUM) {
                val nextLevel = when (profile.membershipLevel) {
                    MembershipLevel.BRONZE -> MembershipLevel.SILVER
                    MembershipLevel.SILVER -> MembershipLevel.GOLD
                    MembershipLevel.GOLD -> MembershipLevel.PLATINUM
                    MembershipLevel.PLATINUM -> MembershipLevel.PLATINUM // Shouldn't reach here
                }

                val pointsNeeded = when (profile.membershipLevel) {
                    MembershipLevel.BRONZE -> 5000 - profile.loyaltyPoints
                    MembershipLevel.SILVER -> 10000 - profile.loyaltyPoints
                    MembershipLevel.GOLD -> 20000 - profile.loyaltyPoints
                    MembershipLevel.PLATINUM -> 0 // Shouldn't reach here
                }

                Text(
                    text = "Earn $pointsNeeded more points to reach ${nextLevel.displayName}",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Progress bar
                val progress = when (profile.membershipLevel) {
                    MembershipLevel.BRONZE -> profile.loyaltyPoints / 5000f
                    MembershipLevel.SILVER -> profile.loyaltyPoints / 10000f
                    MembershipLevel.GOLD -> profile.loyaltyPoints / 20000f
                    MembershipLevel.PLATINUM -> 1f
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.1f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(nextLevel.color.copy(alpha = 0.8f))
                    )
                }
            } else {
                // For platinum members
                Text(
                    text = "You've reached our highest membership tier!",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enjoy your exclusive platinum benefits",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MembershipLevel.PLATINUM.color
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = title.uppercase(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = GoldColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider(
             thickness = 1.dp,
            color = GoldColor.copy(alpha = 0.3f)
        )
    }
}

@Composable
fun ProfileMenuItem(
    icon: Painter,
    title: String,
    textColor: Color = Color.White,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Icon(
            painter = icon,
            contentDescription = null,
            tint = GoldColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Title
        Text(
            text = title,
            fontSize = 16.sp,
            color = textColor
        )

        Spacer(modifier = Modifier.weight(1f))

        // Arrow indicator
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = GoldColor.copy(alpha = 0.6f),
            modifier = Modifier.size(20.dp)
        )
    }

    HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.1f))
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        ProfileScreen(
            navController = rememberNavController()
        )
    }
}