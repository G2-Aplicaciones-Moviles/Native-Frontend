package pe.edu.upc.jameofit.mealplan.data.model

import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    val id: Int,
    val createdByNutritionistId: Long?,
    val assignedToProfileId: Int?,
    val name: String,
    val description: String,
    val preparationTime: Int,
    val difficulty: String,

    @SerializedName("categoryName")
    val category: String,

    @SerializedName("recipeTypeName")
    val recipeType: String,

    @SerializedName("ingredients")
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

