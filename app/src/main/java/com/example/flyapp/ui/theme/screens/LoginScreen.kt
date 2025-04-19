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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.flyapp.ui.theme.navigition.Screen

@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // App Logo with animation
            LogoSection()

            Spacer(modifier = Modifier.height(40.dp))

            // Login Card
            LoginCard(
                email = email,
                onEmailChange = { email = it },
                password = password,
                onPasswordChange = { password = it },
                passwordVisible = passwordVisible,
                onPasswordVisibilityChange = { passwordVisible = it },
                rememberMe = rememberMe,
                onRememberMeChange = { rememberMe = it },
                onLogin = { navController.navigate(Screen.MainScreen.route) },
                onForgotPassword = {
                    navController.navigate(Screen.ForgetPasswordScreen.route)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Social Login Options
            SocialLoginOptions()

            Spacer(modifier = Modifier.weight(1f))

            // Register Link
            RegisterLink(
                onRegister = { navController.navigate(Screen.SignUpScreen.route) }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun LogoSection() {
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
        initialValue = -3f,
        targetValue = 3f,
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
        // Logo circle with glow
        Box(
            modifier = Modifier.size(120.dp),
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
                    .size(100.dp)
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
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App name
        Text(
            text = "FlyApp",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Text(
            text = "Your Journey Begins Here",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun LoginCard(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChange: (Boolean) -> Unit,
    rememberMe: Boolean,
    onRememberMeChange: (Boolean) -> Unit,
    onLogin: () -> Unit,
    onForgotPassword: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "login_card_animation")
    val borderGlow by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "card_border_glow"
    )

    // Login form card with subtle border glow
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
            Text(
                text = "Welcome Back",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Login to your account",
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email field
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email Address") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.email_), // Replace with email icon
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
                        painter = painterResource(R.drawable.password), // Replace with lock icon
                        contentDescription = "Password",
                        tint = Color(0xFF64B5F6)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { onPasswordVisibilityChange(!passwordVisible) }) {
                        Icon(
                            painter = painterResource(
                                if (passwordVisible) R.drawable.eye_password_hide else R.drawable.eye_password_show
                            ), // Replace with visibility icons
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

            // Remember me and Forgot password row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onRememberMeChange(!rememberMe) }
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = onRememberMeChange,
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF0288D1),
                            uncheckedColor = Color.White.copy(alpha = 0.7f),
                            checkmarkColor = Color.White
                        )
                    )
                    Text(
                        text = "Remember me",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = "Forgot Password?",
                    color = Color(0xFF64B5F6),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { onForgotPassword() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Login button with animation
            val buttonGlowAlpha by infiniteTransition.animateFloat(
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
                    onClick = onLogin,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0288D1)
                    ),
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Login",
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
fun SocialLoginOptions() {
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
                text = "  Or continue with  ",
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

        // Social login buttons
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            SocialLoginButton(
                icon = R.drawable.google, // Replace with Google icon
                contentDescription = "Login with Google",
                backgroundColor = Color(0xFF0A192F).copy(alpha = 0.9f)
            )

            SocialLoginButton(
                icon = R.drawable.facebook_ic, // Replace with Facebook icon
                contentDescription = "Login with Facebook",
                backgroundColor = Color(0xFF0A192F).copy(alpha = 0.9f)
            )

            SocialLoginButton(
                icon = R.drawable.apple_1ic, // Replace with Apple icon
                contentDescription = "Login with Apple",
                backgroundColor = Color(0xFF0A192F).copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun SocialLoginButton(
    icon: Int,
    contentDescription: String,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable { /* Handle social login */ },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun RegisterLink(onRegister: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Don't have an account? ",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp
        )

        Text(
            text = "Register",
            color = Color(0xFF64B5F6),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.clickable { onRegister() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}