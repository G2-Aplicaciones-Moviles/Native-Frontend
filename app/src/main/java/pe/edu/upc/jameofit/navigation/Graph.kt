package pe.edu.upc.jameofit.navigation

sealed class Graph(val route: String) {
    data object Gate : Graph("gate_root")              // Decide Auth / Onboarding / Main
    data object Auth : Graph("auth_root")              // Login / Register
    data object Onboarding : Graph("onboarding_root")  // ProfileSetup → HealthProfile → SetupDone
    data object Main : Graph("main_root")              // App autenticada (Home, Goals, etc.)
}