package pe.edu.upc.jameofit.nutritionists.data.model

data class CreateNutritionistPatientRequest(
    val patientId: Long,
    val nutritionistId: Long
)
