package pe.edu.upc.jameofit.home.presentation.navigation

/**
 * Route for the internal Home NavHost graph.
 * Every authenticated screen lives under this graph.
 */
object HomeGraph {
    /** Route of the Home root graph. */
    const val ROUTE = "home_root"
}

/**
 * Routes for each bottom tab graph. A tab graph groups the screens of a single tab
 * to maintain its own back stack.
 */
object TabGraph {
    const val TRACKING = "tab_tracking"
    const val TIPS = "tab_tips"
    const val MESSAGES = "tab_messages"
    const val NUTRITIONISTS = "tab_nutritionists"
}

/**
 * Leaf routes for the root screen of each tab.
 * Navigate to these when you want to land on a tab's home screen.
 */
object HomeRoute {
    const val TRACKING = "tracking/home"
    const val TIPS = "tips/home"
    const val MESSAGES = "messages/home"
    const val NUTRITIONISTS = "nutritionists/home"
}

/**
 * Sub-routes under the Tracking tab.
 */
object TrackingRoute {
    const val MEAL_ACTIVITY = "tracking/meal_activity"
    const val WEEKLY_PROGRESS = "tracking/weekly_progress"
    const val TIP_DETAIL = "tracking/tip_detail"
}

object RecipeRoute {
    const val BREAKFAST = "recipe/breakfast"
    const val LUNCH = "recipe/lunch"
    const val DINNER = "recipe/dinner"
}

/**
 * Drawer-only destinations that sit outside of the tab graphs.
 */
object DrawerRoute {
    const val PROFILE = "drawer/profile"
    const val GOALS = "drawer/goals"
    const val PROGRESS = "drawer/progress"
    const val MEAL_PLANS = "drawer/meal_plans"
    const val SUBSCRIPTIONS = "drawer/subscriptions"
    const val FAQ = "drawer/faq"
    const val SETTINGS = "drawer/settings"
}
