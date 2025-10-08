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
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.navigation.navigation
import pe.edu.upc.jameofit.faq.presentation.view.FaqScreen
import pe.edu.upc.jameofit.goals.presentation.view.GoalsManagementRoute
import pe.edu.upc.jameofit.goals.presentation.di.PresentationModule as GoalsPresentationModule
import pe.edu.upc.jameofit.iam.presentation.di.PresentationModule as IamPresentationModule
import pe.edu.upc.jameofit.nutritionists.presentation.view.NutritionistsScreen
import pe.edu.upc.jameofit.mealplan.presentation.view.MealPlanScreen
import pe.edu.upc.jameofit.recipedetail.presentation.view.BreakfastRecipeDetailScreen
import pe.edu.upc.jameofit.recommendations.presentation.di.PresentationModule as RecommendationsPresentationModule
import pe.edu.upc.jameofit.recommendations.presentation.view.RecommendationsRoute


private fun titleForRoute(route: String) = when {
    route.startsWith("tracking") -> "Inicio"
    route.startsWith("tips") -> "Tips"
    route.startsWith("messages") -> "Mensajes"
    route.startsWith("nutritionists") -> "Nutricionistas"
    route == DrawerRoute.PROFILE -> "Perfil"
    route == DrawerRoute.GOALS -> "Gestionar objetivos"
    route == DrawerRoute.PROGRESS -> "Analíticas y estadísticas"
    route == DrawerRoute.MEAL_PLANS -> "Planes de alimentación"
    route == RecipeRoute.BREAKFAST -> "Detalle de desayuno"
    route == DrawerRoute.SUBSCRIPTIONS -> "Suscripciones"
    route == DrawerRoute.FAQ -> "Preguntas frecuentes"
    route == DrawerRoute.SETTINGS -> "Ajustes"
    else -> "Inicio"
}

private fun toTabOf(route: String?): String? = when {
    route == null -> null
    route.startsWith("tracking/") || route == HomeRoute.TRACKING -> TabGraph.TRACKING
    route.startsWith("tips/") || route == HomeRoute.TIPS -> TabGraph.TIPS
    route.startsWith("messages/") || route == HomeRoute.MESSAGES -> TabGraph.MESSAGES
    route.startsWith("nutritionists/") || route == HomeRoute.NUTRITIONISTS -> TabGraph.NUTRITIONISTS
    else -> null
}

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
        DrawerRoute.SETTINGS,
        DrawerRoute.RECOMMENDATIONS -> TabGraph.TIPS
        RecipeRoute.BREAKFAST -> currentRoute

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
                DrawerRoute.SETTINGS,
                RecipeRoute.BREAKFAST -> true
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
                composable(HomeRoute.TIPS) {
                    val recVm = remember {
                        pe.edu.upc.jameofit.recommendations.presentation.di.PresentationModule.getRecommendationsViewModel()
                    }

                    pe.edu.upc.jameofit.recommendations.presentation.view.RecommendationsRoute(
                        recommendationsViewModel = recVm,
                        onBack = { navController.popBackStack() }
                    )
                }
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

            // Drawer destinations
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
            composable(DrawerRoute.RECOMMENDATIONS) {
                // Recordamos el ViewModel para que no se reinicie al recomponerse
                val recVm = remember {
                    pe.edu.upc.jameofit.recommendations.presentation.di.PresentationModule.getRecommendationsViewModel()
                }

                pe.edu.upc.jameofit.recommendations.presentation.view.RecommendationsRoute(
                    recommendationsViewModel = recVm,
                    onBack = { navController.popBackStack() }
                )
            }



            composable(DrawerRoute.PROGRESS) { PlaceholderScreen("Analíticas y estadísticas") }

            // Aquí pasamos el navController a MealPlanScreen
            composable(DrawerRoute.MEAL_PLANS) { MealPlanScreen(navController = navController) }

            composable(DrawerRoute.SUBSCRIPTIONS) { PlaceholderScreen("Suscripciones") }
            composable(DrawerRoute.FAQ) { FaqScreen() }
            composable(DrawerRoute.SETTINGS) { PlaceholderScreen("Ajustes") }

            // RUTAS DE RECETAS (registradas con los mismos strings que usas en MealPlanScreen)
            composable(RecipeRoute.BREAKFAST) { BreakfastRecipeDetailScreen() }
            composable(RecipeRoute.LUNCH) { PlaceholderScreen("Almuerzo") }
            composable(RecipeRoute.DINNER) { PlaceholderScreen("Cena") }
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
