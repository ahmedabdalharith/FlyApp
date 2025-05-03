package com.example.flyapp.ui.theme.screens

import android.graphics.Paint
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flyapp.R
import kotlinx.coroutines.delay
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

data class FlightInfoTrace(
    val flightNumber: String = "6E 1471",
    val route: String = "Chennai to Dubai",
    val date: String = "FRI, 17 NOV",
    val departure: AirportInfoTrace = AirportInfoTrace(
        code = "MAA",
        city = "Chennai Intl",
        terminal = "Terminal 2",
        gate = "Gate 16",
        time = "6:10 AM",
        status = "On Time",
        countdown = "in 12h 18m",
        latitude = 13.0827,
        longitude = 80.2707
    ),
    val arrival: AirportInfoTrace = AirportInfoTrace(
        code = "DXB",
        city = "Dubai Intl",
        terminal = "Terminal 1",
        gate = "Gate 4",
        time = "10:40 AM",
        status = "On Time",
        countdown = "in 16h 48m",
        baggage = "Baggage belt 4",
        latitude = 25.2048,
        longitude = 55.2708
    ),
    val details: FlightDetailsTrace = FlightDetailsTrace(
        flightCode = "FLI4842",
        seat = "16F",
        qr = "QFR 4"
    ),
    val distance: String = "2,934 km",
    val duration: String = "4h 30m",
    val gateCountdown: String = "12h 16m",
    val progress: Float = 0.15f // Current progress of flight (0-1)
)

data class AirportInfoTrace(
    val code: String,
    val city: String,
    val terminal: String,
    val gate: String,
    val time: String,
    val status: String,
    val countdown: String,
    val baggage: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

data class FlightDetailsTrace(
    val flightCode: String,
    val seat: String,
    val qr: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightTrackerScreen() {
    val flightInfo = remember { FlightInfoTrace() }

    // Animation states
    var showStars by remember { mutableStateOf(false) }
    var showEarth by remember { mutableStateOf(false) }
    var showFlightInfo by remember { mutableStateOf(false) }
    var showDetails by remember { mutableStateOf(false) }
    var use3DEarth by remember { mutableStateOf(true) }

    // Earth rotation animation - used for 2D fallback if needed
    val earthRotation = rememberInfiniteTransition(label = "earth_rotation")
    val rotationAngle by earthRotation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(60000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    // Flight animation
    val flightAnimation = rememberInfiniteTransition(label = "flight_animation")
    val animatedProgress by flightAnimation.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(120000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "flight_progress"
    )

    // Star animation
    val starsAlpha by animateFloatAsState(
        targetValue = if (showStars) 1f else 0f,
        animationSpec = tween(1000),
        label = "stars_alpha"
    )

    // Earth animation
    val earthScale by animateFloatAsState(
        targetValue = if (showEarth) 1f else 0f,
        animationSpec = tween(1200, easing = EaseOutBack),
        label = "earth_scale"
    )

    // Flight info animation
    val infoAlpha by animateFloatAsState(
        targetValue = if (showFlightInfo) 1f else 0f,
        animationSpec = tween(800),
        label = "info_alpha"
    )

    // Details animation
    val detailsAlpha by animateFloatAsState(
        targetValue = if (showDetails) 1f else 0f,
        animationSpec = tween(800),
        label = "details_alpha"
    )

    // Trigger animations sequentially
    LaunchedEffect(key1 = true) {
        showStars = true
        delay(300)
        showEarth = true
        delay(500)
        showFlightInfo = true
        delay(300)
        showDetails = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Stars background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(starsAlpha)
        ) {
            StarsBackground()
        }

        // Main content
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Flight info
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Flight",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )

                    Column {
                        Text(
                            text = "${flightInfo.date} · ${flightInfo.flightNumber}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = flightInfo.route,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // View toggle button
                IconButton(
                    onClick = { use3DEarth = !use3DEarth },
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF212121), CircleShape)
                ) {
                    Icon(
                        imageVector = if (use3DEarth) Icons.Default.Refresh else Icons.Default.Info,
                        contentDescription = "Toggle View",
                        tint = Color.White
                    )
                }
            }

            // Earth view with flight path
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                if (use3DEarth) {
                    // 3D Earth visualization
                    Box(
                        modifier = Modifier
                            .size(280.dp)
                            .scale(earthScale)
                    ) {
                        Earth3DVisualizationScreen(
                            modifier = Modifier,
                            departLat = 13.0827f, // Chennai
                            departLon = 80.2707f,
                            arriveLat = 25.2048f, // Dubai
                            arriveLon = 55.2708f,
                            progress = animatedProgress
                        )
                    }
                } else {
                    // 2D Earth fallback
                    Box {
                        Box(
                            modifier = Modifier
                                .size(280.dp)
                                .scale(earthScale)
                                .rotate(rotationAngle)
                        ) {
                            Earth2DView()
                        }
                    }

                    // Flight path overlay
                    Box(
                        modifier = Modifier
                            .size(280.dp)
                            .scale(earthScale)
                    ) {
                        FlightPathTrace(
                            departLat = flightInfo.departure.latitude,
                            departLon = flightInfo.departure.longitude,
                            arriveLat = flightInfo.arrival.latitude,
                            arriveLon = flightInfo.arrival.longitude,
                            progress = animatedProgress
                        )
                    }
                }
            }

            // Flight details card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .alpha(infoAlpha),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF1A2234).copy(alpha = 0.9f),
                tonalElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Card header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = flightInfo.flightNumber,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = flightInfo.date,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    // Flight details
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        // Timeline and icons
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(end = 16.dp)
                        ) {
                            Icon(
                                painter =painterResource(R.drawable.plane_path),
                                contentDescription = "Departure",
                                tint = Color.Green,
                                modifier = Modifier.size(20.dp)
                            )

                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(80.dp)
                                    .background(Color.DarkGray)
                            )

                            Icon(
                                painter =painterResource(R.drawable.plane_path),
                                contentDescription = "Arrival",
                                tint = Color.Green,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Flight details
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            // Departure info
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = flightInfo.departure.code,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Green
                                    )
                                    Text(
                                        text = flightInfo.departure.city,
                                        fontSize = 14.sp,
                                        color = Color.LightGray
                                    )
                                    Text(
                                        text = "${flightInfo.departure.terminal} · ${flightInfo.departure.gate}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = flightInfo.departure.time,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Green
                                    )
                                    Text(
                                        text = flightInfo.departure.status,
                                        fontSize = 12.sp,
                                        color = Color.Green
                                    )
                                    Text(
                                        text = flightInfo.departure.countdown,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            // Flight progress line
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            ) {
                                Text(
                                    text = "Total ${flightInfo.duration} · ${flightInfo.distance}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }

                            // Arrival info
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = flightInfo.arrival.code,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Green
                                    )
                                    Text(
                                        text = flightInfo.arrival.city,
                                        fontSize = 14.sp,
                                        color = Color.LightGray
                                    )
                                    Text(
                                        text = "${flightInfo.arrival.terminal} · ${flightInfo.arrival.gate}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                    flightInfo.arrival.baggage?.let {
                                        Text(
                                            text = it,
                                            fontSize = 12.sp,
                                            color = Color.Gray
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = flightInfo.arrival.time,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Green
                                    )
                                    Text(
                                        text = flightInfo.arrival.status,
                                        fontSize = 12.sp,
                                        color = Color.Green
                                    )
                                    Text(
                                        text = flightInfo.arrival.countdown,
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Gate departure info
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .alpha(detailsAlpha),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1A2234).copy(alpha = 0.9f)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Gate departure in ${flightInfo.gateCountdown}",
                        color = Color.Green,
                        fontSize = 16.sp
                    )

                    Box(
                        modifier = Modifier
                            .background(Color.Green, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "16",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Icon(
                                painter =painterResource(R.drawable.plane_path),
                                contentDescription = "Gate",
                                tint = Color.Black,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // Bottom info cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .alpha(detailsAlpha),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoCardTrace(
                    icon = painterResource(R.drawable.qrcode),
                    title = flightInfo.details.flightCode,
                    subtitle = "Booking Code",
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                InfoCardTrace(
                    icon = painterResource(R.drawable.seat),
                    title = "Seat ${flightInfo.details.seat}",
                    subtitle = "Seating",
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                InfoCardTrace(
                    icon = painterResource(R.drawable.baggage),
                    title = flightInfo.details.qr,
                    subtitle = "Luggage",
                    modifier = Modifier.weight(1f)
                )
            }

            // Bottom action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .alpha(detailsAlpha),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ActionButtonTrace(
                    icon = Icons.Default.Share,
                    label = "Live Share"
                )

                ActionButtonTrace(
                    icon = Icons.Default.Notifications,
                    label = "Alerts Off"
                )

                ActionButtonTrace(
                    icon = Icons.Default.MoreVert,
                    label = "More info."
                )
            }
        }
    }
}

@Composable
fun StarsBackground() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        repeat(200) {
            val x = Random.nextFloat() * size.width
            val y = Random.nextFloat() * size.height
            val radius = Random.nextFloat() * 1.5f + 0.5f
            val alpha = Random.nextFloat() * 0.8f + 0.2f

            drawCircle(
                color = Color.White.copy(alpha = alpha),
                radius = radius,
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun Earth2DView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1E3B70),
                        Color(0xFF0A1128)
                    )
                )
            )
    ) {
        // Earth continents and features (simplified representation)
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw continents with a subtle green-brown tone
            val continentColor = Color(0xFF2A623D).copy(alpha = 0.5f)
            val oceanOverlay = Color(0xFF1565C0).copy(alpha = 0.2f)

            // Add some landmass shapes
            // Asia/Middle East region (simplified)
            val path = Path().apply {
                moveTo(size.width * 0.5f, size.height * 0.3f)
                cubicTo(
                    size.width * 0.6f, size.height * 0.25f,
                    size.width * 0.7f, size.height * 0.3f,
                    size.width * 0.75f, size.height * 0.4f
                )
                cubicTo(
                    size.width * 0.8f, size.height * 0.45f,
                    size.width * 0.75f, size.height * 0.55f,
                    size.width * 0.7f, size.height * 0.6f
                )
                cubicTo(
                    size.width * 0.65f, size.height * 0.65f,
                    size.width * 0.55f, size.height * 0.6f,
                    size.width * 0.5f, size.height * 0.55f
                )
                cubicTo(
                    size.width * 0.45f, size.height * 0.5f,
                    size.width * 0.45f, size.height * 0.35f,
                    size.width * 0.5f, size.height * 0.3f
                )
                close()
            }

            drawPath(
                path = path,
                color = continentColor
            )

            // India (simplified)
            val indiaPath = Path().apply {
                moveTo(size.width * 0.65f, size.height * 0.45f)
                cubicTo(
                    size.width * 0.67f, size.height * 0.4f,
                    size.width * 0.69f, size.height * 0.42f,
                    size.width * 0.7f, size.height * 0.45f
                )
                cubicTo(
                    size.width * 0.71f, size.height * 0.48f,
                    size.width * 0.68f, size.height * 0.51f,
                    size.width * 0.65f, size.height * 0.5f
                )
                cubicTo(
                    size.width * 0.63f, size.height * 0.49f,
                    size.width * 0.63f, size.height * 0.47f,
                    size.width * 0.65f, size.height * 0.45f
                )
                close()
            }

            drawPath(
                path = indiaPath,
                color = continentColor
            )

            // Arabian Peninsula (simplified)
            val arabiaPath = Path().apply {
                moveTo(size.width * 0.57f, size.height * 0.42f)
                cubicTo(
                    size.width * 0.61f, size.height * 0.4f,
                    size.width * 0.62f, size.height * 0.43f,
                    size.width * 0.63f, size.height * 0.45f
                )
                cubicTo(
                    size.width * 0.62f, size.height * 0.47f,
                    size.width * 0.6f, size.height * 0.48f,
                    size.width * 0.58f, size.height * 0.46f
                )
                cubicTo(
                    size.width * 0.56f, size.height * 0.45f,
                    size.width * 0.55f, size.height * 0.43f,
                    size.width * 0.57f, size.height * 0.42f
                )
                close()
            }

            drawPath(
                path = arabiaPath,
                color = continentColor
            )

            // Ocean overlay for depth illusion
            drawCircle(
                color = oceanOverlay,
                radius = size.width / 2
            )

            // Atmosphere glow effect
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color(0xFF29B6F6).copy(alpha = 0.1f)
                    ),
                    radius = size.width * 0.7f
                ),
                radius = size.width / 2
            )
        }

        // City lights (night side)
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Add some city lights as small dots
            repeat(60) {
                val x = Random.nextFloat() * size.width * 0.8f + size.width * 0.1f
                val y = Random.nextFloat() * size.height * 0.8f + size.height * 0.1f
                val radius = Random.nextFloat() * 1.5f + 0.5f
                val alpha = Random.nextFloat() * 0.5f + 0.3f

                drawCircle(
                    color = Color(0xFFFFEB3B).copy(alpha = alpha),
                    radius = radius,
                    center = Offset(x, y)
                )
            }

            // Major city cluster (India region)
            repeat(10) {
                val baseX = size.width * 0.67f
                val baseY = size.height * 0.45f
                val offsetX = (Random.nextFloat() - 0.5f) * size.width * 0.05f
                val offsetY = (Random.nextFloat() - 0.5f) * size.height * 0.05f

                drawCircle(
                    color = Color(0xFFFFEB3B).copy(alpha = 0.8f),
                    radius = Random.nextFloat() * 1.5f + 1f,
                    center = Offset(baseX + offsetX, baseY + offsetY)
                )
            }

            // Major city cluster (Dubai/Middle East region)
            repeat(10) {
                val baseX = size.width * 0.58f
                val baseY = size.height * 0.43f
                val offsetX = (Random.nextFloat() - 0.5f) * size.width * 0.04f
                val offsetY = (Random.nextFloat() - 0.5f) * size.height * 0.04f

                drawCircle(
                    color = Color(0xFFFFEB3B).copy(alpha = 0.8f),
                    radius = Random.nextFloat() * 1.5f + 1f,
                    center = Offset(baseX + offsetX, baseY + offsetY)
                )
            }
        }
    }
}



@Composable
fun FlightPathTrace(
    departLat: Double,
    departLon: Double,
    arriveLat: Double,
    arriveLon: Double,
    progress: Float
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val depX = (size.width * (departLon + 180) / 360).toFloat()
        val depY = (size.height * (90 - departLat) / 180).toFloat()
        val arrX = (size.width * (arriveLon + 180) / 360).toFloat()
        val arrY = (size.height * (90 - arriveLat) / 180).toFloat()

        // Draw dashed path line
        val flightPath = Path().apply {
            moveTo(depX, depY)

            // Create a curved path (great circle approximation)
            // Control point between the two locations, raised above the direct line
            val midX = (depX + arrX) / 2
            val midY = (depY + arrY) / 2
            val directDist = sqrt((arrX - depX).pow(2) + (arrY - depY).pow(2))
            val curveHeight = -directDist * 0.15f // Negative to curve "upward" on the map

            val controlX = midX
            val controlY = midY + curveHeight

            quadraticBezierTo(controlX, controlY, arrX, arrY)
        }

        // Draw dashed line for complete path
        drawPath(
            path = flightPath,
            color = Color.Gray.copy(alpha = 0.6f),
            style = Stroke(
                width = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
            )
        )
        var curveHeight= 0f
        // Draw solid line for completed portion of path
        val progressPath = Path().apply {
            moveTo(depX, depY)

            // Determine the point at progress % along the curve
            // For a quadratic Bezier curve: B(t) = (1-t)²P₀ + 2(1-t)tP₁ + t²P₂
            // where P₀ is departure, P₁ is control point, P₂ is arrival
            val midX = (depX + arrX) / 2
            val midY = (depY + arrY) / 2
            val directDist = sqrt((arrX - depX).pow(2) + (arrY - depY).pow(2))
            curveHeight= -directDist * 0.15f

            val controlX = midX
            val controlY = midY + curveHeight

            val t = progress
            val oneMinusT = 1 - t
            val curveX = oneMinusT.pow(2) * depX + 2 * oneMinusT * t * controlX + t.pow(2) * arrX
            val curveY = oneMinusT.pow(2) * depY + 2 * oneMinusT * t * controlY + t.pow(2) * arrY

            quadraticTo(controlX, controlY, curveX, curveY)
        }

        drawPath(
            path = progressPath,
            color = Color.Green,
            style = Stroke(width = 3f, cap = StrokeCap.Round)
        )

        // Draw departure and arrival points
        drawCircle(Color.Green, radius = 6f, center = Offset(depX, depY))
        drawCircle(Color.White, radius = 4f, center = Offset(depX, depY))

        drawCircle(Color.Green, radius = 6f, center = Offset(arrX, arrY))
        drawCircle(Color.White, radius = 4f, center = Offset(arrX, arrY))

        // Draw airplane at current position
        val t = progress
        val oneMinusT = 1 - t
        val planeX = oneMinusT.pow(2) * depX + 2 * oneMinusT * t * ((depX + arrX) / 2) + t.pow(2) * arrX
        val planeY = oneMinusT.pow(2) * depY + 2 * oneMinusT * t * ((depY + arrY) / 2 + curveHeight) + t.pow(2) * arrY

        // Calculate angle for plane orientation
        // For a quadratic Bezier curve, the derivative is B'(t) = 2(1-t)(P₁-P₀) + 2t(P₂-P₁)
        val tangentX = 2 * (1 - t) * ((depX + arrX) / 2 - depX) + 2 * t * (arrX - (depX + arrX) / 2)
        val tangentY = 2 * (1 - t) * ((depY + arrY) / 2 + curveHeight - depY) + 2 * t * (arrY - ((depY + arrY) / 2 + curveHeight))
        val angle = atan2(tangentY, tangentX) * (180 / Math.PI).toFloat()

        // Draw airplane
        drawContext.canvas.nativeCanvas.apply {
            save()
            translate(planeX, planeY)
            rotate(angle)

            // Draw simplified airplane shape
            val paint = Paint().apply {
                color = android.graphics.Color.WHITE
                style = Paint.Style.FILL
            }

            // Airplane body
            drawRect(-8f, -2f, 8f, 2f, paint)

            // Wings
            drawRect(-2f, -8f, 2f, 8f, paint)

            restore()
        }

        // Add city names
        drawContext.canvas.nativeCanvas.apply {
            val textPaint = Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 12.sp.toPx()
                textAlign = Paint.Align.CENTER
            }

            // Draw city names with slight offset
            drawText("Chennai", depX, depY + 20f, textPaint)
            drawText("Dubai", arrX, arrY + 20f, textPaint)
        }
    }
}

@Composable
fun InfoCardTrace(
    icon: Painter,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF1A2234)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = icon,
                contentDescription = subtitle,
                tint = Color.Green,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActionButtonTrace(
    icon: ImageVector,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF1A2234))
                .clickable { /* Handle click */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.Green
            )
        }

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FlightTrackerPreview() {
    FlightTrackerScreen()
}