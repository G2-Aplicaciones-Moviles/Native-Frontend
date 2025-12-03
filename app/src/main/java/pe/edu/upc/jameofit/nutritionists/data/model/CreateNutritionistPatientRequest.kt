package pe.edu.upc.jameofit.nutritionists.data.model

data class CreateNutritionistPatientRequest(
    val patientUserId: Long,
    val nutritionistId: Long,
    val serviceType: String,  // "DIET_PLAN" o "PERSONAL_CONSULT"
    val startDate: String? = null,  // ISO format: "2024-01-15T10:00:00"
    val scheduledAt: String? = null  // ISO format: "2024-01-15T10:00:00"
)