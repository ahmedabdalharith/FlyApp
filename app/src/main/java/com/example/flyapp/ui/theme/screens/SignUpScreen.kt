package com.example.flyapp.ui.theme.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.flyapp.R
import com.example.flyapp.ui.theme.components.ParticleEffectBackground

@Composable
fun SignUpScreen(navController: NavHostController) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var agreeToTerms by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

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
        // Reuse the particle effect from your existing screens
        ParticleEffectBackground()

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // App Logo with animation (slightly smaller than login)
            LogoSectionSmall()

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Card
            SignUpCard(
                fullName = fullName,
                onFullNameChange = { fullName = it },
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                confirmPassword = confirmPassword,
                onConfirmPasswordChange = { confirmPassword = it },
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = it },
                confirmPasswordVisible = confirmPasswordVisible,
                onConfirmPasswordVisibilityChange = { confirmPasswordVisible = it },
                agreeToTerms = agreeToTerms,
                onAgreeToTermsChange = { agreeToTerms = it },
                onSignUp = { navController.navigate("home") }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Social Sign Up Options
            SocialSignUpOptions()

            Spacer(modifier = Modifier.height(24.dp))

            // Login Link
            LoginLink(
                onLogin = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LogoSectionSmall() {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_animation")
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_glow"
    )

    val floatAnimation by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_float"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.offset(y = floatAnimation.dp)
    ) {
        // Logo circle with glow (smaller than login)
        Box(
            modifier = Modifier.size(90.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glow effect
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color(0xFF64B5F6).copy(alpha = glow * 0.4f),
                    radius = size.minDimension / 1.8f,
                    center = center
                )
            }

            // Logo circle
            Box(
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF0D47A1),
                                Color(0xFF1976D2)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // App logo/icon
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "FlyApp Logo",
                    modifier = Modifier.size(45.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // App name
        Text(
            text = "Create Account",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )

        Text(
            text = "Start Your Journey",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun SignUpCard(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityChange: (Boolean) -> Unit,
    agreeToTerms: Boolean,
    onAgreeToTermsChange: (Boolean) -> Unit,
    onSignUp: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "signup_card_animation")
    val buttonGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_glow"
    )

    // Sign Up form card with subtle border glow
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0A192F).copy(alpha = 0.85f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Full Name field
            OutlinedTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Full Name") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.profile_ic), // Replace with person icon
                        contentDescription = "Full Name",
                        tint = Color(0xFF64B5F6)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF64B5F6),
                    unfocusedBorderColor = Color(0xFF64B5F6).copy(alpha = 0.5f),
                    focusedLabelColor = Color(0xFF64B5F6),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color(0xFF64B5F6),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email Address") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.email_),
                        contentDescription = "Email",
                        tint = Color(0xFF64B5F6)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF64B5F6),
                    unfocusedBorderColor = Color(0xFF64B5F6).copy(alpha = 0.5f),
                    focusedLabelColor = Color(0xFF64B5F6),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color(0xFF64B5F6),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.password),
                        contentDescription = "Password",
                        tint = Color(0xFF64B5F6)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible) R.drawable.eye_password_hide else R.drawable.eye_password_show
                            ),
                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                            tint = Color(0xFF64B5F6).copy(alpha = 0.7f)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF64B5F6),
                    unfocusedBorderColor = Color(0xFF64B5F6).copy(alpha = 0.5f),
                    focusedLabelColor = Color(0xFF64B5F6),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color(0xFF64B5F6),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password field
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Confirm Password") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.password),
                        contentDescription = "Confirm Password",
                        tint = Color(0xFF64B5F6)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { onConfirmPasswordVisibilityChange(!confirmPasswordVisible) }) {
                        Icon(
                            painter = painterResource(
                                if (confirmPasswordVisible) R.drawable.eye_password_hide else R.drawable.eye_password_show
                            ),
                            contentDescription = if (confirmPasswordVisible) "Hide Password" else "Show Password",
                            tint = Color(0xFF64B5F6).copy(alpha = 0.7f)
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF64B5F6),
                    unfocusedBorderColor = Color(0xFF64B5F6).copy(alpha = 0.5f),
                    focusedLabelColor = Color(0xFF64B5F6),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
                    cursorColor = Color(0xFF64B5F6),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Terms and conditions checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onAgreeToTermsChange(!agreeToTerms) }
            ) {
                Checkbox(
                    checked = agreeToTerms,
                    onCheckedChange = onAgreeToTermsChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF0288D1),
                        uncheckedColor = Color.White.copy(alpha = 0.7f),
                        checkmarkColor = Color.White
                    )
                )

                Text(
                    text = "I agree to the Terms of Service & Privacy Policy",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up button with animation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                // Button glow effect
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRoundRect(
                        color = Color(0xFF2196F3).copy(alpha = buttonGlowAlpha * 0.3f),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(28.dp.toPx()),
                        size = androidx.compose.ui.geometry.Size(size.width + 8.dp.toPx(), size.height + 8.dp.toPx()),
                        topLeft = Offset(-4.dp.toPx(), -4.dp.toPx())
                    )
                }

                Button(
                    onClick = onSignUp,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0288D1)
                    ),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Create Account",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SocialSignUpOptions() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Or divider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.3f)
            )

            Text(
                text = "  Or sign up with  ",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )

            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color.White.copy(alpha = 0.3f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Social signup buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            SocialLoginButton(
                icon = R.drawable.google,
                contentDescription = "Sign up with Google",
                backgroundColor = Color(0xFF0A192F).copy(alpha = 0.9f)
            )

            SocialLoginButton(
                icon = R.drawable.facebook_ic,
                contentDescription = "Sign up with Facebook",
                backgroundColor = Color(0xFF0A192F).copy(alpha = 0.9f)
            )

            SocialLoginButton(
                icon = R.drawable.apple_1ic,
                contentDescription = "Sign up with Apple",
                backgroundColor = Color(0xFF0A192F).copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun LoginLink(onLogin: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Already have an account? ",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )

        Text(
            text = "Login",
            color = Color(0xFF64B5F6),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onLogin() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(rememberNavController())
}