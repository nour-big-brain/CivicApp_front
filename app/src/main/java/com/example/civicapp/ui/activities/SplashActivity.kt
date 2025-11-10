package com.example.civicapp.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.civicapp.MainActivity
import com.example.civicapp.ui.theme.CivicAppTheme
import com.example.civicapp.ui.theme.Primary
import com.example.civicapp.ui.theme.Secondary
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CivicAppTheme {
                SplashScreen(
                    onNavigateToLogin = {
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    },
                    onNavigateToHome = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun SplashScreen(
    onNavigateToLogin: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    var shouldNavigate by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "splash_alpha"
    )

    LaunchedEffect(Unit) {
        isVisible = true
        delay(3000) // Show splash for 3 seconds
        shouldNavigate = true
    }

    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate) {
            // TODO: Check if user is logged in
            // For now, navigate to main app
            onNavigateToHome()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Primary,
                        Secondary
                    )
                )
            )
            .alpha(alpha),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo Icon
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "CivicApp Logo",
            modifier = Modifier.size(100.dp),
            tint = androidx.compose.ui.graphics.Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        // App Name
        Text(
            text = "CivicApp",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = androidx.compose.ui.graphics.Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tagline
        Text(
            text = "Engagez-vous pour votre communauté",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.9f)
        )
    }
}