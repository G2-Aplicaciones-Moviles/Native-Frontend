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
import pe.edu.upc.jameofit.goals.presentation.view.GoalsManagementRoute
import pe.edu.upc.jameofit.goals.presentation.di.PresentationModule as GoalsPresentationModule
import pe.edu.upc.jameofit.iam.presentation.di.PresentationModule as IamPresentationModule

@Composable
fun HomeNavHost(
    navController: NavHostController,
    onRequestLogout: () -> Unit = {}
) {
    val currentRoute = navController.currentBackStackEntryAsState().value
        ?.destination?.route ?: HomeRoute.TRACKING

    HomeScaffold(
        currentDestination = when (currentRoute) {
            HomeRoute.TRACKING -> "bottom_tracking"
            HomeRoute.TIPS -> "bottom_tips"
            HomeRoute.MESSAGES -> "bottom_messages"
            HomeRoute.NUTRITIONISTS -> "bottom_nutritionists"
            DrawerRoute.PROFILE -> DrawerRoute.PROFILE
            DrawerRoute.GOALS -> DrawerRoute.GOALS
            DrawerRoute.SETTINGS -> DrawerRoute.SETTINGS
            else -> "bottom_tracking"
        },
        onNavigateBottom = { key ->
            val dest = when (key) {
                "bottom_tracking" -> HomeRoute.TRACKING
                "bottom_tips" -> HomeRoute.TIPS
                "bottom_messages" -> HomeRoute.MESSAGES
                "bottom_nutritionists" -> HomeRoute.NUTRITIONISTS
                else -> HomeRoute.TRACKING
            }
            navController.navigate(dest) {
                launchSingleTop = true
                restoreState = true
                popUpTo(HomeRoute.TRACKING) { saveState = true }
            }
        },
        onNavigateDrawer = { key ->
            val dest = when (key) {
                DrawerRoute.PROFILE -> DrawerRoute.PROFILE
                DrawerRoute.GOALS -> DrawerRoute.GOALS
                DrawerRoute.SETTINGS -> DrawerRoute.SETTINGS
                else -> DrawerRoute.PROFILE
            }
            navController.navigate(dest) {
                launchSingleTop = true
            }
        },
        onRequestLogout = onRequestLogout,
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
            composable(HomeRoute.NUTRITIONISTS) { PlaceholderScreen("Nutricionistas") }
            composable(DrawerRoute.PROFILE) { PlaceholderScreen("Perfil") }
            composable(DrawerRoute.GOALS) {

                val authVm = IamPresentationModule.getAuthViewModel()
                val goalsVm = GoalsPresentationModule.getGoalsViewModel()
                GoalsManagementRoute(
                    authViewModel = authVm,
                    goalsViewModel = goalsVm,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(DrawerRoute.SETTINGS) { PlaceholderScreen("Ajustes") }
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
