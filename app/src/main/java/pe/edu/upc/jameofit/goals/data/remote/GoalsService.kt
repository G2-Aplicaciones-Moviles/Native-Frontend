package pe.edu.upc.jameofit.goals.data.remote

import pe.edu.upc.jameofit.goals.data.model.DietTypeConfigRequest
import pe.edu.upc.jameofit.goals.data.model.GoalCalorieConfigRequest
import pe.edu.upc.jameofit.goals.model.GoalResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface GoalsService {
    @PUT("api/v1/goals/calories")
    suspend fun saveGoalCalories(
        @Query("userId") userId: Long,
        @Body body: GoalCalorieConfigRequest
    ): Response<GoalResponse>

    @PUT("api/v1/goals/diet-type")
    suspend fun saveDietType(
        @Query("userId") userId: Long,
        @Body body: DietTypeConfigRequest
    ): Response<GoalResponse>

    @GET("api/v1/goals")
    suspend fun getGoal(@Query("userId") userId: Long): Response<GoalResponse>
}