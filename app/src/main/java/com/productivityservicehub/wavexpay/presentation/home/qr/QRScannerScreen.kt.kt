package com.productivityservicehub.wavexpay.presentation.home.qr

import android.Manifest
import android.content.Context
import android.view.ViewGroup
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.*
import com.google.mlkit.vision.barcode.BarcodeScanning
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.delay
import kotlin.math.abs


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRScanSection(onClick: () -> Unit, scrollBehavior: TopAppBarScrollBehavior) {
    var currentIconIndex by remember { mutableStateOf(0) }
    val icons = listOf(
        Icons.Default.QrCodeScanner,
        Icons.Default.QrCode,
        Icons.Default.CameraAlt,
        Icons.Default.TouchApp
    )

    // Rotate icon every 5 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentIconIndex = (currentIconIndex + 1) % icons.size
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "scan")

    // Scanning line animation
    val scanLineOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanLine"
    )

    // Pulse animation for the frame
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Glow animation
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val collapseFraction =
        abs(scrollBehavior.state.heightOffset / scrollBehavior.state.heightOffsetLimit);
    val scale = lerp(1f, 0.55f, collapseFraction);
    val alpha = lerp(1f, 0.3f, collapseFraction);

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(320.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2948B8),
                        Color(0xFF1E3A8A)
                    )
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        // Background pattern
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gridSize = 40f
            val paint = Paint().apply {
                color = Color.White.copy(alpha = 0.05f)
                strokeWidth = 1f
            }

            for (i in 0 until (size.width / gridSize).toInt()) {
                drawLine(
                    color = Color.White.copy(alpha = 0.05f),
                    start = Offset(i * gridSize, 0f),
                    end = Offset(i * gridSize, size.height),
                    strokeWidth = 1f
                )
            }
            for (i in 0 until (size.height / gridSize).toInt()) {
                drawLine(
                    color = Color.White.copy(alpha = 0.05f),
                    start = Offset(0f, i * gridSize),
                    end = Offset(size.width, i * gridSize),
                    strokeWidth = 1f
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Main scanning frame with animations
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .graphicsLayer {
                        scaleX = pulseScale
                        scaleY = pulseScale
                    },
                contentAlignment = Alignment.Center
            ) {
                // Glow effect background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                        .background(
                            Color(0xFFFF6B35).copy(alpha = glowAlpha * 0.3f),
                            RoundedCornerShape(24.dp)
                        )
                        .blur(20.dp)
                )

                // Corner brackets with enhanced design
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cornerLength = 50.dp.toPx()
                    val strokeWidth = 7.dp.toPx()
                    val color = Color(0xFFFF6B35)
                    val innerPadding = 12.dp.toPx()

                    // Top-left
                    drawLine(
                        color, Offset(innerPadding, innerPadding),
                        Offset(cornerLength + innerPadding, innerPadding), strokeWidth,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color, Offset(innerPadding, innerPadding),
                        Offset(innerPadding, cornerLength + innerPadding), strokeWidth,
                        cap = StrokeCap.Round
                    )

                    // Top-right
                    drawLine(
                        color, Offset(size.width - innerPadding, innerPadding),
                        Offset(size.width - cornerLength - innerPadding, innerPadding),
                        strokeWidth, cap = StrokeCap.Round
                    )
                    drawLine(
                        color, Offset(size.width - innerPadding, innerPadding),
                        Offset(size.width - innerPadding, cornerLength + innerPadding),
                        strokeWidth, cap = StrokeCap.Round
                    )

                    // Bottom-left
                    drawLine(
                        color, Offset(innerPadding, size.height - innerPadding),
                        Offset(cornerLength + innerPadding, size.height - innerPadding),
                        strokeWidth, cap = StrokeCap.Round
                    )
                    drawLine(
                        color, Offset(innerPadding, size.height - innerPadding),
                        Offset(innerPadding, size.height - cornerLength - innerPadding),
                        strokeWidth, cap = StrokeCap.Round
                    )

                    // Bottom-right
                    drawLine(
                        color, Offset(size.width - innerPadding, size.height - innerPadding),
                        Offset(
                            size.width - cornerLength - innerPadding,
                            size.height - innerPadding
                        ),
                        strokeWidth, cap = StrokeCap.Round
                    )
                    drawLine(
                        color, Offset(size.width - innerPadding, size.height - innerPadding),
                        Offset(
                            size.width - innerPadding,
                            size.height - cornerLength - innerPadding
                        ),
                        strokeWidth, cap = StrokeCap.Round
                    )

                    // Animated scanning line
                    val scanY = innerPadding + (size.height - innerPadding * 2) * scanLineOffset
                    drawLine(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xFFFF6B35).copy(alpha = 0.8f),
                                Color.Transparent
                            )
                        ),
                        start = Offset(innerPadding, scanY),
                        end = Offset(size.width - innerPadding, scanY),
                        strokeWidth = 3.dp.toPx()
                    )
                }

                // Center content with QR icon and text overlay
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Animated QR icon in background with crossfade
                    AnimatedContent(
                        targetState = currentIconIndex,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(600)) + scaleIn(
                                initialScale = 0.8f,
                                animationSpec = tween(600)
                            ) togetherWith fadeOut(animationSpec = tween(400)) + scaleOut(
                                targetScale = 0.8f,
                                animationSpec = tween(400)
                            )
                        },
                        label = "iconCrossfade",
                        modifier = Modifier.align(Alignment.Center)
                    ) { iconIndex ->
                        Icon(
                            imageVector = icons[iconIndex],
                            contentDescription = null,
                            modifier = Modifier.size(120.dp),
                            tint = Color.White.copy(alpha = 0.4f)
                        )
                    }

                    // Overlay content - Text and hand icon
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Tap to Scan",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Hand/finger icon with pulse animation
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = "Tap to scan",
                            modifier = Modifier
                                .size(40.dp)
                                .graphicsLayer {
                                    scaleX = pulseScale
                                    scaleY = pulseScale
                                },
                            tint = Color.White
                        )
                    }
                }
            }


        }
    }
}



@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QRScannerScreen(
    onBackPressed: () -> Unit,
    onRequestPermission: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var flashEnabled by remember { mutableStateOf(false) }
    var scannedData by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan QR Code", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { flashEnabled = !flashEnabled }) {
                        Icon(
                            if (flashEnabled) Icons.Default.FlashOn else Icons.Default.FlashOff,
                            null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E3A8A)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color.Black)
        ) {
            when {
                cameraPermissionState.status.isGranted -> {
                    CameraPreview(
                        flashEnabled = flashEnabled,
                        onQRCodeScanned = { data ->
                            scannedData = data
                        }
                    )

                    // Scanner overlay
                    ScannerOverlay()

                    // Scanned data display
                    scannedData?.let { data ->
                        ScannedDataCard(
                            data = data,
                            onDismiss = { scannedData = null }
                        )
                    }
                }
                else -> {
                    PermissionDeniedContent(
                        onRequestPermission = { cameraPermissionState.launchPermissionRequest() }
                    )
                }
            }
        }
    }
}


@Composable
fun CameraPreview(
    flashEnabled: Boolean,
    onQRCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    AndroidView(
        modifier = Modifier,
        factory = { ctx: Context ->
            // ✅ Explicit type fixes inference issue
            val previewView: PreviewView = PreviewView(ctx).apply {
                this.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                            processImageProxy(imageProxy, onQRCodeScanned)
                        }
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    val camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                    camera.cameraControl.enableTorch(flashEnabled)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
private fun processImageProxy(
    imageProxy: ImageProxy,
    onQRCodeScanned: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { onQRCodeScanned(it) }
                }
            }
            .addOnFailureListener { e -> e.printStackTrace() }
            .addOnCompleteListener { imageProxy.close() }
    } else {
        imageProxy.close()
    }
}


@Composable
fun ScannerOverlay() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Canvas(
                modifier = Modifier.size(280.dp)
            ) {
                val cornerLength = 50.dp.toPx()
                val strokeWidth = 8.dp.toPx()
                val color = Color(0xFFFF6B35)

                // Top-left corner
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(cornerLength, 0f),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = color,
                    start = Offset(0f, 0f),
                    end = Offset(0f, cornerLength),
                    strokeWidth = strokeWidth
                )

                // Top-right corner
                drawLine(
                    color = color,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width - cornerLength, 0f),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = color,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, cornerLength),
                    strokeWidth = strokeWidth
                )

                // Bottom-left corner
                drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(cornerLength, size.height),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = color,
                    start = Offset(0f, size.height),
                    end = Offset(0f, size.height - cornerLength),
                    strokeWidth = strokeWidth
                )

                // Bottom-right corner
                drawLine(
                    color = color,
                    start = Offset(size.width, size.height),
                    end = Offset(size.width - cornerLength, size.height),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = color,
                    start = Offset(size.width, size.height),
                    end = Offset(size.width, size.height - cornerLength),
                    strokeWidth = strokeWidth
                )

                // Center square outline
                drawRect(
                    color = color,
                    topLeft = Offset(0f, 0f),
                    size = size,
                    style = Stroke(width = 2.dp.toPx())
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Align QR code within frame",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun BoxScope.ScannedDataCard(data: String, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text("Scanned Successfully!", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.ArrowBack, "Close")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = data,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A))
            ) {
                Text("Proceed to Payment")
            }
        }
    }
}

@Composable
fun PermissionDeniedContent(onRequestPermission: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Camera Permission Required",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Please grant camera permission to scan QR codes",
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onRequestPermission) {
                Text("Grant Permission")
            }
        }
    }
}