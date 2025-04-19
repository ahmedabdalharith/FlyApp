package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.ParticleEffectBackground
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.theme.BackgroundColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class FlightDeal(
    val id: Int,
    val destination: String,
    val country: String,
    val price: Int,
    val discountPercentage: Int = 0,
    val imageResId: Int,
    val rating: Float,
    val departureDate: String,
    val returnDate: String,
    val airline: String,
    val tags: List<String> = listOf(),
    val hasPromotion: Boolean = false
)

data class CategoryFilter(
    val name: String,
    val isSelected: Boolean = false
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // State
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    // Categories
    var categories by remember {
        mutableStateOf(
            listOf(
                CategoryFilter("All", true),
                CategoryFilter("Weekend Trips"),
                CategoryFilter("Domestic"),
                CategoryFilter("International"),
                CategoryFilter("Holiday Packages"),
                CategoryFilter("Last Minute")
            )
        )
    }

    // Sample flight deals
    val flightDeals = remember {
        listOf(
            FlightDeal(
                id = 1,
                destination = "Paris",
                country = "France",
                price = 599,
                discountPercentage = 15,
                imageResId = R.drawable.explore_ic, // Replace with your actual image
                rating = 4.7f,
                departureDate = "May 15",
                returnDate = "May 22",
                airline = "Air France",
                tags = listOf("Romantic", "City"),
                hasPromotion = true
            ),
            FlightDeal(
                id = 2,
                destination = "Bali",
                country = "Indonesia",
                price = 849,
                discountPercentage = 20,
                imageResId = R.drawable.explore_ic, // Replace with your actual image
                rating = 4.9f,
                departureDate = "Jun 12",
                returnDate = "Jun 20",
                airline = "Singapore Airlines",
                tags = listOf("Beach", "Relax"),
                hasPromotion = true
            ),
            FlightDeal(
                id = 3,
                destination = "New York",
                country = "USA",
                price = 720,
                imageResId = R.drawable.explore_ic, // Replace with your actual image
                rating = 4.5f,
                departureDate = "Jul 8",
                returnDate = "Jul 14",
                airline = "United Airlines",
                tags = listOf("Shopping", "City")
            ),
            FlightDeal(
                id = 4,
                destination = "Tokyo",
                country = "Japan",
                price = 899,
                discountPercentage = 10,
                imageResId = R.drawable.explore_ic, // Replace with your actual image
                rating = 4.8f,
                departureDate = "Aug 5",
                returnDate = "Aug 19",
                airline = "JAL",
                tags = listOf("Cultural", "Food")
            )
        )
    }

    // Animated gradient background
    val infiniteTransition = rememberInfiniteTransition(label = "background_animation")
    val backgroundAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_animation"
    )

    // Loading effect
    LaunchedEffect(Unit) {
        delay(1500)
        isLoading = false
    }

    // Main UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                BackgroundColor
            )
    ) {
        // Animated particle effect (similar to Login screen)
        ParticleEffectBackground()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = Color(0xFF3BA0FF)
                                )) {
                                    append("Exclusive ")
                                }
                                withStyle(style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = Color(0xFF81C6FF)
                                )) {
                                    append("Offers")
                                }
                            }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.shadow(8.dp)
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Search bar
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                "Search destinations",
                                color = Color(0xFF81C6FF).copy(alpha = 0.6f)
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color(0xFF3BA0FF)
                            )
                        },
                        trailingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF152238))
                                    .clickable { /* Filter action */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painterResource(R.drawable.filter_ic),
                                    contentDescription = "Filter",
                                    tint = Color(0xFF3BA0FF),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .shadow(8.dp, RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color(0xFF81C6FF),
                            unfocusedTextColor = Color(0xFF81C6FF),
                            focusedBorderColor = Color(0xFF3BA0FF),
                            unfocusedBorderColor = Color(0xFF3BA0FF).copy(alpha = 0.5f),
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray,
                            focusedContainerColor = Color(0xFF152238),
                            unfocusedContainerColor = Color(0xFF152238)
                        )
                    )
                }

                // Categories filter with glowing effect
                item {
                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        Text(
                            "Filter By Category",
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF81C6FF),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(start = 16.dp, bottom = 12.dp)
                        )

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(categories) { category ->
                                val borderGlow = if (category.isSelected) backgroundAlpha + 0.6f else 0f

                                Box(
                                    modifier = Modifier
                                        .shadow(if (category.isSelected) 4.dp else 1.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            if (category.isSelected) {
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color(0xFF3BA0FF),
                                                        Color(0xFF4DB5FF)
                                                    )
                                                )
                                            } else {
                                                Brush.horizontalGradient(
                                                    colors = listOf(
                                                        Color(0xFF152238),
                                                        Color(0xFF152238)
                                                    )
                                                )
                                            }
                                        )
                                        .clickable {
                                            categories = categories.map {
                                                it.copy(isSelected = it.name == category.name)
                                            }
                                        }
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = category.name,
                                        fontSize = 14.sp,
                                        fontWeight = if (category.isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (category.isSelected) Color(0xFF0A1929) else Color(0xFF81C6FF)
                                    )
                                }
                            }
                        }
                    }
                }

                // Top deals section
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = Color(0xFF81C6FF)
                                )) {
                                    append("Top ")
                                }
                                withStyle(style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp,
                                    color = Color(0xFF3BA0FF)
                                )) {
                                    append("Deals")
                                }
                            }
                        )

                        Text(
                            "View All",
                            color = Color(0xFF3BA0FF),
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable(
                                onClick = {
                                    navController.navigate(Screen.AllOffersScreen.route)
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                        )
                    }
                }

                // Flight deals
                items(flightDeals) { deal ->
                    AnimatedVisibility(
                        visible = !isLoading,
                        enter = fadeIn(animationSpec = tween(durationMillis = 500)) +
                                slideInVertically(
                                    initialOffsetY = { it / 2 },
                                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                                )
                    ) {
                        EnhancedFlightDealCard(
                            flightDeal = deal,
                            onClick = {
                                navController.navigate(Screen.OfferDetailsScreen.route)
                            },
                            modifier = Modifier.padding(horizontal = 16.dp),
                            borderGlow = backgroundAlpha,
                            onBookClick = {

                                navController.navigate(Screen.BookingFlightScreen.route)
                            }
                        )
                    }
                }

                // Bottom space
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun EnhancedFlightDealCard(
    flightDeal: FlightDeal,
    onBookClick: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    borderGlow: Float = 0f
) {
    val scale = remember { mutableFloatStateOf(1f) }
    val animatedScale by animateFloatAsState(
        targetValue = scale.floatValue,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(animatedScale)
    ) {
        // Glow effect for the border
        if (flightDeal.hasPromotion) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawRoundRect(
                    color = Color(0xFF3BA0FF).copy(alpha = borderGlow * 0.8f),
                    cornerRadius = CornerRadius(20f, 20f),
                    style = Stroke(width = 8f)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    scale.floatValue = 0.95f
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(100)
                        scale.floatValue = 1f
                        onClick()
                    }
                },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF152238)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column {
                // Image and overlay
                Box {
                    // Placeholder for image, replace with your actual image
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF1E3B58),
                                        Color(0xFF16304D)
                                    )
                                )
                            )
                    ) {
                        // In a real app, replace this with your actual image
                        Image(
                            painterResource(R.drawable.emirates_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.Center),
                        )
                    }

                    // Gradient overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0xFF0A1929).copy(alpha = 0.8f)
                                    ),
                                    startY = 0f,
                                    endY = 400f
                                )
                            )
                    )

                    // Price tag with animation
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (flightDeal.discountPercentage > 0) {
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF3BA0FF),
                                            Color(0xFF2E8BD9)
                                        )
                                    )
                                } else {
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFF3BA0FF),
                                            Color(0xFF3BA0FF)
                                        )
                                    )
                                }
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.End) {
                            if (flightDeal.discountPercentage > 0) {
                                Text(
                                    text = "-${flightDeal.discountPercentage}%",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.9f)
                                )

                                Spacer(modifier = Modifier.height(2.dp))

                                Text(
                                    text = buildAnnotatedString {
                                        val originalPrice = flightDeal.price * 100 / (100 - flightDeal.discountPercentage)
                                        withStyle(style = SpanStyle(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp,
                                            color = Color.White.copy(alpha = 0.7f),
                                            textDecoration = TextDecoration.LineThrough
                                        )) {
                                            append("$${originalPrice}")
                                        }
                                    }
                                )
                            }

                            Text(
                                text = "$${flightDeal.price}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        }
                    }

                    // Promotion badge if available
                    if (flightDeal.hasPromotion) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        colors = listOf(
                                            Color(0xFFFF3D71),
                                            Color(0xFFFF5C85)
                                        )
                                    )
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "LIMITED OFFER",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    // Destination info
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = flightDeal.destination,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.LocationOn,
                                contentDescription = null,
                                tint = Color(0xFF3BA0FF),
                                modifier = Modifier.size(14.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = flightDeal.country,
                                fontSize = 14.sp,
                                color = Color(0xFF81C6FF)
                            )
                        }
                    }
                }

                // Flight details
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Dates row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DateRange,
                            contentDescription = null,
                            tint = Color(0xFF3BA0FF),
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "${flightDeal.departureDate} - ${flightDeal.returnDate}",
                            fontSize = 14.sp,
                            color = Color(0xFF81C6FF)
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Rating with enhanced style
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF152238))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD700),
                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Text(
                                    text = flightDeal.rating.toString(),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF81C6FF)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Divider
                    HorizontalDivider(
                         thickness = 1.dp,
                        color = Color(0xFF3BA0FF).copy(alpha = 0.2f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Airline info
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Airline logo placeholder
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0A1929)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = flightDeal.airline.first().toString(),
                                color = Color(0xFF3BA0FF),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = flightDeal.airline,
                            fontSize = 14.sp,
                            color = Color(0xFF81C6FF),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        Button(
                            onClick = { onBookClick },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF3BA0FF)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Book Now",
                                color = Color(0xFF0A1929),
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFF0A1929),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    // Tags
                    if (flightDeal.tags.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            flightDeal.tags.forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFF3BA0FF).copy(alpha = 0.2f))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = tag,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF81C6FF)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun OffersScreenPreview() {
    OffersScreen(rememberNavController())
}
