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
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import com.example.flyapp.ui.theme.data.models.SeatInfo
import com.example.flyapp.ui.theme.data.models.SeatRow
import com.example.flyapp.ui.theme.data.models.SeatType
import com.example.flyapp.ui.theme.navigition.Screen


@Composable
fun AirplaneSeatsScreen(navController: NavHostController) {
    // Create cabin layout
    val firstClassRows = (1..2)
    val businessRows = (3..7)
    val economyRows = (8..32)

    val allRows = remember {
        buildList {
            firstClassRows.forEach { add(SeatRow(it, SeatType.FIRST_CLASS)) }
            businessRows.forEach { add(SeatRow(it, SeatType.BUSINESS)) }
            economyRows.forEach { add(SeatRow(it, SeatType.ECONOMY)) }
        }
    }

    // Create seats data
    val seats = remember {
        mutableStateListOf<SeatInfo>().apply {
            // First class seats (2 on each side)
            firstClassRows.forEach { row ->
                // Left side
                add(SeatInfo("${row}A", row, "A", SeatType.FIRST_CLASS, false, Math.random() > 0.3))
                add(SeatInfo("${row}B", row, "B", SeatType.FIRST_CLASS, false, Math.random() > 0.3))

                // Right side
                add(SeatInfo("${row}J", row, "J", SeatType.FIRST_CLASS, false, Math.random() > 0.3))
                add(SeatInfo("${row}K", row, "K", SeatType.FIRST_CLASS, false, Math.random() > 0.3))
            }

            // Business class seats (2-2-2 configuration)
            businessRows.forEach { row ->
                // Left side
                add(SeatInfo("${row}A", row, "A", SeatType.BUSINESS, false, Math.random() > 0.3))
                add(SeatInfo("${row}C", row, "C", SeatType.BUSINESS, false, Math.random() > 0.3))

                // Middle
                add(SeatInfo("${row}D", row, "D", SeatType.BUSINESS, false, Math.random() > 0.3))
                add(SeatInfo("${row}G", row, "G", SeatType.BUSINESS, false, Math.random() > 0.3))

                // Right side
                add(SeatInfo("${row}H", row, "H", SeatType.BUSINESS, false, Math.random() > 0.3))
                add(SeatInfo("${row}K", row, "K", SeatType.BUSINESS, false, Math.random() > 0.3))
            }

            // Economy class seats (3-3-3 configuration)
            economyRows.forEach { row ->
                // Left section
                add(SeatInfo("${row}A", row, "A", SeatType.ECONOMY, false, Math.random() > 0.3))
                add(SeatInfo("${row}B", row, "B", SeatType.ECONOMY, false, Math.random() > 0.3))
                add(SeatInfo("${row}C", row, "C", SeatType.ECONOMY, false, Math.random() > 0.3))

                // Middle section
                add(SeatInfo("${row}D", row, "D", SeatType.ECONOMY, false, Math.random() > 0.3))
                add(SeatInfo("${row}E", row, "E", SeatType.ECONOMY, false, Math.random() > 0.3))
                add(SeatInfo("${row}F", row, "F", SeatType.ECONOMY, false, Math.random() > 0.3))

                // Right section
                add(SeatInfo("${row}G", row, "G", SeatType.ECONOMY, false, Math.random() > 0.3))
                add(SeatInfo("${row}H", row, "H", SeatType.ECONOMY, false, Math.random() > 0.3))
                add(SeatInfo("${row}K", row, "K", SeatType.ECONOMY, false, Math.random() > 0.3))
            }
        }
    }

    var selectedSeats = remember { mutableStateListOf<String>() }
    var currentClassView by remember { mutableStateOf(SeatType.ECONOMY) }

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
        // Background star particles
        ParticleEffectBackground()

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with Flight Info
            FlightInfoHeader()

            // Class selector tabs
            ClassSelectorTabs(
                currentClass = currentClassView,
                onClassSelected = { currentClassView = it }
            )

            // Cabin visualization
            AirplaneCabinView(currentClassView)

            // Seats grid inside cabin view
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF0A192F).copy(alpha = 0.85f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    // Header legend
                    SeatsLegend()

                    // Divider
                    Divider(
                        color = Color(0xFF64B5F6).copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    // Seats rows
                    LazyColumn(
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        val rowsToShow = when (currentClassView) {
                            SeatType.FIRST_CLASS -> allRows.filter { it.seatType == SeatType.FIRST_CLASS }
                            SeatType.BUSINESS -> allRows.filter { it.seatType == SeatType.BUSINESS }
                            SeatType.ECONOMY -> allRows.filter { it.seatType == SeatType.ECONOMY }
                        }

                        itemsIndexed(rowsToShow) { index, rowData ->
                            val rowNumber = rowData.rowNumber
                            val rowSeats = seats.filter { it.row == rowNumber }

                            SeatRowItem(
                                rowNumber = rowNumber,
                                seats = rowSeats,
                                seatType = rowData.seatType,
                                onSeatSelected = { seat ->
                                    if (seat.isAvailable) {
                                        val targetSeat = seats.find { it.id == seat.id }
                                        targetSeat?.let {
                                            it.isSelected = !it.isSelected
                                            if (it.isSelected) {
                                                selectedSeats.add(it.id)
                                            } else {
                                                selectedSeats.remove(it.id)
                                            }
                                        }
                                    }
                                }
                            )

                            // Add some space between cabin sections
                            if (index == firstClassRows.count() - 1 || index == firstClassRows.count() + businessRows.count() - 1) {
                                CabinDivider()
                            }
                        }
                    }
                }
            }

            // Selected seats info and continue button
            SelectedSeatsInfo(
                selectedSeats = selectedSeats.toList(),
                onContinueClick = {
                    // Navigate to payment/confirmation screen
                    navController.navigate(Screen.PaymentScreen.route)
                }
            )
        }
    }
}

@Composable
fun FlightInfoHeader() {
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
            // Departure info
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "Riyadh",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "RUH",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Text(
                    text = "10:30 AM",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }

            // Flight details
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
                        painter = painterResource(R.drawable.hotel),
                        contentDescription = "Flight",
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = "Flight SV 1734",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Text(
                    text = "2h 15m",
                    color = Color(0xFFBAE6FF),
                    fontSize = 12.sp
                )
            }

            // Arrival info
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Dubai",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = "DXB",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Text(
                    text = "12:45 PM",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun ClassSelectorTabs(currentClass: SeatType, onClassSelected: (SeatType) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SeatTypeTab(
            title = "First Class",
            isSelected = currentClass == SeatType.FIRST_CLASS,
            color = Color(0xFFFFD700),
            onClick = { onClassSelected(SeatType.FIRST_CLASS) }
        )

        SeatTypeTab(
            title = "Business Class",
            isSelected = currentClass == SeatType.BUSINESS,
            color = Color(0xFF03A9F4),
            onClick = { onClassSelected(SeatType.BUSINESS) }
        )

        SeatTypeTab(
            title = "Economy Class",
            isSelected = currentClass == SeatType.ECONOMY,
            color = Color(0xFF4CAF50),
            onClick = { onClassSelected(SeatType.ECONOMY) }
        )
    }
}

@Composable
fun SeatTypeTab(title: String, isSelected: Boolean, color: Color, onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "tab_animation")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tab_glow"
    )

    val scale = if (isSelected) 1.05f else 1f
    val alpha = if (isSelected) 1f else 0.6f
    val borderAlpha = if (isSelected) glow else 0f

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) color.copy(alpha = 0.2f) else Color.Transparent
            )
            .border(
                width = 1.dp,
                color = color.copy(alpha = borderAlpha),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            color = color.copy(alpha = alpha),
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun AirplaneCabinView(currentClass: SeatType) {
    val cabinColor = when (currentClass) {
        SeatType.FIRST_CLASS -> Color(0xFFFFD700)
        SeatType.BUSINESS -> Color(0xFF03A9F4)
        SeatType.ECONOMY -> Color(0xFF4CAF50)
    }

    // Animation for cabin glow
    val infiniteTransition = rememberInfiniteTransition(label = "cabin_animation")
    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cabin_glow"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        // Background glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Main cabin body
            val cabinPath = Path().apply {
                val width = size.width
                val height = size.height

                // Draw fuselage shape
                moveTo(width * 0.1f, height * 0.5f)
                quadraticTo(width * 0.1f, height * 0.2f, width * 0.2f, height * 0.2f)
                lineTo(width * 0.8f, height * 0.2f)
                quadraticTo(width * 0.9f, height * 0.2f, width * 0.9f, height * 0.5f)
                quadraticTo(width * 0.9f, height * 0.8f, width * 0.8f, height * 0.8f)
                lineTo(width * 0.2f, height * 0.8f)
                quadraticTo(width * 0.1f, height * 0.8f, width * 0.1f, height * 0.5f)
                close()
            }

            // Draw cabin outline with glow
            drawPath(
                path = cabinPath,
                color = cabinColor.copy(alpha = 0.2f),
            )

            // Draw cabin outline
            drawPath(
                path = cabinPath,
                color = cabinColor.copy(alpha = glowIntensity),
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )

            // Cabin windows visualization based on class
            when (currentClass) {
                SeatType.FIRST_CLASS -> {
                    // Fewer, larger windows for first class
                    for (i in 1..4) {
                        val x = size.width * (0.2f + i * 0.15f)
                        drawCircle(
                            color = cabinColor.copy(alpha = 0.6f),
                            radius = size.width * 0.02f,
                            center = Offset(x, size.height * 0.2f)
                        )
                        drawCircle(
                            color = cabinColor.copy(alpha = 0.6f),
                            radius = size.width * 0.02f,
                            center = Offset(x, size.height * 0.8f)
                        )
                    }
                }
                SeatType.BUSINESS -> {
                    // Medium density windows
                    for (i in 1..6) {
                        val x = size.width * (0.2f + i * 0.1f)
                        drawCircle(
                            color = cabinColor.copy(alpha = 0.6f),
                            radius = size.width * 0.015f,
                            center = Offset(x, size.height * 0.2f)
                        )
                        drawCircle(
                            color = cabinColor.copy(alpha = 0.6f),
                            radius = size.width * 0.015f,
                            center = Offset(x, size.height * 0.8f)
                        )
                    }
                }
                SeatType.ECONOMY -> {
                    // Many, smaller windows for economy
                    for (i in 1..10) {
                        val x = size.width * (0.15f + i * 0.07f)
                        drawCircle(
                            color = cabinColor.copy(alpha = 0.6f),
                            radius = size.width * 0.01f,
                            center = Offset(x, size.height * 0.2f)
                        )
                        drawCircle(
                            color = cabinColor.copy(alpha = 0.6f),
                            radius = size.width * 0.01f,
                            center = Offset(x, size.height * 0.8f)
                        )
                    }
                }
            }

            // Draw cabin aisle
            drawLine(
                color = cabinColor.copy(alpha = 0.4f),
                start = Offset(size.width * 0.1f, size.height * 0.5f),
                end = Offset(size.width * 0.9f, size.height * 0.5f),
                strokeWidth = 2.dp.toPx()
            )

            // Draw aisle markers
            for (i in 0..10) {
                val x = size.width * (0.1f + i * 0.08f)
                drawLine(
                    color = cabinColor.copy(alpha = 0.2f),
                    start = Offset(x, size.height * 0.45f),
                    end = Offset(x, size.height * 0.55f),
                    strokeWidth = 1.dp.toPx()
                )
            }
        }

        // Class type label
        Text(
            text = when (currentClass) {
                SeatType.FIRST_CLASS -> "First Class"
                SeatType.BUSINESS -> "Business Class"
                SeatType.ECONOMY -> "Economy Class"
            },
            color = cabinColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SeatsLegend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        LegendItem(color = Color(0xFF4CAF50), text = "Available")
        LegendItem(color = Color(0xFFFF5722), text = "Occupied")
        LegendItem(color = Color(0xFF2196F3), text = "Selected")
        LegendItem(color = Color(0xFFE0E0E0).copy(alpha = 0.5f), text = "Exit Row")
    }
}

@Composable
fun LegendItem(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Text(
            text = text,
            color = Color.White.copy(alpha = 0.8f),
            fontSize = 10.sp
        )
    }
}

@Composable
fun SeatRowItem(
    rowNumber: Int,
    seats: List<SeatInfo>,
    seatType: SeatType,
    onSeatSelected: (SeatInfo) -> Unit
) {
    // Show different seat layouts based on class
    val layout = when (seatType) {
        SeatType.FIRST_CLASS -> {
            // 2-0-2 layout for first class
            val leftSeats = seats.filter { it.column in listOf("A", "B") }.sortedBy { it.column }
            val rightSeats = seats.filter { it.column in listOf("J", "K") }.sortedBy { it.column }
            Triple(leftSeats, emptyList(), rightSeats)
        }
        SeatType.BUSINESS -> {
            // 2-2-2 layout for business
            val leftSeats = seats.filter { it.column in listOf("A", "C") }.sortedBy { it.column }
            val middleSeats = seats.filter { it.column in listOf("D", "G") }.sortedBy { it.column }
            val rightSeats = seats.filter { it.column in listOf("H", "K") }.sortedBy { it.column }
            Triple(leftSeats, middleSeats, rightSeats)
        }
        SeatType.ECONOMY -> {
            // 3-3-3 layout for economy
            val leftSeats = seats.filter { it.column in listOf("A", "B", "C") }.sortedBy { it.column }
            val middleSeats = seats.filter { it.column in listOf("D", "E", "F") }.sortedBy { it.column }
            val rightSeats = seats.filter { it.column in listOf("G", "H", "K") }.sortedBy { it.column }
            Triple(leftSeats, middleSeats, rightSeats)
        }
    }

    // Check if this row is an emergency exit row
    val isExitRow = rowNumber in listOf(8, 21)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .background(
                if (isExitRow) Color(0xFF1E3E5E) else Color.Transparent,
                RoundedCornerShape(4.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = if (isExitRow) 6.dp else 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left section
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(start = 4.dp)
            ) {
                layout.first.forEach { seat ->
                    SeatItem(
                        seat = seat,
                        seatType = seatType,
                        onSeatSelected = { onSeatSelected(seat) }
                    )
                }
            }

            // Row number with icon if exit row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isExitRow) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Exit Row",
                        tint = Color(0xFF64B5F6),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                }

                Text(
                    text = rowNumber.toString(),
                    color = if (isExitRow) Color(0xFF64B5F6) else Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    fontWeight = if (isExitRow) FontWeight.Bold else FontWeight.Normal
                )
            }

            // Middle section (if any)
            if (layout.second.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    layout.second.forEach { seat ->
                        SeatItem(
                            seat = seat,
                            seatType = seatType,
                            onSeatSelected = { onSeatSelected(seat) }
                        )
                    }
                }
            }

            // Right section
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(end = 4.dp)
            ) {
                layout.third.forEach { seat ->
                    SeatItem(
                        seat = seat,
                        seatType = seatType,
                        onSeatSelected = { onSeatSelected(seat) }
                    )
                }
            }
        }
    }
}

@Composable
fun SeatItem(
    seat: SeatInfo,
    seatType: SeatType,
    onSeatSelected: () -> Unit
) {
    val seatColor = when {
        !seat.isAvailable -> Color(0xFFFF5722)
        seat.isSelected -> Color(0xFF2196F3)
        else -> when (seatType) {
            SeatType.FIRST_CLASS -> Color(0xFFFFD700).copy(alpha = 0.8f)
            SeatType.BUSINESS -> Color(0xFF03A9F4).copy(alpha = 0.8f)
            SeatType.ECONOMY -> Color(0xFF4CAF50).copy(alpha = 0.8f)
        }
    }

    // Animation for selected seats
    val scale = if (seat.isSelected) 1.1f else 1f
    val borderWidth = if (seat.isSelected) 1.dp else 0.dp

    val infiniteTransition = rememberInfiniteTransition(label = "seat_animation")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "seat_pulse"
    )

    val alpha = if (seat.isSelected) pulseAlpha else 0.8f

    Box(
        modifier = Modifier
            .size(
                width = when (seatType) {
                    SeatType.FIRST_CLASS -> 36.dp
                    SeatType.BUSINESS -> 30.dp
                    SeatType.ECONOMY -> 24.dp
                },
                height = when (seatType) {
                    SeatType.FIRST_CLASS -> 36.dp
                    SeatType.BUSINESS -> 30.dp
                    SeatType.ECONOMY -> 24.dp
                }
            )
            .scale(scale)
            .clip(RoundedCornerShape(4.dp))
            .background(
                seatColor.copy(alpha = alpha),
                RoundedCornerShape(4.dp)
            )
            .border(
                width = borderWidth,
                color = Color.White,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable(enabled = seat.isAvailable) {
                onSeatSelected()
            },
        contentAlignment = Alignment.Center
    ) {
        if (seat.isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Selected",
                tint = Color.White,
                modifier = Modifier.size(
                    when (seatType) {
                        SeatType.FIRST_CLASS -> 16.dp
                        SeatType.BUSINESS -> 14.dp
                        SeatType.ECONOMY -> 12.dp
                    }
                )
            )
        } else {
            Text(
                text = seat.column,
                color = Color.White,
                fontSize = when (seatType) {
                    SeatType.FIRST_CLASS -> 12.sp
                    SeatType.BUSINESS -> 10.sp
                    SeatType.ECONOMY -> 8.sp
                }
            )
        }
    }
}

@Composable
fun CabinDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = Color(0xFF64B5F6).copy(alpha = 0.3f)
            )
            Text(
                text = "Cabin Divider",
                color = Color(0xFF64B5F6),
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Divider(
                modifier = Modifier.weight(1f),
                color = Color(0xFF64B5F6).copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun SelectedSeatsInfo(
    selectedSeats: List<String>,
    onContinueClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
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
            Text(
                text = if (selectedSeats.isEmpty()) "No seats selected" else "Selected Seats: ${selectedSeats.sorted().joinToString(", ")}",
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Enhanced Continue Button with animation and visual effects
            val buttonEnabled = selectedSeats.isNotEmpty()

            // Animation for button glow effect
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

            val buttonScale = if (buttonEnabled) 1f else 0.95f
            val shadowElevation = if (buttonEnabled) 8.dp else 0.dp

            // Price calculation based on selected seats
            val totalPrice = selectedSeats.sumOf { seatId ->
                when {
                    seatId.startsWith("1") || seatId.startsWith("2") -> 450L // First class
                    seatId.startsWith("3") || seatId.startsWith("4") ||
                            seatId.startsWith("5") || seatId.startsWith("6") ||
                            seatId.startsWith("7") -> 250 // Business class
                    else -> 120 // Economy class
                }
            }

            if (selectedSeats.isNotEmpty()) {
                Text(
                    text = "Total: $${totalPrice}",
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .scale(buttonScale),
                contentAlignment = Alignment.Center
            ) {
                // Button glow effect
                if (buttonEnabled) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawRoundRect(
                            color = Color(0xFF2196F3).copy(alpha = glowAlpha * 0.3f),
                            cornerRadius = CornerRadius(24.dp.toPx()),
                            size = Size(size.width + 8.dp.toPx(), size.height + 8.dp.toPx()),
                            topLeft = Offset(-4.dp.toPx(), -4.dp.toPx())
                        )
                    }
                }

                // Main button
                Button(
                    onClick = onContinueClick,
                    enabled = buttonEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0288D1),
                        disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Continue to Payment",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        // Animated arrow for enabled button
                        if (buttonEnabled) {
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
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Continue",
                                tint = Color.White,
                                modifier = Modifier.offset(x = arrowOffset.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Continue",
                                tint = Color.White.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            }

            if (!buttonEnabled) {
                Text(
                    text = "Please select at least one seat to continue",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AirplaneSeatsScreenPreview() {
    AirplaneSeatsScreen(rememberNavController())
}