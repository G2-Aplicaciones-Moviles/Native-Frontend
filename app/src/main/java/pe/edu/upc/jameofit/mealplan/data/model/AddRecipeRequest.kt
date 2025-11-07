package pe.edu.upc.jameofit.mealplan.data.model

data class AddRecipeRequest(
    val recipeId: Long,
    val type: String,
    val day: Int
)
