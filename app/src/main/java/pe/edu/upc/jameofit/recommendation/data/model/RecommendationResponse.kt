package pe.edu.upc.jameofit.recommendation.data.model

data class RecommendationResponse(
    val id: Long,
    val userId: Long?,
    val title: String,
    val content: String,
    val category: String,
    val reason: String?,
    val notes: String?,
    val timeOfDay: String?,
    val score: Double?,
    val status: String?
)
