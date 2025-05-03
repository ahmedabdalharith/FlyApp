package com.example.flyapp.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flyapp.R
import com.example.flyapp.ui.theme.data.models.FlightDetails
@Composable
fun FlightInfoCard(flightDetails: FlightDetails) {
    // Color definitions from the original code
// Цветовая схема для улучшенного пользовательского интерфейса (Color scheme for enhanced user interface)
    val DarkBlue = Color(0xFF001034)
     val DeepBlue = Color(0xFF00204A)
     val MidnightBlue = Color(0xFF002650)
     val AccentBlue = Color(0xFF0288D1)
     val LightBlue = Color(0xFF64B5F6)
     val GlowBlue = Color(0xFF2196F3)
     val SuccessGreen = Color(0xFF4CAF50)
     val GoldAccent = Color(0xFFFFD700)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp), spotColor = AccentBlue.copy(alpha = 0.3f))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LightBlue.copy(alpha = 0.4f),
                        LightBlue.copy(alpha = 0.0f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MidnightBlue.copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with icon
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(LightBlue.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.info_),
                        contentDescription = "Flight Info",
                        tint = LightBlue,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Flight Information",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Animated divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                LightBlue.copy(alpha = 0.4f),
                                LightBlue.copy(alpha = 0.6f),
                                LightBlue.copy(alpha = 0.4f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Flight details in grid format
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Row 1: Departure and Arrival Airports
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FlightDetailItem(
                        title = "DEPARTURE AIRPORT",
                        value = flightDetails.departureAirport,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    FlightDetailItem(
                        title = "ARRIVAL AIRPORT",
                        value = flightDetails.arrivalAirport,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Row 2: Aircraft and Flight Duration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FlightDetailItem(
                        title = "AIRCRAFT",
                        value = flightDetails.aircraft,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    FlightDetailItem(
                        title = "FLIGHT DURATION",
                        value = flightDetails.duration,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Row 3: Terminal and Gate
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FlightDetailItem(
                        title = "TERMINAL",
                        value = flightDetails.terminal,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    FlightDetailItem(
                        title = "GATE",
                        value = flightDetails.gate,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Row 4: Class and Boarding Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FlightDetailItem(
                        title = "CLASS",
                        value = flightDetails.classType,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    FlightDetailItem(
                        title = "BOARDING TIME",
                        value = flightDetails.boardingTime,
                        modifier = Modifier.weight(1f),
                        valueColor = SuccessGreen
                    )
                }
            }
        }
    }
}

@Composable
fun FlightDetailItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.White
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = title,
            color = Color.White.copy(alpha = 0.6f),
            fontSize = 10.sp
        )
        Text(
            text = value,
            color = valueColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}