package pe.edu.upc.jameofit.nutritionists.presentation.di

import pe.edu.upc.jameofit.nutritionists.data.di.DataModule
import pe.edu.upc.jameofit.nutritionists.presentation.viewmodel.NutritionistViewModel

object PresentationModule {

    fun getNutritionistViewModel(): NutritionistViewModel {
        val repo = DataModule.getNutritionistRepository()
        return NutritionistViewModel(repo)
    }
}
