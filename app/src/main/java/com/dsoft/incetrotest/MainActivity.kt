package com.dsoft.incetrotest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dsoft.incetrotest.ui.fragments.details.OrganizationDetailsScreen
import com.dsoft.incetrotest.ui.fragments.list.OrganizationListScreen
import com.dsoft.incetrotest.ui.theme.IncetroTestTheme
import com.dsoft.incetrotest.ui.theme.backgroundMain
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            window.statusBarColor = backgroundMain.toArgb()
            window.navigationBarColor = backgroundMain.toArgb()
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val controller = WindowInsetsControllerCompat(window, window.decorView)
            controller.isAppearanceLightStatusBars = true
            controller.isAppearanceLightNavigationBars = true

            val navController = rememberNavController()
            IncetroTestTheme {
                NavHost(
                    navController = navController,
                    startDestination = "organization_list_screen"
                ) {
                    composable(
                        route = "organization_list_screen"
                    ) {
                        OrganizationListScreen(navController = navController)
                    }
                    composable(
                        route = "organization_details_screen/{organization_id}",
                        arguments = listOf(
                            navArgument("organization_id") {
                                type = NavType.IntType
                            }
                        )
                    ) {
                        OrganizationDetailsScreen(navController = navController)
                    }
                }
            }

        }
    }
}