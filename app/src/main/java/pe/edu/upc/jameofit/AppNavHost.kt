package pe.edu.upc.jameofit

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import pe.edu.upc.jameofit.home.presentation.navigation.HomeNavHost
import pe.edu.upc.jameofit.iam.presentation.di.PresentationModule
import pe.edu.upc.jameofit.iam.presentation.view.ForgotPassword
import pe.edu.upc.jameofit.iam.presentation.view.Login
import pe.edu.upc.jameofit.iam.presentation.view.Register
import pe.edu.upc.jameofit.iam.presentation.view.Welcome
import pe.edu.upc.jameofit.navigation.AuthRoute
import pe.edu.upc.jameofit.profile.presentation.view.HealthSetup
import pe.edu.upc.jameofit.profile.presentation.view.ProfileSetup
import pe.edu.upc.jameofit.profile.presentation.view.SetupDone
import pe.edu.upc.jameofit.shared.data.local.JwtStorage
import pe.edu.upc.jameofit.navigation.Graph
import androidx.core.content.edit

private const val PREF_AUTH = "pref_auth"
private const val KEY_WELCOME_SEEN = "welcome_seen"

private fun markWelcomeSeen(context: Context) {
    context.getSharedPreferences(PREF_AUTH, Context.MODE_PRIVATE)
        .edit {
            putBoolean(KEY_WELCOME_SEEN, true)
        }
}

private fun decideNextRoute(context: Context): String {
    val token = JwtStorage.getToken()
    if (token.isNullOrBlank()) return Graph.Auth.route

    val profileCompleted = context
        .getSharedPreferences("pref_profile", Context.MODE_PRIVATE)
        .getBoolean("profile_completed", false)

    val healthCompleted = context
        .getSharedPreferences("pref_health_profile", Context.MODE_PRIVATE)
        .getBoolean("health_profile_completed", false)

    return if (profileCompleted && healthCompleted) Graph.Main.route else Graph.Onboarding.route
}

@Composable
fun AppNavHost() {
    val nav = rememberNavController()

    NavHost(
        navController = nav,
        startDestination = Graph.Gate.route
    ) {
        // 1) Gate: decide una sola vez a dónde ir (Auth, Onboarding o Main)
        composable(Graph.Gate.route) {
            GateScreen(
                goToAuth = {
                    nav.navigate(Graph.Auth.route) {
                        popUpTo(nav.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                goToOnboarding = {
                    nav.navigate(Graph.Onboarding.route) {
                        popUpTo(nav.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                goToMain = {
                    nav.navigate(Graph.Main.route) {
                        popUpTo(nav.graph.id) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 2) Auth graph
        navigation(startDestination = AuthRoute.GATE, route = Graph.Auth.route) {
            composable(AuthRoute.GATE) {
                val context = LocalContext.current
                LaunchedEffect(true) {
                    val seen = context.getSharedPreferences(PREF_AUTH, Context.MODE_PRIVATE)
                        .getBoolean(KEY_WELCOME_SEEN, false)
                    nav.navigate(if (seen) AuthRoute.LOGIN else AuthRoute.WELCOME) {
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
                        nav.navigate(AuthRoute.REGISTER) {
                            popUpTo(AuthRoute.WELCOME) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    goToLogin = {
                        markWelcomeSeen(context)
                        nav.navigate(AuthRoute.LOGIN) {
                            popUpTo(AuthRoute.WELCOME) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(AuthRoute.LOGIN) {
                val vm = PresentationModule.getAuthViewModel()
                Login(
                    viewmodel = vm,
                    goToRegister = { nav.navigate(AuthRoute.REGISTER) },
                    onLoginSuccess = {
                        nav.navigate(Graph.Gate.route) {
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    goToForgotPassword = { nav.navigate(AuthRoute.FORGOT) }
                )
            }

            composable(AuthRoute.REGISTER) {
                val vm = PresentationModule.getAuthViewModel()
                Register(
                    viewmodel = vm,
                    goToLogin = {
                        nav.navigate(AuthRoute.LOGIN) {
                            popUpTo(AuthRoute.REGISTER) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onRegisterSuccess = {
                        nav.navigate(Graph.Gate.route) {
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(AuthRoute.FORGOT) {
                ForgotPassword(
                    goBack = { nav.popBackStack() },
                    onSubmitEmail = { _ ->
                        nav.popBackStack()
                    }
                )
            }
        }

        // 3) Onboarding graph
        navigation(startDestination = "profile_setup", route = Graph.Onboarding.route) {
            composable("profile_setup") {
                ProfileSetup(
                    onNext = { nav.navigate("health_profile") },
                    onBack = { /* opcional */ }
                )
            }
            composable("health_profile") {
                HealthSetup(
                    onNext = { nav.navigate("setup_done") },
                    onBack = { nav.popBackStack() }
                )
            }
            composable("setup_done") {
                SetupDone(
                    onFinish = {
                        nav.navigate(Graph.Main.route) {
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

        // 4) Main graph
        navigation(startDestination = "home", route = Graph.Main.route) {
            composable("home") {
                val vm = PresentationModule.getAuthViewModel()
                HomeNavHost(
                    navController = rememberNavController(),
                    onRequestLogout = {
                        vm.logout()
                        nav.navigate(Graph.Gate.route) {
                            popUpTo(nav.graph.id) { inclusive = true }
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