package pe.edu.upc.jameofit.mealplan.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.jameofit.mealplan.data.remote.MealPlanService
import pe.edu.upc.jameofit.mealplan.model.Category

class MealPlanRepository(
    private val mealPlanService: MealPlanService
) {
    suspend fun getAllCategories(): List<Category> = withContext(Dispatchers.IO) {
        val res = mealPlanService.getAllCategories()

        if (res.isSuccessful) {
            return@withContext res.body()!!.map { it ->
                Category(it.id, it.name)
            }
        }
        return@withContext emptyList()
    }
}