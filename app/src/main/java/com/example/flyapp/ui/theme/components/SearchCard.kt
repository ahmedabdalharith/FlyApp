package com.example.flyapp.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flyapp.R

@Composable
fun SearchCard() {
    var fromText by remember { mutableStateOf("") }
    var toText by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("") }
    Card(
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F3FA)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            // From Field
            OutlinedTextField(
                value = fromText,
                onValueChange = { fromText = it },
                leadingIcon = {
                    Image(
                        painter = painterResource(R.drawable.airplane_up),
                        contentDescription = "From Icon"
                    )
                },
                label = { Text("From", fontWeight = FontWeight.SemiBold) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // To Field
            OutlinedTextField(
                value = toText,
                onValueChange = { toText = it },
                leadingIcon = {
                    Image(
                        painter = painterResource(R.drawable.airplane_arrival),
                        contentDescription = "To Icon"
                    )
                },
                label = { Text("To", fontWeight = FontWeight.SemiBold) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Date and Filter Row

            DatePickerRow(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )


            Spacer(modifier = Modifier.height(16.dp))

            // Search Button
            GradientButton(
                text = "Search Flight",
                modifier = Modifier.fillMaxWidth()
            ) {
                // Handle click
            }
        }

    }
}

@Preview
@Composable
fun SearchBarPreview(){
    SearchCard()
}

