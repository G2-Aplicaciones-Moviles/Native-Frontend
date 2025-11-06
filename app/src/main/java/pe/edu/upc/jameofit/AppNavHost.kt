package pe.edu.upc.jameofit

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import pe.edu.upc.jameofit.home.presentation.navigation.HomeNavHost
import pe.edu.upc.jameofit.iam.presentation.view.ForgotPassword
import pe.edu.upc.jameofit.iam.presentation.view.Login
import pe.edu.upc.jameofit.iam.presentation.view.Register
import pe.edu.upc.jameofit.iam.presentation.view.Welcome
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.navigation.AuthRoute
import pe.edu.upc.jameofit.navigation.Graph
import pe.edu.upc.jameofit.profile.presentation.view.HealthSetupRoute
import pe.edu.upc.jameofit.profile.presentation.view.ProfileSetup
import pe.edu.upc.jameofit.profile.presentation.view.SetupDone
import pe.edu.upc.jameofit.shared.data.local.JwtStorage
import pe.edu.upc.jameofit.iam.presentation.di.PresentationModule as IamPM
import pe.edu.upc.jameofit.profile.presentation.di.PresentationModule as ProfilePM

private const val PREF_AUTH = "pref_auth"
private const val KEY_WELCOME_SEEN = "welcome_seen"

private fun markWelcomeSeen(context: Context) {
    context.getSharedPreferences(PREF_AUTH, Context.MODE_PRIVATE)
        .edit { putBoolean(KEY_WELCOME_SEEN, true) }
}

private fun decideNextRoute(context: Context): String {
    val token = JwtStorage.getToken()
    if (token.isNullOrBlank()) return Graph.Auth.route

    val profileCompleted = context.getSharedPreferences("pref_profile", Context.MODE_PRIVATE)
        .getBoolean("profile_completed", false)

    val healthCompleted = context.getSharedPreferences("pref_health_profile", Context.MODE_PRIVATE)
        .getBoolean("health_profile_completed", false)

    return if (profileCompleted && healthCompleted) Graph.Main.route else Graph.Onboarding.route
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    // ✅ CORREGIDO: Especificar el tipo explícitamente
    val authViewModel: AuthViewModel = viewModel(factory = IamPM.authViewModelFactory())

    NavHost(navController = navController, startDestination = Graph.Gate.route) {

        // Gate
        composable(Graph.Gate.route) {
            GateScreen(
                goToAuth = {
                    navController.navigate(Graph.Auth.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                goToOnboarding = {
                    navController.navigate(Graph.Onboarding.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                goToMain = {
                    navController.navigate(Graph.Main.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Auth Graph
        navigation(startDestination = AuthRoute.GATE, route = Graph.Auth.route) {
            composable(AuthRoute.GATE) {
                val context = LocalContext.current
                LaunchedEffect(Unit) {
                    val seen = context.getSharedPreferences(PREF_AUTH, Context.MODE_PRIVATE)
                        .getBoolean(KEY_WELCOME_SEEN, false)
                    navController.navigate(if (seen) AuthRoute.LOGIN else AuthRoute.WELCOME) {
                        popUpTo(AuthRoute.GATE) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }

            composable(AuthRoute.WELCOME) {
                val context = LocalContext.current
                Welcome(
                    goToRegister = {
                        markWelcomeSeen(context)
                        navController.navigate(AuthRoute.REGISTER) {
                            popUpTo(AuthRoute.WELCOME) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    goToLogin = {
                        markWelcomeSeen(context)
                        navController.navigate(AuthRoute.LOGIN) {
                            popUpTo(AuthRoute.WELCOME) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(AuthRoute.LOGIN) {
                Login(
                    viewModel = authViewModel,
                    goToRegister = { navController.navigate(AuthRoute.REGISTER) },
                    onLoginSuccess = {
                        navController.navigate(Graph.Gate.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    goToForgotPassword = { navController.navigate(AuthRoute.FORGOT) }
                )
            }

            composable(AuthRoute.REGISTER) {
                Register(
                    viewModel = authViewModel,
                    goToLogin = {
                        navController.navigate(AuthRoute.LOGIN) {
                            popUpTo(AuthRoute.REGISTER) { inclusive = true }
                        }
                    },
                    onRegisterSuccess = {
                        navController.navigate(Graph.Onboarding.route) {
                            popUpTo(Graph.Auth.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(AuthRoute.FORGOT) {
                ForgotPassword(
                    goBack = { navController.popBackStack() },
                    onSubmitEmail = { _ -> navController.popBackStack() }
                )
            }
        }

        // Onboarding Graph
        navigation(startDestination = "profile_setup", route = Graph.Onboarding.route) {
            composable("profile_setup") {
                val authUser by authViewModel.user.collectAsState()

                ProfileSetup(
                    authUserId = authUser.id,
                    onNext = { navController.navigate("health_setup") },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("health_setup") {
                val profileSetupVm = remember { ProfilePM.getProfileSetupViewModel() }

                HealthSetupRoute(
                    authViewModel = authViewModel,
                    profileSetupViewModel = profileSetupVm,
                    onNext = { navController.navigate("setup_done") },
                    onBack = { navController.popBackStack() }
                )
            }

            composable("setup_done") {
                SetupDone(
                    onFinish = {
                        navController.navigate(Graph.Main.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        // Main Graph
        navigation(startDestination = "home", route = Graph.Main.route) {
            composable("home") {
                HomeNavHost(
                    navController = navController,
                    authViewModel = authViewModel,
                    onRequestLogout = {
                        authViewModel.logout()
                        navController.navigate(Graph.Gate.route) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun GateScreen(
    goToAuth: () -> Unit,
    goToOnboarding: () -> Unit,
    goToMain: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(true) {
        when (decideNextRoute(context)) {
            Graph.Auth.route -> goToAuth()
            Graph.Onboarding.route -> goToOnboarding()
            else -> goToMain()
        }
    }
}