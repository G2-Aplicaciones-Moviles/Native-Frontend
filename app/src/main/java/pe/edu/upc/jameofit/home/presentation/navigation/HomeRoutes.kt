package pe.edu.upc.jameofit.home.presentation.navigation

// Graph container for the internal Home NavHost
object HomeGraph {
    const val ROUTE = "home_graph"
}

// Main tabs inside Home (prefixed)
object HomeRoute {
    const val TRACKING = "home/tracking"
    const val PROFILE = "home/profile"
    const val NUTRITIONISTS = "home/nutritionists"
    const val TIPS = "home/tips"
    const val MESSAGES = "home/messages"
}

// Secondary screens reachable from Tracking view (optional redirections)
object TrackingRoute {
    const val MEAL_ACTIVITY = "home/tracking/meal-activity"
    const val WEEKLY_PROGRESS = "home/tracking/weekly-progress"
    const val TIP_DETAIL = "home/tracking/tip-detail"
}