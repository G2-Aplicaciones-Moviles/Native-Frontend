package pe.edu.upc.jameofit.recommendations.data.model

data class RecommendationResponse(
    val id: Long,
    val userId: Long?,
    val templateId: Long?,
    val reason: String?,
    val notes: String?,
    val timeOfDay: String?,
    val score: Double?,
    val status: String?,
    val assignedAt: String? // puedes usar String o LocalDateTime si lo conviertes
)
