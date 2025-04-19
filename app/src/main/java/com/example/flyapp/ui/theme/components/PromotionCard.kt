package com.example.flyapp.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PromotionCard(
    modifier: Modifier = Modifier,
    bgColor: Brush,
    discount: String,
    title: String,
    desc: String,
    airlineName: String,
    airlineLogo: Painter,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(brush = bgColor, shape = RoundedCornerShape(16.dp))
    ) {
        Column(modifier= Modifier.padding(16.dp)) {
            Text(discount, fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontStyle = FontStyle.Italic,
                textAlign= TextAlign.Center)
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Image(
                    painter = airlineLogo,
                    contentDescription = "Airline Logo",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.3f))
                        .padding(2.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = airlineName,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    fontStyle = FontStyle.Italic
                )
            }
            Text(desc, fontSize = 14.sp, color = Color.White, modifier = Modifier.padding(vertical = 8.dp))
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA000)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("BOOK NOW", color = Color(0xFF0D47A1), fontWeight = FontWeight.Bold)
            }
        }
    }
}

