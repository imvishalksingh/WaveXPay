package com.productivityservicehub.wavexpay.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.productivityservicehub.wavexpay.R
import com.productivityservicehub.wavexpay.presentation.home.mainCards.BillSection
import com.productivityservicehub.wavexpay.presentation.home.mainCards.MoneyTransferSection
import com.productivityservicehub.wavexpay.presentation.home.mainCards.PopularSection
import com.productivityservicehub.wavexpay.presentation.home.mainCards.UtilitiesSection
import com.productivityservicehub.wavexpay.presentation.home.qr.QRScanSection
import com.productivityservicehub.wavexpay.presentation.home.topbar.HomeTopBar
import com.productivityservicehub.wavexpay.presentation.wallet.transactions.TransactionHistoryScreen
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import com.productivityservicehub.wavexpay.presentation.wallet.Screen
import com.productivityservicehub.wavexpay.presentation.wallet.WalletApp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToScanner: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToNotifications: () -> Unit,

    onNavigateToPayToContact: () -> Unit,
    onNavigateToBank: () -> Unit,
    onNavigateToSelfAccount: () -> Unit,
    onNavigateToCheckBalance: () -> Unit,
    onQrBtnClick: () -> Unit,

    ) {
    val context = LocalContext.current
    var selectedScreen by remember { mutableStateOf("Home") }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior();
    val navController = rememberNavController()


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            HomeTopBar(
                scrollBehavior = scrollBehavior,
                onSearchClick = onNavigateToSearch,
                onWalletClick = onNavigateToWallet,
                onNotificationsClick = onNavigateToNotifications
            )
        }, bottomBar = {
            BottomAppBar(modifier = Modifier.fillMaxWidth(), content = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.clickable(enabled = true, onClick = {
                        selectedScreen = "Home"
                    }, indication = null , interactionSource = remember { MutableInteractionSource() }), verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Default.Home,
                            contentDescription = null
                        )
                        Text(text = "Home", fontSize = 22.sp);
                    }
                    Box(
                        modifier = Modifier
                            .width(70.dp)
                            .height(70.dp)
                            .clip(RoundedCornerShape(70))
                            .background(Color(0xFF090F9A))
                            .clickable(enabled = true, onClick = onQrBtnClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            painter = painterResource(id = R.drawable.qr_code_scanner_24px),
                            contentDescription = null
                        )

                    }
                    Row(
                        modifier = Modifier.clickable(enabled = true, onClick = {selectedScreen = "History"}),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(35.dp),
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null
                        )
                        Text(text = "History", fontSize = 22.sp);
                    }
                }

            }, containerColor = Color(0xFF03066E), contentColor = Color(0xFFFFFAFA))
        }) { padding ->
        when (selectedScreen) {
            "Home" -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                listOf<Color>(
                                    Color(0xFF02133B),
                                    Color(0xFF021348),
                                    Color(0xFF0041FF)
                                ), 0.0f, 0.5f
                            )
                        )
                        .padding(padding)
                ) {
                    item {
                        QRScanSection(onClick = onNavigateToScanner, scrollBehavior);
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 30.dp, topEnd = 30.dp
                                    )
                                )
                                .background(Color(0xFFFFFFFF))
                                .padding(
                                    top = 20.dp
                                )
                        ) {
                            BillSection()
                            Spacer(modifier = Modifier.height(9.dp))
                            LazyRow(modifier = Modifier.padding(bottom = 9.dp , start = 15.dp , end = 15.dp), horizontalArrangement = Arrangement.SpaceAround){
                                item {
                                    TintedChip(text = "Share WaveXPay, get up to ₹300" , leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.MonetizationOn,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    })
                                    Spacer(modifier = Modifier.width(10.dp))
                                }
                                item { TintedChip(text = "Scan QR, get assured money up to ₹100", leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.QrCodeScanner,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }) }
                            }
                            MoneyTransferSection(
                                onPayToContactClick = onNavigateToPayToContact,
                                onToBankClick = onNavigateToBank,
                                onSelfAccountClick = onNavigateToSelfAccount,
                                onCheckBalanceClick = onNavigateToCheckBalance
                            )
                            Spacer(modifier = Modifier.height(16.dp))

                            PopularSection()
                            Spacer(modifier = Modifier.height(16.dp))
                            UtilitiesSection()
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }

            "History" -> {
                WalletApp(startScreen = Screen.TransactionHistory())
            }

        }
    }
}




@Composable
fun TintedChip(
    text: String,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable (() -> Unit)? = {
        Icon(
            imageVector = Icons.Default.Percent,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    },
    trailingIcon: @Composable (() -> Unit)? = {
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
        )
    },
    backgroundTint: Color = Color(0xFF1E8E3E).copy(alpha = 0.12f),
    borderTint: Color = Color(0xFF1E8E3E).copy(alpha = 0.18f),
    contentTint: Color = Color(0xFF0B441C),
    cornerRadius: Dp = 24.dp,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .wrapContentHeight()
            .clickable { onClick() },
        color = backgroundTint,
        contentColor = contentTint,
        shape = RoundedCornerShape(cornerRadius),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, borderTint)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                CompositionLocalProvider(LocalContentColor provides contentTint) {
                    leadingIcon()
                }
                Spacer(Modifier.width(10.dp))
            }

            Text(
                fontSize = 14.sp,
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = contentTint
            )

            if (trailingIcon != null) {
                Spacer(Modifier.weight(1f))
                CompositionLocalProvider(LocalContentColor provides contentTint) {
                    trailingIcon()
                }
            }
        }
    }
}


