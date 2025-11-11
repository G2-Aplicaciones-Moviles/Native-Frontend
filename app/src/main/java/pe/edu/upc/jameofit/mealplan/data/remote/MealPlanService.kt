package pe.edu.upc.jameofit.mealplan.data.remote

import pe.edu.upc.jameofit.mealplan.data.model.CategoryResponse
import retrofit2.Response
import retrofit2.http.GET

interface MealPlanService {
    @GET("categories")
    suspend fun getAllCategories(): Response<List<CategoryResponse>>
}