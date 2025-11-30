package pe.edu.upc.jameofit.recipe.presentation.di

import pe.edu.upc.jameofit.recipe.data.di.DataModule
import pe.edu.upc.jameofit.recipe.presentation.viewmodel.RecipeViewModel

object PresentationModule {
    fun getRecipeViewModel(): RecipeViewModel {
        val recipeRepo = DataModule.getRecipeRepository()
        return RecipeViewModel(recipeRepo)
    }
}