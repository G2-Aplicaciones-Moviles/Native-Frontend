package pe.edu.upc.jameofit.goals.data.model

import pe.edu.upc.jameofit.goals.model.GoalCalorieConfig


data class GoalCalorieConfigRequest(
    val objective: String,       // "LOSE_WEIGHT" | "MAINTAIN_WEIGHT" | "GAIN_MUSCLE"
    val targetWeightKg: Double,  // > 0
    val pace: String             // "SLOW" | "MODERATE" | "FAST"
) {
    companion object {
        fun fromDomain(config: GoalCalorieConfig): GoalCalorieConfigRequest {
            return GoalCalorieConfigRequest(
                objective = config.objective.name,
                targetWeightKg = config.targetWeightKg,
                pace = config.pace.name
            )
        }
    }
}