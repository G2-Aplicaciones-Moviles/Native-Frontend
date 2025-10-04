package pe.edu.upc.jameofit.goals.model

data class GoalResponse(
    val userId: Long,
    val objective: String,
    val targetWeightKg: Double,
    val pace: String,
    val dietPreset: String,
    val proteinPct: Int?,
    val carbsPct: Int?,
    val fatPct: Int?
)