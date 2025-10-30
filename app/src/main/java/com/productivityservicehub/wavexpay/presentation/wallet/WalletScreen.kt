package com.productivityservicehub.wavexpay.presentation.wallet


import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.productivityservicehub.wavexpay.presentation.wallet.accounts.SelectBankScreen
import com.productivityservicehub.wavexpay.presentation.wallet.balance.BalanceDisplayScreen
import com.productivityservicehub.wavexpay.presentation.wallet.mpin.EnterPinScreen
import com.productivityservicehub.wavexpay.presentation.wallet.transactions.Transaction
import com.productivityservicehub.wavexpay.presentation.wallet.transactions.TransactionDetailScreen
import com.productivityservicehub.wavexpay.presentation.wallet.transactions.TransactionFilter
import com.productivityservicehub.wavexpay.presentation.wallet.transactions.TransactionHistoryScreen
import kotlinx.coroutines.launch

data class BankAccount(
    val id: String,
    val bankName: String,
    val accountNumber: String,
    val balance: Double,
    val icon: ImageVector,
    val color: Color
)



// Theme Colors
object WalletTheme {
    val Primary = Color(0xFF5F259F)
    val PrimaryLight = Color(0xFF8247B5)
    val Accent = Color(0xFF00D09C)
    val Background = Color(0xFFF5F5F5)
    val CardBackground = Color.White
    val TextPrimary = Color(0xFF1F2937)
    val TextSecondary = Color(0xFF6B7280)
    val Success = Color(0xFF00D09C)
    val Error = Color(0xFFEF4444)
    val Warning = Color(0xFFFF9800)
    val HDFCColor = Color(0xFF004C8F)
    val SBIColor = Color(0xFF22409A)
}

// Navigation State
sealed class Screen {
//    object Home : Screen()
    object SelectBank : Screen()
    data class EnterPin(val account: BankAccount) : Screen()
    data class BalanceDisplay(val account: BankAccount, val balance: Double) : Screen()
    data class TransactionHistory(val filter: TransactionFilter = TransactionFilter.ALL) : Screen()
    data class TransactionDetail(val transaction: Transaction) : Screen()
}

@Composable
fun WalletApp(startScreen: Screen = Screen.SelectBank) {
    var currentScreen by remember { mutableStateOf<Screen>(startScreen) }
    val navStack = remember { mutableStateListOf<Screen>(startScreen) }

    fun navigateTo(screen: Screen) {
        navStack.add(screen)
        currentScreen = screen
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun navigateBack() {
        if (navStack.size > 1) {
            navStack.removeLast()
            currentScreen = navStack.last()
        }
    }
    BackHandler(enabled = navStack.size > 1) {
        navigateBack()
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = WalletTheme.Primary,
            background = WalletTheme.Background
        )
    ) {
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            }
        ) { screen ->
            when (screen) {
                is Screen.SelectBank -> SelectBankScreen(
                    onBankSelected = { account -> navigateTo(Screen.EnterPin(account)) },
                    onBackPressed = { navigateBack() },
                    onHistory = { navigateTo(Screen.TransactionHistory()) }
                )
                is Screen.EnterPin -> EnterPinScreen(
                    account = screen.account,
                    onPinVerified = { balance -> navigateTo(Screen.BalanceDisplay(screen.account, balance)) },
                    onBackPressed = { navigateBack() }
                )
                is Screen.BalanceDisplay -> BalanceDisplayScreen(
                    account = screen.account,
                    balance = screen.balance,
                    onClose = {
                        // Navigate back to home
                        navStack.clear()
                        navStack.add(Screen.SelectBank)
                        currentScreen = Screen.SelectBank
                    }
                )
                is Screen.TransactionHistory -> TransactionHistoryScreen(
                    initialFilter = screen.filter,
                    onTransactionClick = { transaction -> navigateTo(Screen.TransactionDetail(transaction)) },
                    onBackPressed = { navigateBack() }
                )
                is Screen.TransactionDetail -> TransactionDetailScreen(
                    transaction = screen.transaction,
                    onBackPressed = { navigateBack() }
                )
            }
        }
    }
}

