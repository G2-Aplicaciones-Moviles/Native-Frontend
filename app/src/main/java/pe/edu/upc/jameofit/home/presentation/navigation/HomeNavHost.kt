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
import androidx.navigation.navigation
import pe.edu.upc.jameofit.faq.presentation.view.FaqScreen
import pe.edu.upc.jameofit.goals.presentation.view.GoalsManagementRoute
import pe.edu.upc.jameofit.goals.presentation.di.PresentationModule as GoalsPresentationModule
import pe.edu.upc.jameofit.iam.presentation.di.PresentationModule as IamPresentationModule
import pe.edu.upc.jameofit.nutritionists.presentation.view.NutritionistsScreen

/**
 * Derives a human-readable top bar title for a given destination [route].
 */
private fun titleForRoute(route: String) = when {
    route.startsWith("tracking") -> "Inicio"
    route.startsWith("tips") -> "Tips"
    route.startsWith("messages") -> "Mensajes"
    route.startsWith("nutritionists") -> "Nutricionistas"
    route == DrawerRoute.PROFILE -> "Perfil"
    route == DrawerRoute.GOALS -> "Gestionar objetivos"
    route == DrawerRoute.PROGRESS -> "Analíticas y estadísticas"
    route == DrawerRoute.MEAL_PLANS -> "Planes de alimentación"
    route == DrawerRoute.SUBSCRIPTIONS -> "Suscripciones"
    route == DrawerRoute.FAQ -> "Preguntas frecuentes"
    route == DrawerRoute.SETTINGS -> "Ajustes"
    else -> "Inicio"
}

/**
 * Maps a leaf [route] to its owning tab ([TabGraph]) based on a stable prefix.
 * Returns null if the route does not belong to any tab (e.g., drawer destinations).
 */
private fun toTabOf(route: String?): String? = when {
    route == null -> null
    route.startsWith("tracking/") || route == HomeRoute.TRACKING -> TabGraph.TRACKING
    route.startsWith("tips/") || route == HomeRoute.TIPS -> TabGraph.TIPS
    route.startsWith("messages/") || route == HomeRoute.MESSAGES -> TabGraph.MESSAGES
    route.startsWith("nutritionists/") || route == HomeRoute.NUTRITIONISTS -> TabGraph.NUTRITIONISTS
    else -> null
}

/**
 * Internal NavHost for the authenticated area. It:
 * - Sets up a subgraph per bottom tab to keep independent back stacks.
 * - Hosts drawer destinations outside of tab graphs to avoid stack contamination.
 * - Wires [HomeScaffold] with selection state and navigation callbacks.
 *
 * The BottomBar navigation always targets the *root leaf* of a tab (see [HomeRoute]),
 * and performs a `popUpTo(HomeGraph.ROUTE)` with `saveState`/`restoreState` to preserve tab stacks.
 */
@Composable
fun HomeNavHost(
    navController: NavHostController,
    onRequestLogout: () -> Unit = {}
) {
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = backStackEntry?.destination?.route ?: HomeRoute.TRACKING
    val currentTab = toTabOf(currentRoute)
    val topTitle = titleForRoute(currentRoute)

    val currentDestinationKey = currentTab ?: when (currentRoute) {
        DrawerRoute.PROFILE,
        DrawerRoute.GOALS,
        DrawerRoute.PROGRESS,
        DrawerRoute.MEAL_PLANS,
        DrawerRoute.SUBSCRIPTIONS,
        DrawerRoute.FAQ,
        DrawerRoute.SETTINGS -> currentRoute

        else -> TabGraph.TRACKING
    }

    HomeScaffold(
        currentDestination = currentDestinationKey,
        onNavigateBottom = { tabRoute ->
            val target = when (tabRoute) {
                TabGraph.TRACKING -> HomeRoute.TRACKING
                TabGraph.TIPS -> HomeRoute.TIPS
                TabGraph.MESSAGES -> HomeRoute.MESSAGES
                TabGraph.NUTRITIONISTS -> HomeRoute.NUTRITIONISTS
                else -> HomeRoute.TRACKING
            }

            val isDrawer = when (currentRoute) {
                DrawerRoute.PROFILE,
                DrawerRoute.GOALS,
                DrawerRoute.PROGRESS,
                DrawerRoute.MEAL_PLANS,
                DrawerRoute.SUBSCRIPTIONS,
                DrawerRoute.FAQ,
                DrawerRoute.SETTINGS -> true

                else -> false
            }
            if (isDrawer) {
                navController.popBackStack(
                    route = HomeGraph.ROUTE,
                    inclusive = false
                )
            }

            navController.navigate(target) {
                popUpTo(HomeGraph.ROUTE) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        onNavigateDrawer = { drawerRoute ->
            if (currentRoute != drawerRoute) {
                navController.navigate(drawerRoute) { launchSingleTop = true }
            }
        },
        onRequestLogout = onRequestLogout,
        topTitle = topTitle
    ) { paddingModifier ->
        NavHost(
            navController = navController,
            startDestination = TabGraph.TRACKING,
            route = HomeGraph.ROUTE,
            modifier = paddingModifier
        ) {
            navigation(
                startDestination = HomeRoute.TRACKING,
                route = TabGraph.TRACKING
            ) {
                composable(HomeRoute.TRACKING) {
                    TrackingHomeScreen(
                        onOpenRecentActivity = { navController.navigate(TrackingRoute.MEAL_ACTIVITY) },
                        onOpenWeeklyProgress = { navController.navigate(TrackingRoute.WEEKLY_PROGRESS) },
                        onOpenTips = { navController.navigate(TabGraph.TIPS) }
                    )
                }
                composable(TrackingRoute.MEAL_ACTIVITY) { PlaceholderScreen("Actividad reciente") }
                composable(TrackingRoute.WEEKLY_PROGRESS) { PlaceholderScreen("Progreso semanal") }
                composable(TrackingRoute.TIP_DETAIL) { PlaceholderScreen("Detalle del tip") }
            }

            navigation(
                startDestination = HomeRoute.TIPS,
                route = TabGraph.TIPS
            ) {
                composable(HomeRoute.TIPS) { PlaceholderScreen("Tips saludables") }
            }

            navigation(
                startDestination = HomeRoute.MESSAGES,
                route = TabGraph.MESSAGES
            ) {
                composable(HomeRoute.MESSAGES) { PlaceholderScreen("Mensajes") }
            }

            navigation(
                startDestination = HomeRoute.NUTRITIONISTS,
                route = TabGraph.NUTRITIONISTS
            ) {
                composable(HomeRoute.NUTRITIONISTS) { NutritionistsScreen() }
            }

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
            composable(DrawerRoute.PROGRESS) { PlaceholderScreen("Analíticas y estadísticas") }
            composable(DrawerRoute.MEAL_PLANS) { PlaceholderScreen("Planes de alimentación") }
            composable(DrawerRoute.SUBSCRIPTIONS) { PlaceholderScreen("Suscripciones") }
            composable(DrawerRoute.FAQ) { FaqScreen() }
            composable(DrawerRoute.SETTINGS) { PlaceholderScreen("Ajustes") }
        }
    }
}

/**
 * Minimal placeholder screen used for stubbing destinations during development.
 *
 * @param title Text displayed in the body.
 */
@Composable
private fun PlaceholderScreen(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(24.dp)
    )
}
