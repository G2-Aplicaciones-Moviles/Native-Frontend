package pe.edu.upc.jameofit.home.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import pe.edu.upc.jameofit.home.presentation.scaffold.HomeScaffold
import pe.edu.upc.jameofit.tracking.presentation.view.TrackingHomeScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp

@Composable
fun HomeNavHost(
    navController: NavHostController
) {
    val currentRoute = navController.currentBackStackEntryAsState().value
        ?.destination?.route ?: HomeRoute.TRACKING

    HomeScaffold(
        currentDestination = when (currentRoute) {
            HomeRoute.TRACKING -> "bottom_tracking"
            HomeRoute.TIPS -> "bottom_tips"
            HomeRoute.MESSAGES -> "bottom_messages"
            HomeRoute.PROFILE -> "bottom_profile"
            HomeRoute.NUTRITIONISTS -> "bottom_nutritionists"
            else -> "bottom_tracking"
        },
        onNavigateBottom = { key ->
            val dest = when (key) {
                "bottom_tracking" -> HomeRoute.TRACKING
                "bottom_tips" -> HomeRoute.TIPS
                "bottom_messages" -> HomeRoute.MESSAGES
                "bottom_profile" -> HomeRoute.PROFILE
                "bottom_nutritionists" -> HomeRoute.NUTRITIONISTS
                else -> HomeRoute.TRACKING
            }
            navController.navigate(dest) {
                launchSingleTop = true
                restoreState = true
                popUpTo(HomeRoute.TRACKING) { saveState = true }
            }
        },
        onNavigateDrawer = { /* TODO: mapear cuando existan destinos del drawer */ },
        topTitle = "Inicio"
    ) { paddingModifier ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute.TRACKING,
            modifier = paddingModifier
        ) {
            composable(HomeRoute.TRACKING) {
                TrackingHomeScreen(
                    onOpenRecentActivity = {
                        navController.navigate(TrackingRoute.MEAL_ACTIVITY)
                    },
                    onOpenWeeklyProgress = {
                        navController.navigate(TrackingRoute.WEEKLY_PROGRESS)
                    },
                    onOpenTips = {
                        navController.navigate(HomeRoute.TIPS)
                    }
                )
            }
            composable(HomeRoute.TIPS) { PlaceholderScreen("Tips saludables") }
            composable(HomeRoute.MESSAGES) { PlaceholderScreen("Mensajes") }
            composable(HomeRoute.PROFILE) { PlaceholderScreen("Perfil") }
            composable(HomeRoute.NUTRITIONISTS) { PlaceholderScreen("Nutricionistas") }

            composable(TrackingRoute.MEAL_ACTIVITY) {
                PlaceholderScreen("Actividad reciente")
            }
            composable(TrackingRoute.WEEKLY_PROGRESS) {
                PlaceholderScreen("Progreso semanal")
            }
            composable(TrackingRoute.TIP_DETAIL) {
                PlaceholderScreen("Detalle del tip")
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(24.dp)
    )
}
