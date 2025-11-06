package pe.edu.upc.jameofit.tracking.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.tracking.data.repository.TrackingRepository
import pe.edu.upc.jameofit.tracking.data.model.TrackingResponse
import pe.edu.upc.jameofit.tracking.data.model.TrackingProgressResponse

class TrackingViewModel(
    private val repository: TrackingRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _tracking = MutableStateFlow<TrackingResponse?>(null)
    val tracking: StateFlow<TrackingResponse?> = _tracking

    // ✅ NUEVO: Estado para el progreso
    private val _progress = MutableStateFlow<TrackingProgressResponse?>(null)
    val progress: StateFlow<TrackingProgressResponse?> = _progress

    /**
     * Crea tracking goal desde profile (llama al backend)
     */
    fun createGoalFromProfile(profileId: Long, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.createGoalFromProfile(profileId)
                onResult(true, null)
            } catch (e: Exception) {
                _error.value = e.message
                onResult(false, e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Crea tracking (vincula userId -> tracking)
     */
    fun createTracking(userId: Long, onResult: (Boolean, String?) -> Unit = { _, _ -> }) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.createTracking(userId)
                onResult(true, null)
            } catch (e: Exception) {
                _error.value = e.message
                onResult(false, e.message)
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Carga el tracking por userId
     */
    fun loadTrackingByUserId(userId: Long, onDone: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.getTrackingByUserId(userId)
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
                val result = repository.getTrackingProgress(userId)
                _progress.value = result
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetError() { _error.value = null }
}