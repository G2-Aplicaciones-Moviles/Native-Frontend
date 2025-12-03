package pe.edu.upc.jameofit.nutritionists.data.di

import pe.edu.upc.jameofit.nutritionists.data.remote.NutritionistService
import pe.edu.upc.jameofit.shared.data.di.SharedDataModule.getRetrofit
import pe.edu.upc.jameofit.nutritionists.data.repository.NutritionistRepository

object DataModule {

    fun getNutritionistService(): NutritionistService =
        getRetrofit().create(NutritionistService::class.java)

    fun getNutritionistRepository(): NutritionistRepository =
        NutritionistRepository(getNutritionistService())
}
