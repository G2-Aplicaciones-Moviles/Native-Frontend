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
            _list.value = repository.getNutritionists()
            _loading.value = false
        }
    }

    fun sendContact(patientId: Long, nutritionistId: Long) {
        viewModelScope.launch {
            val ok = repository.sendContact(patientId, nutritionistId)
            _message.value = if (ok) "Solicitud enviada" else "Error al enviar"
        }
    }
}
