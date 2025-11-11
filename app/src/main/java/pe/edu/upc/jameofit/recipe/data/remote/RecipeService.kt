package pe.edu.upc.jameofit.recipe.data.remote

import pe.edu.upc.jameofit.recipe.data.model.RecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeService {
    @GET("recipes/category/{id}")
    suspend fun getRecipesByCategory(@Path("id") categoryId: Long): Response<List<RecipeResponse>>
}
