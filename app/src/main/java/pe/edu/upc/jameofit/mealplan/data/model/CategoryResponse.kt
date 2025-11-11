package pe.edu.upc.jameofit.mealplan.data.model

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)