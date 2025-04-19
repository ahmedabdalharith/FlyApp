package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.EnhancedBackgroundAnimations

data class SettingsItem(
    val id: String,
    val title: String,
    val description: String? = null,
    val icon: Painter,
    val hasToggle: Boolean = false,
    val hasBadge: Boolean = false,
    val badgeText: String = "",
    val isDivider: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavHostController) {
    // User info
    val userName = "Alex"

    // Settings state
    var notificationsEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }
    var locationEnabled by remember { mutableStateOf(true) }
    var biometricEnabled by remember { mutableStateOf(false) }

    // Settings items grouped by category
    val accountSettings = listOf(
        SettingsItem(
            id = "profile",
            title = "Profile",
            description = "Personal information",
            icon = painterResource(R.drawable.account_avatar_profile),
            hasBadge = false
        ),
        SettingsItem(
            id = "payment",
            title = "Payment Methods",
            description = "Credit cards and other payment options",
            icon = painterResource(R.drawable.payment_request),
            hasBadge = true,
            badgeText = "2"
        ),
        SettingsItem(
            id = "documents",
            title = "Travel Documents",
            description = "Passports, IDs, visas",
            icon = painterResource(R.drawable.documents),
            hasBadge = false
        )
    )

    val appSettings = listOf(
        SettingsItem(
            id = "notifications",
            title = "Notifications",
            description = "Push and email notifications",
            icon = painterResource(R.drawable.notification_),
            hasToggle = true
        ),
        SettingsItem(
            id = "appearance",
            title = "Dark Mode",
            description = "Switch between light and dark theme",
            icon = painterResource(R.drawable.night_mode),
            hasToggle = true
        ),
        SettingsItem(
            id = "location",
            title = "Location Services",
            description = "Use location for better recommendations",
            icon = painterResource(R.drawable.location),
            hasToggle = true
        ),
        SettingsItem(
            id = "biometric",
            title = "Biometric Authentication",
            description = "Login with fingerprint or face",
            icon = painterResource(R.drawable.fingerprint),
            hasToggle = true
        )
    )

    val otherSettings = listOf(
        SettingsItem(
            id = "help",
            title = "Help & Support",
            description = "Get assistance",
            icon = painterResource(R.drawable.help_ic),
            hasBadge = false
        ),
        SettingsItem(
            id = "privacy",
            title = "Privacy Policy",
            description = "How we handle your data",
            icon = painterResource(R.drawable.security),
            hasBadge = false
        ),
        SettingsItem(
            id = "terms",
            title = "Terms of Service",
            description = "User agreement",
            icon = painterResource(R.drawable.documents),
            hasBadge = false
        ),
        SettingsItem(
            id = "about",
            title = "About",
            description = "App version and information",
            icon = painterResource(R.drawable.info_),
            hasBadge = false
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF003045),
                        Color(0xFF004D40),
                        Color(0xFF006064)
                    )
                )
            )
    ) {
        // Background animations - similar to HomeScreen
        EnhancedBackgroundAnimations()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        // User profile
                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(36.dp)
                                .clip(CircleShape)
                                .clickable { /* Open profile */ }
                                .background(Color(0xFF1A3546)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
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
                    .verticalScroll(rememberScrollState())
            ) {
                // Profile summary at the top
                ProfileSummaryCard(userName = userName)

                Spacer(modifier = Modifier.height(24.dp))

                // Account Settings Section
                SettingsSection(
                    title = "Account",
                    items = accountSettings,
                    onToggle = { id, isChecked -> },
                    onItemClick = { id ->
                        when (id) {
                            "profile" -> navController.navigate("profile")
                            "payment" -> navController.navigate("payment")
                            "documents" -> navController.navigate("documents")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // App Settings Section
                SettingsSection(
                    title = "App Settings",
                    items = appSettings,
                    onToggle = { id, isChecked ->
                        when (id) {
                            "notifications" -> notificationsEnabled = isChecked
                            "appearance" -> darkModeEnabled = isChecked
                            "location" -> locationEnabled = isChecked
                            "biometric" -> biometricEnabled = isChecked
                        }
                    },
                    onItemClick = { id ->
                        // For toggleable items, clicking the row also toggles them
                        when (id) {
                            "notifications" -> notificationsEnabled = !notificationsEnabled
                            "appearance" -> darkModeEnabled = !darkModeEnabled
                            "location" -> locationEnabled = !locationEnabled
                            "biometric" -> biometricEnabled = !biometricEnabled
                        }
                    },
                    getToggleState = { id ->
                        when (id) {
                            "notifications" -> notificationsEnabled
                            "appearance" -> darkModeEnabled
                            "location" -> locationEnabled
                            "biometric" -> biometricEnabled
                            else -> false
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Other Settings Section
                SettingsSection(
                    title = "Other",
                    items = otherSettings,
                    onToggle = { id, isChecked -> },
                    onItemClick = { id ->
                        when (id) {
                            "help" -> navController.navigate("help")
                            "privacy" -> navController.navigate("privacy")
                            "terms" -> navController.navigate("terms")
                            "about" -> navController.navigate("about")
                        }
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Logout Button
                EnhancedLogoutButton(
                    onClick = {
                        // Handle logout
                        navController.navigate("welcome") {
                            popUpTo("welcome") { inclusive = true }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun ProfileSummaryCard(userName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
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
            // Profile picture
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF003045)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // User info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = userName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "View and edit your profile",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }

            // Edit profile button
            IconButton(
                onClick = { /* Navigate to profile edit */ },
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50).copy(alpha = 0.2f))
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Profile",
                    tint = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    items: List<SettingsItem>,
    onToggle: (String, Boolean) -> Unit,
    onItemClick: (String) -> Unit,
    getToggleState: (String) -> Boolean = { false }
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Section title
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Section card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1A3546).copy(alpha = 0.9f)
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEachIndexed { index, item ->
                    SettingsItemRow(
                        item = item,
                        isLast = index == items.size - 1,
                        onToggle = { isChecked -> onToggle(item.id, isChecked) },
                        onClick = { onItemClick(item.id) },
                        isToggled = getToggleState(item.id)
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsItemRow(
    item: SettingsItem,
    isLast: Boolean,
    onToggle: (Boolean) -> Unit,
    onClick: () -> Unit,
    isToggled: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with animated background
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF003045)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = item.icon,
                    contentDescription = null,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                if (item.description != null) {
                    Text(
                        text = item.description,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }

            // Toggle, badge, or arrow
            when {
                item.hasToggle -> {
                    Switch(
                        checked = isToggled,
                        onCheckedChange = { onToggle(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF4CAF50),
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.Gray.copy(alpha = 0.5f)
                        )
                    )
                }
                item.hasBadge -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.badgeText,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                else -> {
                    Icon(
                        painter = painterResource(R.drawable.chevronright),
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // Add divider if not the last item
        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 72.dp, end = 16.dp),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.1f)
            )
        }
    }
}

@Composable
fun EnhancedLogoutButton(
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logout_animation")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logout_pulse"
    )

    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(50.dp)
            .scale(scale)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(12.dp),
                ambientColor = Color(0xFFE57373),
                spotColor = Color(0xFFE57373)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE57373)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painterResource(R.drawable.logout),
                contentDescription = "Logout",
                tint = Color.White
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Logout",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(navController = rememberNavController())
}