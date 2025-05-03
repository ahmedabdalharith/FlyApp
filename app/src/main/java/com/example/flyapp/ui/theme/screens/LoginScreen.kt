package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(navController: NavHostController) {
    // Form states with validation
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    // Field validation states
    val isEmailValid = email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isPasswordValid = password.length >= 8
    val isFormValid = isEmailValid && isPasswordValid

    // Processing states
    var isProcessing by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf(false) }

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
        if (!isProcessing) {
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
                        painter = painterResource(id = R.drawable.myplane),
                        contentDescription = "FlyApp Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(bottom = 16.dp)
                    )

                    Text(
                        text = "WELCOME BACK",
                        color = GoldColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Sign in to continue",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    // Error message if login fails
                    AnimatedVisibility(visible = loginError) {
                        Text(
                            text = "Invalid email or password. Please try again.",
                            color = Color.Red.copy(alpha = 0.8f),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 16.dp),
                            textAlign = TextAlign.Center
                        )
                    }

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
                        // Email field
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                loginError = false
                            },
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
                            onValueChange = {
                                password = it
                                loginError = false
                            },
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

                        // Remember me and Forgot password row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Remember me checkbox
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.clickable { rememberMe = !rememberMe }
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .border(1.dp, GoldColor, RoundedCornerShape(4.dp))
                                        .background(
                                            if (rememberMe) GoldColor else Color.Transparent,
                                            RoundedCornerShape(4.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (rememberMe) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.check_circle),
                                            contentDescription = "Remember me",
                                            tint = DarkNavyBlue,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Remember me",
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontSize = 14.sp
                                )
                            }

                            // Forgot password button
                            TextButton(onClick = {
                                navController.navigate(
                                    Screen.ForgotPasswordScreen.route
                                ) {
                                    popUpTo(Screen.LoginScreen.route) { inclusive = true }
                                }
                            }) {
                                Text(
                                    text = "Forgot Password?",
                                    color = GoldColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Login button
                    Button(
                        onClick = {
                            isProcessing = true
                            coroutineScope.launch {
                                delay(2000) // Simulate network request

                                // For demo purposes, let's simulate a successful login with any valid form
                                if (isFormValid) {
                                    // Navigate to main dashboard or home screen
                                    navController.navigate(Screen.MainScreen.route) {
                                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                                    }
                                } else {
                                    loginError = true
                                    isProcessing = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldColor,
                            contentColor = DarkNavyBlue,
                            disabledContainerColor = GoldColor.copy(alpha = 0.5f),
                            disabledContentColor = DarkNavyBlue.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "SIGN IN",
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
                                .height(1.dp),
                            color = GoldColor.copy(alpha = 0.3f)
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

                    // Social login options
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Google sign in
                        SocialSignInButton(
                            icon = R.drawable.google,
                            onClick = { /* Implement Google sign in */ }
                        )

                        // Facebook sign in
                        SocialSignInButton(
                            icon = R.drawable.facebook_ic,
                            onClick = { /* Implement Facebook sign in */ }
                        )

                        // Apple sign in
                        SocialSignInButton(
                            icon = R.drawable.apple_1ic,
                            onClick = { /* Implement Apple sign in */ }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Don't have an account link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Don't have an account?",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )

                        TextButton(onClick = { navController.navigate(Screen.SignUpScreen.route) }) {
                            Text(
                                text = "Create Account",
                                color = GoldColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        } else {
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
                        text = "Signing In...",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun SocialSignInButton(
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
            contentDescription = "Social Sign In",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navController = rememberNavController())
}