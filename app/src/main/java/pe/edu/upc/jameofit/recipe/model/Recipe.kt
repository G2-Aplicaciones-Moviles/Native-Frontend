package pe.edu.upc.jameofit.recipe.model

data class Recipe(
    val id: Long,
    val name: String,
    val description: String,
    val preparationTime: Int,
    val difficulty: String
)
