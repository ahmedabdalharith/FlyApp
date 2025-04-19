package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.draw.clip
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

data class Trip(
    val id: String,
    val from: String,
    val fromCode: String,
    val to: String,
    val toCode: String,
    val date: String,
    val time: String,
    val flightNumber: String,
    val status: String
)

@Composable
fun TripManagementScreen(
    navController: NavHostController
) {
    // Sample data for trips
    val trips = remember {
        listOf(
            Trip(
                id = "T1001",
                from = "Riyadh",
                fromCode = "RUH",
                to = "Dubai",
                toCode = "DXB",
                date = "18 Apr 2025",
                time = "10:30 AM",
                flightNumber = "SV 1734",
                status = "Upcoming"
            ),
            Trip(
                id = "T1002",
                from = "Jeddah",
                fromCode = "JED",
                to = "Cairo",
                toCode = "CAI",
                date = "22 Apr 2025",
                time = "08:15 PM",
                flightNumber = "SV 2145",
                status = "Upcoming"
            ),
            Trip(
                id = "T1003",
                from = "Dubai",
                fromCode = "DXB",
                to = "Riyadh",
                toCode = "RUH",
                date = "25 Apr 2025",
                time = "02:45 PM",
                flightNumber = "SV 1735",
                status = "Upcoming"
            )
        )
    }

    // States
    var isLoading by remember { mutableStateOf(false) }
    var isActionComplete by remember { mutableStateOf(false) }
    var selectedTripId by remember { mutableStateOf<String?>(null) }

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

        // Loading overlay
        AnimatedVisibility(
            visible = isLoading || isActionComplete,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF001034).copy(alpha = 0.85f)),
                contentAlignment = Alignment.Center
            ) {
                if (isLoading) {
                    LoadingAnimation("Processing Request")
                } else if (isActionComplete) {
                    ActionCompleteAnimation(
                        message = "Action Completed Successfully",
                        onContinue = {
                            isActionComplete = false
                        }
                    )
                }
            }
        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            TripManagementHeader()

            Spacer(modifier = Modifier.height(16.dp))

            // Trip list
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(trips) { trip ->
                    TripCard(
                        trip = trip,
                        onEdit = {
                            selectedTripId = trip.id
                            navController.navigate(
                                Screen.BookingFlightScreen
                            )
                            // Here would be navigation to edit screen
                        },
                        onCancel = {
                            selectedTripId = trip.id
                            isLoading = true
                            //delete item


                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(80.dp)) // Space for FAB
                }
            }
        }

        // Add new trip FAB
        FloatingActionButton(
            onClick = {
                // Navigate to new trip screen
            },
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Trip"
            )
        }
    }
}

@Composable
fun TripManagementHeader() {
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
                    color = Color(0xFF64B5F6).copy(alpha = 0.4f),
                    radius = size.minDimension / 2,
                    center = center
                )
            }

            // Plane icon
            Icon(
                painterResource(R.drawable.plane_path),
                contentDescription = "Trip Management",
                tint = Color(0xFF64B5F6),
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "My Trips",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Manage your upcoming and past trips",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TripCard(
    trip: Trip,
    onEdit: () -> Unit,
    onCancel: () -> Unit
) {
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Trip #${trip.id}",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            when (trip.status) {
                                "Completed" -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                                "Upcoming" -> Color(0xFF64B5F6).copy(alpha = 0.2f)
                                "Cancelled" -> Color(0xFFE57373).copy(alpha = 0.2f)
                                else -> Color(0xFF64B5F6).copy(alpha = 0.2f)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = trip.status,
                        color = when (trip.status) {
                            "Completed" -> Color(0xFF4CAF50)
                            "Upcoming" -> Color(0xFF64B5F6)
                            "Cancelled" -> Color(0xFFE57373)
                            else -> Color(0xFF64B5F6)
                        },
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color(0xFF64B5F6).copy(alpha = 0.3f)
            )

            // Flight details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = trip.from,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = trip.fromCode,
                        color = Color(0xFF64B5F6),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(R.drawable.plane_path),
                        contentDescription = "Flight",
                        tint = Color(0xFF64B5F6),
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = trip.flightNumber,
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = trip.to,
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = trip.toCode,
                        color = Color(0xFF64B5F6),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color(0xFF64B5F6).copy(alpha = 0.3f)
            )

            // Date and time info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.date_range),
                    contentDescription = "Date",
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = trip.date,
                    color = Color.White,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    painter = painterResource(R.drawable.access_time),
                    contentDescription = "Time",
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = trip.time,
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onEdit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF64B5F6)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Trip",
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Edit",
                        fontSize = 14.sp
                    )
                }

                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE57373)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Cancel Trip",
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Cancel",
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingAnimation(message: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        // Animated progress indicator
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = Color(0xFF64B5F6),
            strokeWidth = 5.dp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = message,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Please wait while we process your request...",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionCompleteAnimation(
    message: String,
    onContinue: () -> Unit
) {
    var animationPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        AnimatedVisibility(
            visible = animationPlayed,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success check mark
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF4CAF50), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = message,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onContinue,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF64B5F6)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Continue",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TripManagementScreenPreview() {
    TripManagementScreen(
        navController = rememberNavController()
    )
}