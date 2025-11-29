package pe.edu.upc.jameofit.home.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pe.edu.upc.jameofit.home.presentation.scaffold.HomeScaffold
import pe.edu.upc.jameofit.tracking.presentation.view.TrackingHomeScreen
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.navigation
import pe.edu.upc.jameofit.faq.presentation.view.FaqScreen
import pe.edu.upc.jameofit.goals.presentation.view.GoalsManagementRoute
import pe.edu.upc.jameofit.goals.presentation.viewmodel.GoalsViewModel
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.goals.presentation.di.PresentationModule as GoalsPresentationModule
import pe.edu.upc.jameofit.profile.presentation.di.PresentationModule as ProfilePresentationModule
import pe.edu.upc.jameofit.nutritionists.presentation.view.NutritionistsScreen
import pe.edu.upc.jameofit.mealplan.presentation.view.MealPlanScreen
import pe.edu.upc.jameofit.recipe.presentation.view.BreakfastScreen
import pe.edu.upc.jameofit.recipe.presentation.view.LunchScreen
import pe.edu.upc.jameofit.recipe.presentation.view.DinnerScreen
import pe.edu.upc.jameofit.recipe.recipedetail.presentation.view.BreakfastRecipeDetailScreen
import pe.edu.upc.jameofit.recipe.recipedetail.presentation.view.DinnerRecipeDetailScreen
import pe.edu.upc.jameofit.recipe.recipedetail.presentation.view.LunchRecipeDetailScreen
import pe.edu.upc.jameofit.tracking.presentation.viewmodel.TrackingViewModel
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import pe.edu.upc.jameofit.mealplan.presentation.di.PresentationModule.getMealPlanViewModel
import pe.edu.upc.jameofit.mealplan.presentation.view.AddRecipeToMealPlanScreen
import pe.edu.upc.jameofit.mealplan.presentation.view.MealPlanCreateScreen
import pe.edu.upc.jameofit.mealplan.presentation.view.MealPlanDetailScreen
import pe.edu.upc.jameofit.mealplan.presentation.view.RecipeDetailScreen
import pe.edu.upc.jameofit.mealplan.presentation.view.TemplateDetailScreen
import pe.edu.upc.jameofit.mealplan.presentation.view.TemplatesScreen
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel
import pe.edu.upc.jameofit.tracking.presentation.view.MealActivityScreen
import pe.edu.upc.jameofit.profile.presentation.view.ProfileScreen
import pe.edu.upc.jameofit.profile.presentation.view.EditProfileScreen

private fun titleForRoute(route: String) = when {
    route.startsWith("tracking") -> "Inicio"
    route.startsWith("tips") -> "Tips"
    route.startsWith("messages") -> "Mensajes"
    route.startsWith("nutritionists") -> "Nutricionistas"
    route == DrawerRoute.PROFILE -> "Perfil"
    route == DrawerRoute.TEMPLATES -> "Templates de Nutricionistas"  // ✅ NUEVO
    route.startsWith("drawer/template_detail") -> "Detalle del Template"  // ✅ NUEVO
    route.startsWith("drawer/recipe_detail") -> "Detalle de Receta"
    route == DrawerRoute.EDIT_PREFERENCES -> "Editar Preferencias"
    route == DrawerRoute.GOALS -> "Gestionar objetivos"
    route == DrawerRoute.PROGRESS -> "Analíticas y estadísticas"
    route == DrawerRoute.MEAL_PLANS -> "Planes de alimentación"
    route == RecipeRoute.BREAKFAST -> "Desayuno"
    route == RecipeRoute.LUNCH -> "Almuerzo"
    route == RecipeRoute.DINNER -> "Cena"
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNavHost(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    onRequestLogout: () -> Unit = {}
) {
    val homeNavController = rememberNavController()

    val backStackEntry = homeNavController.currentBackStackEntryAsState().value
    val currentRoute = backStackEntry?.destination?.route ?: HomeRoute.TRACKING
    val currentTab = toTabOf(currentRoute)
    val topTitle = titleForRoute(currentRoute)

    val currentDestinationKey = currentTab ?: when (currentRoute) {
        DrawerRoute.PROFILE,
        DrawerRoute.EDIT_PREFERENCES,
        DrawerRoute.GOALS,
        DrawerRoute.PROGRESS,
        DrawerRoute.MEAL_PLANS,
        DrawerRoute.SUBSCRIPTIONS,
        DrawerRoute.FAQ,
        DrawerRoute.SETTINGS,
        DrawerRoute.RECOMMENDATIONS,
        RecipeRoute.BREAKFAST,
        RecipeRoute.LUNCH,
        RecipeRoute.DINNER -> currentRoute
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
                DrawerRoute.EDIT_PREFERENCES,
                DrawerRoute.GOALS,
                DrawerRoute.PROGRESS,
                DrawerRoute.MEAL_PLANS,
                DrawerRoute.SUBSCRIPTIONS,
                DrawerRoute.FAQ,
                DrawerRoute.SETTINGS,
                RecipeRoute.BREAKFAST,
                RecipeRoute.LUNCH,
                RecipeRoute.DINNER -> true
                else -> false
            }
            if (isDrawer) {
                homeNavController.popBackStack(
                    route = HomeGraph.ROUTE,
                    inclusive = false
                )
            }

            homeNavController.navigate(target) {
                popUpTo(HomeGraph.ROUTE) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        },
        onNavigateDrawer = { drawerRoute ->
            if (currentRoute != drawerRoute) {
                homeNavController.navigate(drawerRoute) { launchSingleTop = true }
            }
        },
        onRequestLogout = onRequestLogout,
        topTitle = topTitle
    ) { paddingModifier ->
        NavHost(
            navController = homeNavController,
            startDestination = TabGraph.TRACKING,
            route = HomeGraph.ROUTE,
            modifier = paddingModifier
        ) {
            navigation(
                startDestination = HomeRoute.TRACKING,
                route = TabGraph.TRACKING
            ) {
                composable(HomeRoute.TRACKING) {
                    val trackingVm: TrackingViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return pe.edu.upc.jameofit.tracking.presentation.di.PresentationModule.getTrackingViewModel() as T
                            }
                        }
                    )

                    val authUser by authViewModel.user.collectAsState()
                    val userId = authUser.id

                    TrackingHomeScreen(
                        viewModel = trackingVm,
                        authViewModel = authViewModel,
                        userId = userId,
                        onOpenRecentActivity = { homeNavController.navigate(TrackingRoute.MEAL_ACTIVITY) },
                        onOpenWeeklyProgress = { homeNavController.navigate(TrackingRoute.WEEKLY_PROGRESS) },
                        onOpenTips = { homeNavController.navigate(TabGraph.TIPS) }
                    )
                }
                composable(TrackingRoute.MEAL_ACTIVITY) {
                    val trackingVm: TrackingViewModel = viewModel(
                        factory = object : ViewModelProvider.Factory {
                            @Suppress("UNCHECKED_CAST")
                            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                                return pe.edu.upc.jameofit.tracking.presentation.di.PresentationModule.getTrackingViewModel() as T
                            }
                        }
                    )

                    val authUser by authViewModel.user.collectAsState()
                    val userId = authUser.id

                    MealActivityScreen(
                        viewModel = trackingVm,
                        userId = userId,
                        onBack = { homeNavController.popBackStack() }
                    )
                }
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
                        onBack = { homeNavController.popBackStack() }
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

            // ✅ Pantalla de Perfil
            composable(DrawerRoute.PROFILE) {
                val profileVm: pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return ProfilePresentationModule.getProfileViewModel() as T
                        }
                    }
                )

                ProfileScreen(
                    profileViewModel = profileVm,
                    authViewModel = authViewModel,
                    onEditPreferences = {
                        homeNavController.navigate(DrawerRoute.EDIT_PREFERENCES)
                    },
                    onViewMealPlans = {
                        homeNavController.navigate(DrawerRoute.MEAL_PLANS)
                    },
                    onViewTips = {
                        homeNavController.navigate(TabGraph.TIPS)
                    },
                    onViewDailySummary = {
                        homeNavController.navigate(HomeRoute.TRACKING)
                    },
                    onLogout = onRequestLogout
                )
            }

            // ✅ Pantalla de Editar Preferencias
            composable(DrawerRoute.EDIT_PREFERENCES) {
                val profileVm: pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return ProfilePresentationModule.getProfileViewModel() as T
                        }
                    }
                )

                val authUser by authViewModel.user.collectAsState()

                EditProfileScreen(
                    profileId = authUser.id,
                    viewModel = profileVm,
                    onSaved = {
                        homeNavController.popBackStack()
                    },
                    onBack = {
                        homeNavController.popBackStack()
                    }
                )
            }

            composable(DrawerRoute.GOALS) {
                val goalsVm: GoalsViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return GoalsPresentationModule.getGoalsViewModel() as T
                        }
                    }
                )

                GoalsManagementRoute(
                    authViewModel = authViewModel,
                    goalsViewModel = goalsVm,
                    onBack = { homeNavController.popBackStack() }
                )
            }

            composable(DrawerRoute.RECOMMENDATIONS) {
                val recVm = remember {
                    pe.edu.upc.jameofit.recommendations.presentation.di.PresentationModule.getRecommendationsViewModel()
                }

                pe.edu.upc.jameofit.recommendations.presentation.view.RecommendationsRoute(
                    recommendationsViewModel = recVm,
                    onBack = { homeNavController.popBackStack() }
                )
            }

            composable(DrawerRoute.PROGRESS) { PlaceholderScreen("Analíticas y estadísticas") }

            composable(DrawerRoute.MEAL_PLANS) {
                val mealPlanVm: MealPlanViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return getMealPlanViewModel() as T
                        }
                    }
                )

                val profileVm: pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return ProfilePresentationModule.getProfileViewModel() as T
                        }
                    }
                )

                val authUser by authViewModel.user.collectAsState()

                LaunchedEffect(authUser.id) {
                    profileVm.getProfileById(authUser.id)
                }

                MealPlanScreen(
                    navController = homeNavController,
                    viewModel = mealPlanVm,
                    profileViewModel = profileVm
                )
            }

            composable(DrawerRoute.MEAL_PLAN_CREATE) {
                val mealPlanVm: MealPlanViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return getMealPlanViewModel() as T
                        }
                    }
                )

                val profileVm: pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return ProfilePresentationModule.getProfileViewModel() as T
                        }
                    }
                )

                val uiState by profileVm.uiState.collectAsState()
                val authUser by authViewModel.user.collectAsState()

                LaunchedEffect(Unit) {
                    profileVm.getProfileById(authUser.id)
                }

                when (uiState) {
                    is pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileUiState.Success -> {
                        val profile = (uiState as pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileUiState.Success).profile

                        MealPlanCreateScreen(
                            profile = profile,
                            viewModel = mealPlanVm,
                            onMealPlanCreated = { homeNavController.popBackStack() }
                        )
                    }

                    is pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                        )
                    }

                    is pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileUiState.Error -> {
                        val message = (uiState as pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileUiState.Error).message
                        Text(
                            text = "Error cargando perfil: $message",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                        )
                    }

                    else -> {
                        Text(
                            text = "Cargando perfil...",
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(Alignment.Center)
                        )
                    }
                }
            }

            composable(
                route = DrawerRoute.MEAL_PLAN_DETAIL
            ) { backStackEntry ->
                val mealPlanId = backStackEntry.arguments?.getString("mealPlanId")?.toLongOrNull() ?: 0L

                val mealPlanVm: MealPlanViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return getMealPlanViewModel() as T
                        }
                    }
                )

                MealPlanDetailScreen(
                    mealPlanId = mealPlanId,
                    viewModel = mealPlanVm,
                    authViewModel = authViewModel,
                    navController = homeNavController
                )
            }

            composable(
                route = DrawerRoute.ADD_RECIPE_TO_MEAL_PLAN
            ) { backStackEntry ->
                val mealPlanId = backStackEntry.arguments?.getString("mealPlanId")?.toLongOrNull() ?: 0L

                val mealPlanVm: MealPlanViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return getMealPlanViewModel() as T
                        }
                    }
                )

                AddRecipeToMealPlanScreen(
                    mealPlanId = mealPlanId,
                    navController = homeNavController,
                    mealPlanViewModel = mealPlanVm
                )
            }
            composable(DrawerRoute.TEMPLATES) {
                val mealPlanVm: MealPlanViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return getMealPlanViewModel() as T
                        }
                    }
                )

                TemplatesScreen(
                    navController = homeNavController,
                    viewModel = mealPlanVm
                )
            }

            composable(
                route = DrawerRoute.TEMPLATE_DETAIL
            ) { backStackEntry ->
                val templateId = backStackEntry.arguments?.getString("templateId")?.toLongOrNull() ?: 0L

                val mealPlanVm: MealPlanViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return getMealPlanViewModel() as T
                        }
                    }
                )

                TemplateDetailScreen(
                    templateId = templateId,
                    viewModel = mealPlanVm,
                    authViewModel = authViewModel,
                    navController = homeNavController
                )
            }

            composable(
                route = DrawerRoute.RECIPE_DETAIL
            ) { backStackEntry ->
                val mealPlanId = backStackEntry.arguments?.getString("mealPlanId")?.toLongOrNull() ?: 0L
                val recipeId = backStackEntry.arguments?.getString("recipeId")?.toLongOrNull() ?: 0L

                val mealPlanVm: MealPlanViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return getMealPlanViewModel() as T
                        }
                    }
                )

                RecipeDetailScreen(
                    recipeId = recipeId,
                    mealPlanId = mealPlanId,
                    viewModel = mealPlanVm,
                    onBack = { homeNavController.popBackStack() }
                )
            }

            composable(DrawerRoute.SUBSCRIPTIONS) { PlaceholderScreen("Suscripciones") }
            composable(DrawerRoute.FAQ) { FaqScreen() }
            composable(DrawerRoute.SETTINGS) { PlaceholderScreen("Ajustes") }

            composable(RecipeRoute.BREAKFAST) { BreakfastScreen(navController = homeNavController) }
            composable(RecipeRoute.LUNCH) { LunchScreen(navController = homeNavController) }
            composable(RecipeRoute.DINNER) { DinnerScreen(navController = homeNavController) }

            composable(RecipeRoute.BREAKFAST_DETAIL) { BreakfastRecipeDetailScreen() }
            composable(RecipeRoute.LUNCH_DETAIL) { LunchRecipeDetailScreen() }
            composable(RecipeRoute.DINNER_DETAIL) { DinnerRecipeDetailScreen() }

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