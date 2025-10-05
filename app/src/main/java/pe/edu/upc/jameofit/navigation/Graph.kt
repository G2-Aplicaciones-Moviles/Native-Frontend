package pe.edu.upc.jameofit.navigation

sealed class Graph(val route: String) {
    data object Gate : Graph("gate_root")              // Decide Auth / Onboarding / Main
    data object Auth : Graph("auth_root")              // Login / Register
    data object Onboarding : Graph("onboarding_root")  // ProfileSetup → HealthProfile → SetupDone
    data object Main : Graph("main_root")              // App autenticada (Home, Goals, etc.)
}

object AuthRoute {
    const val GATE = "auth_gate"
    const val WELCOME = "welcome"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val FORGOT = "forgot_password"
}

object NutritionistRoute {
    const val LIST = "home/nutritionists"
    const val DETAIL = "home/nutritionists/{nutritionistId}"
    fun detail(nutritionistId: String) = "home/nutritionists/$nutritionistId"
}
