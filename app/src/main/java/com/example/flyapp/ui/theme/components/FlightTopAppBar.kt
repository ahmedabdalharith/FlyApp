package com.example.flyapp.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.ui.theme.screens.DarkNavyBlue
import com.example.flyapp.ui.theme.screens.GoldColor

@Composable
fun FlightTopAppBar(textOne: String,textTwo : String, navController: NavController,isBacked: Boolean = true) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top=32.dp, bottom = 16.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
       if (isBacked){
           IconButton(
               onClick = { navController.navigateUp() },
               modifier = Modifier
                   .size(40.dp)
                   .background(DarkNavyBlue.copy(alpha = 0.7f), CircleShape)
           ) {
               Icon(
                   imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                   contentDescription = "Back",
                   tint = GoldColor
               )
           }
       }else{
           Spacer(
                modifier = Modifier
                     .size(40.dp)
                     .background(Color.Transparent)
           )
       }
       Text(
                textAlign = TextAlign.Center,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )) {
                        append(textOne)
                    }
                    withStyle(style = SpanStyle(
                        color = GoldColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )) {
                        append(textTwo)
                    }
                }
            )
        // Placeholder for alignment
        Spacer(
            modifier = Modifier
                .size(40.dp)
                .background(Color.Transparent)
        )
    }
}
@Preview
@Composable
fun FlightTopAppBarPreview() {
    FlightTopAppBar(textOne = "Flight", textTwo = "Details", navController = rememberNavController(), isBacked = false)
}
