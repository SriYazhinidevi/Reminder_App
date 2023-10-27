package com.example.mainproject.ui.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.mainproject.R
import kotlinx.coroutines.delay

@Composable
fun AnimatedSplashScreen(navController: NavHostController, onSplashFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(1000)
        navController.popBackStack()
        navController.navigate("List_Todo")
    }
    Splash(alpha = alphaAnim.value)
}

@Composable
fun Splash(alpha: Float) {
    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFD3E0DC), Color(0xFFE1F1DD)),
        startY = 0f,
        endY = 500f
    )
    Box(
        modifier = Modifier
            .background(brush = gradient)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier
                .size(140.dp)
                .alpha(alpha = alpha),
            painter = painterResource(id = R.drawable.baseline_timer_24),
            contentDescription = "Logo Icon",
            tint = Color(0xFF1976D2)
        )
    }
}
