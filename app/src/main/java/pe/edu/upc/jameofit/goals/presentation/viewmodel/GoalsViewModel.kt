package pe.edu.upc.jameofit.goals.presentation.viewmodel

import pe.edu.upc.jameofit.goals.model.DietPreset
import pe.edu.upc.jameofit.goals.model.GoalCalorieConfig
import pe.edu.upc.jameofit.goals.model.ObjectiveType
import pe.edu.upc.jameofit.goals.model.PaceType



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.goals.data.repository.GoalsRepository


class GoalsViewModel(
    private val goalsRepository: GoalsRepository
) : ViewModel() {

    // --------- Estado de Formularios (dos secciones) ---------

    // Sección 1: Objetivo y calorías
    private val _objective = MutableStateFlow(ObjectiveType.LOSE_WEIGHT)
    val objective: StateFlow<ObjectiveType> = _objective

    private val _targetWeightText = MutableStateFlow("") // texto para validar en UI
    val targetWeightText: StateFlow<String> = _targetWeightText

    private val _pace = MutableStateFlow(PaceType.MODERATE)
    val pace: StateFlow<PaceType> = _pace

    // Sección 2: Tipo de dieta (macros no editables; los define backend por preset)
    private val _dietPreset = MutableStateFlow(DietPreset.OMNIVORE)
    val dietPreset: StateFlow<DietPreset> = _dietPreset

    // --------- Estado de UI (carga/éxito/error) ---------
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _goalSaveSuccess = MutableStateFlow<Boolean?>(null)
    val goalSaveSuccess: StateFlow<Boolean?> = _goalSaveSuccess

    private val _dietSaveSuccess = MutableStateFlow<Boolean?>(null)
    val dietSaveSuccess: StateFlow<Boolean?> = _dietSaveSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // --------- Update de campos ---------
    fun selectObjective(value: ObjectiveType) {
        _objective.value = value
    }

    fun updateTargetWeightKgText(value: String) {
        // Deja solo dígitos y punto (para validación simple)
        _targetWeightText.value = value.filter { it.isDigit() || it == '.' }
    }

    fun selectPace(value: PaceType) {
        _pace.value = value
    }

    fun selectDietPreset(value: DietPreset) {
        _dietPreset.value = value
    }

    // --------- Acciones (guardar) ---------
    fun saveGoalCalories() {
        val weight = _targetWeightText.value.toDoubleOrNull()
        if (weight == null || weight <= 0.0) {
            _errorMessage.value = "Ingresa un peso objetivo válido"
            _goalSaveSuccess.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ok = goalsRepository.saveGoalCalories(
                    GoalCalorieConfig(
                        objective = _objective.value,
                        targetWeightKg = weight,
                        pace = _pace.value
                    )
                )
                _goalSaveSuccess.value = ok
                if (!ok) _errorMessage.value = "No se pudo guardar objetivo y calorías."
            } catch (e: Exception) {
                _goalSaveSuccess.value = false
                _errorMessage.value = "Error al guardar objetivo y calorías: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveDietType() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ok = goalsRepository.saveDietType(_dietPreset.value)
                _dietSaveSuccess.value = ok
                if (!ok) _errorMessage.value = "No se pudo actualizar el tipo de dieta."
            } catch (e: Exception) {
                _dietSaveSuccess.value = false
                _errorMessage.value = "Error al actualizar el tipo de dieta: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveAll() {
        // Guarda ambas secciones una detrás de otra
        val weight = _targetWeightText.value.toDoubleOrNull()
        if (weight == null || weight <= 0.0) {
            _errorMessage.value = "Ingresa un peso objetivo válido"
            _goalSaveSuccess.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ok1 = goalsRepository.saveGoalCalories(
                    GoalCalorieConfig(
                        objective = _objective.value,
                        targetWeightKg = weight,
                        pace = _pace.value
                    )
                )
                _goalSaveSuccess.value = ok1
                if (!ok1) _errorMessage.value = "No se pudo guardar objetivo y calorías."

                val ok2 = goalsRepository.saveDietType(_dietPreset.value)
                _dietSaveSuccess.value = ok2
                if (!ok2) _errorMessage.value = "No se pudo actualizar el tipo de dieta."
            } catch (e: Exception) {
                _errorMessage.value = "Error al guardar cambios: ${e.message}"
                _goalSaveSuccess.value = false
                _dietSaveSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --------- Utilitarios de UI ---------
    fun resetGoalSaveSuccess() { _goalSaveSuccess.value = null }
    fun resetDietSaveSuccess() { _dietSaveSuccess.value = null }
    fun resetErrorMessage() { _errorMessage.value = null }
}
