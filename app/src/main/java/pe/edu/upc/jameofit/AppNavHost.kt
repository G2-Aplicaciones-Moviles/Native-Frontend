package pe.edu.upc.jameofit

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import pe.edu.upc.jameofit.home.presentation.navigation.Navigation
import pe.edu.upc.jameofit.iam.presentation.di.PresentationModule
import pe.edu.upc.jameofit.iam.presentation.view.Login
import pe.edu.upc.jameofit.iam.presentation.view.Register
import pe.edu.upc.jameofit.profile.presentation.view.HealthSetup
import pe.edu.upc.jameofit.profile.presentation.view.ProfileSetup
import pe.edu.upc.jameofit.profile.presentation.view.SetupDone
import pe.edu.upc.jameofit.shared.data.local.JwtStorage
import pe.edu.upc.jameofit.navigation.Graph

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
                        popUpTo(0) { inclusive = true }
                    }
                },
                goToOnboarding = {
                    nav.navigate(Graph.Onboarding.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                goToMain = {
                    nav.navigate(Graph.Main.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // 2) Auth graph
        navigation(startDestination = "register", route = Graph.Auth.route) {
            composable("register") {
                val vm = PresentationModule.getAuthViewModel()
                Register(
                    viewmodel = vm,
                    goToLogin = {
                        nav.navigate("login") {
                            popUpTo("register") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onRegisterSuccess = {
                        nav.navigate(Graph.Onboarding.route) {
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable("login") {
                val vm = PresentationModule.getAuthViewModel()
                Login(
                    viewmodel = vm,
                    goToRegister = { nav.navigate("register") },
                    onLoginSuccess = {
                        nav.navigate(Graph.Onboarding.route) {
                            popUpTo(nav.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    goToForgotPassword = { nav.navigate("forgot_password") }
                )
            }

            composable("forgot_password") {
                // TODO: Migrar esta pantalla al nuevo contrato.
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
                // Tu Navigation interna para features (home/goals/profile/…)
                Navigation(
//                    goToLogin = {
//                        nav.navigate(Graph.Auth.route) {
//                            popUpTo(0) { inclusive = true }
//                            launchSingleTop = true
//                        }
//                    }
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
    LaunchedEffect(Unit) {
        val token = JwtStorage.getToken()
        if (token.isNullOrBlank()) {
            goToAuth()
        } else {
            val profileCompleted = context
                .getSharedPreferences("pref_profile", Context.MODE_PRIVATE)
                .getBoolean("profile_completed", false)

            val healthCompleted = context
                .getSharedPreferences("pref_health_profile", Context.MODE_PRIVATE)
                .getBoolean("health_profile_completed", false)

            val needsOnboarding = !(profileCompleted && healthCompleted)
            if (needsOnboarding) goToOnboarding() else goToMain()
        }
    }
}
