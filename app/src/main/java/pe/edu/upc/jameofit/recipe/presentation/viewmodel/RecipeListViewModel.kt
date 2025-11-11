package pe.edu.upc.jameofit.recipe.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.recipe.data.repository.RecipeRepository
import pe.edu.upc.jameofit.recipe.model.Recipe

data class RecipeListUiState(
    val isLoading: Boolean = true,
    val items: List<Recipe> = emptyList(),
    val error: String? = null
)

class RecipeListViewModel(
    private val repository: RecipeRepository,
    private val categoryId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeListUiState())
    val uiState: StateFlow<RecipeListUiState> = _uiState

    init { load() }

    fun load() {
        _uiState.value = RecipeListUiState(isLoading = true)
        viewModelScope.launch {
            repository.getByCategory(categoryId)
                .onSuccess { _uiState.value = RecipeListUiState(isLoading = false, items = it) }
                .onFailure { _uiState.value = RecipeListUiState(isLoading = false, error = it.message) }
        }
    }
}
