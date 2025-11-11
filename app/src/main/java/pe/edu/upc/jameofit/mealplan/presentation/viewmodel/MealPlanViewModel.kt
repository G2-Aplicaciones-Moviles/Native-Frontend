package pe.edu.upc.jameofit.mealplan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.mealplan.data.repository.MealPlanRepository
import pe.edu.upc.jameofit.mealplan.model.Category

class MealPlanViewModel(
    private val mealPlanRepository: MealPlanRepository
) : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    fun getAllCategories() {
        viewModelScope.launch {
            _categories.value = mealPlanRepository.getAllCategories()
        }
    }
}