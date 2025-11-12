package pe.edu.upc.jameofit.goals.model

data class GoalCalorieConfig(
    val objective: ObjectiveType = ObjectiveType.LOSE_WEIGHT,
    val targetWeightKg: Double = 0.0,
    val pace: PaceType = PaceType.MODERATE
)