package com.example.flyapp.ui.theme.screens

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
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
import com.example.flyapp.ui.theme.utils.PdfUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun TicketScreen(
    navController: NavHostController,
    selectedSeats: List<String> = listOf("15A", "15B"), // Default value for preview
    bookingReference: String = "SV" + (100000..999999).random()
) {
    // Get current date for ticket
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.US)
    val currentDate = dateFormat.format(Date())

    // Context for PDF generation
    val context = LocalContext.current

    // Calculate price based on selected seats (same logic as in PaymentScreen)
    val totalPrice = selectedSeats.sumOf { seatId ->
        when {
            seatId.startsWith("1") || seatId.startsWith("2") -> 450L // First class
            seatId.startsWith("3") || seatId.startsWith("4") ||
                    seatId.startsWith("5") || seatId.startsWith("6") ||
                    seatId.startsWith("7") -> 250L // Business class
            else -> 120L // Economy class
        }
    }

    // Scroll state
    val scrollState = rememberScrollState()

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
            // Header
            TicketHeader()

            Spacer(modifier = Modifier.height(24.dp))

            // E-Ticket Card
            ETicketCard(
                selectedSeats = selectedSeats,
                bookingReference = bookingReference,
                date = currentDate
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Boarding Pass Card
            BoardingPassCard(
                selectedSeats = selectedSeats,
                bookingReference = bookingReference,
                date = currentDate
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Actions
            ActionButtons(
                onShare = { /* Share functionality would go here */ },
                onDownload = {
                    // We need to cast context to Activity for proper lifecycle handling
                    val activity = context as? Activity
                    if (activity != null) {
                        // Show loading message
                        Toast.makeText(context, "Generating PDF...", Toast.LENGTH_SHORT).show()

                        // Generate PDF in a coroutine to avoid blocking UI
                        CoroutineScope(Dispatchers.IO).launch {
                            val pdfUri = PdfUtils.generateTicketPdf(
                                activity = activity,
                                bookingReference = bookingReference
                            ) {
                                // Content to be rendered in PDF
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.White)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        // Add app logo/branding for the PDF
                                        Image(
                                            painter = painterResource(R.drawable.plane_ticket),
                                            contentDescription = "FlyApp Logo",
                                            modifier = Modifier.size(64.dp)
                                        )

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Text(
                                            text = "FLIGHT TICKET",
                                            color = Color(0xFF003366),
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = "Booking Reference: $bookingReference",
                                            color = Color(0xFF0055A5),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Include both ticket cards in the PDF
                                        ETicketCard(
                                            selectedSeats = selectedSeats,
                                            bookingReference = bookingReference,
                                            date = currentDate
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        BoardingPassCard(
                                            selectedSeats = selectedSeats,
                                            bookingReference = bookingReference,
                                            date = currentDate
                                        )

                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Add footer with legal information
                                        Text(
                                            text = "This is an electronic ticket. Please present a printed copy or show this PDF at the check-in counter.",
                                            color = Color(0xFF666666),
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                            text = "© ${Calendar.getInstance().get(Calendar.YEAR)} FlyApp - All Rights Reserved",
                                            color = Color(0xFF666666),
                                            fontSize = 10.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            // Switch to main thread to open the PDF
                            withContext(Dispatchers.Main) {
                                pdfUri?.let { uri ->
                                    PdfUtils.openPdf(context, uri)
                                } ?: run {
                                    Toast.makeText(context, "Failed to create PDF", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        Toast.makeText(context, "Cannot access Activity to create PDF", Toast.LENGTH_SHORT).show()
                    }
                },
                onHome = {
                    navController.navigate(Screen.MainScreen.route) {
                        popUpTo(Screen.MainScreen.route) { inclusive = true }
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
@Composable
fun TicketHeader() {
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
            Image(
                painter = painterResource(R.drawable.plane_ticket),
                contentDescription = "Flight",
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your Ticket",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Booking Confirmed",
            color = Color(0xFF4CAF50),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ETicketCard(
    selectedSeats: List<String>,
    bookingReference: String,
    date: String
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
                    text = "E-Ticket",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF64B5F6).copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.plane_ticket),
                        contentDescription = "Flight Logo",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Flight details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Riyadh",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "RUH",
                        color = Color(0xFF64B5F6),
                        fontSize = 16.sp
                    )
                    Text(
                        text = "10:30 AM",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Flight direction",
                        tint = Color(0xFF64B5F6),
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = "2h 15m",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )

                    Text(
                        text = "SV 1734",
                        color = Color(0xFF64B5F6),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Dubai",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "DXB",
                        color = Color(0xFF64B5F6),
                        fontSize = 16.sp
                    )
                    Text(
                        text = "12:45 PM",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFF64B5F6).copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(16.dp))

            // Passenger & Seat Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Passenger",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Passenger",
                            tint = Color(0xFF64B5F6),
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = "Ahmed Ibrahim",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Seat",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = selectedSeats.sorted().joinToString(", "),
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date & Booking Reference
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Date",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.date_range),
                            contentDescription = "Date",
                            tint = Color(0xFF64B5F6),
                            modifier = Modifier.size(16.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(
                            text = date,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Booking Ref",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = bookingReference,
                        color = Color(0xFF4CAF50),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Price Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Paid",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )

                Text(
                    text = "$${800}",
                    color = Color(0xFF4CAF50),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



@Composable
fun ActionButtons(
    onShare: () -> Unit,
    onDownload: () -> Unit,
    onHome: () -> Unit
) {
    // Share button
    Button(
        onClick = onShare,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF64B5F6)
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Icon(
            Icons.Default.Share,
            contentDescription = "Share",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Share Ticket",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Download button
    Button(
        onClick = onDownload,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0A192F)
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.download), // Replace with download icon
            contentDescription = "Download",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Download PDF",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Return to Home button
    Text(
        text = "Return to Home",
        color = Color(0xFF64B5F6),
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .clickable(onClick = onHome)
            .padding(8.dp)
            .fillMaxWidth()
    )
}

@Preview
@Composable
fun TicketScreenPreview() {
    TicketScreen(
        navController = rememberNavController(),
        selectedSeats = listOf("15A", "15B"),
        bookingReference = "SV423975"
    )
}
@Composable
fun BoardingPassCard(
    selectedSeats: List<String>,
    bookingReference: String,
    date: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(0.dp)
        ) {
            // Airline Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF003366),
                                Color(0xFF0055A5)
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Airline Logo
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            // Replace with airline logo
                            Canvas(modifier = Modifier.size(32.dp)) {
                                drawCircle(
                                    color = Color(0xFF0055A5),
                                    radius = size.minDimension / 3,
                                    center = center
                                )
                                drawPath(
                                    path = Path().apply {
                                        moveTo(center.x - 15f, center.y)
                                        lineTo(center.x + 15f, center.y)
                                        moveTo(center.x, center.y - 15f)
                                        lineTo(center.x, center.y + 15f)
                                    },
                                    color = Color.White,
                                    style = Stroke(width = 3f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column {
                            Text(
                                text = "SAUDIA",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Saudi Arabian Airlines",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    Text(
                        text = "BOARDING PASS",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Flight Information Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // From - To with Airline details
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "RUH",
                            color = Color(0xFF003366),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Riyadh",
                            color = Color(0xFF444444),
                            fontSize = 14.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Flight icon with route line
                        Box(modifier = Modifier.width(120.dp)) {
                            Canvas(modifier = Modifier
                                .height(28.dp)
                                .fillMaxWidth()) {
                                // Draw the dashed line
                                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                                drawLine(
                                    color = Color(0xFF0055A5),
                                    start = androidx.compose.ui.geometry.Offset(0f, size.height/2),
                                    end = androidx.compose.ui.geometry.Offset(size.width, size.height/2),
                                    strokeWidth = 1.5f,
                                    pathEffect = pathEffect
                                )

                                // Draw the plane icon
                                drawCircle(
                                    color = Color(0xFF0055A5),
                                    radius = 16f,
                                    center = androidx.compose.ui.geometry.Offset(size.width * 0.7f, size.height/2)
                                )
                            }
                        }

                        Text(
                            text = "SV 1734",
                            color = Color(0xFF0055A5),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Boeing 787-9",
                            color = Color(0xFF666666),
                            fontSize = 10.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "DXB",
                            color = Color(0xFF003366),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = "Dubai",
                            color = Color(0xFF444444),
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date and Time Information
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "DATE",
                            color = Color(0xFF666666),
                            fontSize = 10.sp
                        )
                        Text(
                            text = date,
                            color = Color(0xFF000000),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "TIME",
                            color = Color(0xFF666666),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "10:30",
                            color = Color(0xFF000000),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "FLIGHT DURATION",
                            color = Color(0xFF666666),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "2h 15m",
                            color = Color(0xFF000000),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Perforation line
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            ) {
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
                val y = size.height / 2

                // Draw zigzag pattern
                val path = Path().apply {
                    moveTo(0f, y)
                    var x = 0f
                    val zigzagWidth = 16f
                    val zigzagHeight = 8f

                    while (x < size.width) {
                        lineTo(x + zigzagWidth/2, y - zigzagHeight/2)
                        lineTo(x + zigzagWidth, y)
                        x += zigzagWidth
                    }
                }

                drawPath(
                    path = path,
                    color = Color(0xFF0055A5).copy(alpha = 0.5f),
                    style = Stroke(width = 1f)
                )
            }

            // Passenger and Ticket Information
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PASSENGER NAME",
                            color = Color(0xFF666666),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "DOE/JOHN MR",
                            color = Color(0xFF000000),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "SEAT",
                            color = Color(0xFF666666),
                            fontSize = 10.sp
                        )
                        Text(
                            text = selectedSeats.first(),
                            color = Color(0xFF000000),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "BOARDING",
                            color = Color(0xFF666666),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "09:45",
                            color = Color(0xFF000000),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "GATE",
                            color = Color(0xFF666666),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "B26",
                            color = Color(0xFF000000),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ZONE",
                            color = Color(0xFF666666),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "ZONE 2",
                            color = Color(0xFF000000),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Booking reference
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "BOOKING REFERENCE",
                            color = Color(0xFF666666),
                            fontSize = 10.sp
                        )
                        Text(
                            text = bookingReference,
                            color = Color(0xFF003366),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Text(
                            text = "E-TICKET NUMBER",
                            color = Color(0xFF666666),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "065-${(1000000..9999999).random()}",
                            color = Color(0xFF003366),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Barcode Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // More realistic QR code/barcode
                    Canvas(modifier = Modifier
                        .width(200.dp)
                        .height(60.dp)) {
                        // Draw barcode background
                        drawRect(
                            color = Color.White,
                            size = size
                        )

                        // Draw barcode lines
                        val lineWidth = 3f
                        val spacing = 2f
                        var xOffset = 0f

                        // Create a random but consistent pattern for the barcode
                        val random = java.util.Random(bookingReference.hashCode().toLong())
                        while (xOffset < size.width) {
                            val lineHeight = if (random.nextBoolean()) size.height else size.height * 0.7f
                            val yOffset = if (lineHeight < size.height) (size.height - lineHeight) / 2 else 0f

                            drawRect(
                                color = Color.Black,
                                topLeft = androidx.compose.ui.geometry.Offset(xOffset, yOffset),
                                size = androidx.compose.ui.geometry.Size(lineWidth, lineHeight)
                            )

                            xOffset += lineWidth + spacing
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "SV1734${(1000..9999).random()}${bookingReference}",
                        color = Color(0xFF666666),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Bottom disclaimer
            Text(
                text = "BOARDING GATE CLOSES 20 MINUTES BEFORE DEPARTURE",
                color = Color(0xFF003366),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFE6EEF8))
                    .padding(vertical = 4.dp)
            )
        }
    }
}