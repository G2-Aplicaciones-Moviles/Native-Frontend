package pe.edu.upc.jameofit.recipe.data.remote

import pe.edu.upc.jameofit.recipe.data.model.CategoryResponse
import pe.edu.upc.jameofit.recipe.data.model.CreateRecipeRequest
import pe.edu.upc.jameofit.recipe.data.model.NutritionistResponse
import pe.edu.upc.jameofit.recipe.data.model.RecipeResponse
import pe.edu.upc.jameofit.recipe.data.model.RecipeTemplateResponse
import pe.edu.upc.jameofit.recipe.data.model.RecipeTypeResponse
import retrofit2.Response
import retrofit2.http.*

interface RecipeService {

    // 1) USER CREATES PERSONAL RECIPE
    @POST("/api/v1/recipes/users/{userId}")
    suspend fun createRecipeForUser(
        @Path("userId") userId: Long,
        @Body request: CreateRecipeRequest
    ): Response<RecipeResponse>

    // 2) NUTRITIONIST CREATES TEMPLATE
    @POST("/api/v1/recipes/nutritionists/{userId}")
    suspend fun createRecipeForNutritionist(
        @Path("userId") userId: Long,
        @Body request: CreateRecipeRequest
    ): Response<RecipeResponse>

    // 3) LIST TEMPLATES CREATED BY NUTRITIONIST
    @GET("/api/v1/recipes/nutritionists/{nutritionistUserId}/templates")
    suspend fun getTemplatesByNutritionist(
        @Path("nutritionistUserId") nutritionistUserId: Long
    ): Response<List<RecipeResponse>>

    // 4) ASSIGN / COPY TEMPLATE TO PROFILE
    @POST("/api/v1/recipes/{recipeId}/assign-to-profile/{profileId}")
    suspend fun assignTemplateToProfile(
        @Path("recipeId") recipeId: Int,
        @Path("profileId") profileId: Long
    ): Response<RecipeResponse>

    // 5) LIST ALL TEMPLATES (Global)
    @GET("/api/v1/recipes/templates")
    suspend fun getAllTemplates(): Response<List<RecipeResponse>>

    // 6) LIST RECIPES ASSIGNED TO A SPECIFIC PROFILE
    @GET("/api/v1/recipes/profile/{profileId}")
    suspend fun getRecipesByProfileId(
        @Path("profileId") profileId: Int
    ): Response<List<RecipeResponse>>

    // READ ENDPOINTS
    @GET("/api/v1/recipes")
    suspend fun getAllRecipes(): Response<List<RecipeResponse>>

    @GET("/api/v1/recipes/{recipeId}")
    suspend fun getRecipeById(
        @Path("recipeId") recipeId: Int
    ): Response<RecipeResponse>

    @DELETE("/api/v1/recipes/{recipeId}")
    suspend fun deleteRecipe(
        @Path("recipeId") recipeId: Int
    ): Response<Unit>

    @GET("/api/v1/recipes/category/{categoryId}")
    suspend fun getAllRecipesByCategory(
        @Path("categoryId") categoryId: Long // Usa Long como en el PathVariable del Backend
    ): Response<List<RecipeResponse>>
    @GET("/api/v1/nutritionists")
    suspend fun getAllNutritionists(): Response<List<NutritionistResponse>>
    @GET("/api/v1/categories")
    suspend fun getAllCategories(): Response<List<CategoryResponse>>

    @GET("/api/v1/recipetypes")
    suspend fun getAllRecipeTypes(): Response<List<RecipeTypeResponse>>

    @GET("/api/v1/recipes/templates/detailed")
    suspend fun getAllTemplatesDetailed(): Response<List<RecipeTemplateResponse>>

    // 🆕 2. Obtener templates de un Nutricionista ESPECÍFICO con información del Autor (Filtrado Detallado)
    @GET("/api/v1/recipes/nutritionists/{nutritionistId}/templates/detailed")
    suspend fun getTemplatesByNutritionistDetailed(
        @Path("nutritionistId") nutritionistId: Long
    ): Response<List<RecipeTemplateResponse>>
}