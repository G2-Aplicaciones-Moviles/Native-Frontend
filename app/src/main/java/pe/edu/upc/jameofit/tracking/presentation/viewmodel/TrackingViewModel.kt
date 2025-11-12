package pe.edu.upc.jameofit.tracking.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.tracking.data.repository.TrackingRepository
import pe.edu.upc.jameofit.tracking.data.model.TrackingResponse
import pe.edu.upc.jameofit.tracking.data.model.TrackingProgressResponse
import pe.edu.upc.jameofit.mealplan.data.repository.MealPlanRepository
import pe.edu.upc.jameofit.mealplan.data.model.MealPlanEntryResponse
import pe.edu.upc.jameofit.profile.data.repository.ProfileRepository

class TrackingViewModel(
    private val trackingRepository: TrackingRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _tracking = MutableStateFlow<TrackingResponse?>(null)
    val tracking: StateFlow<TrackingResponse?> = _tracking

    private val _progress = MutableStateFlow<TrackingProgressResponse?>(null)
    val progress: StateFlow<TrackingProgressResponse?> = _progress

    // ✅ NUEVO: Estado para comidas recientes
    private val _recentMeals = MutableStateFlow<List<MealPlanEntryResponse>>(emptyList())
    val recentMeals: StateFlow<List<MealPlanEntryResponse>> = _recentMeals

    // ✅ NUEVO: Indica si el usuario tiene meal plan
    private val _hasMealPlan = MutableStateFlow(false)
    val hasMealPlan: StateFlow<Boolean> = _hasMealPlan

    fun createGoalFromProfile(profileId: Long, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                trackingRepository.createGoalFromProfile(profileId)
                onResult(true, null)
            } catch (e: Exception) {
                _error.value = e.message
                onResult(false, e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createTracking(userId: Long, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                trackingRepository.createTracking(userId)
                onResult(true, null)
            } catch (e: Exception) {
                _error.value = e.message
                onResult(false, e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadTrackingByUserId(userId: Long, onDone: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = trackingRepository.getTrackingByUserId(userId)
                _tracking.value = result
                onDone(true)
            } catch (e: Exception) {
                _error.value = e.message
                onDone(false)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadProgress(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = trackingRepository.getTrackingProgress(userId)
                _progress.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ✅ NUEVO: Cargar actividad reciente del meal plan
    fun loadRecentActivity(userId: Long) {
        viewModelScope.launch {
            try {
                // 1. Obtener profile del usuario (asumiendo profileId = userId)
                val profile = profileRepository.getUserProfile(userId)

                if (profile != null) {
                    // 2. Buscar meal plan activo del profile
                    val currentMealPlan = mealPlanRepository.getCurrentMealPlanByProfile(profile.id)

                    if (currentMealPlan != null) {
                        // 3. Obtener entries con nombres de recetas
                        val entries = mealPlanRepository.getDetailedEntries(currentMealPlan.id)

                        // Mostrar las últimas 5 comidas, más recientes primero
                        _recentMeals.value = entries.takeLast(5).reversed()
                        _hasMealPlan.value = true
                    } else {
                        // No tiene meal plan activo
                        _recentMeals.value = emptyList()
                        _hasMealPlan.value = false
                    }
                } else {
                    // No se encontró el perfil
                    _error.value = "No se encontró el perfil del usuario"
                    _recentMeals.value = emptyList()
                    _hasMealPlan.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message
                _recentMeals.value = emptyList()
                _hasMealPlan.value = false
            }
        }
    }

    fun resetError() {
        _error.value = null
    }
}