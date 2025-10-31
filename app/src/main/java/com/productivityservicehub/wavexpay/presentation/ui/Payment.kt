package com.productivityservicehub.wavexpay.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.productivityservicehub.wavexpay.ComingSoonScreen
import com.productivityservicehub.wavexpay.domain.data.UserPreferences
import com.productivityservicehub.wavexpay.presentation.SplashScreen
import com.productivityservicehub.wavexpay.presentation.login.LoginScreen
import com.productivityservicehub.wavexpay.presentation.home.HomeScreen
import com.productivityservicehub.wavexpay.presentation.home.topbar.NotificationsScreen
import com.productivityservicehub.wavexpay.presentation.home.topbar.SearchScreen
import com.productivityservicehub.wavexpay.presentation.home.qr.QRScannerScreen
import com.productivityservicehub.wavexpay.presentation.login.OtpVerificationScreen
import com.productivityservicehub.wavexpay.presentation.wallet.WalletApp
import com.productivityservicehub.wavexpay.presentation.wallet.transactions.Transaction
import com.productivityservicehub.wavexpay.presentation.wallet.transactions.getAllTransactions
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WaveXApp(onRequestCameraPermission: () -> Unit) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController, userPreferences)
        }

        composable("login") {
            LoginScreen(
                onContinue = { phoneNumber ->
                    navController.navigate("otp/$phoneNumber")
                }
            )
        }

        composable(
            "otp/{phone}",
            arguments = listOf(navArgument("phone") { defaultValue = "" })
        ) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val context = LocalContext.current
            val userPreferences = remember { UserPreferences(context) }
            val scope = rememberCoroutineScope() // ✅ Create here (in composable scope)

            OtpVerificationScreen(
                phoneNumber = phone,
                onVerifyOtp = { otp ->
                    // ✅ Use the scope here
                    scope.launch {
                        userPreferences.setLoggedIn(true)
                    }
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }


        composable("home") {
            HomeScreen(
                onNavigateToScanner = { navController.navigate("scanner") },
                onNavigateToPayToContact = { navController.navigate("pay_contact") },
                onNavigateToBank = { navController.navigate("bank_transfer") },
                onNavigateToSelfAccount = { navController.navigate("self_account") },
                onNavigateToCheckBalance = { navController.navigate("check_balance") },
                onQrBtnClick = { navController.navigate("scanner") },
            )
        }
        composable("scanner") {
            QRScannerScreen(
                onBackPressed = { navController.popBackStack() },
                onRequestPermission = onRequestCameraPermission
            )
        }
        composable("search") {
            SearchScreen(
                onBackPressed = { navController.popBackStack() }
            )
        }
        composable("wallet") {
            WalletApp()
        }
        composable("notifications") {
            NotificationsScreen(
                onBackPressed = { navController.popBackStack() }
            )
        }
        composable("pay_contact") {
            ComingSoonScreen(
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
        composable("bank_transfer") {
            ComingSoonScreen(
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
        composable("self_account") {
            ComingSoonScreen(
                onBackPressed = {
                    navController.popBackStack()
                }
            )
        }
        composable("check_balance") {
            WalletApp()
        }
//        composable("history") {
//            TransactionHistoryScreen(
//                onTransactionClick = { transaction ->
//                    navController.navigate("transactionDetails/${transaction.id}")
//                },
//                onBackPressed = { navController.popBackStack() }
//            )
//        }

//        composable(
//            "transactionDetails/{transactionId}",
//            arguments = listOf(navArgument("transactionId") { type = NavType.StringType })
//        ) { backStackEntry ->
//            val transactionId = backStackEntry.arguments?.getString("transactionId")
//            // Assuming you have a way to get the transaction object from the ID
//            val transaction = getTransactionFromId(transactionId)
//            TransactionDetailScreen(
//                transaction = transaction,
//                onBackPressed = { navController.popBackStack() }
//
//            )
//        }

    }
}


fun getTransactionFromId(id : String?): Transaction? {
    return getAllTransactions().find { it.id==id }
}

