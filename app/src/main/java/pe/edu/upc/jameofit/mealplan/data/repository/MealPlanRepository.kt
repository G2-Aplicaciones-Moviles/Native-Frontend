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

    suspend fun getDetailedEntries(mealPlanId: Int): List<MealPlanEntryResponse> = withContext(Dispatchers.IO) {
        val response = api.getEntriesWithRecipeInfo(mealPlanId.toLong())
        if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }

    suspend fun getCurrentMealPlanByProfile(profileId: Long): MealPlanResponse? = withContext(Dispatchers.IO) {
        val allPlans = getAllMealPlans()
        allPlans?.find { it.profileId == profileId && it.isCurrent }
    }

    // ✅ NUEVO: Obtener meal plans desde el endpoint por profileId
    suspend fun getMealPlansByProfileId(profileId: Long): List<MealPlanResponse>? = withContext(Dispatchers.IO) {
        val response = api.getMealPlansByProfileId(profileId)
        if (response.isSuccessful) response.body() else null
    }

    // ✅ Borrar entry individual del tracking
    suspend fun removeEntryFromTracking(trackingId: Long, entryId: Long): Boolean = withContext(Dispatchers.IO) {
        val response = api.removeEntryFromTracking(trackingId, entryId)
        response.isSuccessful
    }

    // ✅ Borrar meal plan completo limpiando tracking
    suspend fun deleteMealPlanWithTracking(mealPlanId: Long, trackingId: Long): Boolean = withContext(Dispatchers.IO) {
        try {
            // 1. Obtener todas las entries del meal plan
            val entries = getDetailedEntries(mealPlanId.toInt())

            // 2. Borrar cada entry del tracking (ignorar errores individuales)
            entries.forEach { entry ->
                try {
                    removeEntryFromTracking(trackingId, entry.id.toLong())
                } catch (_: Exception) { }
            }

            // 3. Borrar el meal plan
            deleteMealPlan(mealPlanId)
        } catch (_: Exception) {
            false
        }
    }
}
