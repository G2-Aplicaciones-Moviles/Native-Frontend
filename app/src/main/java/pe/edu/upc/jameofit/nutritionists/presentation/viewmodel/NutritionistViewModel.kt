package pe.edu.upc.jameofit.nutritionists.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.nutritionists.data.model.NutritionistResponse
import pe.edu.upc.jameofit.nutritionists.data.repository.NutritionistRepository

class NutritionistViewModel(
    private val repository: NutritionistRepository
) : ViewModel() {

    private val _list = MutableStateFlow<List<NutritionistResponse>>(emptyList())
    val list: StateFlow<List<NutritionistResponse>> = _list

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    fun loadNutritionists() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _list.value = repository.getNutritionists()
            } catch (e: Exception) {
                _message.value = "Error al cargar nutricionistas"
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    fun sendContact(
        patientUserId: Long,
        nutritionistId: Long,
        serviceType: String,
        startDate: String? = null,
        scheduledAt: String? = null
    ) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val ok = repository.sendContact(
                    patientUserId = patientUserId,
                    nutritionistId = nutritionistId,
                    serviceType = serviceType,
                    startDate = startDate,
                    scheduledAt = scheduledAt
                )
                _message.value = if (ok) {
                    "Solicitud enviada exitosamente"
                } else {
                    "Error al enviar la solicitud"
                }

                // Recargar la lista después de enviar solicitud
                if (ok) {
                    loadNutritionists()
                }
            } catch (e: Exception) {
                _message.value = "Error de conexión: ${e.message}"
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}