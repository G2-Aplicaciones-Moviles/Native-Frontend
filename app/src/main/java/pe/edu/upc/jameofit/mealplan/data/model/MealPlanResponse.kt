package pe.edu.upc.jameofit.mealplan.data.model

data class MealPlanResponse(
    val id: Int,
    val name: String,
    val description: String,
    val calories: Double,
    val carbs: Double,
    val proteins: Double,
    val fats: Double,
    val profileId: Long,
    val category: String,
    val isCurrent: Boolean,
    val entries: List<MealPlanEntrySimple> = emptyList(),
    val tags: List<String>
)

data class MealPlanEntrySimple(
    val id: Int,
    val recipeId: Int,
    val day: Int,
    val mealPlanType: Int,
    val mealPlanId: Int
)