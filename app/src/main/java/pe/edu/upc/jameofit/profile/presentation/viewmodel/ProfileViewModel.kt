package pe.edu.upc.jameofit.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.profile.data.repository.ProfileRepository
import pe.edu.upc.jameofit.profile.domain.model.UserProfileRequest
import pe.edu.upc.jameofit.profile.domain.model.UserProfileResponse
import pe.edu.upc.jameofit.tracking.data.repository.TrackingRepository

sealed interface ProfileUiState {
    object Idle : ProfileUiState
    object Loading : ProfileUiState
    data class Success(val profile: UserProfileResponse) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}

class ProfileViewModel(
    private val repository: ProfileRepository,
    private val trackingRepository: TrackingRepository  // ✅ NUEVO
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun createProfile(request: UserProfileRequest, onProfileCreated: (UserProfileResponse) -> Unit) {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            try {
                val response = repository.createProfile(request)
                if (response != null) {
                    _uiState.value = ProfileUiState.Success(response)
                    onProfileCreated(response)
                } else {
                    _uiState.value = ProfileUiState.Error("Error al crear perfil (sin respuesta)")
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun getProfileById(profileId: Long, onProfileLoaded: (UserProfileResponse?) -> Unit = {}) {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            try {
                val profile = repository.getUserProfile(profileId)
                if (profile != null) {
                    _uiState.value = ProfileUiState.Success(profile)
                    onProfileLoaded(profile)
                } else {
                    _uiState.value = ProfileUiState.Error("Perfil no encontrado")
                    onProfileLoaded(null)
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Error cargando perfil: ${e.message}")
                onProfileLoaded(null)
            }
        }
    }

    // ✅ NUEVO: Actualizar perfil + recalcular tracking
    fun updateProfileAndRecalculate(
        profileId: Long,
        request: UserProfileRequest,
        onSuccess: () -> Unit = {},
        onError: (String) -> Unit = {}
    ) {
        _uiState.value = ProfileUiState.Loading
        viewModelScope.launch {
            try {
                // 1. Actualizar perfil
                val updated = repository.updateProfile(profileId, request)

                if (updated) {
                    // 2. Recalcular tracking goal
                    trackingRepository.updateTrackingGoalFromProfile(profileId)

                    // 3. Recargar perfil actualizado
                    val updatedProfile = repository.getUserProfile(profileId)
                    if (updatedProfile != null) {
                        _uiState.value = ProfileUiState.Success(updatedProfile)
                        onSuccess()
                    } else {
                        _uiState.value = ProfileUiState.Error("Perfil actualizado pero no se pudo recargar")
                    }
                } else {
                    _uiState.value = ProfileUiState.Error("No se pudo actualizar el perfil")
                    onError("No se pudo actualizar el perfil")
                }
            } catch (e: Exception) {
                val msg = "Error actualizando perfil: ${e.message}"
                _uiState.value = ProfileUiState.Error(msg)
                onError(msg)
            }
        }
    }

    fun resetState() {
        _uiState.value = ProfileUiState.Idle
    }
}