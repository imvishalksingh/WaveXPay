package com.productivityservicehub.wavexpay.presentation.home.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onSearchClick: () -> Unit,
    onWalletClick: () -> Unit,
    onNotificationsClick: () -> Unit,
) {
    TopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text("Location", fontSize = 12.sp, color = Color.White)
                    Text(
                        "B-297, New ashok Nagar",
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, "Search", tint = Color.White)
            }
            IconButton(onClick = onWalletClick) {
                Icon(Icons.Default.AccountBalanceWallet, "Wallet", tint = Color.White)
            }
            IconButton(onClick = onNotificationsClick) {
                Icon(Icons.Default.Notifications, "Notifications", tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF0041FF)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(onBackPressed: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Services", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E3A8A)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search for bills, recharge, utilities...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, "Search")
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Popular Searches",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            val popularSearches = listOf(
                "Mobile Recharge" to Icons.Default.Phone,
                "Credit Card Bill" to Icons.Default.CreditCard,
                "Electricity Bill" to Icons.Default.Lightbulb,
                "DTH Recharge" to Icons.Default.Satellite,
                "Broadband Bill" to Icons.Default.Wifi,
                "Water Bill" to Icons.Default.WaterDrop,
                "Gas Cylinder" to Icons.Default.LocalGasStation,
                "Loan Repayment" to Icons.Default.AttachMoney
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                popularSearches.forEach { (search, icon) ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { searchQuery = search },
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = Color(0xFFE8EAF6)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        icon,
                                        null,
                                        modifier = Modifier.size(20.dp),
                                        tint = Color(0xFF1E3A8A)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(search, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                Icons.Default.ArrowForward,
                                null,
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(onBackPressed: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notifications", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                    }
                },
                actions = {
                    TextButton(onClick = { }) {
                        Text("Mark all read", color = Color.White, fontSize = 12.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E3A8A)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NotificationCard(
                icon = Icons.Default.CreditCard,
                title = "Credit Card Bill Due",
                message = "Your credit card bill of ₹1200 is due on Wed, 25 Jan. Pay now to avoid late fees.",
                time = "2 hours ago",
                isUnread = true,
                iconColor = Color(0xFFE74C3C)
            )
            NotificationCard(
                icon = Icons.Default.CheckCircle,
                title = "Payment Successful",
                message = "Your mobile recharge of ₹299 was successful. Transaction ID: TXN123456789",
                time = "5 hours ago",
                isUnread = true,
                iconColor = Color(0xFF4CAF50)
            )
            NotificationCard(
                icon = Icons.Default.Lightbulb,
                title = "Electricity Bill Generated",
                message = "Your electricity bill for this month is ₹850. Due date: Nov 5, 2025",
                time = "Yesterday",
                isUnread = false,
                iconColor = Color(0xFFFF9800)
            )
            NotificationCard(
                icon = Icons.Default.LocalOffer,
                title = "Special Offer - 10% Cashback",
                message = "Get 10% cashback on your next recharge. Valid till Oct 31, 2025",
                time = "2 days ago",
                isUnread = false,
                iconColor = Color(0xFF9C27B0)
            )
            NotificationCard(
                icon = Icons.Default.Security,
                title = "Security Alert",
                message = "New login detected from Chrome on Windows. If this wasn't you, please secure your account.",
                time = "3 days ago",
                isUnread = false,
                iconColor = Color(0xFFFF5722)
            )
            NotificationCard(
                icon = Icons.Default.Satellite,
                title = "DTH Recharge Reminder",
                message = "Your DTH subscription will expire in 3 days. Recharge now for uninterrupted service.",
                time = "4 days ago",
                isUnread = false,
                iconColor = Color(0xFF2196F3)
            )
        }
    }
}

@Composable
fun NotificationCard(
    icon: ImageVector,
    title: String,
    message: String,
    time: String,
    isUnread: Boolean,
    iconColor: Color
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isUnread) Color(0xFFE8F4FD) else Color.White,
        shadowElevation = if (isUnread) 3.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = iconColor.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        null,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        title,
                        fontWeight = if (isUnread) FontWeight.Bold else FontWeight.Medium,
                        fontSize = 15.sp,
                        modifier = Modifier.weight(1f)
                    )
                    if (isUnread) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color(0xFF1E3A8A), CircleShape)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    message,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    lineHeight = 18.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    time,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}