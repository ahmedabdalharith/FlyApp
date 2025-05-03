package com.example.flyapp.ui.theme.screens

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.flyapp.R
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random
import androidx.core.graphics.scale

data class Aircraft(
    val id: Int,
    val callSign: String,
    val position: Offset,
    val heading: Float,
    val speed: Float,
    val altitude: Int,
    val aircraftType: AircraftType = AircraftType.COMMERCIAL,
    val size: Float = 12f,
    val threatLevel: ThreatLevel = ThreatLevel.NORMAL,
    val selected: Boolean = false,
    val iconAircraft : Int =AircraftType.COMMERCIAL.getImageVector()
)

enum class AircraftType {
    COMMERCIAL, MILITARY, PRIVATE, UNKNOWN;

    fun getDisplayName(): String {
        return when (this) {
            COMMERCIAL -> "Com"
            MILITARY -> "Mil"
            PRIVATE -> "Pri"
            UNKNOWN -> "Unk"
        }
    }

    fun getColor(): Color {
        return when (this) {
            COMMERCIAL -> Color(0xFF3498DB)
            MILITARY -> Color(0xFFE74C3C)
            PRIVATE -> Color(0xFF2ECC71)
            UNKNOWN -> Color(0xFFFFFFFF)
        }
    }
    fun getImageVector(): Int {
        return when (this) {
            COMMERCIAL -> R.drawable.airplane_up
            MILITARY -> R.drawable.airplane_up
            PRIVATE -> R.drawable.airplane_up
            UNKNOWN -> R.drawable.airplane_up
        }
    }
}

enum class ThreatLevel {
    LOW, NORMAL, HIGH, CRITICAL;

    fun getColor(): Color {
        return when (this) {
            LOW -> Color(0xFF2ECC71)
            NORMAL -> Color(0xFFFFFFFF)
            HIGH -> Color(0xFFFF9800)
            CRITICAL -> Color(0xFFFF0000)
        }
    }
}

enum class RadarMode {
    STANDARD, ENHANCED, THREAT_DETECTION, WEATHER;

    fun getDisplayName(): String {
        return when (this) {
            STANDARD -> "Standard Mode"
            ENHANCED -> "Enhanced Mode"
            THREAT_DETECTION -> "Threat Detection"
            WEATHER -> "Weather Overlay"
        }
    }

    fun getDescription(): String {
        return when (this) {
            STANDARD -> "Basic aircraft tracking"
            ENHANCED -> "Improved tracking with detailed signals"
            THREAT_DETECTION -> "Highlights potential threats and risks"
            WEATHER -> "Shows weather patterns and precipitation"
        }
    }
}

@Composable
fun RadarModeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    icon: Painter
) {
    val accentGreen = Color(0xFF00FF8F)

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF004455) else Color(0xFF002040),
            contentColor = if (isSelected) accentGreen else Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = if (isSelected) accentGreen else Color.White,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = text,
                fontSize = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun EnhancedAircraftRadarScreen() {
    // Theme colors
    val darkNavyBlue = Color(0xFF001034)
    val darkerNavyBlue = Color(0xFF000820)
    val accentGreen = Color(0xFF00FF8F)
    val accentBlue = Color(0xFF00A2FF)

    // UI State
    var radarMode by remember { mutableStateOf(RadarMode.STANDARD) }
    var zoomLevel by remember { mutableFloatStateOf(1f) }
    var showWeatherOverlay by remember { mutableStateOf(false) }
    var animateRadarLines by remember { mutableStateOf(true) }
    var selectedAircraftId by remember { mutableStateOf<Int?>(null) }
    var isPaused by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Radar Properties
    val radarCenterOffset = remember { Offset(500f, 500f) }
    val radarRadius = 480f * zoomLevel

    // Radar Animation
    val infiniteTransition = rememberInfiniteTransition(label = "radar_scan")
    val radarSweepAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radar_sweep"
    )

    // Call signs for aircraft
    val callSigns = remember {
        listOf("UA124", "DL389", "AA567", "SW236", "LH901", "BA475", "QF320", "AF221")
    }

    // Aircraft sample data
    val aircraftList = remember {
        mutableStateListOf(
            Aircraft(
                id = 1,
                callSign = callSigns[0],
                position = Offset(radarCenterOffset.x + 100f, radarCenterOffset.y - 150f),
                heading = 45f,
                speed = 500f,
                altitude = 35000,
                aircraftType = AircraftType.COMMERCIAL
            ),
            Aircraft(
                id = 2,
                callSign = callSigns[1],
                position = Offset(radarCenterOffset.x - 200f, radarCenterOffset.y + 100f),
                heading = 270f,
                speed = 450f,
                altitude = 29000,
                aircraftType = AircraftType.MILITARY,
                threatLevel = ThreatLevel.HIGH
            ),
            Aircraft(
                id = 3,
                callSign = callSigns[2],
                position = Offset(radarCenterOffset.x + 150f, radarCenterOffset.y + 220f),
                heading = 180f,
                speed = 520f,
                altitude = 32000,
                aircraftType = AircraftType.PRIVATE
            ),
            Aircraft(
                id = 4,
                callSign = callSigns[3],
                position = Offset(radarCenterOffset.x - 300f, radarCenterOffset.y - 180f),
                heading = 135f,
                speed = 480f,
                altitude = 31000,
                aircraftType = AircraftType.COMMERCIAL
            ),
            Aircraft(
                id = 5,
                callSign = callSigns[4],
                position = Offset(radarCenterOffset.x + 270f, radarCenterOffset.y - 50f),
                heading = 315f,
                speed = 510f,
                altitude = 37000,
                aircraftType = AircraftType.UNKNOWN,
                threatLevel = ThreatLevel.CRITICAL
            )
        )
    }

    // Weather data
    val weatherCells = remember {
        mutableStateListOf(
            Offset(radarCenterOffset.x + 200f, radarCenterOffset.y - 300f) to 150f,
            Offset(radarCenterOffset.x - 100f, radarCenterOffset.y + 350f) to 120f,
            Offset(radarCenterOffset.x + 350f, radarCenterOffset.y + 150f) to 180f
        )
    }

    // Radar Waves Animation
    val waveCount = 4
    val waveAnimations = List(waveCount) { index ->
        val infiniteTransition = rememberInfiniteTransition(label = "wave_$index")
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 5000,
                    easing = LinearEasing,
                    delayMillis = index * (5000 / waveCount)
                ),
                repeatMode = RepeatMode.Restart
            ),
            label = "wave_progress_$index"
        )
    }

    // Aircraft movement animation
    LaunchedEffect(key1 = isPaused) {
        while (!isPaused) {
            delay(100)
            for (i in aircraftList.indices) {
                val aircraft = aircraftList[i]
                // Update position based on heading and speed
                val radians = Math.toRadians(aircraft.heading.toDouble())
                val moveFactor = aircraft.speed / 10000f
                val newX = aircraft.position.x + (cos(radians) * moveFactor).toFloat()
                val newY = aircraft.position.y + (sin(radians) * moveFactor).toFloat()

                // Check if aircraft is still within radar range
                val distanceFromCenter =
                    sqrt(
                        (newX - radarCenterOffset.x).pow(2) +
                                (newY - radarCenterOffset.y).pow(2)
                    )

                if (distanceFromCenter < radarRadius) {
                    // Update aircraft position
                    aircraftList[i] = aircraft.copy(
                        position = Offset(newX, newY),
                        selected = aircraft.id == selectedAircraftId
                    )
                } else {
                    // Generate new aircraft from a random edge point
                    val angle = Random.nextFloat() * 360f
                    val entryRadians = Math.toRadians(angle.toDouble())
                    val entryX = radarCenterOffset.x + (cos(entryRadians) * (radarRadius * 0.95f)).toFloat()
                    val entryY = radarCenterOffset.y + (sin(entryRadians) * (radarRadius * 0.95f)).toFloat()
                    val newHeading = (angle + 180f) % 360f

                    // Assign random type and threat level
                    val newType = AircraftType.values()[Random.nextInt(AircraftType.values().size)]
                    val newThreatLevel = if (newType == AircraftType.MILITARY && Random.nextBoolean()) {
                        ThreatLevel.values()[Random.nextInt(3, ThreatLevel.values().size)]
                    } else {
                        ThreatLevel.values()[Random.nextInt(0, 2)]
                    }

                    aircraftList[i] = Aircraft(
                        id = aircraft.id,
                        callSign = callSigns[Random.nextInt(callSigns.size)],
                        position = Offset(entryX, entryY),
                        heading = newHeading,
                        speed = 400f + Random.nextFloat() * 200f,
                        altitude = 25000 + Random.nextInt(15000),
                        aircraftType = newType,
                        threatLevel = newThreatLevel,
                        selected = aircraft.id == selectedAircraftId
                    )
                }
            }

            // Move weather cells
            if (showWeatherOverlay) {
                for (i in weatherCells.indices) {
                    val (pos, size) = weatherCells[i]
                    val windDirection = 225f // simulate wind direction in degrees
                    val windSpeed = 0.5f
                    val radians = Math.toRadians(windDirection.toDouble())
                    val newX = pos.x + (cos(radians) * windSpeed).toFloat()
                    val newY = pos.y + (sin(radians) * windSpeed).toFloat()

                    // Check if weather is still within radar range
                    val distanceFromCenter =
                        sqrt(
                            (newX - radarCenterOffset.x).pow(2) +
                                    (newY - radarCenterOffset.y).pow(2)
                        )

                    if (distanceFromCenter < radarRadius * 1.5f) {
                        weatherCells[i] = Offset(newX, newY) to size
                    } else {
                        // Generate new weather cell from a random edge point
                        val angle = Random.nextFloat() * 360f
                        val entryRadians = Math.toRadians(angle.toDouble())
                        val entryX = radarCenterOffset.x + (cos(entryRadians) * (radarRadius * 0.95f)).toFloat()
                        val entryY = radarCenterOffset.y + (sin(entryRadians) * (radarRadius * 0.95f)).toFloat()

                        weatherCells[i] = Offset(entryX, entryY) to (80f + Random.nextFloat() * 150f)
                    }
                }
            }
        }
    }

    // Get selected aircraft
    val selectedAircraft = aircraftList.find { it.id == selectedAircraftId }

    // Main UI
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
        ,color = darkerNavyBlue
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Status bar with mode display
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(darkNavyBlue.copy(alpha = 0.8f))
                    .border(1.dp, accentGreen.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(accentGreen, CircleShape)
                    )
                    Text(
                        text = "AIR TRAFFIC CONTROL SYSTEM",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "MODE: ${radarMode.getDisplayName()}",
                        color = accentBlue,
                        fontSize = 14.sp
                    )

                    Button(
                        onClick = { isPaused = !isPaused },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPaused) Color.Red.copy(alpha = 0.7f) else accentGreen.copy(alpha = 0.7f)
                        ),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = if (isPaused) "RESUME" else "PAUSE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main content with radar and controls
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                Row {
                    // Left side: Controls
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight() ,
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF001A30).copy(alpha = 0.7f)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    accentGreen.copy(alpha = 0.3f),
                                    accentBlue.copy(alpha = 0.3f)
                                )
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Header
                            Text(
                                text = "RADAR CONTROLS",
                                color = accentGreen,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth()
                            )

                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.DarkGray
                            )

                            // Mode selection
                            Text(
                                text = "MODE",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )

                            // Mode buttons
                            RadarModeButton(
                                text = "STANDARD",
                                isSelected = radarMode == RadarMode.STANDARD,
                                onClick = {
                                    radarMode = RadarMode.STANDARD
                                    showWeatherOverlay = false
                                },
                                icon = painterResource(R.drawable.check_circle)
                            )

                            RadarModeButton(
                                text = "ENHANCED",
                                isSelected = radarMode == RadarMode.ENHANCED,
                                onClick = {
                                    radarMode = RadarMode.ENHANCED
                                    showWeatherOverlay = false
                                },
                                icon = painterResource(R.drawable.radar)
                            )

                            RadarModeButton(
                                text = "THREAT",
                                isSelected = radarMode == RadarMode.THREAT_DETECTION,
                                onClick = {
                                    radarMode = RadarMode.THREAT_DETECTION
                                    showWeatherOverlay = false
                                },
                                icon = painterResource(R.drawable.info_)
                            )

                            RadarModeButton(
                                text = "WEATHER",
                                isSelected = radarMode == RadarMode.WEATHER,
                                onClick = {
                                    radarMode = RadarMode.WEATHER
                                    showWeatherOverlay = true
                                },
                                icon = painterResource(R.drawable.clouds)
                            )

                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.DarkGray
                            )

                            // Zoom controls
                            Text(
                                text = "ZOOM LEVEL",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    onClick = { if (zoomLevel > 0.5f) zoomLevel -= 0.25f },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF002040))
                                ) {
                                    Icon(
                                        modifier = Modifier.padding(10.dp),
                                        painter = painterResource(R.drawable.minus_ic),
                                        contentDescription = "Zoom Out",
                                        tint = accentGreen
                                    )
                                }

                                Text(
                                    text = "${(zoomLevel * 100).toInt()}%",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )

                                IconButton(
                                    onClick = { if (zoomLevel < 2f) zoomLevel += 0.25f },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF002040))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Zoom In",
                                        tint = accentGreen
                                    )
                                }
                            }

                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.DarkGray
                            )

                            // Display options
                            Text(
                                text = "DISPLAY OPTIONS",
                                color = Color.Gray,
                                fontSize = 14.sp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = showWeatherOverlay,
                                    onCheckedChange = {
                                        showWeatherOverlay = it
                                        if (it) radarMode = RadarMode.WEATHER
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = accentGreen,
                                        uncheckedColor = Color.Gray
                                    )
                                )

                                Text(
                                    text = "Weather Overlay",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Checkbox(
                                    checked = animateRadarLines,
                                    onCheckedChange = { animateRadarLines = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = accentGreen,
                                        uncheckedColor = Color.Gray
                                    )
                                )

                                Text(
                                    text = "Animation",
                                    color = Color.White,
                                    fontSize = 14.sp
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Status text
                            Text(
                                text = "TRACKING: ${aircraftList.size} AIRCRAFT",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF001A30).copy(alpha = 0.7f)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            width = 1.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    accentBlue.copy(alpha = 0.3f),
                                    accentGreen.copy(alpha = 0.3f)
                                )
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "AIRCRAFT DETAILS",
                                    color = accentBlue,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = accentGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color.DarkGray
                            )

                            // Selected aircraft details
                            if (selectedAircraft != null) {
                                // Header with callsign
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF002040))
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .background(
                                                    selectedAircraft.aircraftType.getColor(),
                                                    CircleShape
                                                )
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = selectedAircraft.callSign,
                                            color = Color.White,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    // Threat indicator
                                    if (selectedAircraft.threatLevel != ThreatLevel.NORMAL) {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .background(
                                                    selectedAircraft.threatLevel.getColor()
                                                        .copy(alpha = 0.3f),
                                                    CircleShape
                                                )
                                                .border(
                                                    1.dp,
                                                    selectedAircraft.threatLevel.getColor(),
                                                    CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = when (selectedAircraft.threatLevel) {
                                                    ThreatLevel.LOW -> "L"
                                                    ThreatLevel.HIGH -> "H"
                                                    ThreatLevel.CRITICAL -> "!"
                                                    else -> ""
                                                },
                                                color = selectedAircraft.threatLevel.getColor(),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Aircraft details
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    DetailRow(
                                        label = "AIRCRAFT TYPE",
                                        value = selectedAircraft.aircraftType.getDisplayName(),
                                        color = selectedAircraft.aircraftType.getColor()
                                    )

                                    DetailRow(
                                        label = "ALTITUDE",
                                        value = "FL${selectedAircraft.altitude / 100} (${(selectedAircraft.altitude)/1000} ft/k)",
                                        color = accentGreen
                                    )

                                    DetailRow(
                                        label = "SPEED",
                                        value = "${selectedAircraft.speed.toInt()} knots",
                                        color = accentBlue
                                    )

                                    DetailRow(
                                        label = "HEADING",
                                        value = "${selectedAircraft.heading.toInt()}°",
                                        color = Color.White
                                    )

                                    DetailRow(
                                        label = "THREAT LEVEL",
                                        value = selectedAircraft.threatLevel.name,
                                        color = selectedAircraft.threatLevel.getColor()
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Aircraft actions
                                Text(
                                    text = "ACTIONS",
                                    color = Color.Gray,
                                    fontSize = 14.sp
                                )

                                Column (
                                    modifier = Modifier.wrapContentWidth().wrapContentHeight(),
                                ) {
                                    Button(
                                        onClick = { /* Handle tracking */

                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = accentGreen.copy(alpha = 0.5f)
                                        ),
                                        shape = RoundedCornerShape(4.dp),
                                    ) {
                                        Text(
                                            text = "TRACK",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Button(
                                        onClick = { /* Handle interception */ },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = accentBlue.copy(alpha = 0.5f)
                                        ),
                                        shape = RoundedCornerShape(4.dp),
                                    ) {
                                        Text(
                                            text = "INTERCEPT",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            } else {
                                // No aircraft selected state
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(150.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFF001428))
                                        .border(
                                            width = 1.dp,
                                            color = Color.DarkGray,
                                            shape = RoundedCornerShape(8.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "No aircraft selected.\nTap on an aircraft to view details.",
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            // Aircraft list
                            Text(
                                text = "AIRCRAFT IN RANGE",
                                color = accentBlue,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .shadow(4.dp, RoundedCornerShape(8.dp))
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF001428))
                                    .padding(8.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                // Aircraft list items
                                aircraftList.forEach { aircraft ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(
                                                if (aircraft.id == selectedAircraftId) {
                                                    Color(0xFF003366)
                                                } else {
                                                    Color.Transparent
                                                }
                                            )
                                            .clickable {
                                                selectedAircraftId = aircraft.id
                                            }
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(8.dp)
                                                    .background(
                                                        aircraft.aircraftType.getColor(),
                                                        CircleShape
                                                    )
                                            )

                                            Spacer(modifier = Modifier.width(8.dp))

                                            Text(
                                                text = aircraft.callSign,
                                                color = Color.White,
                                                fontSize = 14.sp,
                                                fontWeight = if (aircraft.id == selectedAircraftId) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = "FL${aircraft.altitude / 100}",
                                                color = Color.Gray,
                                                fontSize = 12.sp
                                            )

                                            if (aircraft.threatLevel == ThreatLevel.HIGH || aircraft.threatLevel == ThreatLevel.CRITICAL) {
                                                Spacer(modifier = Modifier.width(8.dp))

                                                Box(
                                                    modifier = Modifier
                                                        .size(8.dp)
                                                        .background(
                                                            aircraft.threatLevel.getColor(),
                                                            CircleShape
                                                        )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(
                    Modifier.height(16.dp)
                )
                Box(
                    modifier = Modifier
                ) {
                    // Radar Container
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .align(Alignment.Center),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 8.dp
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            width = 2.dp,
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    accentGreen.copy(alpha = 0.7f),
                                    accentBlue.copy(alpha = 0.3f)
                                )
                            )
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF001A2E),
                                            Color(0xFF000814)
                                        )
                                    )
                                )
                                .padding(4.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures { offset ->
                                        // Find tapped aircraft
                                        val canvasSize = size.toSize()
                                        val radarCenter =
                                            Offset(canvasSize.width / 2, canvasSize.height / 2)
                                        val actualRadius = minOf(
                                            canvasSize.width,
                                            canvasSize.height
                                        ) * 0.4f * zoomLevel

                                        // Convert screen coordinates to radar coordinates for each aircraft
                                        for (aircraft in aircraftList) {
                                            val relativeX =
                                                (aircraft.position.x - radarCenterOffset.x) / radarRadius * actualRadius
                                            val relativeY =
                                                (aircraft.position.y - radarCenterOffset.y) / radarRadius * actualRadius
                                            val screenPos = Offset(
                                                radarCenter.x + relativeX,
                                                radarCenter.y + relativeY
                                            )

                                            // Check if user tapped near this aircraft
                                            val tapDistance = sqrt(
                                                (offset.x - screenPos.x).pow(2) +
                                                        (offset.y - screenPos.y).pow(2)
                                            )

                                            // Adjust tap threshold based on zoom
                                            val tapThreshold = 30f * zoomLevel

                                            if (tapDistance <= tapThreshold) {
                                                selectedAircraftId =
                                                    if (selectedAircraftId == aircraft.id) null else aircraft.id
                                                break
                                            }
                                        }
                                    }
                                }
                        ) {
                            // Radar Canvas
                            Canvas(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                val radarBgColor = when (radarMode) {
                                    RadarMode.STANDARD -> Color(0xFF001A2E)
                                    RadarMode.ENHANCED -> Color(0xFF001E35)
                                    RadarMode.THREAT_DETECTION -> Color(0xFF1A1A2E)
                                    RadarMode.WEATHER -> Color(0xFF001A3A)
                                }

                                val gridColor = when (radarMode) {
                                    RadarMode.STANDARD -> Color(0xFF0A5C64).copy(alpha = 0.6f)
                                    RadarMode.ENHANCED -> Color(0xFF0A7C84).copy(alpha = 0.7f)
                                    RadarMode.THREAT_DETECTION -> Color(0xFF5C5C0A).copy(alpha = 0.6f)
                                    RadarMode.WEATHER -> Color(0xFF0A5C94).copy(alpha = 0.6f)
                                }

                                val sweepColor = when (radarMode) {
                                    RadarMode.STANDARD -> Color(0xFF00FF00).copy(alpha = 0.7f)
                                    RadarMode.ENHANCED -> Color(0xFF00FFFF).copy(alpha = 0.8f)
                                    RadarMode.THREAT_DETECTION -> Color(0xFFFFFF00).copy(alpha = 0.7f)
                                    RadarMode.WEATHER -> Color(0xFF00AAFF).copy(alpha = 0.7f)
                                }

                                // Adjust radar center based on the current canvas size
                                val adjustedCenter = Offset(size.width / 2, size.height / 2)
                                val adjustedRadius = minOf(size.width, size.height) * 0.4f * zoomLevel

                                // Radar background
                                drawCircle(
                                    color = radarBgColor,
                                    radius = adjustedRadius,
                                    center = adjustedCenter,
                                    style = Stroke(width = 2f)
                                )

                                // Radar glow effect
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF003366).copy(alpha = 0.2f),
                                            Color(0xFF001A2E).copy(alpha = 0.0f)
                                        ),
                                        center = adjustedCenter,
                                        radius = adjustedRadius * 1.5f
                                    ),
                                    center = adjustedCenter,
                                    radius = adjustedRadius
                                )

                                // Radar grid circles
                                for (i in 1..4) {
                                    drawCircle(
                                        color = gridColor,
                                        radius = adjustedRadius * (i / 4f),
                                        center = adjustedCenter,
                                        style = Stroke(width = 1f)
                                    )
                                }

                                // Radar grid lines
                                for (i in 0 until 8) {
                                    val angle = i * 45f
                                    val radians = Math.toRadians(angle.toDouble())
                                    val x2 = adjustedCenter.x + (cos(radians) * adjustedRadius).toFloat()
                                    val y2 = adjustedCenter.y + (sin(radians) * adjustedRadius).toFloat()

                                    drawLine(
                                        color = gridColor,
                                        start = adjustedCenter,
                                        end = Offset(x2, y2),
                                        strokeWidth = 1f
                                    )
                                }

                                // Radar sweep animation
                                if (animateRadarLines) {
                                    val sweepPath = Path().apply {
                                        moveTo(adjustedCenter.x, adjustedCenter.y)
                                        lineTo(
                                            adjustedCenter.x + cos(Math.toRadians(radarSweepAngle.toDouble())).toFloat() * adjustedRadius,
                                            adjustedCenter.y + sin(Math.toRadians(radarSweepAngle.toDouble())).toFloat() * adjustedRadius
                                        )
                                        close()
                                    }

                                    drawPath(
                                        path = sweepPath,
                                        color = sweepColor,
                                        style = Stroke(width = 2f)
                                    )

                                    // Fading trail
                                    val trailAngle = (radarSweepAngle - 30) % 360
                                    val trailPath = Path().apply {
                                        moveTo(adjustedCenter.x, adjustedCenter.y)
                                        arcTo(
                                            rect = Rect(
                                                adjustedCenter.x - adjustedRadius,
                                                adjustedCenter.y - adjustedRadius,
                                                adjustedCenter.x + adjustedRadius,
                                                adjustedCenter.y + adjustedRadius
                                            ),
                                            startAngleDegrees = trailAngle,
                                            sweepAngleDegrees = 30f,
                                            forceMoveTo = false
                                        )
                                        close()
                                    }

                                    drawPath(
                                        path = trailPath,
                                        brush = Brush.radialGradient(
                                            colors = listOf(
                                                sweepColor.copy(alpha = 0.4f),
                                                sweepColor.copy(alpha = 0.0f)
                                            ),
                                            center = adjustedCenter,
                                            radius = adjustedRadius
                                        ),
                                        style = Fill
                                    )
                                }

                                // Radar waves animation
                                if (animateRadarLines) {
                                    waveAnimations.forEach { animatedValue ->
                                        val waveProgress = animatedValue.value
                                        drawCircle(
                                            color = sweepColor.copy(alpha = (1 - waveProgress) * 0.4f),
                                            radius = adjustedRadius * waveProgress,
                                            center = adjustedCenter,
                                            style = Stroke(width = 2f)
                                        )
                                    }
                                }

                                // Weather overlay if enabled
                                if (showWeatherOverlay) {
                                    weatherCells.forEach { (position, size) ->
                                        // Convert radar coordinates to screen coordinates
                                        val relativeX = (position.x - radarCenterOffset.x) / radarRadius * adjustedRadius
                                        val relativeY = (position.y - radarCenterOffset.y) / radarRadius * adjustedRadius
                                        val screenPos = Offset(
                                            adjustedCenter.x + relativeX,
                                            adjustedCenter.y + relativeY
                                        )

                                        // Draw weather cells with different colors based on intensity
                                        val scaledSize = (size / radarRadius) * adjustedRadius
                                        val weatherIntensity = size / 200f // 0.0 to 1.0 based on size
                                        val weatherColor = when {
                                            weatherIntensity > 0.8f -> Color(0xFFFF0000) // Red for heavy
                                            weatherIntensity > 0.5f -> Color(0xFFFF6600) // Orange for moderate
                                            else -> Color(0xFF00AAFF) // Blue for light
                                        }

                                        drawCircle(
                                            brush = Brush.radialGradient(
                                                colors = listOf(
                                                    weatherColor.copy(alpha = 0.7f),
                                                    weatherColor.copy(alpha = 0.0f)
                                                ),
                                                center = screenPos,
                                                radius = scaledSize
                                            ),
                                            radius = scaledSize,
                                            center = screenPos
                                        )
                                    }
                                }

                                // Draw aircraft
                                aircraftList.forEach { aircraft ->
                                    // Convert radar coordinates to screen coordinates
                                    val relativeX = (aircraft.position.x - radarCenterOffset.x) / radarRadius * adjustedRadius
                                    val relativeY = (aircraft.position.y - radarCenterOffset.y) / radarRadius * adjustedRadius
                                    val screenPos = Offset(
                                        adjustedCenter.x + relativeX,
                                        adjustedCenter.y + relativeY
                                    )

                                    // Aircraft icon - different shape based on type
                                    when (aircraft.aircraftType) {
                                        AircraftType.COMMERCIAL -> {
                                            // Commercial aircraft (diamond shape)
                                            val size = 6f * zoomLevel
                                            val path = Path().apply {
                                                moveTo(screenPos.x, screenPos.y - size)
                                                lineTo(screenPos.x + size, screenPos.y)
                                                lineTo(screenPos.x, screenPos.y + size)
                                                lineTo(screenPos.x - size, screenPos.y)
                                                close()
                                            }

                                            // Draw aircraft
                                            drawPath(
                                                path = path,
                                                color = aircraft.aircraftType.getColor(),
                                                style = Fill
                                            )
                                        }
                                        AircraftType.MILITARY -> {
                                            // Military aircraft (triangle shape)
                                            val size = 6f * zoomLevel
                                            val path = Path().apply {
                                                moveTo(screenPos.x, screenPos.y - size)
                                                lineTo(screenPos.x + size, screenPos.y + size)
                                                lineTo(screenPos.x - size, screenPos.y + size)
                                                close()
                                            }

                                            // Draw aircraft
                                            drawPath(
                                                path = path,
                                                color = aircraft.aircraftType.getColor(),
                                                style = Fill
                                            )
                                        }
                                        AircraftType.PRIVATE -> {
                                            // Private aircraft (circle shape)
                                            drawCircle(
                                                color = aircraft.aircraftType.getColor(),
                                                radius = 5f * zoomLevel,
                                                center = screenPos,
                                                style = Fill
                                            )
                                        }
                                        AircraftType.UNKNOWN -> {
                                            // Unknown aircraft (square shape)
                                            val size = 5f * zoomLevel
                                            val path = Path().apply {
                                                moveTo(screenPos.x - size, screenPos.y - size)
                                                lineTo(screenPos.x + size, screenPos.y - size)
                                                lineTo(screenPos.x + size, screenPos.y + size)
                                                lineTo(screenPos.x - size, screenPos.y + size)
                                                close()
                                            }

                                            // Draw aircraft
                                            drawPath(
                                                path = path,
                                                color = aircraft.aircraftType.getColor(),
                                                style = Fill
                                            )
                                        }
                                    }

                                    // Draw heading indicator
                                    val headingSize = 16f * zoomLevel
                                    val radians = Math.toRadians(aircraft.heading.toDouble())
                                    val headingX = screenPos.x + (cos(radians) * headingSize).toFloat()
                                    val headingY = screenPos.y + (sin(radians) * headingSize).toFloat()

                                    drawLine(
                                        color = aircraft.aircraftType.getColor().copy(alpha = 0.7f),
                                        start = screenPos,
                                        end = Offset(headingX, headingY),
                                        strokeWidth = 1.5f * zoomLevel
                                    )

                                    // If threat detection mode or threat level is high, show threat circle
                                    if (radarMode == RadarMode.THREAT_DETECTION ||
                                        aircraft.threatLevel == ThreatLevel.HIGH ||
                                        aircraft.threatLevel == ThreatLevel.CRITICAL) {

                                        val threatRadius = when (aircraft.threatLevel) {
                                            ThreatLevel.LOW -> 10f
                                            ThreatLevel.NORMAL -> 12f
                                            ThreatLevel.HIGH -> 18f
                                            ThreatLevel.CRITICAL -> 25f
                                        } * zoomLevel

                                        drawCircle(
                                            color = aircraft.threatLevel.getColor().copy(alpha = 0.3f),
                                            radius = threatRadius,
                                            center = screenPos,
                                            style = Fill
                                        )

                                        drawCircle(
                                            color = aircraft.threatLevel.getColor().copy(alpha = 0.7f),
                                            radius = threatRadius,
                                            center = screenPos,
                                            style = Stroke(width = 1f)
                                        )
                                    }

                                    // Draw selection indicator
                                    if (aircraft.selected) {
                                        drawCircle(
                                            color = accentGreen.copy(alpha = 0.7f),
                                            radius = 20f * zoomLevel,
                                            center = screenPos,
                                            style = Stroke(width = 2f)
                                        )

                                        // Pulsing effect for selected aircraft
                                        val pulseFactor = (sin(System.currentTimeMillis() / 300.0) * 0.2 + 0.8).toFloat()
                                        drawCircle(
                                            color = accentGreen.copy(alpha = 0.3f * pulseFactor),
                                            radius = 30f * zoomLevel * pulseFactor,
                                            center = screenPos,
                                            style = Stroke(width = 1f)
                                        )
                                    }

                                    // Draw altitude text for enhanced mode
                                    if (radarMode == RadarMode.ENHANCED || aircraft.selected) {
                                        rotate(degrees = 0f) {
                                            drawContext.canvas.nativeCanvas.drawText(
                                                "FL${aircraft.altitude / 100}",
                                                screenPos.x + 10f * zoomLevel,
                                                screenPos.y - 10f * zoomLevel,
                                                android.graphics.Paint().apply {
                                                    color = android.graphics.Color.WHITE
                                                    textSize = 10f * zoomLevel
                                                    isFakeBoldText = true
                                                }
                                            )
                                        }
                                    }

                                    // Draw callsign for selected or enhanced mode
                                    if (aircraft.selected || radarMode == RadarMode.ENHANCED) {
                                        rotate(degrees = 0f) {
                                            drawContext.canvas.nativeCanvas.drawText(
                                                aircraft.callSign,
                                                screenPos.x + 10f * zoomLevel,
                                                screenPos.y,
                                                android.graphics.Paint().apply {
                                                    color = if (aircraft.selected)
                                                        android.graphics.Color.rgb(0, 255, 143)
                                                    else
                                                        android.graphics.Color.WHITE
                                                    textSize = 12f * zoomLevel
                                                    isFakeBoldText = true
                                                }
                                            )
                                        }
                                    }
                                }

                                // Draw compass markers
                                val compassRadius = adjustedRadius + 20
                                val directions = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
                                directions.forEachIndexed { index, direction ->
                                    val angle = index * 45.0
                                    val radians = Math.toRadians(angle)
                                    val x = adjustedCenter.x + (cos(radians) * compassRadius).toFloat()
                                    val y = adjustedCenter.y + (sin(radians) * compassRadius).toFloat()

                                    rotate(degrees = 0f) {
                                        drawContext.canvas.nativeCanvas.drawText(
                                            direction,
                                            x - 5f,
                                            y + 5f,
                                            android.graphics.Paint().apply {
                                                color = android.graphics.Color.rgb(0, 162, 255)
                                                textSize = 12f
                                                isFakeBoldText = true
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )

        Text(
            text = value,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
// Aircraft drawing function using realistic aircraft images
fun drawAircraft(
    canvas: Canvas,
    aircraft: Aircraft,
    screenPos: Offset,
    zoomLevel: Float,
    resources: Resources,
    rotationDegrees: Float = 0f
) {
    // Get the appropriate aircraft bitmap based on type
    val bitmapResId = when (aircraft.aircraftType) {
        AircraftType.COMMERCIAL -> R.drawable.myplane
        AircraftType.MILITARY -> R.drawable.myplane
        AircraftType.PRIVATE -> R.drawable.myplane
        else -> R.drawable.myplane
    }

    // Base size of the aircraft image (adjust as needed)
    val baseSize = when (aircraft.aircraftType) {
        AircraftType.COMMERCIAL -> 24
        AircraftType.MILITARY -> 20
        AircraftType.PRIVATE -> 16
        else -> 12
    }

    // Scale size based on zoom level
    val scaledSize = (baseSize * zoomLevel).toInt()

    // Load and scale the bitmap
    val originalBitmap = BitmapFactory.decodeResource(resources, bitmapResId)
    val scaledBitmap = originalBitmap.scale(scaledSize, scaledSize)

    // Create a matrix for rotation
    val matrix = Matrix().apply {
        // Rotate around the center of the bitmap
        postRotate(
            rotationDegrees,
            scaledBitmap.width / 2f,
            scaledBitmap.height / 2f
        )
        // Position the bitmap with its center at the screen position
        postTranslate(
            screenPos.x - scaledBitmap.width / 2f,
            screenPos.y - scaledBitmap.height / 2f
        )
    }

    // Draw the bitmap with rotation
    canvas.drawBitmap(scaledBitmap, matrix, null)

    // Clean up to prevent memory leaks
    if (scaledBitmap != originalBitmap) {
        scaledBitmap.recycle()
    }
    originalBitmap.recycle()
}

@Preview(showBackground = true)
@Composable
fun EnhancedAircraftRadarPreview() {
    EnhancedAircraftRadarScreen()
}