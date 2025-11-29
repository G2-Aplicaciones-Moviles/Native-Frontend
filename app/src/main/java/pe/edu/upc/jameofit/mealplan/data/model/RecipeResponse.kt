package pe.edu.upc.jameofit.mealplan.data.model

data class RecipeResponse(
    val id: Long,
    val name: String,
    val description: String,
    val preparationTime: Int,
    val difficulty: String,
    val category: String,
    val recipeType: String,
    val recipeIngredients: List<RecipeIngredientResponse>
)

data class RecipeIngredientResponse(
    val ingredient: IngredientResponse,
    val amountGrams: Double
)

data class IngredientResponse(
    val id: Long,
    val name: String
)

