package com.example.flyapp.ui.theme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DottedLine(
    modifier: Modifier = Modifier,
    lineHeight: Dp = 2.dp,
    notchRadius: Dp = 16.dp,
    backgroundColor: Color = Color(0xFFE3F2FD),
    dashColor: Color = backgroundColor,
    dashWidth: Dp = 10.dp,
    dashGap: Dp = 6.dp,
    capStyle: StrokeCap = StrokeCap.Round,
    overflowBy: Dp = 24.dp
) {
    OverflowingBox(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(notchRadius * 2)
        ) {
            val width = size.width
            val height = size.height
            val radius = notchRadius.toPx()
            val centerY = height / 2
            val overflowPx = overflowBy.toPx()

            drawArc(
                color = backgroundColor,
                startAngle = 90f,
                sweepAngle = -180f,
                useCenter = true,
                topLeft = Offset(x = -radius, y = centerY - radius),
                size = Size(radius * 2, radius * 2)
            )
            drawArc(
                color = backgroundColor,
                startAngle = 270f,
                sweepAngle = -180f,
                useCenter = true,
                topLeft = Offset(x = width - radius, y = centerY - radius),
                size = Size(radius * 2, radius * 2)
            )
            val dashWidthPx = dashWidth.toPx()
            val dashGapPx = dashGap.toPx()
            var startX = -overflowPx
            while (startX < width + overflowPx) {
                drawLine(
                    color = dashColor,
                    start = Offset(startX, centerY),
                    end = Offset(startX + dashWidthPx, centerY),
                    strokeWidth = lineHeight.toPx(),
                    cap = capStyle
                )
                startX += dashWidthPx + dashGapPx
            }
        }
    }
}

@Composable
fun OverflowingBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val width = constraints.maxWidth
        val height = placeables.maxOfOrNull { it.height } ?: 0
        layout(width, height) {
            placeables.forEach { placeable ->
                placeable.place(0, 0)
            }
        }
    }
}
