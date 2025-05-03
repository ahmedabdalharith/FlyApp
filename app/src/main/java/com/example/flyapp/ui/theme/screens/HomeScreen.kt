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
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.navigition.Screen
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale


@Composable
fun HomeScreen(navController: NavController) {
    // Sample data
    val allOffers = remember {
        listOf(
            FlightOffer(
                id = "FLASH001",
                type = OfferType.FLASH_SALE,
                title = "48-Hour Sale: NYC to London",
                description = "Limited time offer with huge savings on direct flights",
                departureCity = "New York",
                departureCode = "JFK",
                arrivalCity = "London",
                arrivalCode = "LHR",
                originalPrice = 799.99,
                discountedPrice = 499.99,
                validUntil = "May 2, 2025",
                discountPercentage = 38,
                isFeatured = true,
                airline = "Sky Airways"
            ),
            FlightOffer(
                id = "SEAS001",
                type = OfferType.SEASONAL,
                title = "Spring Getaway to Paris",
                description = "Enjoy the beauty of Paris in spring with special rates",
                departureCity = "Boston",
                departureCode = "BOS",
                arrivalCity = "Paris",
                arrivalCode = "CDG",
                originalPrice = 899.99,
                discountedPrice = 649.99,
                validUntil = "May 15, 2025",
                discountPercentage = 28,
                promoCode = "SPRING25",
                airline = "Global Airlines"
            ),
            FlightOffer(
                id = "LAST001",
                type = OfferType.LAST_MINUTE,
                title = "Last Minute: LA to Miami",
                description = "Spontaneous trip? Grab these last-minute fares",
                departureCity = "Los Angeles",
                departureCode = "LAX",
                arrivalCity = "Miami",
                arrivalCode = "MIA",
                originalPrice = 449.99,
                discountedPrice = 299.99,
                validUntil = "May 3, 2025",
                discountPercentage = 33,
                isFeatured = true,
                airline = "Coastal Connect"
            ),
            FlightOffer(
                id = "FREQ001",
                type = OfferType.FREQUENT_FLYER,
                title = "Double Miles: Chicago to Tokyo",
                description = "Earn double miles on our premium routes",
                departureCity = "Chicago",
                departureCode = "ORD",
                arrivalCity = "Tokyo",
                arrivalCode = "HND",
                originalPrice = 1299.99,
                discountedPrice = 1099.99,
                validUntil = "May 31, 2025",
                discountPercentage = 15,
                promoCode = "MILES2X",
                airline = "Star Flights"
            ),
            FlightOffer(
                id = "BUND001",
                type = OfferType.BUNDLE,
                title = "Flight + Hotel: NYC to Las Vegas",
                description = "Complete package with 3 nights at a premium hotel",
                departureCity = "New York",
                departureCode = "JFK",
                arrivalCity = "Las Vegas",
                arrivalCode = "LAS",
                originalPrice = 899.99,
                discountedPrice = 699.99,
                validUntil = "May 25, 2025",
                discountPercentage = 22,
                promoCode = "BUNDLE22",
                airline = "Desert Express"
            )
        )
    }

    val popularDestinations = remember {
        listOf(
            Destination(
                id = "DEST001",
                city = "Paris",
                country = "France",
                popularity = 98,
                code = "CDG",
                imageRes = R.drawable.paris,
                rating = 4.8f ,
                isFeatured = true,
                description = "The city of lights and love, known for its art, fashion, and culture."
            ),
            Destination(
                id = "DEST002",
                city = "Tokyo",
                country = "Japan",
                popularity = 95,
                code = "HND",
                imageRes = R.drawable.tokyo,
                rating = 4.7f,
                isFeatured = true,
                description = "A bustling metropolis blending traditional culture with modern technology."
            ),
            Destination(
                id = "DEST003",
                city = "New York",
                country = "USA",
                popularity = 99,
                code = "JFK",
                imageRes = R.drawable.new_york,
                rating = 4.9f,
                isFeatured = true,
                description = "The Big Apple, known for its iconic skyline, Broadway shows, and diverse culture."
            ),
            

        )
    }

    // State for search text
    var searchText by remember { mutableStateOf("") }

    // Animation states
    var showContent by remember { mutableStateOf(false) }

    // Background animation (matching existing screens)
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
        // Security pattern background (matching existing screens)
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
            val stroke = Stroke(width = 1f, pathEffect = pathEffect)
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
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn(tween(800)) + slideInVertically(
                initialOffsetY = { it / 3 },
                animationSpec = tween(durationMillis = 800)
            )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 56.dp)
            ) {
                item {
                    // Top app bar
                    HomeTopAppBar()

                    // Greeting section
                    GreetingSection(navController)

                    // Search section
                    SearchSection(
                        searchText = searchText,
                        onSearchTextChange = { searchText = it }
                    )

                    // Featured Offers section
                    FeaturedOffersSection(
                        offers = allOffers.filter { it.isFeatured },
                        navController = navController
                    )

                    // Popular Destinations section
                    PopularDestinationsSection(
                        destinations = popularDestinations,
                        navController = navController
                    )

                    // All Offers section
                    AllOffersSection(
                        offers = allOffers,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun HomeTopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App logo/name
        Text(
            modifier = Modifier.weight(1f).padding(top = 16.dp),
            textAlign = TextAlign.Center,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )) {
                    append("FLY")
                }
                withStyle(style = SpanStyle(
                    color = GoldColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )) {
                    append("APP")
                }
            }
        )


    }
}

@Composable
fun GreetingSection(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Hello, Ahmed",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Where would you like to fly today?",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 16.sp
            )
        }
        // Profile button
        IconButton(
            onClick = { 
                
                navController.navigate(Screen.ProfileScreen.route)
            },
            modifier = Modifier
                .size(40.dp)
                .border(1.dp, GoldColor.copy(alpha = 0.8f), CircleShape)
                .background(DarkNavyBlue.copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = GoldColor
            )
        }

    }
}

@Composable
fun SearchSection(
    searchText: String,
    onSearchTextChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(12.dp))
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GoldColor.copy(alpha = 0.7f),
                            GoldColor.copy(alpha = 0.3f)
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                ),
            placeholder = { Text("Search destinations, flights...", color = Color.White.copy(alpha = 0.6f)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = GoldColor
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = GoldColor
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = DarkNavyBlue.copy(alpha = 0.85f),
                unfocusedContainerColor = DarkNavyBlue.copy(alpha = 0.85f),
                disabledContainerColor = DarkNavyBlue.copy(alpha = 0.85f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = GoldColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}

@Composable
fun FeaturedOffersSection(
    offers: List<FlightOffer>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Section title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Featured Offers",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "View All",
                color = GoldColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    // Navigate to view all offers
                    navController.navigate(Screen.AllOffersScreen.route)
                }
            )
        }

        // Featured offers carousel
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(offers) { offer ->
                FeaturedOfferCard(
                    offer = offer,
                    onClick = {
                        navController.navigate(Screen.OfferDetailsScreen.route)
                    }
                )
            }
        }
    }
}

@Composable
fun FeaturedOfferCard(
    offer: FlightOffer,
    onClick: () -> Unit
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    formatter.currency = Currency.getInstance(offer.currency)

    Card(
        modifier = Modifier
            .size(width = 340.dp, height = 240.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.7f),
                        GoldColor.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DarkNavyBlue.copy(alpha = 0.7f),
                                DeepBlue.copy(alpha = 0.9f)
                            )
                        )
                    )
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Offer type badge
                Box(
                    modifier = Modifier
                        .background(
                            color = offer.type.color(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = offer.type.displayName().uppercase(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Title
                Text(
                    text = offer.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Description
                Text(
                    text = offer.description,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Route
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = offer.departureCode,
                            color = GoldColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = offer.departureCity,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        painter = painterResource(R.drawable.plane_path),
                        contentDescription = null,
                        tint = GoldColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = offer.arrivalCode,
                            color = GoldColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = offer.arrivalCity,
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Price and discount
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = formatter.format(offer.discountedPrice),
                            color = GoldColor,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = formatter.format(offer.originalPrice),
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFF4CAF50),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "-${offer.discountPercentage}%",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PopularDestinationsSection(
    destinations: List<Destination>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Section title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Popular Destinations",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "View All",
                color = GoldColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    // Navigate to view all destinations
                    navController.navigate(Screen.AllDestinationsScreen.route)
                }
            )
        }

        // Popular destinations carousel
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(destinations) { destination ->
                DestinationCard(
                    destination = destination,
                    onClick = {
                        // Navigate to destination details
                        navController.navigate(Screen.FlightDetailsScreen.route)
                    }
                )
            }
        }
    }
}

@Composable
fun DestinationCard(
    destination: Destination,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(width = 170.dp, height = 220.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.7f),
                        GoldColor.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Placeholder for destination image (use actual resource in your app)
            Image(
                painter = painterResource(id = R.drawable.new_york), // Replace with actual image resource
                contentDescription = destination.city,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient overlay for better text visibility
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            ),
                            startY = 0f,
                            endY = 500f
                        )
                    )
            )

            // Destination info
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = destination.city,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = destination.country,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Popularity indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${destination.popularity}% match",
                        color = GoldColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
@Composable
fun AllOffersSection(
    offers: List<FlightOffer>,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Section title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "All Offers",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "View All",
                color = GoldColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable {
                    // Navigate to view all offers
                    navController.navigate(Screen.AllOffersScreen.route)
                }
            )
        }

        // Display offers in a vertical list
        offers.take(3).forEach { offer ->
            OfferListItem(
                offer = offer,
                onClick = {
                    navController.navigate(Screen.OfferDetailsScreen.route)
                }
            )
        }

        // "Show more" button
        if (offers.size > 3) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MediumBlue,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    GoldColor.copy(alpha = 0.7f),
                                    GoldColor.copy(alpha = 0.3f)
                                )
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            navController.navigate(Screen.AllOffersScreen.route)
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Show More Offers",
                        color = GoldColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun OfferListItem(
    offer: FlightOffer,
    onClick: () -> Unit
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    formatter.currency = Currency.getInstance(offer.currency)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.6f),
                        GoldColor.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side - Route info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Offer type badge
                Box(
                    modifier = Modifier
                        .background(
                            color = offer.type.color(),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = offer.type.displayName().uppercase(),
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Route
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = offer.departureCode,
                        color = GoldColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Icon(
                        painter = painterResource(R.drawable.plane_path),
                        contentDescription = null,
                        tint = GoldColor,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(16.dp)
                    )

                    Text(
                        text = offer.arrivalCode,
                        color = GoldColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Cities
                Text(
                    text = "${offer.departureCity} to ${offer.arrivalCity}",
                    color = Color.White,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Airline if available
                if (offer.airline != null) {
                    Text(
                        text = offer.airline,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp
                    )
                }
            }

            // Right side - Price info
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatter.format(offer.discountedPrice),
                    color = GoldColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = formatter.format(offer.originalPrice),
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    textDecoration = TextDecoration.LineThrough
                )

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF4CAF50),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "-${offer.discountPercentage}%",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Valid until ${offer.validUntil}",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 8.sp
                )
            }
        }
    }
}

// Preview function to see the UI in Android Studio
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        HomeScreen(navController = navController)
    }
}
