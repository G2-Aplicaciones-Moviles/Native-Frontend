package pe.edu.upc.jameofit.recipe.data.model

import com.google.gson.annotations.SerializedName

data class RecipeTemplateResponse(
    val id: Int,
    val name: String?,
    val description: String?,
    val preparationTime: Int?,
    val difficulty: String?,

    @SerializedName("categoryName")
    val category: String?,
    @SerializedName("recipeTypeName")
    val recipeType: String?,

    val createdByNutritionistId: Long,
    val nutritionistName: String?,

    @SerializedName("ingredients")
    val recipeIngredients: List<RecipeIngredientResponse>
)