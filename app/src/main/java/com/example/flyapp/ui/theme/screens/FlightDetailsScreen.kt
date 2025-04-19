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
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
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
import com.example.flyapp.ui.theme.components.FlightInfoCard
import com.example.flyapp.ui.theme.components.ParticleEffectBackground
import com.example.flyapp.ui.theme.data.models.FlightDetails
import com.example.flyapp.ui.theme.navigition.Screen


@Composable
fun FlightDetailsScreen(navController: NavHostController) {
    // Sample flight details
    val flightDetails = FlightDetails(
        flightNumber = "SV 1734",
        airline = "Saudi Airlines",
        departureCity = "Riyadh",
        departureAirport = "King Khalid International Airport",
        departureCode = "RUH",
        departureTime = "10:30 AM",
        departureDate = "Fri, 18 Apr 2025",
        arrivalCity = "Dubai",
        arrivalAirport = "Dubai International Airport",
        arrivalCode = "DXB",
        arrivalTime = "12:45 PM",
        arrivalDate = "Fri, 18 Apr 2025",
        duration = "2h 15m",
        aircraft = "Boeing 777-300ER",
        gate = "B12",
        terminal = "Terminal 1",
        boardingTime = "09:50 AM",
        classType = "Economy",
        price = 650.0,
        hasWifi = true,
        hasMeal = true,
        luggageAllowance = "23 KG"
    )

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
        ParticleEffectBackground()

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Header with styled flight route info
                FlightRouteHeader(flightDetails)

                Spacer(modifier = Modifier.height(24.dp))

                // Flight details card
                FlightInfoCard(flightDetails)

                Spacer(modifier = Modifier.height(16.dp))

                // Boarding pass preview
                BoardingPassPreview(flightDetails)

                Spacer(modifier = Modifier.height(16.dp))

                // Amenities section
                AmenitiesSection(flightDetails)

                Spacer(modifier = Modifier.height(24.dp))

                // Bottom actions
                ActionButtons(
                    onSelectSeats = { navController.navigate(Screen.AirplaneSeatsScreen.route) }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun FlightRouteHeader(flightDetails: FlightDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF002650).copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = flightDetails.airline,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Departure info
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = flightDetails.departureCity,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = flightDetails.departureCode,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = flightDetails.departureTime,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                // Flight details with animated plane
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                            .size(48.dp)
                            .offset(y = offsetY.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Glow behind plane
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawCircle(
                                color = Color(0xFF64B5F6).copy(alpha = 0.4f),
                                radius = size.minDimension / 2,
                                center = center
                            )
                        }

                        // Plane icon
                        Image(
                            painter = painterResource(R.drawable.plane_path),
                            contentDescription = "Flight",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = "Flight ${flightDetails.flightNumber}",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Text(
                        text = flightDetails.duration,
                        color = Color(0xFFBAE6FF),
                        fontSize = 12.sp
                    )
                }

                // Arrival info
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = flightDetails.arrivalCity,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = flightDetails.arrivalCode,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = flightDetails.arrivalTime,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date display
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = flightDetails.departureDate,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )

                Text(
                    text = flightDetails.arrivalDate,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}


@Composable
fun InfoItem(title: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 12.sp
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
fun BoardingPassPreview(flightDetails: FlightDetails) {
    val infiniteTransition = rememberInfiniteTransition(label = "boarding_pass_animation")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "boarding_pass_glow"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color(0xFF64B5F6).copy(alpha = glow * 0.3f),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A192F).copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painterResource(R.drawable.plane_ticket),
                    contentDescription = "Boarding Pass",
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = "Boarding Pass",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Image(
                    painterResource(R.drawable.qr),
                    contentDescription = "QR Code",
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Ticket details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Passenger",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "JOHN DOE",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Seat",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Select →",
                        color = Color(0xFF64B5F6),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight route visualization
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val centerY = height / 2

                    // Draw dashed line
                    val dashWidth = 8f
                    val gapWidth = 4f
                    var startX = 0f

                    while (startX < width) {
                        drawLine(
                            color = Color(0xFF64B5F6).copy(alpha = 0.3f),
                            start = Offset(startX, centerY),
                            end = Offset(startX + dashWidth, centerY),
                            strokeWidth = 2f
                        )
                        startX += dashWidth + gapWidth
                    }

                    // Draw departure point
                    drawCircle(
                        color = Color(0xFF64B5F6),
                        radius = 8f,
                        center = Offset(width * 0.1f, centerY)
                    )

                    // Draw arrival point
                    drawCircle(
                        color = Color(0xFF64B5F6),
                        radius = 8f,
                        center = Offset(width * 0.9f, centerY)
                    )

                    // Draw plane
                    val planeX = width * 0.5f
                    val planeY = centerY - 15f

                    // Draw plane path
                    val planePath = Path().apply {
                        moveTo(planeX - 10f, planeY + 5f)
                        lineTo(planeX + 10f, planeY - 5f)
                        lineTo(planeX + 7f, planeY - 5f)
                        lineTo(planeX + 5f, planeY - 10f)
                        lineTo(planeX - 5f, planeY - 10f)
                        lineTo(planeX - 7f, planeY - 5f)
                        lineTo(planeX - 10f, planeY + 5f)
                        close()
                    }

                    drawPath(
                        path = planePath,
                        color = Color(0xFF64B5F6).copy(alpha = glow),
                    )
                }

                // City labels
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = flightDetails.departureCode,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, top = 32.dp)
                    )

                    Text(
                        text = flightDetails.arrivalCode,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 16.dp, top = 32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Boarding time info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF002650).copy(alpha = 0.5f),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(R.drawable.access_time),
                    contentDescription = "Boarding Time",
                    tint = Color(0xFF4CAF50)
                )

                Text(
                    text = "Boarding starts at ${flightDetails.boardingTime}",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun AmenitiesSection(flightDetails: FlightDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A192F).copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Flight Amenities",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AmenityItem(
                icon = painterResource(R.drawable.language_),
                title = "Luggage",
                description = flightDetails.luggageAllowance,
                isActive = true
            )

            AmenityItem(
                icon = painterResource(R.drawable.wifi_ic),
                title = "Wi-Fi",
                description = if (flightDetails.hasWifi) "Available" else "Not Available",
                isActive = flightDetails.hasWifi
            )

            AmenityItem(
                icon = painterResource(R.drawable.restaurant_plate),
                title = "Meal",
                description = if (flightDetails.hasMeal) "Included" else "Not Included",
                isActive = flightDetails.hasMeal
            )
        }
    }
}

@Composable
fun AmenityItem(
    icon: Painter,
    title: String,
    description: String,
    isActive: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    if (isActive) Color(0xFF0D47A1).copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.2f),
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

        Text(
            text = description,
            color = if (isActive) Color(0xFF4CAF50) else Color.Gray,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionButtons(onSelectSeats: () -> Unit) {
    // Animation for button
    val infiniteTransition = rememberInfiniteTransition(label = "button_animation")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_glow"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // Price summary
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF002650).copy(alpha = 0.7f)
            ),
            shape = RoundedCornerShape(16.dp)
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
                        text = "Total Price",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )

                    Text(
                        text = "$${String.format("%.2f", 650.00)}",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "1 Adult",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        }

        // Select Seats button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            // Button glow effect
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRoundRect(
                    color = Color(0xFF2196F3).copy(alpha = glowAlpha * 0.3f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx()),
                    size = androidx.compose.ui.geometry.Size(size.width + 8.dp.toPx(), size.height + 8.dp.toPx()),
                    topLeft = Offset(-4.dp.toPx(), -4.dp.toPx())
                )
            }

            Button(
                onClick = onSelectSeats,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0288D1)
                ),
                shape = RoundedCornerShape(28.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Seats",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Animated arrow
                    val arrowOffset by infiniteTransition.animateFloat(
                        initialValue = 0f,
                        targetValue = 4f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(800, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "arrow_animation"
                    )

                    Icon(
                        painterResource(R.drawable.seat_green),
                        contentDescription = "Select Seats",
                        tint = Color.White,
                        modifier = Modifier.offset(x = arrowOffset.dp)
                    )
                }
            }
        }
    }
}


data class Particle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val speed: Float,
    val alpha: Float=0.2f,

    )
@Preview(showBackground = true)
@Composable
fun FlightDetailsScreenPreview() {
    FlightDetailsScreen(rememberNavController())
}