package com.example.android_kyc_assignment.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.android_kyc_assignment.ui.screens.AccountDetailsScreen
import com.example.android_kyc_assignment.ui.screens.AccountsScreen
import com.example.android_kyc_assignment.ui.screens.CameraScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "accounts"
    ) {
        composable("accounts") {
            AccountsScreen(
                onCustomerClick = { customerId ->
                    navController.navigate("details/$customerId")
                }
            )
        }

        composable(
            route = "details/{customerId}",
            arguments = listOf(navArgument("customerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customerId") ?: return@composable
            AccountDetailsScreen(
                customerId = customerId,
                onBackClick = { navController.popBackStack() },
                onCaptureClick = { id ->
                    navController.navigate("camera/$id")
                }
            )
        }

        composable(
            route = "camera/{customerId}",
            arguments = listOf(navArgument("customerId") { type = NavType.IntType })
        ) { backStackEntry ->
            val customerId = backStackEntry.arguments?.getInt("customerId") ?: return@composable
            CameraScreen(
                customerId = customerId,
                onPhotoCaptured = {
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
