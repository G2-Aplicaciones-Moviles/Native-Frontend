package pe.edu.upc.jameofit.profile.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.edu.upc.jameofit.profile.data.repository.ProfileRepository
import pe.edu.upc.jameofit.profile.domain.model.UserProfileRequest
import pe.edu.upc.jameofit.tracking.data.repository.TrackingRepository

sealed class ProfileSetupUiState {
    object Idle : ProfileSetupUiState()
    object Loading : ProfileSetupUiState()
    data class Success(val profileId: Long) : ProfileSetupUiState()
    data class Error(val message: String) : ProfileSetupUiState()
}

class ProfileSetupViewModel(
    private val profileRepository: ProfileRepository,
    private val trackingRepository: TrackingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileSetupUiState>(ProfileSetupUiState.Idle)
    val uiState: StateFlow<ProfileSetupUiState> = _uiState

    /**
     * Crea el UserProfile y luego automáticamente crea TrackingGoal y Tracking
     */
    fun createCompleteProfile(request: UserProfileRequest, context: Context) {
        viewModelScope.launch {
            _uiState.value = ProfileSetupUiState.Loading

            try {
                // 1. Crear UserProfile
                val profileResp = withContext(Dispatchers.IO) {
                    profileRepository.createProfile(request)
                }

                val profileId = profileResp?.id ?: throw IllegalStateException("Profile ID is null")

                // 2. Crear TrackingGoal desde el Profile
                withContext(Dispatchers.IO) {
                    trackingRepository.createGoalFromProfile(profileId)
                }

                // 3. Crear Tracking para el usuario
                withContext(Dispatchers.IO) {
                    trackingRepository.createTracking(request.userId)
                }

                // 4. Marcar setup completo en SharedPreferences
                context.getSharedPreferences("pref_profile", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("profile_completed", true)
                    .apply()

                context.getSharedPreferences("pref_health_profile", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("health_profile_completed", true)
                    .apply()

                _uiState.value = ProfileSetupUiState.Success(profileId)

            } catch (e: Exception) {
                _uiState.value = ProfileSetupUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    fun reset() {
        _uiState.value = ProfileSetupUiState.Idle
    }
}