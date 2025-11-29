package pe.edu.upc.jameofit.mealplan.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.jameofit.mealplan.data.remote.MealPlanService
import pe.edu.upc.jameofit.mealplan.data.model.*
import pe.edu.upc.jameofit.mealplan.data.remote.MealPlanTemplateResponse

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

    // ✅ ACTUALIZADO: Ahora recibe userId como parámetro
    suspend fun createMealPlan(
        userId: Long,  // ← NUEVO parámetro
        request: CreateMealPlanRequest
    ): MealPlanResponse? = withContext(Dispatchers.IO) {
        val response = api.createMealPlan(userId, request)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun addEntry(mealPlanId: Long, recipeId: Long, type: String, day: Int): Boolean = withContext(Dispatchers.IO) {
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
    // ✅ NUEVO: Obtener templates
    suspend fun getTemplates(): List<MealPlanResponse>? = withContext(Dispatchers.IO) {
        val response = api.getTemplates()
        if (response.isSuccessful) response.body() else null
    }

    // ✅ NUEVO: Obtener templates detallados
    suspend fun getTemplatesDetailed(): List<MealPlanTemplateResponse>? = withContext(Dispatchers.IO) {
        val response = api.getTemplatesDetailed()
        if (response.isSuccessful) response.body() else null
    }

    suspend fun getAllRecipes(): List<RecipeResponse>? = withContext(Dispatchers.IO) {
        try {
            val response = api.getAllRecipes()
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("MealPlanRepository", "getAllRecipes -> success, count=${body?.size ?: 0}")
                body
            } else {
                Log.e("MealPlanRepository", "getAllRecipes -> error http ${response.code()} : ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("MealPlanRepository", "getAllRecipes -> exception: ${e.message}", e)
            null
        }
    }

    suspend fun getRecipeById(recipeId: Long): RecipeResponse? = withContext(Dispatchers.IO) {
        try {
            val response = api.getRecipeById(recipeId)
            if (response.isSuccessful) {
                val body = response.body()
                Log.d("MealPlanRepository", "getRecipeById -> success, recipe=${body?.name}")
                body
            } else {
                Log.e("MealPlanRepository", "getRecipeById -> error http ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("MealPlanRepository", "getRecipeById -> exception: ${e.message}", e)
            null
        }
    }

}
