package pe.edu.upc.jameofit.recipe.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.jameofit.recipe.data.remote.RecipeService
import pe.edu.upc.jameofit.recipe.model.Recipe

class RecipeRepository(
    private val service: RecipeService
) {
    suspend fun getByCategory(categoryId: Long): Result<List<Recipe>> = withContext(Dispatchers.IO) {
        runCatching {
            val res = service.getRecipesByCategory(categoryId)
            if (!res.isSuccessful) error("HTTP ${res.code()}")
            res.body().orEmpty().map {
                Recipe(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    preparationTime = it.preparationTime,
                    difficulty = it.difficulty
                )
            }
        }
    }
}
