package com.productivityservicehub.wavexpay.presentation.home.qr

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.productivityservicehub.wavexpay.domain.data.PaymentRepository
import com.productivityservicehub.wavexpay.domain.data.PaymentRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class QRScannerUiState(
    val scannedData: String? = null,
    val isProcessing: Boolean = false,
    val error: String? = null,
    val flashEnabled: Boolean = false
)

class QRScannerViewModel(
    private val repository: PaymentRepository = PaymentRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(QRScannerUiState())
    val uiState: StateFlow<QRScannerUiState> = _uiState.asStateFlow()

    fun onQRCodeScanned(data: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isProcessing = true)
            repository.processScanData(data).fold(
                onSuccess = { processedData ->
                    _uiState.value = _uiState.value.copy(
                        scannedData = processedData,
                        isProcessing = false,
                        error = null
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isProcessing = false,
                        error = exception.message
                    )
                }
            )
        }
    }

    fun toggleFlash() {
        _uiState.value = _uiState.value.copy(
            flashEnabled = !_uiState.value.flashEnabled
        )
    }

    fun clearScannedData() {
        _uiState.value = _uiState.value.copy(scannedData = null)
    }
}