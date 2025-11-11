package pe.edu.upc.jameofit.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pe.edu.upc.jameofit.recipe.data.repository.RecipeRepository

class RecipeListVMFactory(
    private val repo: RecipeRepository,
    private val categoryId: Long
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return RecipeListViewModel(repo, categoryId) as T
    }
}
