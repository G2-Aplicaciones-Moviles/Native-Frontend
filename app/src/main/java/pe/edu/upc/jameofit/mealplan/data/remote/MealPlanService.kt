package pe.edu.upc.jameofit.mealplan.data.remote

import pe.edu.upc.jameofit.mealplan.data.model.AddRecipeRequest
import pe.edu.upc.jameofit.mealplan.data.model.MealPlanResponse
import pe.edu.upc.jameofit.mealplan.data.model.MealPlanEntryResponse
import pe.edu.upc.jameofit.mealplan.data.model.CreateMealPlanRequest
import retrofit2.Response
import retrofit2.http.*

interface MealPlanService {

    @GET("meal-plan")
    suspend fun getAllMealPlans(): Response<List<MealPlanResponse>>

    @GET("meal-plan/{mealPlanId}")
    suspend fun getMealPlanById(
        @Path("mealPlanId") mealPlanId: Long
    ): Response<MealPlanResponse>

    @GET("meal-plan/detailed/{mealPlanId}")
    suspend fun getEntriesWithRecipeInfo(
        @Path("mealPlanId") mealPlanId: Long
    ): Response<List<MealPlanEntryResponse>>

    @POST("meal-plan")
    suspend fun createMealPlan(
        @Body request: CreateMealPlanRequest
    ): Response<MealPlanResponse>

    @POST("meal-plan/{mealPlanId}/entries")
    suspend fun addEntryToMealPlan(
        @Path("mealPlanId") mealPlanId: Long,
        @Body request: AddRecipeRequest
    ): Response<Unit>

    @DELETE("meal-plan/{mealPlanId}")
    suspend fun deleteMealPlan(
        @Path("mealPlanId") mealPlanId: Long
    ): Response<Unit>
}
