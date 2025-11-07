package pe.edu.upc.jameofit.mealplan.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.jameofit.mealplan.data.remote.MealPlanService
import pe.edu.upc.jameofit.mealplan.data.model.*

class MealPlanRepository(
    private val api: MealPlanService
) {

    suspend fun getAllMealPlans(): List<MealPlanResponse>? = withContext(Dispatchers.IO) {
        val response = api.getAllMealPlans()
        if (response.isSuccessful) response.body() else null
    }

    suspend fun getMealPlanById(id: Long): MealPlanResponse? = withContext(Dispatchers.IO) {
        val response = api.getMealPlanById(id)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun getEntries(mealPlanId: Long): List<MealPlanEntryResponse>? = withContext(Dispatchers.IO) {
        val response = api.getEntriesWithRecipeInfo(mealPlanId)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun createMealPlan(request: CreateMealPlanRequest): MealPlanResponse? = withContext(Dispatchers.IO) {
        val response = api.createMealPlan(request)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun addEntry(mealPlanId: Long, recipeId: Int, type: String, day: Int): Boolean = withContext(Dispatchers.IO) {
        val request = AddRecipeRequest(
            recipeId = recipeId.toLong(),
            type = type,
            day = day
        )
        val response = api.addEntryToMealPlan(mealPlanId, request)
        response.isSuccessful
    }

    suspend fun deleteMealPlan(mealPlanId: Long): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteMealPlan(mealPlanId)
        response.isSuccessful
    }
}
