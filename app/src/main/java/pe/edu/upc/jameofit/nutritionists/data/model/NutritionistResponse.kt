package pe.edu.upc.jameofit.nutritionists.data.model

data class NutritionistResponse(
    val id: Long,
    val userId: Long,
    val fullName: String?,          // Devuelve el backend
    val licenseNumber: String?,
    val speciality: String?,
    val acceptingNewPatients: Boolean?,
    val experiencesYears: Int?,
    val bio: String?,
    val profilePictureUrl: String?
)
