package com.productivityservicehub.wavexpay.presentation.wallet.mpin

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.productivityservicehub.wavexpay.presentation.wallet.BankAccount
import com.productivityservicehub.wavexpay.presentation.wallet.WalletTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterPinScreen(
    account: BankAccount,
    onPinVerified: (Double) -> Unit,
    onBackPressed: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val correctPin = "123456"
    val pinLength = 6

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Enter UPI PIN", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WalletTheme.Primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WalletTheme.Background)
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Bank Logo
            Surface(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(16.dp),
                color = account.color.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        account.icon,
                        contentDescription = null,
                        tint = account.color,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                account.bankName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = WalletTheme.TextPrimary
            )

            Text(
                "****${account.accountNumber.takeLast(4)}",
                fontSize = 14.sp,
                color = WalletTheme.TextSecondary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                "Enter UPI PIN to check balance",
                fontSize = 16.sp,
                color = WalletTheme.TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            // PIN Dots
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(pinLength) { index ->
                    PinDot(
                        isFilled = index < pin.length,
                        isError = isError
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Number Pad
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("", "0", "⌫")
                ).forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        row.forEach { digit ->
                            NumberButton(
                                text = digit,
                                onClick = {
                                    if (isLoading) return@NumberButton
                                    when (digit) {
                                        "⌫" -> if (pin.isNotEmpty()) {
                                            pin = pin.dropLast(1)
                                            isError = false
                                        }
                                        "", " " -> {}
                                        else -> if (pin.length < pinLength) {
                                            pin += digit
                                            isError = false

                                            if (pin.length == pinLength) {
                                                scope.launch {
                                                    isLoading = true
                                                    delay(800)
                                                    if (pin == correctPin) {
                                                        onPinVerified(account.balance)
                                                    } else {
                                                        isError = true
                                                        delay(600)
                                                        pin = ""
                                                        isError = false
                                                    }
                                                    isLoading = false
                                                }
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            if (isError) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Incorrect PIN. Please try again.",
                    color = WalletTheme.Error,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Demo PIN: 1234",
                color = WalletTheme.TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                CircularProgressIndicator(color = WalletTheme.Primary)
            }
        }
    }
}

@Composable
fun PinDot(isFilled: Boolean, isError: Boolean) {
    val scale by animateFloatAsState(
        targetValue = if (isFilled) 1f else 0.85f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh)
    )

    Box(
        modifier = Modifier
            .size(14.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                if (isError) WalletTheme.Error.copy(alpha = 0.3f)
                else if (isFilled) WalletTheme.Primary else Color.LightGray
            )
            .border(
                width = 1.dp,
                color = if (isError) WalletTheme.Error
                else if (isFilled) WalletTheme.Primary
                else Color.Gray,
                shape = CircleShape
            )
    )
}

@Composable
fun RowScope.NumberButton(text: String, onClick: () -> Unit) {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1.3f)
            .scale(scale.value)
            .clickable(enabled = text.isNotEmpty()) {
                scope.launch {
                    scale.animateTo(0.9f, animationSpec = tween(50))
                    scale.animateTo(1f, animationSpec = tween(50))
                }
                onClick()
            },
        shape = RoundedCornerShape(12.dp),
        color = if (text.isNotEmpty()) WalletTheme.CardBackground else Color.Transparent,
        shadowElevation = if (text.isNotEmpty()) 1.dp else 0.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (text.isNotEmpty()) {
                Text(
                    text,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = WalletTheme.TextPrimary
                )
            }
        }
    }
}
