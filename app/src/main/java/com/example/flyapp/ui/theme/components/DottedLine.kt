package com.example.flyapp.ui.theme.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun DottedLine(
    lineHeight : Dp = 2.dp,
    notchRadius: Dp = 16.dp,
    backgroundColor : Color= Color(0xFFE3F2FD),
    dashColor:Color = backgroundColor
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)

    ) {
        val width = size.width
        val height = size.height
        val radius = notchRadius.toPx()
        val centerY = height / 2

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


        val dashWidth = 10.dp.toPx()
        val dashGap = 6.dp.toPx()
        var startX = radius
        while (startX < width - radius) {
            drawLine(
                color = dashColor,
                start = Offset(startX, centerY),
                end = Offset(startX + dashWidth, centerY),
                strokeWidth = lineHeight.toPx()
            )
            startX += dashWidth + dashGap
        }
    }
}
