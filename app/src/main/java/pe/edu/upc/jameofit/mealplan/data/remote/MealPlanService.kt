package pe.edu.upc.jameofit.mealplan.data.remote

import pe.edu.upc.jameofit.mealplan.data.model.AddRecipeRequest
import pe.edu.upc.jameofit.mealplan.data.model.MealPlanResponse
import pe.edu.upc.jameofit.mealplan.data.model.MealPlanEntryResponse
import pe.edu.upc.jameofit.mealplan.data.model.CreateMealPlanRequest
import retrofit2.Response
import retrofit2.http.*

interface MealPlanService {

    @GET("/api/v1/meal-plan")
    suspend fun getAllMealPlans(): Response<List<MealPlanResponse>>

    @GET("/api/v1/meal-plan/{mealPlanId}")
    suspend fun getMealPlanById(
        @Path("mealPlanId") mealPlanId: Long
    ): Response<MealPlanResponse>

    @GET("/api/v1/meal-plan/detailed/{mealPlanId}")
    suspend fun getEntriesWithRecipeInfo(
        @Path("mealPlanId") mealPlanId: Long
    ): Response<List<MealPlanEntryResponse>>

    @POST("/api/v1/meal-plan")
    suspend fun createMealPlan(
        @Body request: CreateMealPlanRequest
    ): Response<MealPlanResponse>

    @POST("/api/v1/meal-plan/{mealPlanId}/entries")
    suspend fun addEntryToMealPlan(
        @Path("mealPlanId") mealPlanId: Long,
        @Body request: AddRecipeRequest
    ): Response<Unit>

    @DELETE("/api/v1/meal-plan/{mealPlanId}")
    suspend fun deleteMealPlan(
        @Path("mealPlanId") mealPlanId: Long
    ): Response<Unit>

    // ✅ NUEVO: Borrar entry del tracking
    @DELETE("/api/v1/meal-plan-entries/tracking/{trackingId}/entry/{mealPlanEntryId}")
    suspend fun removeEntryFromTracking(
        @Path("trackingId") trackingId: Long,
        @Path("mealPlanEntryId") mealPlanEntryId: Long
    ): Response<Unit>
}