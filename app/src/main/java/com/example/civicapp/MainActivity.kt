package com.example.civicapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.civicapp.ui.navigation.AppNavHost
import com.example.civicapp.ui.theme.CivicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CivicAppTheme {
                AppNavHost()
            }
        }
    }
}