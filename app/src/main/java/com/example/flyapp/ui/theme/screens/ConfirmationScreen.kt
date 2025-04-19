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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.flyapp.ui.theme.navigition.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@Composable
fun ConfirmationScreen(
    navController: NavHostController,
    selectedSeats: List<String> = listOf("15A", "15B") // Default value for preview
) {
    // Booking details
    val bookingReference = remember { generateBookingReference() }
    val currentDate = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()) }
    val scrollState = rememberScrollState()

    // Animation state
    var animationPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

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
        // Background particles
        ParticleEffectBackground()

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = animationPlayed,
                enter = slideInVertically(
                    initialOffsetY = { -50 },
                    animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 500)
                )
            ) {
                // Header
                ConfirmationHeader(bookingReference)
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = animationPlayed,
                enter = slideInVertically(
                    initialOffsetY = { 50 },
                    animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 700)
                )
            ) {
                // Booking Details Card
                BookingDetailsCard(bookingReference, selectedSeats, currentDate)
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = animationPlayed,
                enter = slideInVertically(
                    initialOffsetY = { 100 },
                    animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 900)
                )
            ) {
                // Passenger Information Card
                PassengerInformationCard(selectedSeats)
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(
                visible = animationPlayed,
                enter = slideInVertically(
                    initialOffsetY = { 150 },
                    animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 1100)
                )
            ) {
                // Additional Information Card
                AdditionalInformationCard()
            }

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(
                visible = animationPlayed,
                enter = slideInVertically(
                    initialOffsetY = { 200 },
                    animationSpec = tween(durationMillis = 1300, easing = FastOutSlowInEasing)
                ) + fadeIn(
                    animationSpec = tween(durationMillis = 1300)
                )
            ) {
                // Action Buttons
                Column {
                    // Download Ticket Button
                    Button(
                        onClick = {
                            navController.navigate(Screen.TicketScreen.route)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
                        ),
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.hotel),
                            contentDescription = "Download",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Download E-Ticket",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Return to Home Button
                    Button(
                        onClick = {
                            navController.navigate(Screen.MainScreen.route) {
                                popUpTo("home") { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF64B5F6)
                        ),
                        shape = RoundedCornerShape(28.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Return to Home",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = animationPlayed,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 1500)
                )
            ) {
                // Support text
                Text(
                    text = "Need help? Contact Support",
                    color = Color(0xFF64B5F6),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable { /* Open support */ }
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ConfirmationHeader(bookingReference: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Animated plane icon
        val infiniteTransition = rememberInfiniteTransition(label = "plane_animation")
        val offsetY by infiniteTransition.animateFloat(
            initialValue = -3f,
            targetValue = 3f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "plane_float"
        )

        Box(
            modifier = Modifier
                .size(60.dp)
                .offset(y = offsetY.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glow behind plane
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color(0xFF4CAF50).copy(alpha = 0.4f),
                    radius = size.minDimension / 2,
                    center = center
                )
            }

            // Plane icon with success color
            Icon(
                painter = painterResource(R.drawable.hotel),
                contentDescription = "Confirmation",
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Booking Confirmed!",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Booking Reference: $bookingReference",
            color = Color(0xFF4CAF50),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BookingDetailsCard(bookingReference: String, selectedSeats: List<String>, currentDate: String) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Flight Details",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Flight SV 1734",
                    color = Color(0xFF64B5F6),
                    fontSize = 14.sp
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color(0xFF64B5F6).copy(alpha = 0.3f)
            )

            // Flight route details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Riyadh (RUH)",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "10:30 AM, Today",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Flight direction",
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(24.dp)
                )

                Column {
                    Text(
                        text = "Dubai (DXB)",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "12:45 PM, Today",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color(0xFF64B5F6).copy(alpha = 0.3f)
            )

            // Booking Information
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Booking Date",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = currentDate,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Booking Reference",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = bookingReference,
                        color = Color(0xFF4CAF50),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Flight Duration",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "2h 15m",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Gate",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "B12",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Boarding Time",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = "09:50 AM",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Seat(s)",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Text(
                        text = selectedSeats.sorted().joinToString(", "),
                        color = Color(0xFF64B5F6),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PassengerInformationCard(selectedSeats: List<String>) {
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Passenger",
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Passenger Information",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color(0xFF64B5F6).copy(alpha = 0.3f)
            )

            // Passenger 1
            PassengerInfoRow("John Doe", "Adult", "Passport: AB1234567")

            Spacer(modifier = Modifier.height(8.dp))

            // Additional passenger if multiple seats
            if (selectedSeats.size > 1) {
                PassengerInfoRow("Jane Doe", "Adult", "Passport: CD7654321")
            }
        }
    }
}

@Composable
fun PassengerInfoRow(name: String, type: String, document: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFF64B5F6).copy(alpha = 0.2f), RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Passenger",
                tint = Color(0xFF64B5F6),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = type,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )

                Text(
                    text = "•",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )

                Text(
                    text = document,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun AdditionalInformationCard() {
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
                text = "Important Information",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color(0xFF64B5F6).copy(alpha = 0.3f)
            )

            InfoItem(
                iconRes = R.drawable.date_range,
                title = "Check-in",
                description = "Online check-in available 24 hours before departure"
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoItem(
                iconRes = R.drawable.creditcard,
                title = "Baggage",
                description = "Checked baggage: 23kg, Carry-on: 7kg"
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoItem(
                iconRes = R.drawable.paypal,
                title = "Airport Guidelines",
                description = "Please arrive at least 2 hours before departure time"
            )
        }
    }
}

@Composable
fun InfoItem(iconRes: Int, title: String, description: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = title,
            tint = Color(0xFF64B5F6),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = description,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}

// Helper function to generate a random booking reference
fun generateBookingReference(): String {
    val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
    return (1..6).map { chars[Random.nextInt(0, chars.length)] }.joinToString("")
}

@Preview
@Composable
fun ConfirmationScreenPreview() {
    ConfirmationScreen(
        navController = rememberNavController(),
        selectedSeats = listOf("15A", "15B")
    )
}