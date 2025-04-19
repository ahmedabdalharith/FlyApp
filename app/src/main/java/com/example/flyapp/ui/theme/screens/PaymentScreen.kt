package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.ParticleEffectBackground
import com.example.flyapp.ui.theme.navigition.Screen

@Composable
fun PaymentScreen(
    navController: NavHostController,
    selectedSeats: List<String> = listOf("15A", "15B") // Default value for preview
) {
    // Calculate price based on selected seats
    val totalPrice = selectedSeats.sumOf { seatId ->
        when {
            seatId.startsWith("1") || seatId.startsWith("2") -> 450L // First class
            seatId.startsWith("3") || seatId.startsWith("4") ||
                    seatId.startsWith("5") || seatId.startsWith("6") ||
                    seatId.startsWith("7") -> 250 // Business class
            else -> 120 // Economy class
        }
    }

    // Payment form states
    var cardNumber by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("Credit Card") }

    // Processing state
//    var isProcessing by remember { mutableStateOf(false) }
//    var isPaymentComplete by remember { mutableStateOf(false) }

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

        // Payment processing overlay
//        AnimatedVisibility(
//            visible = isProcessing || isPaymentComplete,
//            enter = fadeIn(),
//            exit = fadeOut()
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color(0xFF001034).copy(alpha = 0.85f)),
//                contentAlignment = Alignment.Center
//            ) {
//                if (isProcessing) {
//                    ProcessingPaymentAnimation()
//                } else if (isPaymentComplete) {
//                    PaymentSuccessAnimation(
//                        onContinue = {
//                            navController.navigate(Screen.ConfirmationScreen.route) {
//                                popUpTo(Screen.PaymentScreen.route) { inclusive = false }
//                            }
//                        }
//                    )
//                }
//            }
//        }

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            PaymentHeader(totalPrice)

            Spacer(modifier = Modifier.height(16.dp))

            // Booking Summary
            BookingSummary(selectedSeats)

            Spacer(modifier = Modifier.height(24.dp))

            // Payment Method Selector
            PaymentMethodSelector(
                selectedMethod = selectedPaymentMethod,
                onMethodSelected = { selectedPaymentMethod = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Payment Form
            PaymentForm(
                cardNumber = cardNumber,
                onCardNumberChange = { if (it.length <= 19) cardNumber = formatCardNumber(it) },
                cardHolderName = cardHolderName,
                onCardHolderNameChange = { cardHolderName = it },
                expiryDate = expiryDate,
                onExpiryDateChange = { if (it.length <= 5) expiryDate = formatExpiryDate(it) },
                cvv = cvv,
                onCvvChange = { if (it.length <= 3) cvv = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Pay Button
            PayButton(
                amount = totalPrice.toInt(),
                enabled = isFormValid(cardNumber, cardHolderName, expiryDate, cvv),
                onClick = {
//                  isProcessing = true
//                    isProcessing = false
//                    isPaymentComplete = true
                    navController.navigate(
                        Screen.ConfirmationScreen.route
                    ) {
                        popUpTo(Screen.PaymentScreen.route) { inclusive = false }
                    }

                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Back button
            Text(
                text = "Back to Seat Selection",
                color = Color(0xFF64B5F6),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .clickable { navController.popBackStack() }
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun PaymentHeader(totalAmount: Long) {
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
            Icon(
                painter = painterResource(R.drawable.plane_ic),
                contentDescription = "Flight",
                tint = Color(0xFF64B5F6),
                modifier = Modifier.size(36.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Complete Payment",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Total: $${totalAmount}",
            color = Color(0xFF4CAF50),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BookingSummary(selectedSeats: List<String>) {
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
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Booking Summary",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Flight SV 1734",
                    color = Color(0xFF64B5F6),
                    fontSize = 14.sp
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                 color = Color(0xFF64B5F6).copy(alpha = 0.3f)
            )

            // Flight details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Riyadh (RUH)",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "10:30 AM, Today",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Flight direction",
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(24.dp)
                )

                Column {
                    Text(
                        text = "Dubai (DXB)",
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "12:45 PM, Today",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                 color = Color(0xFF64B5F6).copy(alpha = 0.3f)
            )

            // Seats info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Passenger",
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Selected Seats: ${selectedSeats.sorted().joinToString(", ")}",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Duration info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.date_range),
                    contentDescription = "Duration",
                    tint = Color(0xFF64B5F6),
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Flight Duration: 2h 15m",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun PaymentMethodSelector(
    selectedMethod: String,
    onMethodSelected: (String) -> Unit
) {
    val paymentMethods = listOf("Credit Card", "Debit Card", "PayPal")

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
            Text(
                text = "Payment Method",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            paymentMethods.forEach { method ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { onMethodSelected(method) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = method == selectedMethod,
                        onClick = { onMethodSelected(method) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF64B5F6),
                            unselectedColor = Color.White.copy(alpha = 0.6f)
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = method,
                        color = if (method == selectedMethod) Color.White else Color.White.copy(alpha = 0.6f),
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Payment method icon
                    Icon(
                        painter = when (method) {
                            "Credit Card", "Debit Card" -> painterResource(R.drawable.creditcard)
                            "PayPal" -> painterResource(R.drawable.paypal)
                            else -> painterResource(R.drawable.creditcard)
                        },
                        contentDescription = method,
                        tint = if (method == selectedMethod) Color(0xFF64B5F6) else Color.White.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentForm(
    cardNumber: String,
    onCardNumberChange: (String) -> Unit,
    cardHolderName: String,
    onCardHolderNameChange: (String) -> Unit,
    expiryDate: String,
    onExpiryDateChange: (String) -> Unit,
    cvv: String,
    onCvvChange: (String) -> Unit
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
            Text(
                text = "Card Details",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card Number Field
            OutlinedTextField(
                value = cardNumber,
                onValueChange = onCardNumberChange,
                label = { Text("Card Number") },
                leadingIcon = {
                    Image(
                        painter =painterResource(R.drawable.creditcard),
                        contentDescription = "Card Number",
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF64B5F6),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    focusedLabelColor = Color(0xFF64B5F6),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card Holder Name Field
            OutlinedTextField(
                value = cardHolderName,
                onValueChange = onCardHolderNameChange,
                label = { Text("Card Holder Name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Card Holder",
                        tint = Color(0xFF64B5F6)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF64B5F6),
                    unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                    focusedLabelColor = Color(0xFF64B5F6),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color.White,
                    focusedPlaceholderColor = Color.White.copy(alpha = 0.7f),
                    unfocusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                    focusedLeadingIconColor = Color.White,
                    unfocusedLeadingIconColor = Color.White
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Expiry Date and CVV in one row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Expiry Date Field
                OutlinedTextField(
                    value = expiryDate,
                    onValueChange = onExpiryDateChange,
                    label = { Text("MM/YY") },
                    leadingIcon = {
                        Icon(
                            painter =painterResource(R.drawable.date_range),
                            contentDescription = "Expiry Date",
                            tint = Color(0xFF64B5F6)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF64B5F6),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedLabelColor = Color(0xFF64B5F6),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                        cursorColor = Color.White,
                        focusedPlaceholderColor = Color.White.copy(alpha = 0.7f),
                        unfocusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                        focusedLeadingIconColor = Color.White,
                        unfocusedLeadingIconColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )

                // CVV Field
                OutlinedTextField(
                    value = cvv,
                    onValueChange = onCvvChange,
                    label = { Text("CVV") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "CVV",
                            tint = Color(0xFF64B5F6)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF64B5F6),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedLabelColor = Color(0xFF64B5F6),
                        unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                        cursorColor = Color.White,
                        focusedPlaceholderColor = Color.White.copy(alpha = 0.7f),
                        unfocusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
                        focusedLeadingIconColor = Color.White,
                        unfocusedLeadingIconColor = Color.White
                    ),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Security message
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Secure",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Your payment information is secure and encrypted",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun PayButton(
    amount: Int,
    enabled: Boolean,
    onClick:  () -> Unit
) {
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        contentAlignment = Alignment.Center
    ) {
        // Button glow effect
        if (enabled) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawRoundRect(
                    color = Color(0xFF4CAF50).copy(alpha = glowAlpha * 0.3f),
                    size = size,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx())
                )
            }
        }

        Button(
            onClick = onClick,
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50),
                disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
            ),
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Pay $${amount}",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ProcessingPaymentAnimation() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        // Animated progress indicator
        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = Color(0xFF64B5F6),
            strokeWidth = 5.dp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Processing Payment",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Please wait while we process your payment...",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PaymentSuccessAnimation(onContinue: () -> Unit) {
    var animationPlayed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement= Arrangement.Center,
        modifier = Modifier.padding(32.dp)
    ) {
        AnimatedVisibility(
            visible = animationPlayed,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success check mark
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF4CAF50), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Success",
                        tint = Color.White,
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Payment Successful!",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your booking has been confirmed.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onContinue,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF64B5F6)
                    ),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "View Booking Details",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// Helper functions
fun formatCardNumber(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }
    val formatted = StringBuilder()

    for (i in digitsOnly.indices) {
        if (i > 0 && i % 4 == 0) {
            formatted.append(" ")
        }
        formatted.append(digitsOnly[i])
    }

    return formatted.toString()
}

fun formatExpiryDate(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }
    return if (digitsOnly.length > 2) {
        "${digitsOnly.substring(0, 2)}/${digitsOnly.substring(2)}"
    } else {
        digitsOnly
    }
}

fun isFormValid(cardNumber: String, cardHolderName: String, expiryDate: String, cvv: String): Boolean {
    val cardNumberDigits = cardNumber.filter { it.isDigit() }
    return cardNumberDigits.length >= 16 &&
            cardHolderName.isNotBlank() &&
            expiryDate.length == 5 &&
            cvv.length == 3
}
@Preview
@Composable
fun PaymentScreenPreview() {
    PaymentScreen(
        navController = rememberNavController(),
        selectedSeats = listOf("15A", "15B")
    )
}