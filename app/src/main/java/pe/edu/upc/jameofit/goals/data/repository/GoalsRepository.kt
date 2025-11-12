package pe.edu.upc.jameofit.goals.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.jameofit.goals.data.model.DietTypeConfigRequest
import pe.edu.upc.jameofit.goals.data.model.GoalCalorieConfigRequest
import pe.edu.upc.jameofit.goals.data.remote.GoalsService
import pe.edu.upc.jameofit.goals.model.DietPreset

import pe.edu.upc.jameofit.goals.model.GoalCalorieConfig
import pe.edu.upc.jameofit.goals.model.GoalResponse

class GoalsRepository(
    private val goalsService: GoalsService
) {
    suspend fun saveGoalCalories(userId: Long, config: GoalCalorieConfig): Boolean =
        goalsService.saveGoalCalories(userId, GoalCalorieConfigRequest.fromDomain(config)).isSuccessful

    suspend fun saveDietType(userId: Long, preset: DietPreset): Boolean =
        goalsService.saveDietType(userId, DietTypeConfigRequest.fromPreset(preset)).isSuccessful

    suspend fun getGoal(userId: Long): GoalResponse? =
        goalsService.getGoal(userId).body()
}