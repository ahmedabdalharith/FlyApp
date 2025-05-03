package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.flyapp.ui.theme.components.FlightTopAppBar
import com.example.flyapp.ui.theme.navigition.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PaymentScreen(
    navController: NavHostController,
    selectedSeats: List<String> = listOf("15A", "15B") // Default value for preview
) {
    // Calculate price based on selected seats with a more structured approach
    val totalPrice = selectedSeats.sumOf { seatId ->
        when {
            seatId.startsWith("1") || seatId.startsWith("2") -> 450L // First class
            seatId.startsWith("3") || seatId.startsWith("4") ||
                    seatId.startsWith("5") || seatId.startsWith("6") ||
                    seatId.startsWith("7") -> 250L // Business class
            else -> 120L // Economy class
        }
    }

    // Payment form states with enhanced validation
    var cardNumber by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var selectedPaymentMethod by remember { mutableStateOf("Credit Card") }
    var isCardFlipped by remember { mutableStateOf(false) }

    // Field validation states for visual feedback
    val isCardNumberValid = cardNumber.replace("\\s".toRegex(), "").length >= 16
    val isCardHolderValid = cardHolderName.length >= 3
    val isExpiryDateValid = expiryDate.matches("\\d{2}/\\d{2}".toRegex())
    val isCvvValid = cvv.length >= 3

    // Processing states with animation control
    var isProcessing by remember { mutableStateOf(false) }
    var isPaymentComplete by remember { mutableStateOf(false) }
    var showConfetti by remember { mutableStateOf(false) }

    // Coroutine scope for animations
    val coroutineScope = rememberCoroutineScope()

    // Scroll state
    val scrollState = rememberScrollState()

    // Get card type from card number with improved detection
    val cardType = getCardType(cardNumber)

    // Main content visibility state
    var showMainContent by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepBlue,     // Dark blue (from booking screen)
                        MediumBlue,   // Medium blue (from booking screen)
                        DarkNavyBlue  // Navy blue (from booking screen)
                    )
                )
            )
    ) {
        // Security pattern background (matching booking screen)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Draw security pattern lines (like passport security pattern)
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 3f), 0f)
            drawLine(
                color = Color.White.copy(alpha = 0.05f),
                start = Offset(0f, 0f),
                end = Offset(canvasWidth, canvasHeight),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )

            drawLine(
                color = Color.White.copy(alpha = 0.05f),
                start = Offset(canvasWidth, 0f),
                end = Offset(0f, canvasHeight),
                strokeWidth = 1f,
                pathEffect = pathEffect
            )

            // Draw circular watermark
            val stroke = Stroke(width = 1f, pathEffect = pathEffect)
            for (i in 1..5) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.03f),
                    radius = canvasHeight / 3f * i / 5f,
                    center = Offset(canvasWidth / 2f, canvasHeight / 2f),
                    style = stroke
                )
            }
        }

        // Main payment content
        if (showMainContent) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Top app bar (matching booking screen style)
                FlightTopAppBar(
                    textOne = "PAY",
                    textTwo = "MENT",
                    navController= navController,

                )

                // Main content area
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Booking summary card with gold border (matching booking screen style)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(8.dp, RoundedCornerShape(16.dp))
                            .border(
                                width = 1.dp,
                                brush = Brush.linearGradient(
                                    colors = listOf(
                                        GoldColor,       // Gold
                                        Color(0xFFFFD700), // Gold
                                        GoldColor        // Gold
                                    )
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = DarkNavyBlue.copy(alpha = 0.85f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Order summary header
                            Text(
                                text = "ORDER SUMMARY",
                                color = GoldColor,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = GoldColor.copy(alpha = 0.3f)
                            )

                            // Seats information
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Selected Seats:",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = selectedSeats.joinToString(", "),
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            // Seat type breakdown
                            val firstClassSeats = selectedSeats.count { it.startsWith("1") || it.startsWith("2") }
                            val businessClassSeats = selectedSeats.count {
                                it.startsWith("3") || it.startsWith("4") ||
                                        it.startsWith("5") || it.startsWith("6") ||
                                        it.startsWith("7")
                            }
                            val economyClassSeats = selectedSeats.size - firstClassSeats - businessClassSeats

                            if (firstClassSeats > 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "First Class (x$firstClassSeats):",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "$${firstClassSeats * 450}",
                                        color = GoldColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            if (businessClassSeats > 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Business Class (x$businessClassSeats):",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "$${businessClassSeats * 250}",
                                        color = GoldColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            if (economyClassSeats > 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Economy Class (x$economyClassSeats):",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "$${economyClassSeats * 120}",
                                        color = GoldColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            // Fee and taxes (estimated)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Taxes & Fees:",
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "$${(totalPrice * 0.1).toInt()}",
                                    color = GoldColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 8.dp),
                                color = GoldColor.copy(alpha = 0.5f)
                            )

                            // Total
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "TOTAL:",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "$${totalPrice + (totalPrice * 0.1).toInt()}",
                                    color = GoldColor,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Payment method selector (styled to match booking screen)
                    Text(
                        text = "PAYMENT METHOD",
                        color = GoldColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Payment method options
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, GoldColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .background(DarkNavyBlue.copy(alpha = 0.7f))
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        val paymentMethods = listOf("Credit Card", "PayPal", "Apple Pay")
                        paymentMethods.forEach { method ->
                            val isSelected = method == selectedPaymentMethod
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (isSelected) GoldColor else Color.Transparent
                                    )
                                    .clickable { selectedPaymentMethod = method }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = method,
                                    color = if (isSelected) DarkNavyBlue else GoldColor.copy(alpha = 0.9f),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Credit card display
                    EnhancedCreditCard(
                        cardNumber = cardNumber,
                        cardHolderName = cardHolderName,
                        expiryDate = expiryDate,
                        cvv = cvv,
                        cardType = cardType,
                        isFlipped = isCardFlipped,
                        onFlip = { isCardFlipped = !isCardFlipped }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Card input fields (styled to match booking screen)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, GoldColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .background(DarkNavyBlue.copy(alpha = 0.7f))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Card number field
                        OutlinedTextField(
                            value = cardNumber,
                            onValueChange = { if (it.length <= 19) cardNumber = formatCardNumber(it) },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Card Number") },
                            leadingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.creditcard),
                                    contentDescription = "Card",
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = cardNumber.isNotEmpty() && !isCardNumberValid,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                unfocusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                focusedLabelColor = GoldColor,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = GoldColor,
                                unfocusedBorderColor = GoldColor.copy(alpha = 0.5f),
                                errorBorderColor = Color.Red.copy(alpha = 0.7f),
                                errorLabelColor = Color.Red.copy(alpha = 0.7f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Card holder name field
                        OutlinedTextField(
                            value = cardHolderName,
                            onValueChange = { cardHolderName = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Card Holder Name") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Person",
                                    tint = GoldColor
                                )
                            },
                            isError = cardHolderName.isNotEmpty() && !isCardHolderValid,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                unfocusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                focusedLabelColor = GoldColor,
                                unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = GoldColor,
                                unfocusedBorderColor = GoldColor.copy(alpha = 0.5f),
                                errorBorderColor = Color.Red.copy(alpha = 0.7f),
                                errorLabelColor = Color.Red.copy(alpha = 0.7f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Expiry date and CVV fields in a row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Expiry date field
                            OutlinedTextField(
                                value = expiryDate,
                                onValueChange = { if (it.length <= 5) expiryDate = formatExpiryDate(it) },
                                modifier = Modifier.weight(1.5f),
                                label = { Text("Expiry Date") },
                                placeholder = { Text("MM/YY") },
                                leadingIcon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.date_range),
                                        contentDescription = "Expiry Date",
                                        tint = GoldColor
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                isError = expiryDate.isNotEmpty() && !isExpiryDateValid,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                    unfocusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                    focusedLabelColor = GoldColor,
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = GoldColor,
                                    unfocusedBorderColor = GoldColor.copy(alpha = 0.5f),
                                    errorBorderColor = Color.Red.copy(alpha = 0.7f),
                                    errorLabelColor = Color.Red.copy(alpha = 0.7f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )

                            // CVV field
                            OutlinedTextField(
                                value = cvv,
                                onValueChange = { if (it.length <= 4) cvv = it },
                                modifier = Modifier
                                    .weight(1f)
                                    .onFocusChanged {
                                        if (it.isFocused) {
                                            isCardFlipped = true
                                        } else if (cvv.isEmpty()) {
                                            isCardFlipped = false
                                        }
                                    },
                                label = { Text("CVV") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Lock,
                                        contentDescription = "CVV",
                                        tint = GoldColor
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                visualTransformation = PasswordVisualTransformation(),
                                isError = cvv.isNotEmpty() && !isCvvValid,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                    unfocusedContainerColor = DarkNavyBlue.copy(alpha = 0.7f),
                                    focusedLabelColor = GoldColor,
                                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White,
                                    focusedBorderColor = GoldColor,
                                    unfocusedBorderColor = GoldColor.copy(alpha = 0.5f),
                                    errorBorderColor = Color.Red.copy(alpha = 0.7f),
                                    errorLabelColor = Color.Red.copy(alpha = 0.7f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Pay button (matching booking screen style)
                    Button(
                        onClick = {
                            isProcessing = true
                            coroutineScope.launch {
                                delay(2000)
                                isProcessing = false
                                isPaymentComplete = true
                                delay(500)
                                showConfetti = true
                                showMainContent = false
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = isCardNumberValid && isCardHolderValid && isExpiryDateValid && isCvvValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldColor,
                            contentColor = DarkNavyBlue,
                            disabledContainerColor = GoldColor.copy(alpha = 0.5f),
                            disabledContentColor = DarkNavyBlue.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = "Secure",
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PAY $${totalPrice + (totalPrice * 0.1).toInt()}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Back button
                    TextButton(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = GoldColor
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Return to Seat Selection",
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Secure payment note
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Lock,
                            contentDescription = "Secure",
                            modifier = Modifier.size(16.dp),
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Secure Payment Processing",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else if (isPaymentComplete) {
            // Payment success screen
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                if (showConfetti) {
                    ConfettiEffect()
                }

                PaymentSuccessAnimation(
                    onContinue = {
                        navController.navigate(Screen.TicketScreen.route) {
                            popUpTo(Screen.PaymentScreen.route) { inclusive = false }
                        }
                    }
                )
            }
        } else if (isProcessing) {
            // Processing payment animation
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF001034).copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                ProcessingPaymentAnimation()
            }
        }
    }
}

@Composable
fun EnhancedCreditCard(
    cardNumber: String,
    cardHolderName: String,
    expiryDate: String,
    cvv: String,
    cardType: CardType,
    isFlipped: Boolean,
    onFlip: () -> Unit
) {
    // Animation values
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "card_flip"
    )

    val cardElevation by animateFloatAsState(
        targetValue = if (isFlipped) 12f else 8f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label = "card_elevation"
    )

    // Card colors based on card type
    val cardGradient = when (cardType) {
        CardType.VISA -> Brush.linearGradient(
            colors = listOf(Color(0xFF1A1F71), Color(0xFF0E1847), Color(0xFF000B29))
        )
        CardType.MASTERCARD -> Brush.linearGradient(
            colors = listOf(Color(0xFF1A1F71), Color(0xFF5A2222), Color(0xFF3A0A0A))
        )
        CardType.AMEX -> Brush.linearGradient(
            colors = listOf(Color(0xFF2671B5), Color(0xFF1B4E84), Color(0xFF0E305D))
        )
        else -> Brush.linearGradient(
            colors = listOf(Color(0xFF1E3C72), Color(0xFF0E2546), Color(0xFF091426))
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 8.dp)
            .shadow(cardElevation.dp, RoundedCornerShape(16.dp))
            .clickable { onFlip() }
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            },
        contentAlignment = Alignment.Center
    ) {
        // Card front
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Hide back when card is flipped
                    alpha = if (rotation < 90f) 1f else 0f
                }
                .clip(RoundedCornerShape(16.dp))
                .background(cardGradient)
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GoldColor.copy(alpha = 0.7f),
                            GoldColor.copy(alpha = 0.3f),
                            GoldColor.copy(alpha = 0.7f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Column {
                // Card chip and logo row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                 Row(
                        verticalAlignment = Alignment.CenterVertically
                 ) {
                     // Chip icon
                     Image(
                         painter = painterResource(id = R.drawable.sim_card_chip),
                         contentDescription = "Card Chip",
                         modifier = Modifier.size(40.dp)
                     )
                     Spacer(
                            modifier = Modifier.width(8.dp)
                     )
                     //wi fi
                     Image(
                         painter = painterResource(id = R.drawable.wifi_ic),
                         contentDescription = "wifi",
                         modifier = Modifier.size(32.dp)
                             .rotate(90f)
                     )
                 }

                    // Card type logo
                    when (cardType) {
                        CardType.VISA -> Image(
                            painter = painterResource(id = R.drawable.visa),
                            contentDescription = "Visa",
                            modifier = Modifier.size(40.dp)
                        )
                        CardType.MASTERCARD -> Image(
                            painter = painterResource(id = R.drawable.mastercard),
                            contentDescription = "MasterCard",
                            modifier = Modifier.size(40.dp)
                        )
                        CardType.AMEX -> Image(
                            painter = painterResource(id = R.drawable.amex),
                            contentDescription = "American Express",
                            modifier = Modifier.size(40.dp)
                        )
                        else -> Image(
                            painter = painterResource(id = R.drawable.visa),
                            contentDescription = "Credit Card",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                // Card number
                Text(
                    text = if (cardNumber.isEmpty()) "•••• •••• •••• ••••" else cardNumber,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Card holder and expiry
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "CARD HOLDER",
                            color = GoldColor.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (cardHolderName.isEmpty()) "YOUR NAME" else cardHolderName.uppercase(),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "EXPIRES",
                            color = GoldColor.copy(alpha = 0.7f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (expiryDate.isEmpty()) "MM/YY" else expiryDate,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Security elements (patterns) for card
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Draw security pattern lines
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 2f), 0f)

                // Security circles
                for (i in 1..3) {
                    drawCircle(
                        color = GoldColor.copy(alpha = 0.05f),
                        radius = canvasHeight / 6f * i / 3f,
                        center = Offset(canvasWidth - 30f, canvasHeight - 30f),
                        style = Stroke(width = 0.5f, pathEffect = pathEffect)
                    )
                }
            }
        }

        // Card back
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    // Hide front when card is not flipped
                    rotationY = 180f
                    alpha = if (rotation >= 90f) 1f else 0f
                }
                .clip(RoundedCornerShape(16.dp))
                .background(cardGradient)
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            GoldColor.copy(alpha = 0.7f),
                            GoldColor.copy(alpha = 0.3f),
                            GoldColor.copy(alpha = 0.7f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Magnetic stripe
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(Color.Black.copy(alpha = 0.7f))
                )

                Spacer(modifier = Modifier.height(20.dp))

                // CVV strip
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(0.7f)
                            .height(40.dp)
                            .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(4.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp)
                                .background(Color.White)
                        ) {
                            Text(
                                text = if (cvv.isEmpty()) "***" else cvv,
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(0.3f)) {
                        when (cardType) {
                            CardType.VISA -> Icon(
                                painter = painterResource(id = R.drawable.visa),
                                contentDescription = "Visa",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                            CardType.MASTERCARD -> Icon(
                                painter = painterResource(id = R.drawable.mastercard),
                                contentDescription = "MasterCard",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                            CardType.AMEX -> Icon(
                                painter = painterResource(id = R.drawable.amex),
                                contentDescription = "American Express",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                            else -> Icon(
                                painter = painterResource(id = R.drawable.creditcard),
                                contentDescription = "Credit Card",
                                tint = GoldColor,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Security note
                Text(
                    text = "This card is property of FlyApp Bank. Authorized use only.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}

// Card type enum
enum class CardType {
    VISA,
    MASTERCARD,
    AMEX,
    UNKNOWN
}

// Function to format card number with spaces
fun formatCardNumber(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }
    val formatted = StringBuilder()

    digitsOnly.forEachIndexed { index, char ->
        if (index > 0 && index % 4 == 0) {
            formatted.append(" ")
        }
        formatted.append(char)
    }

    return formatted.toString()
}

// Function to format expiry date with slash
fun formatExpiryDate(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }
    return when {
        digitsOnly.length <= 2 -> digitsOnly
        else -> "${digitsOnly.substring(0, 2)}/${digitsOnly.substring(2)}"
    }
}

// Function to determine card type based on number
fun getCardType(cardNumber: String): CardType {
    val digits = cardNumber.replace("\\s".toRegex(), "")
    return when {
        digits.startsWith("4") -> CardType.VISA
        digits.startsWith("5") -> CardType.MASTERCARD
        digits.startsWith("3") -> CardType.AMEX
        else -> CardType.UNKNOWN
    }
}

@Composable
fun ProcessingPaymentAnimation() {
    var currentStep by remember { mutableIntStateOf(1) }
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(currentStep) {
        if (currentStep <= 3) {
            animatedProgress.snapTo(0f)
            animatedProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 800, easing = LinearEasing)
            )
            delay(200)
            currentStep++
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(16.dp))
            .background(DarkNavyBlue)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.7f),
                        GoldColor.copy(alpha = 0.3f),
                        GoldColor.copy(alpha = 0.7f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Payment processing animation
        Box(
            modifier = Modifier.size(80.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(80.dp),
                color = GoldColor,
                trackColor = GoldColor.copy(alpha = 0.2f),
                strokeWidth = 4.dp
            )

            // Animated icons for processing steps
            when (currentStep) {
                1 -> Icon(
                    painterResource(R.drawable.creditcard),
                    contentDescription = "Card",
                    tint = GoldColor,
                    modifier = Modifier.size(36.dp)
                )
                2 -> Icon(
                    painterResource(R.drawable.security),
                    contentDescription = "Security",
                    tint = GoldColor,
                    modifier = Modifier.size(36.dp)
                )
                3 -> Icon(
                    painterResource(R.drawable.dollar_),
                    contentDescription = "Bank",
                    tint = GoldColor,
                    modifier = Modifier.size(36.dp)
                )
                else -> Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Complete",
                    tint = GoldColor,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Processing message
        Text(
            text = when (currentStep) {
                1 -> "Validating Card Information..."
                2 -> "Encrypting Payment Data..."
                3 -> "Processing Payment..."
                else -> "Payment Successful"
            },
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar
        LinearProgressIndicator(
            progress = { animatedProgress.value },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = GoldColor,
            trackColor = GoldColor.copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Please wait message
        Text(
            text = "Please do not close this window",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PaymentSuccessAnimation(onContinue: () -> Unit) {
    var showCheckmark by remember { mutableStateOf(false) }
    var showText by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500)
        showCheckmark = true
        delay(1000)
        showText = true
        delay(500)
        showButton = true
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(16.dp))
            .background(DarkNavyBlue)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        GoldColor.copy(alpha = 0.7f),
                        GoldColor.copy(alpha = 0.3f),
                        GoldColor.copy(alpha = 0.7f)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Success animation
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(GoldColor.copy(alpha = 0.1f), CircleShape)
                .border(2.dp, GoldColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column {
                AnimatedVisibility(
                    visible = showCheckmark,
                    // Replace problematic EaseOutBack with safer FastOutSlowInEasing
                    enter = scaleIn(animationSpec = tween(500, easing = FastOutSlowInEasing)) +
                            fadeIn(animationSpec = tween(300))
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = "Success",
                        tint = GoldColor,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Success message
        AnimatedVisibility(
            visible = showText,
            enter = fadeIn(animationSpec = tween(800))
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Payment Successful!",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your tickets have been reserved successfully",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Continue button
        AnimatedVisibility(
            visible = showButton,
            enter = fadeIn(animationSpec = tween(500))
        ) {
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldColor,
                    contentColor = DarkNavyBlue
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "VIEW MY TICKETS",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun ConfettiEffect() {
    // Confetti state
    class ConfettiPiece(
        val color: Color,
        initialPosition: Offset,
        val size: Float,
        val speed: Float,
        val angle: Float
    ) {
        var position by mutableStateOf(initialPosition)
        var rotation by mutableFloatStateOf(0f)
    }

    val colors = listOf(
        GoldColor,
        Color(0xFFE5B80B),
        Color(0xFFD4AF37),
        Color(0xFFFFC125),
        Color.White
    )

    // Create confetti pieces
    val confetti = remember {
        List(100) {
            ConfettiPiece(
                color = colors.random(),
                initialPosition = Offset(
                    x = (Math.random() * 1000).toFloat(),
                    y = -(Math.random() * 500).toFloat()
                ),
                size = (5 + Math.random() * 10).toFloat(),
                speed = (2 + Math.random() * 8).toFloat(),
                angle = (Math.random() * 360).toFloat()
            )
        }
    }

    // Animation - Use standard Float instead of Animatable for more stability
    var animProgress by remember { mutableFloatStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val animState = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000),
            repeatMode = RepeatMode.Restart
        ),
        label = "confetti_progress"
    )

    // Update confetti positions
    LaunchedEffect(animState.value) {
        animProgress = animState.value
        confetti.forEach { piece ->
            val radians = Math.toRadians(piece.angle.toDouble())
            piece.position = Offset(
                x = piece.position.x + (cos(radians) * piece.speed).toFloat(),
                y = piece.position.y + (sin(radians) * piece.speed * 2).toFloat() + piece.speed * 2
            )
            piece.rotation += piece.speed * 0.1f

            // Reset confetti pieces that fall off screen to create continuous effect
            if (piece.position.y > 2000) {
                piece.position = Offset(
                    x = (Math.random() * 1000).toFloat(),
                    y = -(Math.random() * 500).toFloat()
                )
            }
        }
    }

    // Draw confetti
    Canvas(modifier = Modifier.fillMaxSize()) {
        confetti.forEach { piece ->
            withTransform({
                translate(piece.position.x, piece.position.y)
                rotate(piece.rotation)
            }) {
                drawRect(
                    color = piece.color,
                    topLeft = Offset(-piece.size / 2, -piece.size / 2),
                    size = Size(piece.size, piece.size)
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    PaymentScreen(
        navController = rememberNavController(),
        selectedSeats = listOf("15A", "15B", "22C")
    )
}