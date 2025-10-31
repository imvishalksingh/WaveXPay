package com.productivityservicehub.wavexpay.presentation

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.productivityservicehub.wavexpay.domain.data.UserPreferences
import com.productivityservicehub.wavexpay.presentation.wallet.WalletTheme
import kotlinx.coroutines.delay
import kotlin.random.Random


@Composable
fun SplashScreen(navController: NavController, userPreferences: UserPreferences) {
    var startAnimation by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isLoggedIn by userPreferences.isLoggedIn.collectAsState(initial = false)


    // Logo scale animation
    val logoScale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    // Logo rotation animation
    val logoRotation by animateFloatAsState(
        targetValue = if (startAnimation) 360f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
    )

    // Text fade in animation
    val textAlpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800, delayMillis = 500)
    )

    // Tagline slide up animation
    val taglineOffset by animateDpAsState(
        targetValue = if (startAnimation) 0.dp else 50.dp,
        animationSpec = tween(durationMillis = 800, delayMillis = 700)
    )

    // Particle system
    val particles = remember {
        List(30) {
            ParticleState(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 4 + 2,
                speed = Random.nextFloat() * 0.5f + 0.2f
            )
        }
    }

    // Animated gradient
    val infiniteTransition = rememberInfiniteTransition()
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000)
        if (isLoggedIn) {
            navController.navigate("home") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF2D1B69),
                        Color(0xFF5F259F),
                        Color(0xFF8247B5)
                    ),
                    start = Offset(gradientOffset, gradientOffset),
                    end = Offset(gradientOffset + 500f, gradientOffset + 500f)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Animated particles background
        Canvas(modifier = Modifier.fillMaxSize()) {
            particles.forEach { particle ->
                val progress = (gradientOffset / 1000f + particle.speed) % 1f
                drawCircle(
                    color = Color.White.copy(alpha = 0.3f * (1 - progress)),
                    radius = particle.size,
                    center = Offset(
                        size.width * particle.x,
                        size.height * ((particle.y + progress) % 1f)
                    )
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated logo with glow effect
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(150.dp)
                    .scale(logoScale)
                    .rotate(logoRotation)
            ) {
                // Glow effect
                Surface(
                    modifier = Modifier.size(140.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.1f)
                ) {}

                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.15f)
                ) {}

                // Main logo
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 20.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = WalletTheme.Primary,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // App name with fade in
            Text(
                "WavexPay",
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier
                    .alpha(textAlpha)
                    .scale(logoScale),
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline with slide up
            Text(
                "Your Smart Payment Partner",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.9f),
                modifier = Modifier
                    .alpha(textAlpha)
                    .offset(y = taglineOffset)
            )

            Spacer(modifier = Modifier.height(80.dp))

            // Loading indicator
            if (startAnimation) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .alpha(textAlpha)
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                }
            }
        }

        // Bottom branding
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .alpha(textAlpha),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Secure & Fast Payments",
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

data class ParticleState(
    val x: Float,
    val y: Float,
    val size: Float,
    val speed: Float
)