package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.example.flyapp.ui.theme.components.FlightTopAppBar
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.theme.DarkNavyBlue
import com.example.flyapp.ui.theme.theme.DeepBlue
import com.example.flyapp.ui.theme.theme.GoldColor
import com.example.flyapp.ui.theme.theme.MediumBlue
import java.text.NumberFormat
import java.util.Locale

// Data model for offers
data class FlightOffer(
    val id: String,
    val type: OfferType,
    val title: String,
    val description: String,
    val departureCity: String,
    val departureCode: String,
    val arrivalCity: String,
    val arrivalCode: String,
    val originalPrice: Double,
    val discountedPrice: Double,
    val currency: String = "USD",
    val validUntil: String,
    val promoCode: String? = null,
    val discountPercentage: Int,
    val isFeatured: Boolean = false,
    val rating: Float = 0f,
    val airline: String? = null
)

enum class OfferType {
    FLASH_SALE,
    SEASONAL,
    LAST_MINUTE,
    FREQUENT_FLYER,
    BUNDLE;

    fun displayName(): String {
        return when (this) {
            FLASH_SALE -> "Flash Sale"
            SEASONAL -> "Seasonal"
            LAST_MINUTE -> "Last Minute"
            FREQUENT_FLYER -> "Frequent Flyer"
            BUNDLE -> "Bundle Deal"
        }
    }

    fun color(): Color {
        return when (this) {
            FLASH_SALE -> Color(0xFFE53935)
            SEASONAL -> Color(0xFF43A047)
            LAST_MINUTE -> Color(0xFFFF9800)
            FREQUENT_FLYER -> Color(0xFF2196F3)
            BUNDLE -> Color(0xFF9C27B0)
        }
    }
}

@Composable
fun AllOffersScreen(
    navController: NavController
) {
    // Tab selection state
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("All Offers", "Flash Sale", "Seasonal", "Last Minute", "Frequent Flyer", "Bundle")

    // Sample offers data
    val allOffers = remember {
        mutableStateOf(
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
                    id = "FLASH002",
                    type = OfferType.FLASH_SALE,
                    title = "24-Hour Sale: San Francisco to Vancouver",
                    description = "Ultra-low fares for this popular route",
                    departureCity = "San Francisco",
                    departureCode = "SFO",
                    arrivalCity = "Vancouver",
                    arrivalCode = "YVR",
                    originalPrice = 349.99,
                    discountedPrice = 229.99,
                    validUntil = "May 1, 2025",
                    discountPercentage = 34,
                    airline = "Pacific Air"
                ),
                FlightOffer(
                    id = "SEAS002",
                    type = OfferType.SEASONAL,
                    title = "Summer Special: Miami to Cancun",
                    description = "Start your summer early with these special rates",
                    departureCity = "Miami",
                    departureCode = "MIA",
                    arrivalCity = "Cancun",
                    arrivalCode = "CUN",
                    originalPrice = 399.99,
                    discountedPrice = 299.99,
                    validUntil = "May 30, 2025",
                    discountPercentage = 25,
                    promoCode = "SUMMER25",
                    isFeatured = true,
                    airline = "Tropical Wings"
                ),
                FlightOffer(
                    id = "LAST002",
                    type = OfferType.LAST_MINUTE,
                    title = "Last Call: Dallas to Chicago",
                    description = "Final seats available at discounted rates",
                    departureCity = "Dallas",
                    departureCode = "DFW",
                    arrivalCity = "Chicago",
                    arrivalCode = "ORD",
                    originalPrice = 299.99,
                    discountedPrice = 199.99,
                    validUntil = "May 2, 2025",
                    discountPercentage = 33,
                    airline = "Central Connect"
                ),
                FlightOffer(
                    id = "FREQ002",
                    type = OfferType.FREQUENT_FLYER,
                    title = "Triple Miles: NYC to Rome",
                    description = "Limited time offer to earn triple miles",
                    departureCity = "New York",
                    departureCode = "JFK",
                    arrivalCity = "Rome",
                    arrivalCode = "FCO",
                    originalPrice = 899.99,
                    discountedPrice = 799.99,
                    validUntil = "May 20, 2025",
                    discountPercentage = 11,
                    promoCode = "TRIPLE3",
                    airline = "Atlantic Air"
                ),
                FlightOffer(
                    id = "BUND002",
                    type = OfferType.BUNDLE,
                    title = "Flight + Car: Los Angeles to Denver",
                    description = "Explore the mountains with flight and SUV rental",
                    departureCity = "Los Angeles",
                    departureCode = "LAX",
                    arrivalCity = "Denver",
                    arrivalCode = "DEN",
                    originalPrice = 599.99,
                    discountedPrice = 479.99,
                    validUntil = "May 25, 2025",
                    discountPercentage = 20,
                    promoCode = "CARFREE",
                    airline = "Mountain Express"
                )
            )
        )
    }

    // Filter offers based on selected tab
    val displayedOffers = when (selectedTab) {
        0 -> allOffers.value
        1 -> allOffers.value.filter { it.type == OfferType.FLASH_SALE }
        2 -> allOffers.value.filter { it.type == OfferType.SEASONAL }
        3 -> allOffers.value.filter { it.type == OfferType.LAST_MINUTE }
        4 -> allOffers.value.filter { it.type == OfferType.FREQUENT_FLYER }
        5 -> allOffers.value.filter { it.type == OfferType.BUNDLE }
        else -> allOffers.value
    }

    // Animation for the background effect (matching existing screens)
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
            modifier = Modifier.fillMaxSize()
        ) {
            // Top app bar
            FlightTopAppBar(
                textOne = "ALL ",
                textTwo = "OFFERS",
                navController = navController)

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Limited time flight deals",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

            // Category tabs
            CustomTabRow(
                selectedTabIndex = selectedTab,
                tabs = tabs,
                onTabSelected = { selectedTab = it }
            )

            // List of offers
            if (displayedOffers.isEmpty()) {
                EmptyOffersView(tabName = tabs[selectedTab])
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 16.dp)
                ) {
                    items(displayedOffers) { offer ->
                        OfferCard(
                            offer = offer,
                            onOfferSelected = {
                                // Navigate to offer details or directly to booking
                                navController.navigate("${Screen.SearchResultsScreen.route}?offer=${offer.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomTabRow(
    selectedTabIndex: Int,
    tabs: List<String>,
    onTabSelected: (Int) -> Unit
) {
    // Create a scrollable tab row with custom styling
    Column {
        androidx.compose.foundation.lazy.LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
        ) {
            items(tabs.size) { index ->
                val isSelected = selectedTabIndex == index
                Box(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            color = if (isSelected) GoldColor else DarkNavyBlue.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(50)
                        )
                        .border(
                            width = 1.dp,
                            color = GoldColor.copy(alpha = if (isSelected) 0.9f else 0.5f),
                            shape = RoundedCornerShape(50)
                        )
                        .clickable { onTabSelected(index) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = tabs[index].uppercase(),
                        color = if (isSelected) DarkNavyBlue else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Divider under tabs
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = GoldColor.copy(alpha = 0.3f)
        )
    }
}


@Composable
fun OfferCard(
    offer: FlightOffer,
    onOfferSelected: () -> Unit
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    formatter.currency = java.util.Currency.getInstance(offer.currency)
    val formattedOriginalPrice = formatter.format(offer.originalPrice)
    val formattedDiscountedPrice = formatter.format(offer.discountedPrice)

    Card(
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
            )
            .clickable { onOfferSelected() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Offer type badge and title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Offer badge
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

                // Valid until date
                Text(
                    text = "Valid until ${offer.validUntil}",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            // Offer title
            Text(
                text = offer.title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Offer description
            Text(
                text = offer.description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Flight route and airline
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Route
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = offer.departureCode,
                            color = GoldColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = offer.departureCity,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "To",
                        tint = GoldColor,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .size(16.dp)
                    )

                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = offer.arrivalCode,
                            color = GoldColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Text(
                            text = offer.arrivalCity,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }

                // Airline if available
                offer.airline?.let {
                    Text(
                        text = it,
                        color = GoldColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Divider(color = GoldColor.copy(alpha = 0.3f), thickness = 1.dp)

            // Price and discount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Discount percentage
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.Red.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${offer.discountPercentage}% OFF",
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Price
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = formattedOriginalPrice,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 14.sp,
                        textDecoration = TextDecoration.LineThrough
                    )

                    Text(
                        text = formattedDiscountedPrice,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Promo code if available
            offer.promoCode?.let {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Use code: ",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )

                    Text(
                        text = it,
                        color = GoldColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyOffersView(tabName: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "No Offers",
            tint = GoldColor.copy(alpha = 0.7f),
            modifier = Modifier.size(64.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No $tabName Available",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Check back soon for new offers",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AllOffersScreenPreview() {
    MaterialTheme {
        Surface {
            AllOffersScreen(rememberNavController())
        }
    }
}
//package com.example.flyapp.ui.theme.screens
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.tween
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.slideInVertically
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Info
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.icons.filled.Star
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.alpha
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import com.example.flyapp.R
//import com.example.flyapp.ui.theme.components.ParticleEffectBackground
//import com.example.flyapp.ui.theme.utils.formatDate
//import kotlinx.coroutines.delay
//
//// Data class for travel offers
//data class TravelOffer(
//    val id: String,
//    val title: String,
//    val destination: String,
//    val country: String,
//    val description: String,
//    val imageResId: Int,
//    val originalPrice: Double,
//    val discountedPrice: Double,
//    val discountPercentage: Int,
//    val rating: Float,
//    val validUntil: Long, // Timestamp
//    val features: List<DestinationFeature>,
//    val limitedTime: Boolean = false,
//    val availableSeats: Int? = null
//)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AllOffersScreen(navController: NavHostController) {
//    // Sample offers data
//    val offers = remember {
//        listOf(
//            TravelOffer(
//                id = "paris_spring",
//                title = "Spring in Paris",
//                destination = "Paris",
//                country = "France",
//                description = "Experience the magic of Paris in spring with our special discount package including flights, 5-night hotel stay, and Seine river cruise.",
//                imageResId = R.drawable.paris,
//                originalPrice = 1299.99,
//                discountedPrice = 899.99,
//                discountPercentage = 30,
//                rating = 4.7f,
//                validUntil = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000), // Valid for 7 days
//                features = listOf(DestinationFeature.CITY, DestinationFeature.HISTORIC, DestinationFeature.FOOD),
//                limitedTime = true,
//                availableSeats = 8
//            ),
//            TravelOffer(
//                id = "bali_retreat",
//                title = "Bali Wellness Retreat",
//                destination = "Bali",
//                country = "Indonesia",
//                description = "Rejuvenate your mind and body with our all-inclusive wellness retreat in beautiful Bali. Includes yoga sessions, spa treatments, and guided meditation.",
//                imageResId = R.drawable.emirates_logo,
//                originalPrice = 1799.99,
//                discountedPrice = 1399.99,
//                discountPercentage = 22,
//                rating = 4.9f,
//                validUntil = System.currentTimeMillis() + (14 * 24 * 60 * 60 * 1000), // Valid for 14 days
//                features = listOf(DestinationFeature.BEACH, DestinationFeature.RELAXATION),
//                limitedTime = true,
//                availableSeats = 5
//            ),
//            TravelOffer(
//                id = "tokyo_adventure",
//                title = "Tokyo Cultural Experience",
//                destination = "Tokyo",
//                country = "Japan",
//                description = "Immerse yourself in Japanese culture with our Tokyo package. Includes traditional tea ceremony, sushi making class, and guided tours of historic temples.",
//                imageResId = R.drawable.tokyo,
//                originalPrice = 1599.99,
//                discountedPrice = 1249.99,
//                discountPercentage = 20,
//                rating = 4.8f,
//                validUntil = System.currentTimeMillis() + (21 * 24 * 60 * 60 * 1000), // Valid for 21 days
//                features = listOf(DestinationFeature.CITY, DestinationFeature.FOOD, DestinationFeature.HISTORIC)
//            ),
//            TravelOffer(
//                id = "santorini_summer",
//                title = "Santorini Summer Escape",
//                destination = "Santorini",
//                country = "Greece",
//                description = "Enjoy stunning sunsets and crystal-clear waters with our luxury Santorini package. Includes private villa accommodation with infinity pool access.",
//                imageResId = R.drawable.emirates_logo,
//                originalPrice = 1899.99,
//                discountedPrice = 1499.99,
//                discountPercentage = 21,
//                rating = 4.9f,
//                validUntil = System.currentTimeMillis() + (10 * 24 * 60 * 60 * 1000), // Valid for 10 days
//                features = listOf(DestinationFeature.BEACH, DestinationFeature.RELAXATION, DestinationFeature.FOOD),
//                limitedTime = true,
//                availableSeats = 3
//            ),
//            TravelOffer(
//                id = "nyc_weekend",
//                title = "New York City Weekend",
//                destination = "New York",
//                country = "United States",
//                description = "Experience the excitement of NYC with our weekend getaway package. Broadway show tickets and guided tour of top attractions included.",
//                imageResId = R.drawable.new_york,
//                originalPrice = 1299.99,
//                discountedPrice = 999.99,
//                discountPercentage = 25,
//                rating = 4.6f,
//                validUntil = System.currentTimeMillis() + (14 * 24 * 60 * 60 * 1000), // Valid for 14 days
//                features = listOf(DestinationFeature.CITY, DestinationFeature.NIGHTLIFE, DestinationFeature.SHOPPING)
//            ),
//            TravelOffer(
//                id = "rome_culture",
//                title = "Italian Heritage Tour",
//                destination = "Rome",
//                country = "Italy",
//                description = "Discover the rich history and culinary delights of Rome with our special cultural tour package with skip-the-line access to major attractions.",
//                imageResId = R.drawable.emirates_logo,
//                originalPrice = 1099.99,
//                discountedPrice = 849.99,
//                discountPercentage = 23,
//                rating = 4.8f,
//                validUntil = System.currentTimeMillis() + (21 * 24 * 60 * 60 * 1000), // Valid for 21 days
//                features = listOf(DestinationFeature.HISTORIC, DestinationFeature.FOOD, DestinationFeature.CITY)
//            ),
//            TravelOffer(
//                id = "machu_picchu_trek",
//                title = "Machu Picchu Adventure",
//                destination = "Machu Picchu",
//                country = "Peru",
//                description = "Embark on an unforgettable journey to the ancient Incan citadel with our guided trekking package including accommodations and expert guides.",
//                imageResId = R.drawable.emirates_logo,
//                originalPrice = 1899.99,
//                discountedPrice = 1399.99,
//                discountPercentage = 26,
//                rating = 4.9f,
//                validUntil = System.currentTimeMillis() + (30 * 24 * 60 * 60 * 1000), // Valid for 30 days
//                features = listOf(DestinationFeature.HISTORIC, DestinationFeature.ADVENTURE, DestinationFeature.MOUNTAIN),
//                limitedTime = true,
//                availableSeats = 10
//            )
//        )
//    }
//
//    var searchQuery by remember { mutableStateOf("") }
//    var isSearchActive by remember { mutableStateOf(false) }
//    var filteredOffers by remember { mutableStateOf(offers) }
//
//    // Animation states
//    var showContent by remember { mutableStateOf(false) }
//    val contentAlpha by animateFloatAsState(
//        targetValue = if (showContent) 1f else 0f,
//        animationSpec = tween(durationMillis = 800),
//        label = "content_alpha"
//    )
//
//    // Filter offers based on search query
//    LaunchedEffect(searchQuery) {
//        filteredOffers = if (searchQuery.isEmpty()) {
//            offers
//        } else {
//            offers.filter {
//                it.title.contains(searchQuery, ignoreCase = true) ||
//                        it.destination.contains(searchQuery, ignoreCase = true) ||
//                        it.country.contains(searchQuery, ignoreCase = true) ||
//                        it.description.contains(searchQuery, ignoreCase = true)
//            }
//        }
//    }
//
//    // Trigger content animation
//    LaunchedEffect(key1 = true) {
//        delay(300)
//        showContent = true
//    }
//
//    // Scroll state
//    val lazyListState = rememberLazyListState()
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(
//                brush = Brush.verticalGradient(
//                    colors = listOf(
//                        Color(0xFF001034),
//                        Color(0xFF003045),
//                        Color(0xFF004D40)
//                    )
//                )
//            )
//    ) {
//        // Enhanced background animations
//        ParticleEffectBackground()
//
//        Scaffold(
//            containerColor = Color.Transparent,
//            topBar = {
//                TopAppBar(
//                    title = {
//                        if (!isSearchActive) {
//                            Text(
//                                text = "Special Offers",
//                                style = MaterialTheme.typography.titleMedium.copy(
//                                    fontWeight = FontWeight.Bold,
//                                    color = Color.White
//                                )
//                            )
//                        } else {
//                            TextField(
//                                value = searchQuery,
//                                onValueChange = { searchQuery = it },
//                                placeholder = {
//                                    Text(
//                                        "Search offers...",
//                                        color = Color.White.copy(alpha = 0.6f)
//                                    )
//                                },
//                                modifier = Modifier.fillMaxWidth(),
//                                singleLine = true,
//                                colors = TextFieldDefaults.colors(
//                                    focusedContainerColor = Color.Transparent,
//                                    unfocusedContainerColor = Color.Transparent,
//                                    disabledContainerColor = Color.Transparent,
//                                    focusedIndicatorColor = Color.White,
//                                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
//                                    focusedTextColor = Color.White,
//                                    unfocusedTextColor = Color.White
//                                )
//                            )
//                        }
//                    },
//                    navigationIcon = {
//                        IconButton(onClick = { navController.popBackStack() }) {
//                            Icon(
//                                imageVector = Icons.Default.ArrowBack,
//                                contentDescription = "Back",
//                                tint = Color.White
//                            )
//                        }
//                    },
//                    actions = {
//                        IconButton(
//                            onClick = { isSearchActive = !isSearchActive }
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.Search,
//                                contentDescription = "Search",
//                                tint = Color.White
//                            )
//                        }
//                        IconButton(onClick = { /* Show filters */ }) {
//                            Icon(
//                                painterResource(R.drawable.filter_ic),
//                                contentDescription = "Filter",
//                                tint = Color.White
//                            )
//                        }
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = Color.Transparent
//                    )
//                )
//            },
//        ) { paddingValues ->
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(paddingValues)
//                    .alpha(contentAlpha)
//            ) {
//                if (filteredOffers.isEmpty()) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(32.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally
//                        ) {
//                            Icon(
//                                painter = painterResource(R.drawable.plane_ticket),
//                                contentDescription = null,
//                                modifier = Modifier.size(64.dp),
//                                tint = Color.White.copy(alpha = 0.7f)
//                            )
//                            Spacer(modifier = Modifier.height(16.dp))
//                            Text(
//                                text = "No offers found",
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = Color.White,
//                                textAlign = TextAlign.Center
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = "Try adjusting your search or filters",
//                                fontSize = 16.sp,
//                                color = Color.White.copy(alpha = 0.7f),
//                                textAlign = TextAlign.Center
//                            )
//                        }
//                    }
//                } else {
//                    LazyColumn(
//                        state = lazyListState,
//                        contentPadding = PaddingValues(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        items(filteredOffers) { offer ->
//                            AnimatedVisibility(
//                                visible = showContent,
//                                enter = fadeIn(tween(800)) + slideInVertically(
//                                    initialOffsetY = { it / 2 },
//                                    animationSpec = tween(durationMillis = 800)
//                                )
//                            ) {
//                                OfferCard(
//                                    offer = offer,
//                                    onOfferClick = {
//                                        // Navigate to offer details
//                                        navController.navigate("offer_details/${offer.id}")
//                                    }
//                                )
//                            }
//                        }
//                        // Add some space at the bottom for FAB
//                        item { Spacer(modifier = Modifier.height(80.dp)) }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun OfferCard(
//    offer: TravelOffer,
//    onOfferClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(onClick = onOfferClick)
//            .shadow(8.dp, RoundedCornerShape(16.dp)),
//        shape = RoundedCornerShape(16.dp),
//        colors = CardDefaults.cardColors(
//            containerColor = Color(0xFF1A3546).copy(alpha = 0.9f)
//        )
//    ) {
//        Column(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            // Image with overlay
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(180.dp)
//            ) {
//                // Replace with actual image loading from resources
//                Image(
//                    painter = painterResource(id = R.drawable.plane_ticket), // Placeholder image
//                    contentDescription = offer.title,
//                    contentScale = ContentScale.Crop,
//                    modifier = Modifier.fillMaxSize()
//                )
//
//                // Gradient overlay
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(
//                            brush = Brush.verticalGradient(
//                                colors = listOf(
//                                    Color.Transparent,
//                                    Color(0xFF1A3546).copy(alpha = 0.7f)
//                                )
//                            )
//                        )
//                )
//
//                // Discount badge and countdown
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp)
//                ) {
//                    // Discount badge
//                    Surface(
//                        modifier = Modifier
//                            .align(Alignment.TopStart),
//                        color = Color(0xFFE91E63),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Text(
//                            text = "${offer.discountPercentage}% OFF",
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold,
//                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
//                        )
//                    }
//
//                    // Limited time badge if applicable
//                    if (offer.limitedTime) {
//                        Surface(
//                            modifier = Modifier
//                                .align(Alignment.TopEnd),
//                            color = Color(0xFFFF9800),
//                            shape = RoundedCornerShape(8.dp)
//                        ) {
//                            Row(
//                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Icon(
//                                    imageVector = Icons.Default.Info,
//                                    contentDescription = null,
//                                    tint = Color.White,
//                                    modifier = Modifier.size(16.dp)
//                                )
//                                Spacer(modifier = Modifier.width(4.dp))
//                                Text(
//                                    text = if (offer.availableSeats != null) "${offer.availableSeats} seats left" else "Limited time",
//                                    color = Color.White,
//                                    fontSize = 12.sp,
//                                    fontWeight = FontWeight.Medium
//                                )
//                            }
//                        }
//                    }
//
//                    // Title and location
//                    Column(
//                        modifier = Modifier
//                            .align(Alignment.BottomStart)
//                    ) {
//                        Text(
//                            text = offer.title,
//                            color = Color.White,
//                            fontSize = 24.sp,
//                            fontWeight = FontWeight.Bold
//                        )
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Icon(
//                                imageVector = Icons.Default.LocationOn,
//                                contentDescription = null,
//                                tint = Color.White.copy(alpha = 0.7f),
//                                modifier = Modifier.size(16.dp)
//                            )
//                            Spacer(modifier = Modifier.width(4.dp))
//                            Text(
//                                text = "${offer.destination}, ${offer.country}",
//                                color = Color.White.copy(alpha = 0.7f),
//                                fontSize = 14.sp
//                            )
//                        }
//                    }
//                }
//            }
//
//            // Offer details
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp)
//            ) {
//                // Description
//                Text(
//                    text = offer.description,
//                    color = Color.White.copy(alpha = 0.8f),
//                    fontSize = 14.sp,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                // Price information
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    // Discounted price
//                    Text(
//                        text = "$${offer.discountedPrice}",
//                        color = Color(0xFF4CAF50),
//                        fontWeight = FontWeight.Bold,
//                        fontSize = 20.sp
//                    )
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    // Original price (crossed out)
//                    Text(
//                        text = "$${offer.originalPrice}",
//                        color = Color.White.copy(alpha = 0.6f),
//                        textDecoration = TextDecoration.LineThrough,
//                        fontSize = 14.sp
//                    )
//
//                    Spacer(modifier = Modifier.weight(1f))
//
//                    // Rating
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Star,
//                            contentDescription = null,
//                            tint = Color(0xFFFFD700),
//                            modifier = Modifier.size(20.dp)
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text(
//                            text = offer.rating.toString(),
//                            color = Color.White,
//                            fontWeight = FontWeight.Bold,
//                            fontSize = 16.sp
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // Valid until date
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        painterResource(R.drawable.date_range),
//                        contentDescription = null,
//                        tint = Color.White.copy(alpha = 0.7f),
//                        modifier = Modifier.size(16.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        text = "Valid until: ${formatDate(offer.validUntil)}",
//                        color = Color.White.copy(alpha = 0.7f),
//                        fontSize = 12.sp
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//
//                // Features
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    offer.features.take(3).forEach { feature ->
//                        FeatureChip(feature = feature)
//                    }
//                }
//            }
//        }
//    }
//}
//@Preview(showBackground = true)
//@Composable
//fun AllOffersScreenPreview() {
//    AllOffersScreen(navController = rememberNavController())
//}