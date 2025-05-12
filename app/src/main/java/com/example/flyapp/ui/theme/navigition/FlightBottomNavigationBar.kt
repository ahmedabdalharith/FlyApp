package com.example.flyapp.ui.theme.navigition

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.theme.GoldColor
import com.example.flyapp.ui.theme.theme.MediumBlue


@Composable
fun FlightBottomNavigationBar(
    navController: NavHostController,
    useRtlLayout: Boolean = false,
    notificationCounts: Map<String, Int> = emptyMap(),
    isVisible: Boolean = true,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Performance optimization: Only recalculate when destination changes
    val currentRoute by remember(currentDestination) {
        derivedStateOf { currentDestination?.route }
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides if (useRtlLayout) LayoutDirection.Rtl else LayoutDirection.Ltr
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            NavigationBarContent(
                currentRoute = currentRoute,
                navController = navController,
                notificationCounts = notificationCounts,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun NavigationBarContent(
    currentRoute: String?,
    navController: NavHostController,
    notificationCounts: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color(0x40000000)
            )
            .height(100.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MediumBlue)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem.items.forEach { item ->
                val isSelected = currentRoute == item.route

                EnhancedNavigationItem(
                    icon = painterResource(id = item.icon),
                    label = item.title,
                    isSelected = isSelected,
                    badgeCount = notificationCounts[item.route] ?: 0,
                    onClick = {
                        if (currentRoute != item.route) {
                            navigateToDestination(navController, item.route)
                        }
                    }
                )
            }
        }
    }
}

private fun navigateToDestination(navController: NavHostController, route: String) {
    navController.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
}
@Composable
fun EnhancedNavigationItem(
    icon: Painter,
    label: String,
    isSelected: Boolean,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    // Animation constants for Dp values
    val dpAnimationSpec = spring<Dp>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    // Animation constants for Float values
    val floatAnimationSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    val itemSize by animateDpAsState(
        targetValue = if (isSelected) 48.dp else 40.dp,
        animationSpec = dpAnimationSpec,
        label = "itemSize"
    )

    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = floatAnimationSpec,
        label = "scale"
    )

    val color = if (isSelected) GoldColor else Color.White.copy(alpha = 0.6f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(4.dp)
            .width(72.dp)
            .scale(scale)
            .clickable(onClick = onClick)
    ) {
        NavigationItemIcon(
            icon = icon,
            label = label,
            isSelected = isSelected,
            badgeCount = badgeCount,
            color = color,
            itemSize = itemSize
        )

        // Label is only visible when item is selected
        if (isSelected) {
            NavigationItemLabel(label = label, color = color)
        }
    }
}

@Composable
private fun NavigationItemIcon(
    icon: Painter,
    label: String,
    isSelected: Boolean,
    badgeCount: Int,
    color: Color,
    itemSize: Dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(itemSize)
    ) {
        // Background circle for selected item
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0x10FFFFFF))
                    .border(
                        width = 1.dp,
                        brush = Brush.radialGradient(
                            colors = listOf(
                                GoldColor.copy(alpha = 0.8f),
                                GoldColor.copy(alpha = 0.3f)
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }

        // Badge with notification count
        BadgedBox(
            badge = {
                if (badgeCount > 0) {
                    Badge(
                        containerColor = Color(0xFFFF3D71),
                        contentColor = Color.White
                    ) {
                        Text(
                            text = if (badgeCount > 99) "99+" else badgeCount.toString(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 3.dp)
                        )
                    }
                }
            }
        ) {
            Icon(
                painter = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun NavigationItemLabel(label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        // Gold indicator line for selected item
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .height(2.dp)
                .width(20.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            GoldColor.copy(alpha = 0.3f),
                            GoldColor,
                            GoldColor.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(1.dp)
                )
        )
    }
}

/**
 * Navigation items for the bottom navigation bar
 */
sealed class BottomNavItem(val route: String, val icon: Int, val title: String) {
    object Home : BottomNavItem(route = Screen.HomeScreen.route, icon = R.drawable.home_, title = "Home")
    object Offers : BottomNavItem(Screen.AllOffersScreen.route, icon = R.drawable.explore_ic, title = "Offers")
    object Trips : BottomNavItem(Screen.TripManagementScreen.route, icon = R.drawable.receipt_ic, title = "My Trips")
    object Search : BottomNavItem(Screen.SearchFlightScreen.route, icon = R.drawable.search, title = "Search")
    object SettingsScreen : BottomNavItem(Screen.SettingsScreen.route, icon = R.drawable.settings, title = "Settings")

    companion object {
        val items = listOf(Home, Offers, Trips, Search, SettingsScreen)
    }
}

@Preview
@Composable
fun EnhancedFlightBottomNavigationBarPreview() {
    val mockNotifications = mapOf(
        Screen.AllOffersScreen.route to 3,
        Screen.TripManagementScreen.route to 7
    )

    Surface(color = Color(0xFF1467BF)) {
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