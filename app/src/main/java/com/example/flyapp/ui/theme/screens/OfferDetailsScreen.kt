package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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
fun OfferDetailsScreen(
    navController: NavController,
    offerId: String? = null
) {
    // Finding the offer from the sample data based on ID
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

    val selectedOffer = allOffers.find { it.id == offerId } ?: allOffers.first()
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    formatter.currency = java.util.Currency.getInstance(selectedOffer.currency)

    // Animation states
    var showContent by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

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

    // Content fade-in animation
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            // Top app bar
            FlightTopAppBar(
                textOne = "OFFER",
                textTwo = "DETAILS",
                navController= navController,
            )

            // Scrollable content
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn(tween(800)) + slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = tween(durationMillis = 800)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = 16.dp)
                ) {
                    // Header with offer type
                    OfferTypeHeader(offerType = selectedOffer.type)

                    // Title
                    Text(
                        text = selectedOffer.title,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    // Description
                    Text(
                        text = selectedOffer.description,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
                    )

                    // Flight route visualization
                    FlightRouteCard(offer = selectedOffer)

                    // Price details
                    PriceDetailsCard(offer = selectedOffer, formatter = formatter)

                    // Booking conditions
                    BookingConditionsCard(offer = selectedOffer)

                    // Promo code if available
                    selectedOffer.promoCode?.let {
                        PromoCodeCard(promoCode = it)
                    }

                    // Book now button
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            // Navigate to booking screen
                            navController.navigate(Screen.FlightDetailsScreen.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldColor,
                            contentColor = DarkNavyBlue
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Flight Details",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Bottom padding
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
@Composable
fun OfferTypeHeader(offerType: OfferType) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = offerType.color(),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = offerType.displayName().uppercase(),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Share button
        IconButton(
            onClick = { /* Share functionality */
                // Implement share functionality here
            },
            modifier = Modifier
                .size(36.dp)
                .border(1.dp, GoldColor.copy(alpha = 0.5f), CircleShape)
                .background(DarkNavyBlue.copy(alpha = 0.7f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = GoldColor,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun FlightRouteCard(offer: FlightOffer) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
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
            // Airline name if available
            offer.airline?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Image(
                        painterResource(R.drawable.emirates_logo),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp).clip(
                            RoundedCornerShape(6.dp)
                        )
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = it,
                        color = GoldColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = GoldColor.copy(alpha = 0.3f)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Flight route visualization
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Departure
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = offer.departureCode,
                        color = GoldColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = offer.departureCity,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                // Flight path visualization
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    // Dashed line with plane
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(0.4f),
                             color = GoldColor.copy(alpha = 0.5f)
                        )

                        Icon(
                            painterResource(R.drawable.plane_path),
                            contentDescription = null,
                            tint = GoldColor,
                            modifier = Modifier.size(24.dp)
                        )

                        HorizontalDivider(
                            modifier = Modifier.weight(0.4f),
                            color = GoldColor.copy(alpha = 0.5f)
                        )
                    }

                    // Flight duration (placeholder)
                    Text(
                        text = "Direct Flight",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // Arrival
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = offer.arrivalCode,
                        color = GoldColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = offer.arrivalCity,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Additional flight info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FlightInfoChip(
                    painterResource(R.drawable.date_range),
                    label = "Flexible dates",
                    iconTint = GoldColor
                )

                FlightInfoChip(
                    painterResource(R.drawable.access_time),
                    label = "Quick booking",
                    iconTint = GoldColor
                )
            }
        }
    }
}

@Composable
fun FlightInfoChip(
    icon: Painter,
    label: String,
    iconTint: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF0A2440))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun PriceDetailsCard(
    offer: FlightOffer,
    formatter: NumberFormat
) {
    val formattedOriginalPrice = formatter.format(offer.originalPrice)
    val formattedDiscountedPrice = formatter.format(offer.discountedPrice)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.creditcard),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "PRICE DETAILS",
                    color = GoldColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Original price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Original Price",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
                Text(
                    text = formattedOriginalPrice,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.LineThrough
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Discount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Discount (${offer.discountPercentage}%)",
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp
                )
                Text(
                    text = "-${formatter.format(offer.originalPrice - offer.discountedPrice)}",
                    color = Color(0xFF4CAF50),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider( thickness = 1.dp, color = GoldColor.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(8.dp))

            // Final price
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Price",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formattedDiscountedPrice,
                    color = GoldColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Price per person note
            Text(
                text = "Price is per person, including all taxes and fees",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                textAlign = TextAlign.End
            )
        }
    }
}

@Composable
fun BookingConditionsCard(offer: FlightOffer) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = GoldColor,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "BOOKING CONDITIONS",
                    color = GoldColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Conditions list
            ConditionItem(
                label = "Offer valid until",
                value = offer.validUntil
            )

            Spacer(modifier = Modifier.height(8.dp))

            ConditionItem(
                label = "Offer type",
                value = offer.type.displayName()
            )

            Spacer(modifier = Modifier.height(8.dp))

            ConditionItem(
                label = "Airline",
                value = offer.airline ?: "Multiple airlines"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ConditionItem(
                label = "Baggage allowance",
                value = "Standard (1 checked item, 1 carry-on)"
            )

            Spacer(modifier = Modifier.height(8.dp))

            ConditionItem(
                label = "Cancellation",
                value = "Flexible - Fee applies"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Additional notes
            Text(
                text = "Please review all terms before booking. Fares are subject to availability and may change without notice.",
                color = Color.White.copy(alpha = 0.6f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun ConditionItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PromoCodeCard(promoCode: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
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
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "EXCLUSIVE PROMO CODE",
                color = GoldColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MediumBlue)
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
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = GoldColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                letterSpacing = 4.sp
                            )
                        ) {
                            append(promoCode)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Apply this code during checkout to claim your special offer",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun OfferDetailsScreenPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        OfferDetailsScreen(
            navController = rememberNavController(),
            offerId = "FLASH001"
        )
    }
}