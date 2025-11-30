package pe.edu.upc.jameofit.recipe.data.di

import pe.edu.upc.jameofit.recipe.data.remote.RecipeService
import pe.edu.upc.jameofit.recipe.data.repository.RecipeRepository
import pe.edu.upc.jameofit.shared.data.di.SharedDataModule.getRetrofit // Asumo que esta clase es accesible

object DataModule {

    fun getRecipeService(): RecipeService =
        getRetrofit().create(RecipeService::class.java)

    fun getRecipeRepository(): RecipeRepository =
        RecipeRepository(getRecipeService())
}