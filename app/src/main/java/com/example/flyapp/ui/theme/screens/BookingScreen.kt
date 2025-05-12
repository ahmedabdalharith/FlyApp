package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.ui.theme.components.FlightTopAppBar
import com.example.flyapp.ui.theme.theme.DarkNavyBlue
import com.example.flyapp.ui.theme.theme.DeepBlue
import com.example.flyapp.ui.theme.theme.GoldColor
import com.example.flyapp.ui.theme.theme.MediumBlue

@Composable
fun BookingScreen(
    navController: NavHostController,
    flightNumber: String = "FL-452",
    departureCity: String = "New York",
    arrivalCity: String = "Los Angeles",
    departureTime: String = "08:30 AM",
    arrivalTime: String = "11:45 AM"
) {
    // Seat selection states
    var selectedSeats by remember { mutableStateOf(listOf<String>()) }
    var seatType by remember { mutableStateOf("Economy") }

    // Seat grid generation
    val rows = listOf("A", "B", "C", "D", "E", "F")
    val firstClassRows = 2
    val businessClassRows = 3
    val economyClassRows = 1

    // Seat row generation logic
    fun generateSeatGrid(): List<List<String>> {
        return (1..7).map { rowNumber ->
            rows.map { letter ->
                "$rowNumber$letter"
            }
        }
    }

    val seatGrid = remember { generateSeatGrid() }

    // Seat selection animation
    val selectedSeatScale = remember { Animatable(1f) }

    // Scroll state
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepBlue,     // Darkest blue
                        MediumBlue,   // Medium blue
                        DarkNavyBlue  // Navy blue
                    )
                )
            )
    ) {
        // Security pattern background
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

        // Main content column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Top app bar with flight booking text
            FlightTopAppBar(
                textOne = "SEAT",
                textTwo = "SELECTION",
                navController = navController
            )

            // Flight details card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                GoldColor,       // Gold
                                Color(0xFFFFD700), // Gold
                                GoldColor        // Gold
                            )
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkNavyBlue.copy(alpha = 0.85f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Flight details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = departureCity,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = departureTime,
                                color = GoldColor,
                                fontSize = 14.sp
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = arrivalCity,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = arrivalTime,
                                color = GoldColor,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        color = GoldColor.copy(alpha = 0.3f)
                    )

                    // Flight number and additional details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Flight Number:",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Text(
                            text = flightNumber,
                            color = GoldColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Seat class selector
            Text(
                text = "SELECT SEAT CLASS",
                color = GoldColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
            )

            // Seat class selection row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, GoldColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .background(DarkNavyBlue.copy(alpha = 0.7f))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val seatClasses = listOf("First", "Business", "Economy")
                seatClasses.forEach { cls ->
                    val isSelected = cls.lowercase() == seatType.lowercase()
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) GoldColor else Color.Transparent
                            )
                            .clickable { seatType = cls }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cls,
                            color = if (isSelected) DarkNavyBlue else GoldColor.copy(alpha = 0.9f),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Seat grid
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "SEAT MAP",
                    color = GoldColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Seat layout visualization
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    GoldColor.copy(alpha = 0.7f),
                                    GoldColor.copy(alpha = 0.3f),
                                    GoldColor.copy(alpha = 0.7f)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    shape = RoundedCornerShape(16.dp),
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
                        // Seat grid layout
                        seatGrid.forEachIndexed { rowIndex, rowSeats ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowSeats.forEach { seatId ->
                                    val isSeatAvailable = when {
                                        rowIndex < firstClassRows && seatType == "First" -> true
                                        rowIndex in firstClassRows until (firstClassRows + businessClassRows) && seatType == "Business" -> true
                                        rowIndex >= (firstClassRows + businessClassRows) && seatType == "Economy" -> true
                                        else -> false
                                    }

                                    val isSelected = seatId in selectedSeats

                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                when {
                                                    isSelected -> GoldColor
                                                    !isSeatAvailable -> Color.Gray.copy(alpha = 0.3f)
                                                    else -> DarkNavyBlue.copy(alpha = 0.7f)
                                                }
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = when {
                                                    isSelected -> GoldColor.copy(alpha = 0.8f)
                                                    !isSeatAvailable -> Color.Gray.copy(alpha = 0.3f)
                                                    else -> GoldColor.copy(alpha = 0.3f)
                                                },
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable(
                                                enabled = isSeatAvailable && !isSelected
                                            ) {
                                                if (selectedSeats.size < 4) {
                                                    selectedSeats = if (isSelected) {
                                                        selectedSeats.filter { it != seatId }
                                                    } else {
                                                        selectedSeats + seatId
                                                    }
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = seatId,
                                            color = when {
                                                isSelected -> DarkNavyBlue
                                                !isSeatAvailable -> Color.Gray.copy(alpha = 0.5f)
                                                else -> Color.White
                                            },
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }
                        }

                        // Seat legend
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly) {
                            // Available seats legend
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Available Seat Legend
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .background(
                                                DarkNavyBlue.copy(alpha = 0.7f),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = GoldColor.copy(alpha = 0.3f),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    )
                                    Text(
                                        text = "Available",
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }

                                // Selected Seat Legend
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .background(
                                                GoldColor,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = GoldColor.copy(alpha = 0.8f),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    )
                                    Text(
                                        text = "Selected",
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }

                                // Unavailable Seat Legend
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .background(
                                                Color.Gray.copy(alpha = 0.3f),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .border(
                                                width = 1.dp,
                                                color = Color.Gray.copy(alpha = 0.3f),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    )
                                    Text(
                                        text = "Unavailable",
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Continue Selection Button
            Button(
                onClick = {
                    if (selectedSeats.isNotEmpty()) {
                        // Navigate to next screen with selected seats and seat type
//                        navController.navigate(
//                            "${Screen.PassengerDetails.route}/$seatType/${selectedSeats.joinToString(",")}"
//                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldColor,
                    contentColor = DarkNavyBlue
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Continue (${selectedSeats.size} Seat${if (selectedSeats.size != 1) "s" else ""})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingScreenPreview() {
    BookingScreen(
        navController = rememberNavController()
    )
}