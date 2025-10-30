package com.productivityservicehub.wavexpay.presentation.wallet.accounts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.productivityservicehub.wavexpay.presentation.wallet.BankAccount
import com.productivityservicehub.wavexpay.presentation.wallet.WalletTheme
import kotlinx.coroutines.launch



fun getSampleBankAccounts() = listOf(
    BankAccount("1", "HDFC Bank", "1234", 25430.50, Icons.Default.AccountBalance, WalletTheme.HDFCColor),
    BankAccount("2", "SBI", "9876", 18250.75, Icons.Default.AccountBalance, WalletTheme.SBIColor),
    BankAccount("3", "ICICI Bank", "5678", 32100.00, Icons.Default.AccountBalance, Color(0xFFFF6B35))
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectBankScreen(
    onBankSelected: (BankAccount) -> Unit,
    onBackPressed: () -> Unit,
    onHistory: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Account", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(WalletTheme.Background)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getSampleBankAccounts()) { account ->
                SelectableBankCard(
                    account = account,
                    onClick = { onBankSelected(account) }
                )
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    shape = RoundedCornerShape(12.dp),
                    color = WalletTheme.CardBackground,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = WalletTheme.Accent.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Bolt,
                                    contentDescription = null,
                                    tint = WalletTheme.Accent,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "UPI Lite",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = WalletTheme.TextPrimary
                            )
                            Text(
                                "For quick payments up to ₹500",
                                fontSize = 13.sp,
                                color = WalletTheme.TextSecondary
                            )
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = WalletTheme.TextSecondary)
                    }
                }
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { },
                    shape = RoundedCornerShape(12.dp),
                    color = WalletTheme.CardBackground,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = WalletTheme.Primary.copy(alpha = 0.1f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = WalletTheme.Primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "WavexPay Wallet",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                                color = WalletTheme.TextPrimary
                            )
                            Text(
                                "Balance: ₹2,450.00",
                                fontSize = 13.sp,
                                color = WalletTheme.TextSecondary
                            )
                        }
                        Icon(Icons.Default.ChevronRight, null, tint = WalletTheme.TextSecondary)
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = WalletTheme.Primary
                    )
                ) {
                    Icon(Icons.Default.Add, null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create New UPI Account", fontWeight = FontWeight.Medium)
                }
            }

            item {
                QuickActionCard(
                    icon = Icons.Default.History,
                    label = "History",
                    color = Color(0xFF9C27B0),
                    onClick = onHistory,
                )
            }
        }
    }
}

@Composable
fun SelectableBankCard(account: BankAccount, onClick: () -> Unit) {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale.value)
            .clickable {
                scope.launch {
                    scale.animateTo(0.97f, animationSpec = tween(100))
                    scale.animateTo(1f, animationSpec = tween(100))
                }
                onClick()
            },
        shape = RoundedCornerShape(12.dp),
        color = WalletTheme.CardBackground,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(56.dp),
                shape = RoundedCornerShape(8.dp),
                color = account.color.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        account.icon,
                        contentDescription = null,
                        tint = account.color,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    account.bankName,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = WalletTheme.TextPrimary
                )
                Text(
                    "****${account.accountNumber.takeLast(4)}",
                    fontSize = 14.sp,
                    color = WalletTheme.TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = WalletTheme.TextSecondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}


@Composable
fun QuickActionCard(
    icon: ImageVector,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val scale = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier
            .aspectRatio(1f)
            .scale(scale.value)
            .clickable {
                scope.launch {
                    scale.animateTo(0.9f, animationSpec = tween(100))
                    scale.animateTo(1f, animationSpec = tween(100))
                }
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        color = WalletTheme.CardBackground,
        shadowElevation = 2.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = color.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = color,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = WalletTheme.TextPrimary,
                maxLines = 2
            )
        }
    }
}

