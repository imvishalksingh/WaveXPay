package com.productivityservicehub.wavexpay.domain.data

import androidx.compose.ui.graphics.Color

data class Service(
    val id: String,
    val name: String,
    val icon: String,
    val category: ServiceCategory
)

enum class ServiceCategory {
    MONEY_TRANSFER,
    POPULAR,
    UTILITY
}

data class Bill(
    val id: String,
    val name: String,
    val amount: Double,
    val dueDate: String,
    val isPaid: Boolean = false
)

object AppColors {
    val PrimaryBlue = Color(0xFF1E3A8A)
    val SecondaryOrange = Color(0xFFFF6B35)
    val LightBlue = Color(0xFF4A90E2)
    val BackgroundBlue = Color(0xFF0A1A3A)
    val SurfaceBlue = Color(0xFF2948B8)
    val CardBackground = Color(0xFFF5F5F5)
}