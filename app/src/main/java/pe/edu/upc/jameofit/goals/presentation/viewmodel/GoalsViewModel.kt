package pe.edu.upc.jameofit.goals.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pe.edu.upc.jameofit.goals.data.repository.GoalsRepository
import pe.edu.upc.jameofit.goals.model.*
import pe.edu.upc.jameofit.profile.data.repository.ProfileRepository
import pe.edu.upc.jameofit.profile.domain.model.UserProfileRequest
import pe.edu.upc.jameofit.tracking.data.repository.TrackingRepository
import java.util.Locale

class GoalsViewModel(
    private val goalsRepository: GoalsRepository,
    private val profileRepository: ProfileRepository,
    private val trackingRepository: TrackingRepository
) : ViewModel() {

    private val _objective = MutableStateFlow<ObjectiveType?>(null)
    val objective: StateFlow<ObjectiveType?> = _objective

    private val _targetWeightText = MutableStateFlow<String?>(null)
    val targetWeightText: StateFlow<String?> = _targetWeightText

    private val _pace = MutableStateFlow<PaceType?>(null)
    val pace: StateFlow<PaceType?> = _pace

    private val _dietPreset = MutableStateFlow<DietPreset?>(null)
    val dietPreset: StateFlow<DietPreset?> = _dietPreset

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _goalSaveSuccess = MutableStateFlow<Boolean?>(null)
    val goalSaveSuccess: StateFlow<Boolean?> = _goalSaveSuccess

    private val _dietSaveSuccess = MutableStateFlow<Boolean?>(null)
    val dietSaveSuccess: StateFlow<Boolean?> = _dietSaveSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _profileId = MutableStateFlow<Long?>(null)
    val profileId: StateFlow<Long?> = _profileId

    private val _trackingCreated = MutableStateFlow<Boolean?>(null)
    val trackingCreated: StateFlow<Boolean?> = _trackingCreated

    private val _loaded = MutableStateFlow(false)
    val loaded: StateFlow<Boolean> = _loaded

    fun load(userId: Long, force: Boolean = false) {
        if (_loaded.value && !force) return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val resp = goalsRepository.getGoal(userId)
                resp?.let { applyFromResponse(it) }
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
        _objective.value = goal.objective.toObjectiveTypeOrNull()
        _targetWeightText.value = goal.targetWeightKg.takeIf { it > 0 }?.toString()
        _pace.value = goal.pace.toPaceTypeOrNull()
        _dietPreset.value = goal.dietPreset.toDietPresetOrNull()
    }

    fun selectObjective(value: ObjectiveType) { _objective.value = value }
    fun updateTargetWeightKgText(value: String) { _targetWeightText.value = value.filter { it.isDigit() || it == '.' } }
    fun selectPace(value: PaceType) { _pace.value = value }
    fun selectDietPreset(value: DietPreset) { _dietPreset.value = value }

    fun saveGoalCalories(userId: Long, overrideWeight: Double? = null, overridePace: PaceType? = null) {
        val weight = overrideWeight ?: _targetWeightText.value?.toDoubleOrNull()
        val paceToUse = overridePace ?: _pace.value
        val objectiveToUse = _objective.value

        if (weight == null || weight <= 0.0) { _errorMessage.value = "Ingresa un peso válido"; _goalSaveSuccess.value = false; return }
        if (paceToUse == null) { _errorMessage.value = "Selecciona un ritmo"; _goalSaveSuccess.value = false; return }
        if (objectiveToUse == null) { _errorMessage.value = "Selecciona un objetivo"; _goalSaveSuccess.value = false; return }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ok = goalsRepository.saveGoalCalories(userId, GoalCalorieConfig(objectiveToUse, weight, paceToUse))
                _goalSaveSuccess.value = ok
                if (!ok) _errorMessage.value = "No se pudo guardar objetivo"
                else reload(userId)
            } catch (e: Exception) {
                _goalSaveSuccess.value = false
                _errorMessage.value = "Error al guardar: ${e.message}"
            } finally { _isLoading.value = false }
        }
    }

    fun saveDietType(userId: Long) {
        val preset = _dietPreset.value ?: run { _errorMessage.value = "Selecciona dieta"; _dietSaveSuccess.value = false; return }
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ok = goalsRepository.saveDietType(userId, preset)
                _dietSaveSuccess.value = ok
                if (!ok) _errorMessage.value = "No se pudo guardar dieta"
                else reload(userId)
            } catch (e: Exception) {
                _dietSaveSuccess.value = false
                _errorMessage.value = "Error al guardar dieta: ${e.message}"
            } finally { _isLoading.value = false }
        }
    }

    fun resetGoalSaveSuccess() { _goalSaveSuccess.value = null }
    fun resetDietSaveSuccess() { _dietSaveSuccess.value = null }
    fun resetErrorMessage() { _errorMessage.value = null }
    fun invalidate() {
        _loaded.value = false
        _goalSaveSuccess.value = null
        _dietSaveSuccess.value = null
        _errorMessage.value = null
        _profileId.value = null
        _trackingCreated.value = null
    }
}

// ------------------ Extensiones ------------------
private fun String.toObjectiveTypeOrNull(): ObjectiveType? = when (this.trim().lowercase(Locale.ROOT)) {
    "bajarpeso","lose","loseweight" -> ObjectiveType.LOSE_WEIGHT
    "mantenerpeso","maintain","maintainweight" -> ObjectiveType.MAINTAIN_WEIGHT
    "ganarmusculo","gain","gainmuscle" -> ObjectiveType.GAIN_MUSCLE
    else -> null
}

private fun String.toPaceTypeOrNull(): PaceType? = when (this.trim().lowercase(Locale.ROOT)) {
    "slow","lento","025" -> PaceType.SLOW
    "moderate","moderado","05" -> PaceType.MODERATE
    "fast","rapido","075" -> PaceType.FAST
    else -> null
}

private fun String.toDietPresetOrNull(): DietPreset? = when (this.trim().lowercase(Locale.ROOT)) {
    "omnivore","omnivoro" -> DietPreset.OMNIVORE
    "vegetarian","vegetariano" -> DietPreset.VEGETARIAN
    "vegan","vegano" -> DietPreset.VEGAN
    "low_carb","lowcarb" -> DietPreset.LOW_CARB
    "high_protein","highprotein" -> DietPreset.HIGH_PROTEIN
    "mediterranean","mediterranea" -> DietPreset.MEDITERRANEAN
    else -> null
}
