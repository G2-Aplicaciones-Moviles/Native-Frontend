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
import pe.edu.upc.jameofit.goals.model.GoalResponse
import java.util.Locale

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

    // Control de carga inicial
    private val _loaded = MutableStateFlow(false)
    val loaded: StateFlow<Boolean> = _loaded

    // --------- Carga inicial desde backend ---------
    fun load(userId: Long, force: Boolean = false) {
        if (_loaded.value && !force) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val resp = goalsRepository.getGoal(userId)  // ahora puede ser null
                if (resp != null) applyFromResponse(resp)   // solo aplicas si hay body
                // si resp == null asumimos "no configurado" y dejamos defaults
                _loaded.value = true
            } catch (e: Exception) {
                _errorMessage.value = "No se pudo cargar tus metas: ${e.message}"
                _loaded.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun reload(userId: Long) = load(userId, force = true)

    private fun applyFromResponse(goal: GoalResponse) {
        // objective
        goal.objective
            .toObjectiveTypeOrNull()
            ?.let { _objective.value = it }

        // peso
        goal.targetWeightKg
            .takeIf { it > 0.0 }
            ?.let { _targetWeightText.value = it.toString() }

        // pace
        goal.pace
            .toPaceTypeOrNull()
            ?.let { _pace.value = it }

        // diet preset
        goal.dietPreset
            .toDietPresetOrNull()
            ?.let { _dietPreset.value = it }
    }

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
    fun saveGoalCalories(userId: Long) {
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
                    userId = userId,
                    config = GoalCalorieConfig(
                        objective = _objective.value,
                        targetWeightKg = weight,
                        pace = _pace.value
                    )
                )
                _goalSaveSuccess.value = ok
                if (!ok) {
                    _errorMessage.value = "No se pudo guardar objetivo y ritmo de progreso."
                } else {
                    // Si el backend normaliza valores, refrescamos
                    reload(userId)
                }
            } catch (e: Exception) {
                _goalSaveSuccess.value = false
                _errorMessage.value = "Error al guardar objetivo y ritmo de progreso: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveDietType(userId: Long) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ok = goalsRepository.saveDietType(
                    userId = userId,
                    preset = _dietPreset.value
                )
                _dietSaveSuccess.value = ok
                if (!ok) {
                    _errorMessage.value = "No se pudo actualizar el tipo de dieta."
                } else {
                    // Refrescar tras guardar
                    reload(userId)
                }
            } catch (e: Exception) {
                _dietSaveSuccess.value = false
                _errorMessage.value = "Error al actualizar el tipo de dieta: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveAll(userId: Long) {
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
                    userId = userId,
                    config = GoalCalorieConfig(
                        objective = _objective.value,
                        targetWeightKg = weight,
                        pace = _pace.value
                    )
                )
                _goalSaveSuccess.value = ok1
                if (!ok1) _errorMessage.value = "No se pudo guardar objetivo y ritmo."

                val ok2 = goalsRepository.saveDietType(
                    userId = userId,
                    preset = _dietPreset.value
                )
                _dietSaveSuccess.value = ok2
                if (!ok2) _errorMessage.value = "No se pudo actualizar el tipo de dieta."

                if (ok1 && ok2) {
                    reload(userId)
                }
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

    // (opcional) invalidar cuando cierres sesión
    fun invalidate() {
        _loaded.value = false
        _goalSaveSuccess.value = null
        _dietSaveSuccess.value = null
        _errorMessage.value = null
    }
}


private fun String.toObjectiveTypeOrNull(): ObjectiveType? {
    val key = this.trim().uppercase(Locale.ROOT)
    ObjectiveType.entries.firstOrNull { it.name == key }?.let { return it }

    return when (normalizeKey()) {
        "bajar_peso", "bajardepeso", "bajar", "lose", "lose_weight", "loseweight" ->
            ObjectiveType.LOSE_WEIGHT
        "mantener_peso", "mantener", "maintain", "maintain_weight", "maintainweight" ->
            ObjectiveType.MAINTAIN_WEIGHT
        "ganar_musculo", "ganarmusculo", "ganar", "gain", "gain_muscle", "gainmuscle" ->
            ObjectiveType.GAIN_MUSCLE
        else -> null
    }
}

private fun String.toPaceTypeOrNull(): PaceType? {
    val key = this.trim().uppercase(Locale.ROOT)
    PaceType.entries.firstOrNull { it.name == key }?.let { return it }

    return when (normalizeKey()) {
        "slow", "lento", "0.25", "025" -> PaceType.SLOW
        "moderate", "moderado", "0.5", "05" -> PaceType.MODERATE
        "fast", "rapido", "rapido", "rápido", "0.75", "075" -> PaceType.FAST
        else -> null
    }
}

private fun String.toDietPresetOrNull(): DietPreset? {
    val key = this.trim().uppercase(Locale.ROOT)
    DietPreset.entries.firstOrNull { it.name == key }?.let { return it }

    return when (normalizeKey()) {
        "omnivore", "omnivoro", "omnivoro" -> DietPreset.OMNIVORE
        "vegetarian", "vegetariano" -> DietPreset.VEGETARIAN
        "vegan", "vegano" -> DietPreset.VEGAN
        "low_carb", "baja_en_carbohidratos", "lowcarb" -> DietPreset.LOW_CARB
        "high_protein", "alta_en_proteinas", "highprotein" -> DietPreset.HIGH_PROTEIN
        "mediterranean", "mediterranea", "mediterranea" -> DietPreset.MEDITERRANEAN
        else -> null
    }
}

/** Minúsculas (locale neutro), sin tildes, sin espacios, guiones → guion_bajo */
private fun String.normalizeKey(): String =
    lowercase(Locale.ROOT)
        .replace("á","a").replace("é","e").replace("í","i").replace("ó","o").replace("ú","u")
        .replace("-", "_")
        .replace(" ", "")