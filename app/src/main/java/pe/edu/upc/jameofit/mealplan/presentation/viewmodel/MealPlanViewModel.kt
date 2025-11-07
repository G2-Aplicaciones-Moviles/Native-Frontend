package pe.edu.upc.jameofit.mealplan.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.mealplan.data.model.CreateMealPlanRequest
import pe.edu.upc.jameofit.mealplan.data.model.MealPlanEntryResponse
import pe.edu.upc.jameofit.mealplan.data.model.MealPlanResponse
import pe.edu.upc.jameofit.mealplan.data.repository.MealPlanRepository

class MealPlanViewModel(
    private val repository: MealPlanRepository
) : ViewModel() {

    private val _mealPlans = MutableStateFlow<List<MealPlanResponse>>(emptyList())
    val mealPlans: StateFlow<List<MealPlanResponse>> = _mealPlans

    private val _entries = MutableStateFlow<List<MealPlanEntryResponse>>(emptyList())
    val entries: StateFlow<List<MealPlanEntryResponse>> = _entries

    private val _selectedMealPlan = MutableStateFlow<MealPlanResponse?>(null)
    val selectedMealPlan: StateFlow<MealPlanResponse?> = _selectedMealPlan

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    /**
     * Carga todos los MealPlans disponibles desde el backend
     */
    fun loadMealPlans() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _mealPlans.value = repository.getAllMealPlans() ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Error cargando MealPlans: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Carga las entradas de un MealPlan específico
     */
    fun loadEntries(mealPlanId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _entries.value = repository.getEntries(mealPlanId) ?: emptyList()
            } catch (e: Exception) {
                _error.value = "Error cargando entradas: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Carga un MealPlan específico por su ID
     */
    fun loadMealPlanById(mealPlanId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val mealPlan = repository.getMealPlanById(mealPlanId)
                _selectedMealPlan.value = mealPlan
            } catch (e: Exception) {
                _error.value = "Error cargando MealPlan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Crea un nuevo MealPlan
     */
    fun createMealPlan(
        name: String,
        description: String,
        calories: Double,
        carbs: Double,
        proteins: Double,
        fats: Double,
        profileId: Long,
        category: String,
        isCurrent: Boolean,
        tags: List<String>
    ) {
        viewModelScope.launch {
            try {
                val request = CreateMealPlanRequest(
                    name = name,
                    description = description,
                    calories = calories,
                    carbs = carbs,
                    proteins = proteins,
                    fats = fats,
                    profileId = profileId,
                    category = category,
                    isCurrent = isCurrent,
                    tags = tags
                )
                repository.createMealPlan(request)
                loadMealPlans()
            } catch (e: Exception) {
                _error.value = "Error creando MealPlan: ${e.message}"
            }
        }
    }

    fun deleteMealPlan(mealPlanId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteMealPlan(mealPlanId)
                _mealPlans.value = _mealPlans.value.filterNot { it.id == mealPlanId.toInt() }
            } catch (e: Exception) {
                _error.value = "Error eliminando MealPlan: ${e.message}"
            }
        }
    }

    fun addRecipeToMealPlan(
        mealPlanId: Long,
        recipeId: Int,
        type: String,
        day: Int,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = repository.addEntry(mealPlanId, recipeId, type, day)
                if (success) {
                    loadEntries(mealPlanId)
                    onSuccess()
                } else {
                    val msg = "Error agregando receta al meal plan"
                    _error.value = msg
                    onError(msg)
                }
            } catch (e: Exception) {
                val msg = "Error agregando receta: ${e.message}"
                _error.value = msg
                onError(msg)
            } finally {
                _isLoading.value = false
            }
        }
    }


}
