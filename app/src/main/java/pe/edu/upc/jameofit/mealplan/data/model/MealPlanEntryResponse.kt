package pe.edu.upc.jameofit.mealplan.data.model

data class MealPlanEntryResponse(
    val id: Int,
    val recipeId: Int,
    val recipeName: String?,
    val recipeDescription: String?,
    val day: Int,
    val mealPlanType: Int,
    val mealPlanId: Int
)
