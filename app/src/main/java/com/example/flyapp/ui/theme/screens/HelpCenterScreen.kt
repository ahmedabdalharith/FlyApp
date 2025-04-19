package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.ParticleEffectBackground

data class HelpCategory(
    val title: String,
    val icon: Int,
    val faqs: List<FAQ>
)

data class FAQ(
    val question: String,
    val answer: String
)

@Composable
fun HelpCenterScreen(navController: NavHostController) {
    // Sample help categories and FAQs
    val helpCategories = listOf(
        HelpCategory(
            title = "Booking & Reservations",
            icon = R.drawable.plane_ticket,
            faqs = listOf(
                FAQ(
                    question = "How do I change my flight date?",
                    answer = "To change your flight date, go to 'My Bookings' section and select the flight you wish to modify. Click on 'Change Flight' and follow the instructions. Please note that changes may incur fees depending on your ticket type and how close to departure you are making the change."
                ),
                FAQ(
                    question = "What is your cancellation policy?",
                    answer = "Our cancellation policy varies based on the fare type you've purchased. Economy tickets typically allow cancellations up to 24 hours before departure with a fee. Refundable tickets can be cancelled anytime for a full refund. Please check your specific ticket terms in the booking confirmation email."
                ),
                FAQ(
                    question = "Can I book tickets for someone else?",
                    answer = "Yes, you can book tickets for other passengers. During the booking process, you'll be asked to enter passenger details. Make sure to provide accurate information for all travelers as it appears on their identification documents."
                )
            )
        ),
        HelpCategory(
            title = "Check-in & Boarding",
            icon = R.drawable.check_in_reception,
            faqs = listOf(
                FAQ(
                    question = "When can I check in for my flight?",
                    answer = "Online check-in usually opens 24-48 hours before departure and closes 1-2 hours before flight time, depending on the route. You can check in through our app or website. Airport counter check-in typically opens 3 hours before departure for international flights and 2 hours for domestic flights."
                ),
                FAQ(
                    question = "How do I get my boarding pass?",
                    answer = "After completing check-in, you can download your boarding pass directly to your phone through our app, have it emailed to you, or print it. You can also obtain a physical boarding pass at the airport check-in counter or self-service kiosks."
                ),
                FAQ(
                    question = "What documents do I need for boarding?",
                    answer = "For domestic flights, you'll need a valid photo ID. For international flights, a passport (valid for at least 6 months beyond your travel date) is required. Some destinations may require visas or other travel documents. Always check specific requirements for your destination before traveling."
                )
            )
        ),
        HelpCategory(
            title = "Baggage Information",
            icon = R.drawable.baggage,
            faqs = listOf(
                FAQ(
                    question = "What are the baggage allowance limits?",
                    answer = "Baggage allowance varies by ticket class and route. Economy typically includes 1 checked bag (23kg) and 1 carry-on (7kg). Business class usually includes 2 checked bags (32kg each) and 1 carry-on (10kg). Additional or overweight baggage is subject to fees."
                ),
                FAQ(
                    question = "What items are prohibited in luggage?",
                    answer = "Prohibited items include flammable substances, explosives, compressed gases, toxic materials, and certain sharp objects. Liquids in carry-on must follow the 100ml rule. Electronic devices with lithium batteries should be in carry-on. For a complete list, please check our website or local aviation authority guidelines."
                ),
                FAQ(
                    question = "What if my baggage is lost or damaged?",
                    answer = "If your baggage is lost, damaged, or delayed, report it immediately at the baggage claim area before leaving the airport. Fill out a Property Irregularity Report (PIR) and keep a copy. You can track the status of your claim through our website or app. Compensation is provided according to applicable regulations."
                )
            )
        ),
        HelpCategory(
            title = "In-Flight Services",
            icon = R.drawable.restaurant_plate,
            faqs = listOf(
                FAQ(
                    question = "How do I request a special meal?",
                    answer = "Special meals (vegetarian, vegan, kosher, etc.) should be requested at least 24 hours before departure. You can add this request during booking or later through 'Manage Booking' in our app or website. We recommend confirming your special meal request during check-in."
                ),
                FAQ(
                    question = "Is Wi-Fi available on board?",
                    answer = "Wi-Fi is available on most of our international flights and select domestic routes. Availability is indicated during booking and on your flight details page. Various packages are available for purchase, from messaging-only to full browsing. Business class tickets often include complimentary Wi-Fi access."
                ),
                FAQ(
                    question = "What entertainment options are available?",
                    answer = "Our in-flight entertainment system offers movies, TV shows, music, games, and more. Content varies by aircraft and route. You can also download our app before your flight to access additional entertainment options. Some routes offer personal screens, while others may use a streaming service to your personal device."
                )
            )
        ),
        HelpCategory(
            title = "Payment & Refunds",
            icon = R.drawable.payment_request,
            faqs = listOf(
                FAQ(
                    question = "What payment methods do you accept?",
                    answer = "We accept major credit/debit cards (Visa, Mastercard, American Express), PayPal, Apple Pay, Google Pay, and bank transfers for some regions. Some routes may offer installment payment options. All payments are processed securely through our encrypted payment system."
                ),
                FAQ(
                    question = "How long do refunds take to process?",
                    answer = "Refund processing time depends on your payment method. Credit card refunds typically take 7-14 business days to appear on your statement. Bank transfers may take 10-20 business days. Refunds to electronic wallets are usually faster, often within 3-5 business days."
                ),
                FAQ(
                    question = "Can I use flight credits for future bookings?",
                    answer = "Yes, flight credits from cancelled bookings or vouchers can be used for future flights. To use credits, select 'Pay with flight credit' during the booking process and enter your voucher code or reference number. Flight credits typically have an expiration date, so check the terms of your specific credit."
                )
            )
        )
    )

    var searchQuery by remember { mutableStateOf("") }
    var expandedCategoryIndex by remember { mutableStateOf<Int?>(null) }
    var expandedFaqIndices by remember { mutableStateOf(mapOf<Int, Set<Int>>()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF001034),
                        Color(0xFF003045),
                        Color(0xFF004D40)
                    )
                )
            )
    ) {
        // Reuse the particle effect from the FlightDetailsScreen
        ParticleEffectBackground()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Header
                HelpCenterHeader(
                    onBackClick = { navController.navigateUp() },
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Support options section
                SupportOptionsCard()

                Spacer(modifier = Modifier.height(24.dp))

                // FAQ section header
                Text(
                    text = "Frequently Asked Questions",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            // Filter categories and FAQs based on search query
            val filteredCategories = if (searchQuery.isBlank()) {
                helpCategories
            } else {
                helpCategories.map { category ->
                    category.copy(
                        faqs = category.faqs.filter { faq ->
                            faq.question.contains(searchQuery, ignoreCase = true) ||
                                    faq.answer.contains(searchQuery, ignoreCase = true)
                        }
                    )
                }.filter { it.faqs.isNotEmpty() || it.title.contains(searchQuery, ignoreCase = true) }
            }

            items(filteredCategories.indices.toList()) { categoryIndex ->
                val category = filteredCategories[categoryIndex]
                CategoryCard(
                    category = category,
                    isExpanded = expandedCategoryIndex == categoryIndex,
                    expandedFaqIndices = expandedFaqIndices[categoryIndex] ?: emptySet(),
                    onCategoryClick = {
                        expandedCategoryIndex = if (expandedCategoryIndex == categoryIndex) null else categoryIndex
                    },
                    onFaqClick = { faqIndex ->
                        expandedFaqIndices = expandedFaqIndices.toMutableMap().apply {
                            val currentSet = this[categoryIndex] ?: emptySet()
                            this[categoryIndex] = if (faqIndex in currentSet) {
                                currentSet - faqIndex
                            } else {
                                currentSet + faqIndex
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))

                // Contact section
                ContactSupportCard()

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun HelpCenterHeader(
    onBackClick: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Back button and title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = "Help Center",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search bar
        TextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search for help topics...", color = Color.White.copy(alpha = 0.6f)) },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White.copy(alpha = 0.7f)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            Icons.Default.Close, // Replace with clear icon
                            contentDescription = "Clear search",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF0A192F).copy(alpha = 0.7f),
                unfocusedContainerColor = Color(0xFF0A192F).copy(alpha = 0.7f),
                disabledContainerColor = Color(0xFF0A192F).copy(alpha = 0.7f),
                cursorColor = Color(0xFF64B5F6),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.White)
        )
    }
}

@Composable
fun SupportOptionsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A192F).copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "How can we help you?",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SupportOptionItem(
                    icon = painterResource(R.drawable.chat_),
                    title = "Live Chat",
                    isActive = true
                )

                SupportOptionItem(
                    icon = painterResource(R.drawable.phone_),
                    title = "Call Support",
                    isActive = true
                )

                SupportOptionItem(
                    icon = painterResource(R.drawable.email_),
                    title = "Email Us",
                    isActive = true
                )
            }
        }
    }
}

@Composable
fun SupportOptionItem(
    icon:Painter,
    title: String,
    isActive: Boolean
) {
    // Reuse styling from AmenityItem
    val infiniteTransition = rememberInfiniteTransition(label = "support_item_animation")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "support_glow"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { /* Handle click */ }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (isActive) Color(0xFF0D47A1).copy(alpha = 0.5f + (glowAlpha * 0.2f))
                    else Color.Gray.copy(alpha = 0.2f),
                    CircleShape
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                tint = if (isActive) Color(0xFF64B5F6) else Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun CategoryCard(
    category: HelpCategory,
    isExpanded: Boolean,
    expandedFaqIndices: Set<Int>,
    onCategoryClick: () -> Unit,
    onFaqClick: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A192F).copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Category Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCategoryClick() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color(0xFF0D47A1).copy(alpha = 0.5f), CircleShape)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(category.icon),
                        contentDescription = category.title,
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Text(
                    text = category.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                // Expand/collapse icon
                Icon(
                    painter = painterResource(
                        if (isExpanded) R.drawable.collapse else R.drawable.expand
                    ), // Replace with proper expand/collapse icons
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier.size(24.dp)
                )
            }

            // FAQs when expanded
            if (isExpanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 1.dp,
                    color = Color(0xFF143a5a)
                )

                category.faqs.forEachIndexed { index, faq ->
                    FAQItem(
                        faq = faq,
                        isExpanded = index in expandedFaqIndices,
                        onClick = { onFaqClick(index) }
                    )

                    if (index < category.faqs.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            thickness = 0.5.dp,
                            color = Color(0xFF143a5a)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FAQItem(
    faq: FAQ,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = faq.question,
                color = Color.White,
                fontWeight = if (isExpanded) FontWeight.Bold else FontWeight.Normal,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(
                    if (isExpanded) R.drawable.collapse else R.drawable.expand
                ), // Replace with proper icons
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = Color(0xFF64B5F6),
                modifier = Modifier.size(20.dp)
            )
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = faq.answer,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun ContactSupportCard() {
    // Animation for the contact card
    val infiniteTransition = rememberInfiniteTransition(label = "contact_card_animation")
    val borderGlow by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "border_glow"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF002650).copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated circle with support icon
            Box(
                modifier = Modifier.padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer glow
                Canvas(modifier = Modifier.size(80.dp)) {
                    drawCircle(
                        color = Color(0xFF64B5F6).copy(alpha = borderGlow),
                        radius = size.minDimension / 2
                    )
                }

                // Inner circle with icon
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFF0D47A1).copy(alpha = 0.7f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.help_ic), // Replace with support icon
                        contentDescription = "Support",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Need more help?",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Our support team is available 24/7 to assist you with any issues or questions you might have.",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ContactMethod(
                    icon = painterResource(R.drawable.phone_),
                    text = "+201032432988",
                    description = "Call Support"
                )

                ContactMethod(
                    icon = painterResource(R.drawable.email_),
                    text = "support@flyapp.com",
                    description = "Email Support"
                )
            }
        }
    }
}

@Composable
fun ContactMethod(
    icon: Painter,
    text: String,
    description: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = description,
            tint = Color(0xFF64B5F6),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = text,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = description,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HelpCenterScreenPreview() {
    HelpCenterScreen(rememberNavController())
}