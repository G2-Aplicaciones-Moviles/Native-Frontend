package pe.edu.upc.jameofit.mealplan.data.di

import pe.edu.upc.jameofit.mealplan.data.remote.MealPlanService
import pe.edu.upc.jameofit.mealplan.data.repository.MealPlanRepository
import pe.edu.upc.jameofit.shared.data.di.SharedDataModule.getRetrofit

object DataModule {

    fun getMealPlanService(): MealPlanService =
        getRetrofit().create(MealPlanService::class.java)

    fun getMealPlanRepository(): MealPlanRepository =
        MealPlanRepository(getMealPlanService())
}
