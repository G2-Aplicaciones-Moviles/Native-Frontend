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
    private val _objective = MutableStateFlow<ObjectiveType?>(null)
    val objective: StateFlow<ObjectiveType?> = _objective

    private val _targetWeightText = MutableStateFlow<String?>(null)
    val targetWeightText: StateFlow<String?> = _targetWeightText

    private val _pace = MutableStateFlow<PaceType?>(null)
    val pace: StateFlow<PaceType?> = _pace

    // Sección 2: Tipo de dieta (macros no editables; los define backend por preset)
    private val _dietPreset = MutableStateFlow<DietPreset?>(null)
    val dietPreset: StateFlow<DietPreset?> = _dietPreset

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
        android.util.Log.d("GoalsViewModel", "=== APLICANDO RESPUESTA ===")
        android.util.Log.d("GoalsViewModel", "GoalResponse completo: $goal")

        // Aplica TODOS los valores, no solo los que parsean
        val parsedObjective = goal.objective.toObjectiveTypeOrNull()
        android.util.Log.d("GoalsViewModel", "objective raw: '${goal.objective}' → parsed: $parsedObjective")
        _objective.value = parsedObjective // 👈 Siempre actualiza, incluso si es null

        val validWeight = goal.targetWeightKg.takeIf { it > 0.0 }
        android.util.Log.d("GoalsViewModel", "targetWeightKg raw: ${goal.targetWeightKg} → valid: $validWeight")
        _targetWeightText.value = validWeight?.toString() // 👈 Puede ser null

        val parsedPace = goal.pace.toPaceTypeOrNull()
        android.util.Log.d("GoalsViewModel", "pace raw: '${goal.pace}' → parsed: $parsedPace")
        _pace.value = parsedPace

        val parsedDiet = goal.dietPreset.toDietPresetOrNull()
        android.util.Log.d("GoalsViewModel", "dietPreset raw: '${goal.dietPreset}' → parsed: $parsedDiet")
        _dietPreset.value = parsedDiet

        android.util.Log.d("GoalsViewModel", "=== FIN APLICAR RESPUESTA ===")
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
    /**
     * Guarda la configuración de objetivo y calorías.
     *
     * @param userId ID del usuario
     * @param overrideWeight Peso objetivo a usar. Si es null, usa el valor del StateFlow _targetWeightText
     * @param overridePace Ritmo a usar. Si es null, usa el valor del StateFlow _pace
     */
    fun saveGoalCalories(
        userId: Long,
        overrideWeight: Double? = null,
        overridePace: PaceType? = null
    ) {
        val weight = overrideWeight ?: _targetWeightText.value?.toDoubleOrNull()
        if (weight == null || weight <= 0.0) {
            _errorMessage.value = "Ingresa un peso objetivo válido"
            _goalSaveSuccess.value = false
            return
        }

        val paceToUse = overridePace ?: _pace.value
        if (paceToUse == null) {
            _errorMessage.value = "Selecciona un ritmo de progreso"
            _goalSaveSuccess.value = false
            return
        }

        val objectiveToUse = _objective.value
        if (objectiveToUse == null) {
            _errorMessage.value = "Selecciona un objetivo"
            _goalSaveSuccess.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ok = goalsRepository.saveGoalCalories(
                    userId = userId,
                    config = GoalCalorieConfig(
                        objective = objectiveToUse,
                        targetWeightKg = weight,
                        pace = paceToUse
                    )
                )
                _goalSaveSuccess.value = ok
                if (!ok) {
                    _errorMessage.value = "No se pudo guardar objetivo y ritmo de progreso."
                } else {
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
        val presetToUse = _dietPreset.value
        if (presetToUse == null) {
            _errorMessage.value = "Selecciona un tipo de dieta"
            _dietSaveSuccess.value = false
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ok = goalsRepository.saveDietType(
                    userId = userId,
                    preset = presetToUse
                )
                _dietSaveSuccess.value = ok
                if (!ok) {
                    _errorMessage.value = "No se pudo actualizar el tipo de dieta."
                } else {
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