package pe.edu.upc.jameofit.recipe.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.jameofit.recipe.data.model.CategoryResponse
import pe.edu.upc.jameofit.recipe.data.model.CreateRecipeRequest
import pe.edu.upc.jameofit.recipe.data.model.NutritionistResponse
import pe.edu.upc.jameofit.recipe.data.model.RecipeResponse
import pe.edu.upc.jameofit.recipe.data.model.RecipeTemplateResponse
import pe.edu.upc.jameofit.recipe.data.model.RecipeTypeResponse
import pe.edu.upc.jameofit.recipe.data.remote.RecipeService

class RecipeRepository(
    private val api: RecipeService
) {

    suspend fun getAllRecipes(): List<RecipeResponse>? = withContext(Dispatchers.IO) {
        val response = api.getAllRecipes()
        if (response.isSuccessful) response.body() else null
    }

    suspend fun getRecipeById(id: Int): RecipeResponse? = withContext(Dispatchers.IO) {
        val response = api.getRecipeById(id)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun createRecipeForUser(userId: Long, request: CreateRecipeRequest): RecipeResponse? = withContext(Dispatchers.IO) {
        val response = api.createRecipeForUser(userId, request)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun createRecipeForNutritionist(nutritionistId: Long, request: CreateRecipeRequest): RecipeResponse? = withContext(Dispatchers.IO) {
        val response = api.createRecipeForNutritionist(nutritionistId, request)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun assignTemplateToProfile(recipeId: Int, profileId: Long): RecipeResponse? = withContext(Dispatchers.IO) {
        val response = api.assignTemplateToProfile(recipeId, profileId)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun getAllTemplates(): List<RecipeResponse>? = withContext(Dispatchers.IO) {
        val response = api.getAllTemplates()
        if (response.isSuccessful) response.body() else null
    }

    suspend fun getTemplatesByNutritionist(nutritionistId: Long): List<RecipeResponse>? = withContext(Dispatchers.IO) {
        val response = api.getTemplatesByNutritionist(nutritionistId)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun getRecipesByProfileId(profileId: Int): List<RecipeResponse>? = withContext(Dispatchers.IO) {
        val response = api.getRecipesByProfileId(profileId)
        if (response.isSuccessful) response.body() else null
    }
    suspend fun getAllNutritionists(): List<NutritionistResponse>? = withContext(Dispatchers.IO) {
        val response = api.getAllNutritionists()
        if (response.isSuccessful) response.body() else null
    }
    suspend fun deleteRecipe(recipeId: Int): Boolean = withContext(Dispatchers.IO) {
        val response = api.deleteRecipe(recipeId)
        response.isSuccessful
    }
    suspend fun getAllTemplatesWithAuthor(): List<RecipeTemplateResponse>? = withContext(Dispatchers.IO) {
        val response = api.getAllTemplatesDetailed()
        if (response.isSuccessful) response.body() else null
    }

    /** Obtiene templates filtrados por un nutricionista específico (Vista Filtrada). */
    suspend fun getTemplatesByNutritionistWithAuthor(nutritionistId: Long): List<RecipeTemplateResponse>? = withContext(Dispatchers.IO) {
        val response = api.getTemplatesByNutritionistDetailed(nutritionistId)
        if (response.isSuccessful) response.body() else null
    }
    suspend fun getRecipesByCategoryId(categoryId: Long): List<RecipeResponse>? = withContext(Dispatchers.IO) {
        val response = api.getAllRecipesByCategory(categoryId)
        if (response.isSuccessful) response.body() else null
    }

    suspend fun getAllCategories(): List<CategoryResponse>? = withContext(Dispatchers.IO) {
        val response = api.getAllCategories()
        if (response.isSuccessful) response.body() else null
    }

    // 🆕 NUEVO: Obtener tipos de receta
    suspend fun getAllRecipeTypes(): List<RecipeTypeResponse>? = withContext(Dispatchers.IO) {
        val response = api.getAllRecipeTypes()
        if (response.isSuccessful) response.body() else null
    }
}