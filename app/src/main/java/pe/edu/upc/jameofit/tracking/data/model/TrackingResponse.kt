package pe.edu.upc.jameofit.tracking.data.model

data class MacronutrientValuesDto(
    val id: Long,
    val calories: Double,
    val carbs: Double,
    val proteins: Double,
    val fats: Double
)

data class TrackingMealPlanEntryDto(
    val id: Long,
    val recipeId: Int,
    val mealPlanType: String,
    val dayNumber: Int,
    val recipeName: String? = null
)

data class TrackingGoalDto(
    val id: Long,
    val userId: Long,
    val targetMacros: MacronutrientValuesDto
)

data class TrackingResponse(
    val id: Long,
    val userId: Long,
    val date: String,
    val consumedMacros: MacronutrientValuesDto,
    val trackingGoal: TrackingGoalDto?, // ahora sí lo tenemos
    val mealPlanEntries: List<TrackingMealPlanEntryDto>
)
