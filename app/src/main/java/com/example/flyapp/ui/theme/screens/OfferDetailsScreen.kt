package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.ParticleEffectBackground
import com.example.flyapp.ui.theme.utils.formatDate
import kotlinx.coroutines.delay

// Data class for included items in the offer package
data class OfferIncludedItem(
    val title: String,
    val description: String,
    val icon: @Composable () -> Unit
)

// Data class for offer highlights
data class OfferHighlight(
    val title: String,
    val description: String
)

// Data for offer details
data class OfferDetails(
    val offer: TravelOffer,
    val includedItems: List<OfferIncludedItem>,
    val highlights: List<OfferHighlight>,
    val recommendedDates: List<String>,
    val termsAndConditions: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferDetailsScreen(
    navController: NavHostController,
    offerId: String
) {
    // Dummy data retrieval - in a real app, this would come from ViewModel/Repository
    val offerDetails = getSampleOfferDetails(offerId)
    if (offerDetails == null) {
        // Show error or navigate back if offer not found
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Offer not found", color = Color.White)
        }
        return
    }

    val offer = offerDetails.offer

    // Animation states
    var showContent by remember { mutableStateOf(false) }
    val contentAlpha by animateFloatAsState(
        targetValue = if (showContent) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "content_alpha"
    )

    // Trigger content animation
    LaunchedEffect(key1 = true) {
        delay(300)
        showContent = true
    }

    // Scroll state
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF001034),
                        Color(0xFF003045),
                        Color(0xFF004D40)
                    )
                )
            )
    ) {
        // Enhanced background animations
        ParticleEffectBackground()

        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Offer Details",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Share offer */ }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { /* Add to favorites */ }) {
                            Icon(
                                imageVector = Icons.Rounded.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            floatingActionButton = {
                BookNowFAB(offer = offer)
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
                    .alpha(contentAlpha)
            ) {
                // Header image
                HeaderSection(offer)

                // Main content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Title and location
                    Text(
                        text = offer.title,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${offer.destination}, ${offer.country}",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Rating
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = offer.rating.toString(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // Price section
                    PriceSection(offer)

                    // Valid until date
                    ValiditySection(offer)

                    // Description
                    Text(
                        text = "About this offer",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                    )

                    Text(
                        text = offer.description,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    )

                    // What's included section
                    IncludedItemsSection(offerDetails.includedItems)

                    // Highlights Section
                    HighlightsSection(offerDetails.highlights)

                    // Recommended dates
                    RecommendedDatesSection(offerDetails.recommendedDates)

                    // Feature tags
                    FeaturesSection(offer.features)

                    // Terms and conditions
                    TermsSection(offerDetails.termsAndConditions)

                    // Book now button
                    BookingButton(offer)

                    // Bottom spacer for FAB
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun HeaderSection(offer: TravelOffer) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        // Image
        Image(
            painter = painterResource(id = R.drawable.plane_ticket), // Replace with actual image
            contentDescription = offer.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x80000000),
                            Color(0xCC000000)
                        )
                    )
                )
        )

        // Discount badge
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart),
            color = Color(0xFFE91E63),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "${offer.discountPercentage}% OFF",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        // Limited time badge if applicable
        if (offer.limitedTime) {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd),
                color = Color(0xFFFF9800),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(R.drawable.filter_ic),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (offer.availableSeats != null) "${offer.availableSeats} seats left" else "Limited time",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun PriceSection(offer: TravelOffer) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A2432).copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Special Price",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "$${offer.discountedPrice}",
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "$${offer.originalPrice}",
                        color = Color.White.copy(alpha = 0.6f),
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            Surface(
                color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "You save: $${offer.originalPrice - offer.discountedPrice}",
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun ValiditySection(offer: TravelOffer) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFFF9800).copy(alpha = 0.2f),
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                   painterResource(R.drawable.date_range),
                    contentDescription = null,
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = "Offer Valid Until",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Text(
                text = formatDate(offer.validUntil),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun IncludedItemsSection(includedItems: List<OfferIncludedItem>) {
    Text(
        text = "What's Included",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A2432).copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            includedItems.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF4CAF50).copy(alpha = 0.2f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            item.icon()
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = item.title,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.description,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }

                if (index < includedItems.size - 1) {
                    Divider(
                        color = Color.White.copy(alpha = 0.1f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun HighlightsSection(highlights: List<OfferHighlight>) {
    Text(
        text = "Highlights",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        highlights.forEach { highlight ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF03A9F4).copy(alpha = 0.2f),
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF03A9F4),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = highlight.title,
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    if (highlight.description.isNotEmpty()) {
                        Text(
                            text = highlight.description,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendedDatesSection(recommendedDates: List<String>) {
    Text(
        text = "Recommended Travel Dates",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(recommendedDates) { date ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0A2432).copy(alpha = 0.8f)
                ),
                border = BorderStroke(1.dp, Color(0xFF4CAF50).copy(alpha = 0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painterResource(R.drawable.date_range),
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = date,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturesSection(features: List<DestinationFeature>) {
    Text(
        text = "Features",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(features) { feature ->
            FeatureChip(feature = feature)
        }
    }
}

@Composable
fun TermsSection(terms: String) {
    Text(
        text = "Terms & Conditions",
        color = Color.White,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A2432).copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = terms,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun BookingButton(offer: TravelOffer) {
    Button(
        onClick = { /* Book now */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Book This Offer",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BookNowFAB(offer: TravelOffer) {
    FloatingActionButton(
        onClick = { /* Quick booking */ },
        containerColor = Color(0xFF4CAF50),
        contentColor = Color.White
    ) {
        Icon(
            painterResource(R.drawable.plane_path),
            contentDescription = "Book Now"
        )
    }
}

// Helper function to get sample offer details
private fun getSampleOfferDetails(offerId: String): OfferDetails? {
    // This would normally come from a repository or API
    val sampleOffer = when (offerId) {
        "paris_spring" -> TravelOffer(
            id = "paris_spring",
            title = "Spring in Paris",
            destination = "Paris",
            country = "France",
            description = "Experience the magic of Paris in spring with our special discount package. Stroll along the Seine River, visit world-famous museums like the Louvre and Musée d'Orsay, and enjoy authentic French cuisine at charming cafés. This package includes direct flights, 5-night accommodation at a boutique hotel in central Paris, daily breakfast, a Seine river cruise, and a guided tour of the city's highlights. Perfect for couples and cultural enthusiasts.",
            imageResId = R.drawable.paris,
            originalPrice = 1299.99,
            discountedPrice = 899.99,
            discountPercentage = 30,
            rating = 4.7f,
            validUntil = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000), // Valid for 7 days
            features = listOf(DestinationFeature.CITY, DestinationFeature.HISTORIC, DestinationFeature.FOOD),
            limitedTime = true,
            availableSeats = 8
        )
        "bali_retreat" -> TravelOffer(
            id = "bali_retreat",
            title = "Bali Wellness Retreat",
            destination = "Bali",
            country = "Indonesia",
            description = "Rejuvenate your mind and body with our all-inclusive wellness retreat in beautiful Bali. Located amidst lush rice terraces and tropical forests, our partner resort offers the perfect setting for relaxation and self-discovery. The package includes return flights, 7 nights at a luxury wellness resort, three organic meals daily, daily yoga and meditation sessions, two traditional Balinese spa treatments, and a guided tour to ancient temples and local villages.",
            imageResId = R.drawable.emirates_logo,
            originalPrice = 1799.99,
            discountedPrice = 1399.99,
            discountPercentage = 22,
            rating = 4.9f,
            validUntil = System.currentTimeMillis() + (14 * 24 * 60 * 60 * 1000), // Valid for 14 days
            features = listOf(DestinationFeature.BEACH, DestinationFeature.RELAXATION),
            limitedTime = true,
            availableSeats = 5
        )
        else -> null
    }

    if (sampleOffer == null) return null

    // Return offer details
    return OfferDetails(
        offer = sampleOffer,
        includedItems = listOf(
            OfferIncludedItem(
                title = "Return Flights",
                description = "Direct flights from your nearest international airport with 23kg checked baggage allowance.",
                icon = {
                    Icon(
                        painterResource(R.drawable.plane_path),
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                }
            ),
            OfferIncludedItem(
                title = "Premium Accommodation",
                description = when (sampleOffer.id) {
                    "paris_spring" -> "5 nights at a boutique hotel in central Paris, within walking distance to major attractions."
                    "bali_retreat" -> "7 nights at a luxury wellness resort with private pool villa."
                    else -> "Luxury accommodation at our partner hotels and resorts."
                },
                icon = {
                    Icon(
                        painterResource(R.drawable.hotel),
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                }
            ),
            OfferIncludedItem(
                title = "Exclusive Experiences",
                description = when (sampleOffer.id) {
                    "paris_spring" -> "Seine river cruise and guided city tour with skip-the-line access to major attractions."
                    "bali_retreat" -> "Daily yoga and meditation sessions, two traditional Balinese spa treatments."
                    else -> "Curated local experiences designed to immerse you in the destination."
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        ),
        highlights = listOf(
            OfferHighlight(
                title = "Exclusive ${sampleOffer.discountPercentage}% discount for limited time",
                description = ""
            ),
            OfferHighlight(
                title = when (sampleOffer.id) {
                    "paris_spring" -> "Central location near all major Paris attractions"
                    "bali_retreat" -> "Award-winning wellness resort in tranquil setting"
                    else -> "Premium accommodation at top-rated properties"
                },
                description = ""
            ),
            OfferHighlight(
                title = "Flexible booking policy",
                description = "Free cancellation up to 30 days before departure"
            ),
            OfferHighlight(
                title = "Personalized itinerary assistance",
                description = "Our travel experts will help you plan the perfect trip"
            )
        ),
        recommendedDates = when (sampleOffer.id) {
            "paris_spring" -> listOf("Apr 15-20", "May 3-8", "May 17-22", "Jun 7-12")
            "bali_retreat" -> listOf("May 10-17", "Jun 5-12", "Jun 19-26", "Jul 8-15")
            else -> listOf("Please contact us")
        },
        termsAndConditions = "Offer valid for bookings made before ${formatDate(sampleOffer.validUntil)}. Prices are per person based on double occupancy. Single supplement applies. Subject to availability. Payment in full required at time of booking. Cancellation policy applies. Flights are non-refundable once booked. Travel insurance recommended and not included in package price."
    )
}

// Preview function for the screen (optional)
@Composable
@androidx.compose.ui.tooling.preview.Preview
fun PreviewOfferDetailsScreen() {
    val navController = rememberNavController()
    OfferDetailsScreen(navController = navController, offerId = "paris_spring")
}


