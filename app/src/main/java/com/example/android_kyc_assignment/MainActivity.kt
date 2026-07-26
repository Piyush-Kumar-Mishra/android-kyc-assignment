package com.example.android_kyc_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.android_kyc_assignment.ui.navigation.AppNavigation
import com.example.android_kyc_assignment.ui.theme.AndroidkycassignmentTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidkycassignmentTheme {
                val navController = rememberNavController()
                AppNavigation(navController = navController)
            }
        }
    }
}

