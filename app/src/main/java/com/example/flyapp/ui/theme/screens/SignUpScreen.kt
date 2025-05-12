package com.example.flyapp.ui.theme.screens

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.FlightTopAppBar
import com.example.flyapp.ui.theme.navigition.Screen
import com.example.flyapp.ui.theme.theme.DarkNavyBlue
import com.example.flyapp.ui.theme.theme.DeepBlue
import com.example.flyapp.ui.theme.theme.GoldColor
import com.example.flyapp.ui.theme.theme.MediumBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(navController: NavHostController) {
    // Form states with validation
    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    // Field validation states
    val isEmailValid = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isFullNameValid = fullName.length >= 3
    val isPasswordValid = password.length >= 8
    val doPasswordsMatch = password == confirmPassword && password.isNotEmpty()
    val isFormValid = isEmailValid && isFullNameValid && isPasswordValid && doPasswordsMatch && termsAccepted

    // Processing states
    var isProcessing by remember { mutableStateOf(false) }
    var isSignUpComplete by remember { mutableStateOf(false) }

    // Coroutine scope for animations
    val coroutineScope = rememberCoroutineScope()

    // Scroll state
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DeepBlue,
                        MediumBlue,
                        DarkNavyBlue
                    )
                )
            )
    ) {
        // Security pattern background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Draw security pattern lines
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

        // Main content
        if (!isSignUpComplete && !isProcessing) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // Top app bar
                FlightTopAppBar(
                    textOne = "FLY",
                    textTwo = "APP",
                    navController = navController,
                )

                // Main content area
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo and title
                    Image(
                        painter = painterResource(id = R.drawable.plane_real),
                        contentDescription = "FlyApp Logo",
                        modifier = Modifier
                            .size(120.dp)
                            .padding(bottom = 8.dp),
                    )
                    Text(
                        text = "CREATE ACCOUNT",
                        color = GoldColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Form fields
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .border(1.dp, GoldColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .background(DarkNavyBlue.copy(alpha = 0.7f))
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Full Name field
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Full Name") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Person",
                                    tint = GoldColor
                                )
                            },
                            isError = fullName.isNotEmpty() && !isFullNameValid,
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
                                errorLabelColor = Color.Red.copy(alpha = 0.7f),

                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        // Email field
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Email Address") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = "Email",
                                    tint = GoldColor
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            isError = email.isNotEmpty() && !isEmailValid,
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


                        // Password field
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Password") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Lock,
                                    contentDescription = "Password",
                                    tint = GoldColor
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        painter = if (passwordVisible)
                                            painterResource(R.drawable.eye_password_hide) else painterResource(R.drawable.eye_password_show),
                                        contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                                        tint = GoldColor.copy(alpha = 0.7f)
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible)
                                VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            isError = password.isNotEmpty() && !isPasswordValid,
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

                        if (password.isNotEmpty() && !isPasswordValid) {
                            Text(
                                text = "Password must be at least 8 characters",
                                color = Color.Red.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }

                        // Confirm Password field
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Confirm Password") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Lock,
                                    contentDescription = "Confirm Password",
                                    tint = GoldColor
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                    Icon(
                                        painter = if (passwordVisible)
                                            painterResource(R.drawable.eye_password_hide) else painterResource(R.drawable.eye_password_show),
                                        contentDescription = if (confirmPasswordVisible) "Hide Password" else "Show Password",
                                        tint = GoldColor.copy(alpha = 0.7f)
                                    )
                                }
                            },
                            visualTransformation = if (confirmPasswordVisible)
                                VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            isError = confirmPassword.isNotEmpty() && !doPasswordsMatch,
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

                        if (confirmPassword.isNotEmpty() && !doPasswordsMatch) {
                            Text(
                                text = "Passwords do not match",
                                color = Color.Red.copy(alpha = 0.7f),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }

                        // Terms and conditions checkbox
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { termsAccepted = !termsAccepted },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .border(1.dp, GoldColor, RoundedCornerShape(4.dp))
                                    .background(
                                        if (termsAccepted) GoldColor else Color.Transparent,
                                        RoundedCornerShape(4.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (termsAccepted) {
                                    Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = "Accepted",
                                        tint = DarkNavyBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "I agree to the Terms of Service and Privacy Policy",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sign Up button
                    Button(
                        onClick = {
                            isProcessing = true
                            coroutineScope.launch {
                                delay(2000) // Simulate network request
                                isProcessing = false
                                isSignUpComplete = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = isFormValid,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldColor,
                            contentColor = DarkNavyBlue,
                            disabledContainerColor = GoldColor.copy(alpha = 0.5f),
                            disabledContentColor = DarkNavyBlue.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "CREATE ACCOUNT",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Or divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp)
                            , color = GoldColor.copy(alpha = 0.3f)
                        )

                        Text(
                            text = "OR",
                            color = GoldColor.copy(alpha = 0.7f),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.dp),
                            color = GoldColor.copy(alpha = 0.3f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Social signup options
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Google sign up
                        SocialButton(
                            icon = R.drawable.google,
                            onClick = { /* Implement Google sign up */ }
                        )

                        // Facebook sign up
                        SocialButton(
                            icon = R.drawable.facebook_ic,
                            onClick = { /* Implement Facebook sign up */ }
                        )

                        // Apple sign up
                        SocialButton(
                            icon = R.drawable.apple_1ic,
                            onClick = { /* Implement Apple sign up */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Already have an account link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account?",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )

                        TextButton(onClick = { navController.navigate(Screen.LoginScreen.route) }) {
                            Text(
                                text = "Sign In",
                                color = GoldColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else if (isProcessing) {
            // Processing animation
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF001034).copy(alpha = 0.9f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp),
                        color = GoldColor,
                        trackColor = GoldColor.copy(alpha = 0.2f),
                        strokeWidth = 4.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Creating Your Account...",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else if (isSignUpComplete) {
            // Sign up success screen
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SignUpSuccessAnimation(
                    onContinue = {
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.SignUpScreen.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun SocialButton(
    icon: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(56.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(DarkNavyBlue.copy(alpha = 0.8f))
            .border(1.dp, GoldColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Social Sign Up",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun SignUpSuccessAnimation(onContinue: () -> Unit) {
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
                    enter = scaleIn(animationSpec = tween(500)) +
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
                    text = "Account Created!",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your account has been created successfully",
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
                    text = "SIGN IN NOW",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = rememberNavController())
}
