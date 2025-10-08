package pe.edu.upc.jameofit.recommendations.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.recommendations.data.repository.RecommendationsRepository
import pe.edu.upc.jameofit.recommendations.data.model.RecommendationResponse

class RecommendationsViewModel(
    private val repository: RecommendationsRepository
) : ViewModel() {

    private val _recommendations = MutableStateFlow<List<RecommendationResponse>>(emptyList())
    val recommendations: StateFlow<List<RecommendationResponse>> = _recommendations

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadRecommendations(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val data = repository.getRecommendations(userId)
                _recommendations.value = data ?: emptyList()
            } catch (e: Exception) {
                _error.value = "No se pudieron cargar las recomendaciones: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
