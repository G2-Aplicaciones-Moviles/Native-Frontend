package pe.edu.upc.jameofit.recipe.data.model

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("userId") val userId: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("preparationTime") val preparationTime: Int,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("category") val category: String,
    @SerializedName("recipeType") val recipeType: String,
    @SerializedName("ingredients") val ingredients: List<String>
)
