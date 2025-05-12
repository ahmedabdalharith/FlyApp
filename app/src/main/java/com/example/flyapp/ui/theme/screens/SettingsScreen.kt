package com.example.flyapp.ui.theme.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.FlightTopAppBar
import androidx.core.net.toUri
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.theme.DarkNavyBlue
import com.example.flyapp.ui.theme.theme.DeepBlue
import com.example.flyapp.ui.theme.theme.GoldColor
import com.example.flyapp.ui.theme.theme.MediumBlue

@Composable
fun SettingsScreen(navController: NavHostController) {
    // Settings state
    var darkModeEnabled by remember { mutableStateOf(true) }
    var notificationsEnabled by remember { mutableStateOf(true) }
    var biometricsEnabled by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("English") }
    var selectedCurrency by remember { mutableStateOf("USD") }
    var context = LocalContext.current
    var showRateUsDialog by remember { mutableStateOf(false) }
    // Scroll state
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepBlue,     // Dark blue (matching existing screen)
                        MediumBlue,   // Medium blue (matching existing screen)
                        DarkNavyBlue  // Navy blue (matching existing screen)
                    )
                )
            ).padding(
                bottom = 60.dp
            )
    ) {
        // Security pattern background (matching existing screen)
        Canvas(modifier = Modifier.fillMaxSize()) {
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
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Top app bar (matching existing screen style)
            FlightTopAppBar(
                textOne = "SETT",
                textTwo = "INGS",
                navController = navController,
                isBacked = true
            )

            // Settings content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Profile section
                ProfileSection(navController)

                Spacer(modifier = Modifier.height(24.dp))

                // Preferences section
                SettingsCard(title = "Preferences") {
                    // Dark mode toggle
                    SettingsToggleItem(
                        icon = painterResource(R.drawable.night_mode),
                        title = "Dark Mode",
                        subtitle = "Use dark theme throughout the app",
                        checked = darkModeEnabled,
                        onCheckedChange = { darkModeEnabled = it }
                    )

                    SettingsDivider()

                    // Notifications toggle
                    SettingsToggleItem(
                        icon = painterResource(R.drawable.notification_),
                        title = "Notifications",
                        subtitle = "Enable push notifications",
                        checked = notificationsEnabled,
                        onCheckedChange = { notificationsEnabled = it }
                    )

                    SettingsDivider()

                    // Language selector
                    SettingsSelectionItem(
                        icon = painterResource(R.drawable.language_),
                        title = "Language",
                        value = selectedLanguage,
                        onClick = { /* Open language selector */ }
                    )

                    SettingsDivider()

                    // Currency selector
                    SettingsSelectionItem(
                        icon = painterResource(R.drawable.creditcard),
                        title = "Currency",
                        value = selectedCurrency,
                        onClick = { /* Open currency selector */ }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Security section
                SettingsCard(title = "Security & Privacy") {
                    // Biometrics toggle
                    SettingsToggleItem(
                        icon = painterResource(R.drawable.fingerprint),
                        title = "Biometric Authentication",
                        subtitle = "Use fingerprint or face ID to log in",
                        checked = biometricsEnabled,
                        onCheckedChange = { biometricsEnabled = it }
                    )

                    SettingsDivider()

                    // Privacy policy
                    SettingsClickableItem(
                        icon = painterResource(R.drawable.security),
                        title = "Privacy Policy",
                        onClick = {
                            // Open privacy policy in browser
                            val intent = Intent(Intent.ACTION_VIEW,
                                "https://flyapp.com/privacy-policy".toUri())
                            context.startActivity(intent)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Support section
                SettingsCard(title = "Support") {
                    // Help center
                    SettingsClickableItem(
                        icon = painterResource(R.drawable.help_ic),
                        title = "Help Center",
                        onClick = {
                            navController.navigate(
                                Screen.HelpCenterScreen.route
                            )
                        }
                    )

                    SettingsDivider()

                    // Support tickets
                    SettingsClickableItem(
                        icon = painterResource(R.drawable.favorite_),
                        title = "Rate Us",
                        onClick = {
                            showRateUsDialog = true
                        }                    )
                }
                if (showRateUsDialog) {
                    RateUsDialog(
                        onSubmitRating = { rating ->
                            showRateUsDialog = false

                            // If rating is high (4-5), direct to app store
                            if (rating >= 4) {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = "market://details?id=${context.packageName}".toUri()
                                        setPackage("com.android.vending")
                                    }
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // Fallback to browser if Play Store isn't available
                                    val webIntent = Intent(Intent.ACTION_VIEW,
                                        "https://play.google.com/store/apps/details?id=${context.packageName}".toUri())
                                    context.startActivity(webIntent)
                                }
                            } else {
                                // For lower ratings, navigate to feedback form
                                navController.navigate(Screen.FeedbackFormScreen.route)
                            }
                        },
                        onDismiss = { showRateUsDialog = false }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // App info
                Text(
                    text = "1.0.0",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProfileSection(navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(GoldColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile",
                    tint = GoldColor,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Profile details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Ahmed Ibrahim",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "ahmed.alharith@gmail.com",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Verified",
                        tint = GoldColor,
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Premium Member",
                        color = GoldColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Edit profile button
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .clickable {
                        // Navigate to profile edit screen
                        navController.navigate(Screen.ProfileEditorScreen.route)
                    }
                    .background(GoldColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = "Edit Profile",
                    tint = GoldColor
                )
            }
        }
    }
}

@Composable
fun SettingsCard(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.5f),
                        GoldColor.copy(alpha = 0.2f),
                        GoldColor.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Card title
            Text(
                text = title.uppercase(),
                color = GoldColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(16.dp)
            )

            HorizontalDivider(
                color = GoldColor.copy(alpha = 0.3f),
                thickness = 1.dp
            )

            // Card content
            content()
        }
    }
}

@Composable
fun SettingsToggleItem(
    icon: Painter,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(GoldColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = GoldColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = subtitle,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }

        // Toggle switch with animated colors
        val thumbColor by animateColorAsState(
            targetValue = if (checked) GoldColor else Color.Gray,
            animationSpec = tween(300, easing = FastOutSlowInEasing),
            label = "thumb_color"
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = thumbColor,
                checkedTrackColor = GoldColor.copy(alpha = 0.3f),
                checkedBorderColor = GoldColor.copy(alpha = 0.5f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.DarkGray.copy(alpha = 0.3f),
                uncheckedBorderColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun SettingsSelectionItem(
    icon: Painter,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(GoldColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = GoldColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        // Value text
        Text(
            text = value,
            color = GoldColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Arrow icon
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = "Select",
            tint = GoldColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SettingsClickableItem(
    icon: Painter,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(GoldColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = GoldColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text
        Text(
            text = title,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        // Arrow icon
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = "Go",
            tint = GoldColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SettingsDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(start = 56.dp, end = 16.dp),
        color = GoldColor.copy(alpha = 0.2f),
        thickness = 0.5.dp
    )
}
@Composable
fun RateUsDialog(
    onSubmitRating: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedRating by remember { mutableIntStateOf(0) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = DarkNavyBlue
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Rate FlyApp",
                    color = GoldColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "We'd love to hear your feedback!",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Star rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (i in 1..5) {
                        Icon(
                            painter = painterResource(
                                id = if (i <= selectedRating) {
                                    R.drawable.ic_star_filled // Make sure you have this drawable
                                } else {
                                    R.drawable.ic_star_outline // Make sure you have this drawable
                                }
                            ),
                            contentDescription = "Star $i",
                            tint = if (i <= selectedRating) GoldColor else Color.Gray,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(horizontal = 4.dp)
                                .clickable { selectedRating = i }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = Color.White.copy(alpha = 0.7f)
                        )
                    ) {
                        Text("LATER")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { onSubmitRating(selectedRating) },
                        enabled = selectedRating > 0,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldColor,
                            contentColor = Color.Black,
                            disabledContainerColor = GoldColor.copy(alpha = 0.3f),
                            disabledContentColor = Color.Black.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("SUBMIT")
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingsScreen(navController = rememberNavController())
    }
}
