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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableStateListOf
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
import com.example.flyapp.ui.theme.components.FlightTopAppBar
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.theme.DarkNavyBlue
import com.example.flyapp.ui.theme.theme.DeepBlue
import com.example.flyapp.ui.theme.theme.GoldColor
import com.example.flyapp.ui.theme.theme.MediumBlue
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.util.Locale

@Composable
fun OffersScreen(
    navController: NavController
) {
    // Sample data for flight offers
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
            ),
            FlightOffer(
                id = "LAST002",
                type = OfferType.LAST_MINUTE,
                title = "Weekend Escape: Seattle to Vancouver",
                description = "Perfect for a quick getaway this weekend",
                departureCity = "Seattle",
                departureCode = "SEA",
                arrivalCity = "Vancouver",
                arrivalCode = "YVR",
                originalPrice = 299.99,
                discountedPrice = 199.99,
                validUntil = "May 4, 2025",
                discountPercentage = 33,
                airline = "Pacific North"
            ),
            FlightOffer(
                id = "SEAS002",
                type = OfferType.SEASONAL,
                title = "Summer Preview: Atlanta to Cancun",
                description = "Book early for the best summer vacation rates",
                departureCity = "Atlanta",
                departureCode = "ATL",
                arrivalCity = "Cancun",
                arrivalCode = "CUN",
                originalPrice = 599.99,
                discountedPrice = 479.99,
                validUntil = "May 20, 2025",
                discountPercentage = 20,
                promoCode = "SUMMER20",
                airline = "Southern Skies"
            )
        )
    }

    // Keep track of favorited offers
    val favoriteOffers = remember { mutableStateListOf<String>() }

    // Filter states
    val offerTypes = remember { OfferType.entries }
    var selectedOfferType by remember { mutableStateOf<OfferType?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    // Filtered offers
    val filteredOffers = remember(selectedOfferType, searchQuery) {
        allOffers.filter { offer ->
            (selectedOfferType == null || offer.type == selectedOfferType) &&
                    (searchQuery.isEmpty() ||
                            offer.title.contains(searchQuery, ignoreCase = true) ||
                            offer.departureCity.contains(searchQuery, ignoreCase = true) ||
                            offer.arrivalCity.contains(searchQuery, ignoreCase = true) ||
                            offer.departureCode.contains(searchQuery, ignoreCase = true) ||
                            offer.arrivalCode.contains(searchQuery, ignoreCase = true))
        }
    }

    // Featured offers
    val featuredOffers = remember { allOffers.filter { it.isFeatured } }

    // Animation states
    var showContent by remember { mutableStateOf(false) }

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

    // Currency formatter
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)

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
            .padding(bottom = 64.dp)

    ) {
        // Security pattern background (same as other screens)
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
                textOne = "SPECIAL",
                textTwo = "OFFERS",
                navController = navController
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Limited time flight deals",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
            // Content with animation
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(800)) + slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = tween(durationMillis = 800)
                )
            ) {
                // Scrollable content
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    // Search bar
                    item {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it }
                        )
                    }

                    // Offer type filters
                    item {
                        OfferTypeFilters(
                            offerTypes = offerTypes,
                            selectedType = selectedOfferType,
                            onTypeSelected = { selectedOfferType = it }
                        )
                    }

                    // Featured offers section if available
                    if (featuredOffers.isNotEmpty() && (selectedOfferType == null || featuredOffers.any { it.type == selectedOfferType })) {
                        item {
                            Text(
                                text = "FEATURED DEALS",
                                color = GoldColor,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                            )

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                items(featuredOffers.filter {
                                    selectedOfferType == null || it.type == selectedOfferType
                                }) { offer ->
                                    FeaturedOfferCard(
                                        offer = offer,
                                        formatter = formatter,
                                        isFavorite = favoriteOffers.contains(offer.id),
                                        onFavoriteClick = {
                                            if (favoriteOffers.contains(offer.id)) {
                                                favoriteOffers.remove(offer.id)
                                            } else {
                                                favoriteOffers.add(offer.id)
                                            }
                                        },
                                        onClick = {
                                            navController.navigate(Screen.OfferDetailsScreen.route)
                                        }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Divider(color = GoldColor.copy(alpha = 0.3f), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // All offers section header
                    item {
                        Text(
                            text = if (selectedOfferType != null) "${selectedOfferType!!.displayName().uppercase()} OFFERS" else "ALL OFFERS",
                            color = GoldColor,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    // All offers or filtered offers
                    if (filteredOffers.isEmpty()) {
                        item {
                            EmptyOffersView(searchQuery, selectedOfferType)
                        }
                    } else {
                        items(filteredOffers) { offer ->
                            OfferCard(
                                offer = offer,
                                formatter = formatter,
                                isFavorite = favoriteOffers.contains(offer.id),
                                onFavoriteClick = {
                                    if (favoriteOffers.contains(offer.id)) {
                                        favoriteOffers.remove(offer.id)
                                    } else {
                                        favoriteOffers.add(offer.id)
                                    }
                                },
                                onClick = {
                                    navController.navigate(Screen.OfferDetailsScreen.route)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    // Bottom padding
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.7f),
                        GoldColor.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            ),
        placeholder = {
            Text(
                text = "Search cities, airports, or offers...",
                color = Color.White.copy(alpha = 0.6f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = GoldColor
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painterResource(R.drawable.filter_ic),
                        contentDescription = "Clear",
                        tint = GoldColor
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = DarkNavyBlue.copy(alpha = 0.9f),
            unfocusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
            disabledContainerColor = DarkNavyBlue,
            cursorColor = GoldColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true
    )
}

@Composable
fun OfferTypeFilters(
    offerTypes: List<OfferType>,
    selectedType: OfferType?,
    onTypeSelected: (OfferType?) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // "All" filter
        item {
            FilterChip(
                text = "All",
                isSelected = selectedType == null,
                backgroundColor = GoldColor,
                onClick = { onTypeSelected(null) }
            )
        }

        // Type filters
        items(offerTypes) { type ->
            FilterChip(
                text = type.displayName(),
                isSelected = selectedType == type,
                backgroundColor = type.color(),
                onClick = { onTypeSelected(type) }
            )
        }
    }
}

@Composable
fun FilterChip(
    text: String,
    isSelected: Boolean,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) backgroundColor else DarkNavyBlue.copy(alpha = 0.7f)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) backgroundColor else GoldColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.White,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun FeaturedOfferCard(
    offer: FlightOffer,
    formatter: NumberFormat,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    formatter.currency = java.util.Currency.getInstance(offer.currency)
    val formattedPrice = formatter.format(offer.discountedPrice)

    Card(
        modifier = Modifier
            .width(280.dp)
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
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Featured badge and favorite icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Offer type badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(offer.type.color())
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = offer.type.displayName().uppercase(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Favorite button
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else GoldColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = offer.title,
                color = GoldColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Route
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Departure
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(2f)
                ) {
                    Text(
                        text = offer.departureCode,
                        color = GoldColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = offer.departureCity,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                // Arrow
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = GoldColor,
                    modifier = Modifier
                        .weight(1f)
                        .size(20.dp)
                )

                // Arrival
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(2f)
                ) {
                    Text(
                        text = offer.arrivalCode,
                        color = GoldColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = offer.arrivalCity,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider( color = GoldColor.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            // Price and discount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Discount percentage
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF4CAF50).copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "-${offer.discountPercentage}%",
                        color = Color(0xFF4CAF50),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Price
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = formatter.format(offer.originalPrice),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        text = formattedPrice,
                        color = GoldColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun OfferCard(
    offer: FlightOffer,
    formatter: NumberFormat,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
) {
    formatter.currency = java.util.Currency.getInstance(offer.currency)
    val formattedPrice = formatter.format(offer.discountedPrice)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.7f),
                        GoldColor.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top row with offer type and favorite button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Offer type badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(offer.type.color())
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = offer.type.displayName().uppercase(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Favorite button
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else GoldColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title and description
            Text(
                text = offer.title,
                color = GoldColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = offer.description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Flight route info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Departure
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(2f)
                ) {
                    Text(
                        text = offer.departureCode,
                        color = GoldColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = offer.departureCity,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                // Plane icon or arrow
                Icon(
                    painter = painterResource(R.drawable.plane_path),
                    contentDescription = null,
                    tint = GoldColor,
                    modifier = Modifier
                        .weight(1f)
                        .size(24.dp)
                )

                // Arrival
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(2f)
                ) {
                    Text(
                        text = offer.arrivalCode,
                        color = GoldColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = offer.arrivalCity,
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Airline and valid until
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (offer.airline != null) {
                        Text(
                            text = "Airline: ",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = offer.airline,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Text(
                    text = "Valid until: ${offer.validUntil}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price and discount info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // Promo code if available
                if (offer.promoCode != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .border(
                                width = 1.dp,
                                color = GoldColor.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                append("PROMO: ")
                                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = GoldColor)) {
                                    append(offer.promoCode)
                                }
                            },
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp
                        )
                    }
                } else {
                    // Discount percentage
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF4CAF50).copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "-${offer.discountPercentage}%",
                            color = Color(0xFF4CAF50),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Price
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = formatter.format(offer.originalPrice),
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        text = formattedPrice,
                        color = GoldColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyOffersView(searchQuery: String, selectedType: OfferType?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.plane_path),
            contentDescription = null,
            tint = GoldColor.copy(alpha = 0.5f),
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (searchQuery.isNotEmpty())
                "No matching offers found"
            else if (selectedType != null)
                "No ${selectedType.displayName()} offers available"
            else
                "No offers available at the moment",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (searchQuery.isNotEmpty())
                "Try different search terms or check back later"
            else if (selectedType != null)
                "Try selecting a different category"
            else
                "Please check back soon for new offers",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}
@Preview(showBackground = true)
@Composable
fun OffersScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        OffersScreen(
            navController = rememberNavController()
        )
    }
}