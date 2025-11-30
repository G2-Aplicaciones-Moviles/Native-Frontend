package pe.edu.upc.jameofit.recipe.data.model

import com.google.gson.annotations.SerializedName

data class NutritionistResponse(
    val id: Long,
    @SerializedName("fullName")
    val name: String?,
    val userId: Long
)