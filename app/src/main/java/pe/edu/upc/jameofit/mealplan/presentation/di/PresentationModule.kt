package pe.edu.upc.jameofit.mealplan.presentation.di

import pe.edu.upc.jameofit.mealplan.data.di.DataModule.getMealPlanRepository
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel

object PresentationModule {
    fun getMealPlanViewModel(): MealPlanViewModel {
        return MealPlanViewModel(getMealPlanRepository())
    }
}
