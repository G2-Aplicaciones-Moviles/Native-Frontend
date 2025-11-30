package pe.edu.upc.jameofit.recipe.data.model

data class CreateRecipeRequest(
    val name: String,
    val description: String,
    val preparationTime: Int,
    val difficulty: String,
    val categoryId: Long,
    val recipeTypeId: Long
)