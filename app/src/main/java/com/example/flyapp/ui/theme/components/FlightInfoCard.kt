package com.example.flyapp.ui.theme.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flyapp.ui.theme.data.models.FlightDetails
import com.example.flyapp.ui.theme.screens.InfoItem

@Composable
fun FlightInfoCard(flightDetails: FlightDetails) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A192F).copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Flight Information",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Flight info grid
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    InfoItem(
                        title = "Aircraft",
                        value = flightDetails.aircraft,
                        modifier = Modifier.weight(1f)
                    )
                    InfoItem(
                        title = "Class",
                        value = flightDetails.classType,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    InfoItem(
                        title = "Terminal",
                        value = flightDetails.terminal,
                        modifier = Modifier.weight(1f)
                    )
                    InfoItem(
                        title = "Gate",
                        value = flightDetails.gate,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    InfoItem(
                        title = "Boarding",
                        value = flightDetails.boardingTime,
                        modifier = Modifier.weight(1f)
                    )
                    InfoItem(
                        title = "Luggage",
                        value = flightDetails.luggageAllowance,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}