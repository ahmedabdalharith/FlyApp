package com.example.flyapp.ui.theme.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.navigition.Screen
import kotlinx.coroutines.delay

@Composable
fun FlightBottomNavigationBar(
    navController: NavHostController,
    useRtlLayout: Boolean = false,
    notificationCounts: Map<String, Int> = emptyMap(),
    modifier: Modifier = Modifier
) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val currentRoute = currentDestination?.route

    // Color scheme with no white - using blue tones instead
    val airlineBackground = Color(0xFF0A1929)
    val airlinePrimary = Color(0xFF3BA0FF)     // Selected item color
    val airlineSecondary = Color(0xFF81C6FF)   // Text color for selected item (instead of white)
    val unselectedColor = Color(0xFF7F8487)    // Unselected items

    // Wrap with RTL provider if needed
    CompositionLocalProvider(
        LocalLayoutDirection provides if (useRtlLayout) LayoutDirection.Rtl else LayoutDirection.Ltr
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 24.dp,
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                ),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            colors = CardDefaults.cardColors(
                containerColor = airlineBackground
            )
        ) {
            // Enhanced wave effect at the top of the navigation bar
            EnhancedWaveEffect(
                primaryColor = airlinePrimary.copy(alpha = 0.3f),
                secondaryColor = airlinePrimary.copy(alpha = 0.15f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BottomNavItem.items.forEach { item ->
                    val isSelected = currentRoute == item.route

                    EnhancedBottomNavigationItem(
                        icon = painterResource(id = item.icon),
                        label = item.title,
                        isSelected = isSelected,
                        badgeCount = notificationCounts[item.route] ?: 0,
                        selectedColor = airlinePrimary,
                        unselectedColor = unselectedColor,
                        labelColor = airlineSecondary,  // Changed from white to light blue
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EnhancedWaveEffect(
    primaryColor: Color,
    secondaryColor: Color,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wave_animation")
    val waveAnim1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave1"
    )

    val waveAnim2 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave2"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
    ) {
        // First wave
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .offset(x = (waveAnim1 * 120).dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0f),
                            primaryColor,
                            primaryColor.copy(alpha = 0f),
                        )
                    )
                )
        )

        // Second wave
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .offset(x = (-waveAnim2 * 120).dp)
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            secondaryColor.copy(alpha = 0f),
                            secondaryColor,
                            secondaryColor.copy(alpha = 0f),
                        )
                    )
                )
        )
    }
}

@Composable
fun EnhancedBottomNavigationItem(
    icon: Painter,
    label: String,
    isSelected: Boolean,
    badgeCount: Int = 0,
    selectedColor: Color,
    unselectedColor: Color,
    labelColor: Color,
    onClick: () -> Unit
) {
    // Animation scales with smoother transitions
    val itemScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = tween(350, easing = FastOutSlowInEasing),
        label = "scale"
    )

    val backgroundAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(350),
        label = "bg_alpha"
    )

    // Enhanced flight animation for selected item
    val infiniteTransition = rememberInfiniteTransition(label = "icon_hover")
    val iconHover by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hover"
    )

    // Enhanced bounce when first selected
    var startAnimation by remember { mutableStateOf(false) }
    val bounce by animateFloatAsState(
        targetValue = if (startAnimation && isSelected) 1.25f else 1f,
        animationSpec = tween(350, easing = FastOutSlowInEasing),
        label = "bounce"
    )

    LaunchedEffect(isSelected) {
        if (isSelected) {
            startAnimation = true
            delay(350)
            startAnimation = false
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(itemScale)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            // Enhanced background glow effect when selected
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .alpha(backgroundAlpha * 0.4f)
                        .background(
                            selectedColor,
                            CircleShape
                        )
                        .blur(radius = 4.dp)
                )
            }

            // Badge with improved visibility
            BadgedBox(
                badge = {
                    if (badgeCount > 0) {
                        Badge(
                            containerColor = Color(0xFFFF3D71),
                            modifier = Modifier
                                .offset(x = 3.dp, y = (-3).dp)
                                .shadow(2.dp, CircleShape)
                        ) {
                            Text(
                                text = if (badgeCount > 99) "99+" else badgeCount.toString(),
                                color = Color(0xFFDCEAFF),  // Light blue instead of white
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 3.dp)
                            )
                        }
                    }
                }
            ) {
                // Icon with enhanced hover effect when selected
                Icon(
                    painter = icon,
                    contentDescription = label,
                    tint = if (isSelected) selectedColor else unselectedColor,
                    modifier = Modifier
                        .size(if (isSelected) 28.dp else 24.dp)
                        .scale(bounce)
                        .offset(y = if (isSelected) (iconHover * 1.5f).dp else 0.dp)
                        .graphicsLayer {
                            if (isSelected) {
                                alpha = 0.9f + (iconHover * 0.1f)
                            }
                        }
                )
            }
        }

        // Title with enhanced animation - using blue instead of white
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = label,
                color = if (isSelected) labelColor else unselectedColor,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .graphicsLayer {
                        if (isSelected) {
                            alpha = 0.9f + (iconHover * 0.1f)
                        }
                    }
            )
        }

        // Enhanced indicator dot when selected
        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(4.dp)
                    .background(selectedColor, CircleShape)
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: Int, val title: String) {
    object Home : BottomNavItem(route = Screen.HomeScreen.route, icon = R.drawable.home_, title = "Home")
    object Offers : BottomNavItem(Screen.OffersScreen.route, icon = R.drawable.explore_ic, title = "Offers")
    object Trips : BottomNavItem(Screen.TripManagementScreen.route, icon = R.drawable.receipt_ic, title = "My Trips")
    object HelpCenter : BottomNavItem(Screen.HelpCenterScreen.route, icon = R.drawable.help_ic, title = "Help")

    companion object {
        val items = listOf(Home, Offers, Trips, HelpCenter)
    }
}

@Preview
@Composable
fun FlightBottomNavigationBarPreview() {
    val mockNotifications = mapOf(
        Screen.OffersScreen.route to 3,
        Screen.TripManagementScreen.route to 7
    )

    Surface(color = Color(0xFF102C44)) {
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            FlightBottomNavigationBar(
                navController = rememberNavController(),
                useRtlLayout = false,
                notificationCounts = mockNotifications
            )
        }
    }
}