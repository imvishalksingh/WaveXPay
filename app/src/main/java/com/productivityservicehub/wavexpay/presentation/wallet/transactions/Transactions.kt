package com.productivityservicehub.wavexpay.presentation.wallet.transactions

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Satellite
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.productivityservicehub.wavexpay.presentation.wallet.WalletTheme
import kotlinx.coroutines.launch
import kotlin.math.abs

data class Transaction(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val date: String,
    val time: String,
    val icon: ImageVector,
    val status: TransactionStatus,
    val utr: String,
    val toFrom: String,
    val paymentMode: String
)

enum class TransactionStatus { SUCCESS, PENDING, FAILED, REFUNDED }
enum class TransactionFilter { ALL, SENT, RECEIVED, REFUNDS }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    initialFilter: TransactionFilter = TransactionFilter.ALL,
    onTransactionClick: (Transaction) -> Unit,
    onBackPressed: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf(initialFilter) }
    val allTransactions = remember { getAllTransactions() }

    val filteredTransactions = remember(selectedFilter) {
        when (selectedFilter) {
            TransactionFilter.ALL -> allTransactions
            TransactionFilter.SENT -> allTransactions.filter { it.amount < 0 }
            TransactionFilter.RECEIVED -> allTransactions.filter { it.amount > 0 }
            TransactionFilter.REFUNDS -> allTransactions.filter { it.status == TransactionStatus.REFUNDED }
        }
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Transaction History", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(Icons.Default.ArrowBack, "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = WalletTheme.Primary,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White
                    ),
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Search, null, tint = Color.White)
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.FilterList, null, tint = Color.White)
                        }
                    }
                )

                // Filter Tabs
                Surface(
                    color = WalletTheme.Primary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ScrollableTabRow(
                        selectedTabIndex = selectedFilter.ordinal,
                        containerColor = WalletTheme.Primary,
                        contentColor = Color.White,
                        edgePadding = 16.dp,
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedFilter.ordinal]),
                                color = Color.White
                            )
                        }
                    ) {
                        TransactionFilter.values().forEach { filter ->
                            Tab(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                text = {
                                    Text(
                                        when (filter) {
                                            TransactionFilter.ALL -> "All"
                                            TransactionFilter.SENT -> "Sent"
                                            TransactionFilter.RECEIVED -> "Received"
                                            TransactionFilter.REFUNDS -> "Refunds"
                                        },
                                        fontWeight = if (selectedFilter == filter) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (filteredTransactions.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Receipt,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No transactions found",
                        fontSize = 16.sp,
                        color = WalletTheme.TextSecondary
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WalletTheme.Background)
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredTransactions) { transaction ->
                    DetailedTransactionCard(
                        transaction = transaction,
                        onClick = { onTransactionClick(transaction) }
                    )
                }
            }
        }
    }
}

@Composable
fun DetailedTransactionCard(transaction: Transaction, onClick: () -> Unit) {
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
                modifier = Modifier.size(52.dp),
                shape = CircleShape,
                color = when {
                    transaction.amount > 0 -> WalletTheme.Success.copy(alpha = 0.1f)
                    else -> Color(0xFFFFEBEE)
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        transaction.icon,
                        contentDescription = null,
                        tint = when {
                            transaction.amount > 0 -> WalletTheme.Success
                            else -> WalletTheme.Error
                        },
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    transaction.title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = WalletTheme.TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "${transaction.date} • ${transaction.time}",
                    fontSize = 12.sp,
                    color = WalletTheme.TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (transaction.status) {
                        TransactionStatus.SUCCESS -> WalletTheme.Success.copy(alpha = 0.1f)
                        TransactionStatus.PENDING -> WalletTheme.Warning.copy(alpha = 0.1f)
                        TransactionStatus.FAILED -> WalletTheme.Error.copy(alpha = 0.1f)
                        TransactionStatus.REFUNDED -> Color(0xFFE3F2FD)
                    }
                ) {
                    Text(
                        when (transaction.status) {
                            TransactionStatus.SUCCESS -> "Success"
                            TransactionStatus.PENDING -> "Pending"
                            TransactionStatus.FAILED -> "Failed"
                            TransactionStatus.REFUNDED -> "Refunded"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = when (transaction.status) {
                            TransactionStatus.SUCCESS -> WalletTheme.Success
                            TransactionStatus.PENDING -> WalletTheme.Warning
                            TransactionStatus.FAILED -> WalletTheme.Error
                            TransactionStatus.REFUNDED -> Color(0xFF1976D2)
                        },
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "${if (transaction.amount > 0) "+" else ""}₹${String.format("%.2f",
                        abs(transaction.amount)
                    )}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = when {
                        transaction.amount > 0 -> WalletTheme.Success
                        else -> WalletTheme.Error
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = WalletTheme.TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionDetailScreen(
    transaction: Transaction?,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transaction Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WalletTheme.Primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Share, null, tint = Color.White)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Download, null, tint = Color.White)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(WalletTheme.Background)
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Status Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = when (transaction?.status) {
                        TransactionStatus.SUCCESS -> WalletTheme.Success
                        TransactionStatus.PENDING -> WalletTheme.Warning
                        TransactionStatus.FAILED -> WalletTheme.Error
                        TransactionStatus.REFUNDED -> Color(0xFF1976D2)
                        else -> Color(0xFFFFFFFF)
                    },
                    shadowElevation = 4.dp
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            when (transaction?.status) {
                                TransactionStatus.SUCCESS -> Icons.Default.CheckCircle
                                TransactionStatus.PENDING -> Icons.Default.Schedule
                                TransactionStatus.FAILED -> Icons.Default.Cancel
                                TransactionStatus.REFUNDED -> Icons.Default.Refresh
                                else -> Icons.Default.Refresh
                            },
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(64.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            when (transaction?.status) {
                                TransactionStatus.SUCCESS -> "Payment Successful"
                                TransactionStatus.PENDING -> "Payment Pending"
                                TransactionStatus.FAILED -> "Payment Failed"
                                TransactionStatus.REFUNDED -> "Payment Refunded"
                                else -> "Failed"
                            },
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        transaction?.amount?.let {
                            Text(
                                "${if (it > 0) "+" else ""}₹${String.format("%.2f",
                                    abs(transaction.amount)
                                )}",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            item {
                // Transaction Details Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = WalletTheme.CardBackground,
                    shadowElevation = 2.dp
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            "Transaction Details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = WalletTheme.TextPrimary
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        DetailRow("Transaction Type", transaction?.title)
                        DetailRow("Category", transaction?.category)
                        transaction?.amount?.let {
                            DetailRow(
                                if (it > 0) "From" else "To",
                                transaction.toFrom
                            )
                        }
                        DetailRow("Payment Mode", transaction?.paymentMode)
                        DetailRow("Date & Time", "${transaction?.date}, ${transaction?.time}")
                        DetailRow("UTR / Reference ID", transaction?.utr, isLast = true)
                    }
                }
            }

            item {
                // Action Buttons
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    if (transaction?.status == TransactionStatus.SUCCESS) {
                        Button(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WalletTheme.Primary
                            )
                        ) {
                            Icon(Icons.Default.Refresh, null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Repeat Transaction", modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }

                    OutlinedButton(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = WalletTheme.Primary
                        )
                    ) {
                        Icon(Icons.Default.Phone, null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Contact Support", modifier = Modifier.padding(vertical = 4.dp))
                    }

                    if (transaction!!.status == TransactionStatus.FAILED) {
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = WalletTheme.Error
                            )
                        ) {
                            Icon(Icons.Default.Report, null, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Report Issue", modifier = Modifier.padding(vertical = 4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String?, value: String?, isLast: Boolean? = false) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                label!!,
                fontSize = 14.sp,
                color = WalletTheme.TextSecondary,
                modifier = Modifier.weight(1f)
            )
            Text(
                value!!,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = WalletTheme.TextPrimary,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1.5f)
            )
        }
        if (!isLast!!) {
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
        }
    }
}

fun getSampleTransactions() = listOf(
    Transaction("1", "Mobile Recharge", "Utilities", -299.0, "Today", "2:30 PM", Icons.Default.Phone, TransactionStatus.SUCCESS, "UTR12345678901", "Airtel Prepaid", "UPI"),
    Transaction("2", "Money Received", "Credit", 1000.0, "Yesterday", "11:45 AM", Icons.Default.AccountBalance, TransactionStatus.SUCCESS, "UTR98765432109", "Rahul Sharma", "UPI"),
    Transaction("3", "Electricity Bill", "Utilities", -850.0, "Oct 27", "3:15 PM", Icons.Default.Lightbulb, TransactionStatus.SUCCESS, "UTR11223344556", "BSES Rajdhani", "UPI"),
    Transaction("4", "DTH Recharge", "Utilities", -450.0, "Oct 26", "9:20 AM", Icons.Default.Satellite, TransactionStatus.PENDING, "UTR66778899001", "Tata Play", "UPI")
)

fun getAllTransactions() = getSampleTransactions() + listOf(
    Transaction("5", "Credit Card Payment", "Payment", -1200.0, "Oct 25", "1:00 PM", Icons.Default.CreditCard, TransactionStatus.SUCCESS, "UTR22334455667", "HDFC Credit Card", "UPI"),
    Transaction("6", "Cashback Received", "Reward", 50.0, "Oct 24", "5:30 PM", Icons.Default.CardGiftcard, TransactionStatus.SUCCESS, "UTR77889900112", "WavexPay Cashback", "Wallet"),
    Transaction("7", "Water Bill", "Utilities", -320.0, "Oct 23", "11:00 AM", Icons.Default.WaterDrop, TransactionStatus.SUCCESS, "UTR33445566778", "DJB Water Board", "UPI"),
    Transaction("8", "Money Transfer", "Transfer", -500.0, "Oct 22", "4:45 PM", Icons.Default.Send, TransactionStatus.PENDING, "UTR44556677889", "Priya Verma", "UPI"),
    Transaction("9", "Shopping", "Retail", -1850.0, "Oct 21", "7:20 PM", Icons.Default.ShoppingCart, TransactionStatus.SUCCESS, "UTR55667788990", "Amazon Pay", "UPI"),
    Transaction("10", "Refund", "Credit", 299.0, "Oct 20", "2:10 PM", Icons.Default.Refresh, TransactionStatus.REFUNDED, "UTR66778899001", "Flipkart", "UPI"),
    Transaction("11", "Food Delivery", "Food", -450.0, "Oct 19", "8:30 PM", Icons.Default.Restaurant, TransactionStatus.SUCCESS, "UTR77889900223", "Zomato", "UPI"),
    Transaction("12", "Money Sent", "Transfer", -750.0, "Oct 18", "10:15 AM", Icons.Default.Send, TransactionStatus.FAILED, "UTR88990011334", "Amit Kumar", "UPI")
)