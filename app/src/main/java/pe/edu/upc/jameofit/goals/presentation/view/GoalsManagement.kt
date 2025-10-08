@file:OptIn(ExperimentalMaterial3Api::class)

package pe.edu.upc.jameofit.goals.presentation.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.goals.model.DietPreset
import pe.edu.upc.jameofit.goals.model.ObjectiveType

import pe.edu.upc.jameofit.goals.model.PaceType
import pe.edu.upc.jameofit.goals.presentation.viewmodel.GoalsViewModel
import pe.edu.upc.jameofit.shared.presentation.components.ErrorSnackbarHost
import pe.edu.upc.jameofit.shared.presentation.components.FullscreenLoader
import pe.edu.upc.jameofit.shared.presentation.components.showErrorOnce
import pe.edu.upc.jameofit.ui.theme.JameoBlue
import pe.edu.upc.jameofit.ui.theme.JameoGreen

@Composable
fun GoalsManagement(
    userId: Long,
    viewmodel: GoalsViewModel,
    onBack: () -> Unit
) {
    DisposableEffect(Unit) {
        android.util.Log.d("GoalsManagement", "Composable creado/recompuesto")
        onDispose {
            android.util.Log.d("GoalsManagement", "Composable destruido")
        }
    }

    LaunchedEffect(userId) {
        android.util.Log.d("GoalsManagement", "LaunchedEffect ejecutado para userId: $userId")
        viewmodel.load(userId)
    }
    // ✅ Flag local para evitar múltiples cargas
    var hasInitiallyLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        if (!hasInitiallyLoaded) {
            android.util.Log.d("GoalsManagement", "Primera carga para userId: $userId")
            viewmodel.load(userId)
            hasInitiallyLoaded = true
        }
    }

    // ---------- STATE FROM VM ----------
    val objective by viewmodel.objective.collectAsState()
    val targetWeightText by viewmodel.targetWeightText.collectAsState()
    val pace by viewmodel.pace.collectAsState()
    val dietPreset by viewmodel.dietPreset.collectAsState()

    // Valores con defaults
    val currentObjective = objective ?: ObjectiveType.LOSE_WEIGHT
    val currentTargetWeight = targetWeightText ?: ""
    val currentPace = pace ?: PaceType.MODERATE
    val currentDietPreset = dietPreset ?: DietPreset.OMNIVORE

    val isLoading by viewmodel.isLoading.collectAsState()
    val goalSaveSuccess by viewmodel.goalSaveSuccess.collectAsState()
    val dietSaveSuccess by viewmodel.dietSaveSuccess.collectAsState()
    val errorMessage by viewmodel.errorMessage.collectAsState()

    // ---------- UI helpers ----------
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val ctx = LocalContext.current

    // Errores → Snackbar
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            scope.launch { snackbarHostState.showErrorOnce(errorMessage!!) }
            viewmodel.resetErrorMessage()
        }
    }

    // Éxitos → Toasts
    LaunchedEffect(goalSaveSuccess) {
        if (goalSaveSuccess == true) {
            Toast.makeText(ctx, "Objetivo y calorías actualizados", Toast.LENGTH_SHORT).show()
            viewmodel.resetGoalSaveSuccess()
        }
    }
    LaunchedEffect(dietSaveSuccess) {
        if (dietSaveSuccess == true) {
            Toast.makeText(ctx, "Tipo de dieta actualizado", Toast.LENGTH_SHORT).show()
            viewmodel.resetDietSaveSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Volver", tint = Color.Black)
                }
                Text(
                    "Gestión de objetivos",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            }

            // ================== Sección 1: Objetivo y ritmo de progreso ==================
            SectionTitle("Objetivo y ritmo de progreso")

            // Objetivo
            var objExpanded by remember { mutableStateOf(false) }
            val objectiveOptions = listOf(
                "Bajar de peso" to ObjectiveType.LOSE_WEIGHT,
                "Mantener el peso" to ObjectiveType.MAINTAIN_WEIGHT,
                "Ganar masa muscular" to ObjectiveType.GAIN_MUSCLE
            )
            // 👇 CAMBIADO: firstOrNull con fallback
            val objSelectedLabel = objectiveOptions.firstOrNull { it.second == currentObjective }?.first
                ?: "Bajar de peso"

            ExposedDropdownMenuBox(
                expanded = objExpanded,
                onExpandedChange = { objExpanded = !objExpanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = objSelectedLabel,
                    onValueChange = {},
                    label = { Text("Objetivo") },
                    trailingIcon = { TrailingIcon(objExpanded) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF0F0F0),
                        unfocusedContainerColor = Color(0xFFF0F0F0),
                        focusedIndicatorColor = JameoGreen,
                        unfocusedIndicatorColor = JameoGreen,
                        focusedLabelColor = JameoGreen,
                        unfocusedLabelColor = Color.Gray
                    )
                )
                ExposedDropdownMenu(expanded = objExpanded, onDismissRequest = { objExpanded = false }) {
                    objectiveOptions.forEach { (label, value) ->
                        DropdownMenuItem(text = { Text(label) }, onClick = {
                            viewmodel.selectObjective(value)
                            objExpanded = false
                        })
                    }
                }
            }

            // Peso objetivo
            val targetWeightError = currentTargetWeight.toDoubleOrNull()?.let { it <= 0.0 } == true
            TextField(
                value = currentTargetWeight,
                onValueChange = { s -> viewmodel.updateTargetWeightKgText(s) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Peso objetivo (kg)") },
                isError = targetWeightError,
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF0F0F0),
                    unfocusedContainerColor = Color(0xFFF0F0F0),
                    focusedIndicatorColor = JameoGreen,
                    unfocusedIndicatorColor = JameoGreen,
                    focusedLabelColor = JameoGreen,
                    unfocusedLabelColor = Color.Gray
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next)
            )
            if (targetWeightError) Assistive("Ingresa un peso válido mayor a 0 kg")

            // Ritmo
            var paceExpanded by remember { mutableStateOf(false) }
            val paceOptions = listOf(
                "Lento (0.25 kg/sem)" to PaceType.SLOW,
                "Moderado (0.5 kg/sem)" to PaceType.MODERATE,
                "Rápido (0.75 kg/sem)" to PaceType.FAST
            )
            // 👇 CAMBIADO: firstOrNull con fallback
            val paceSelectedLabel = paceOptions.firstOrNull { it.second == currentPace }?.first
                ?: "Moderado (0.5 kg/sem)"

            ExposedDropdownMenuBox(
                expanded = paceExpanded,
                onExpandedChange = { paceExpanded = !paceExpanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = paceSelectedLabel,
                    onValueChange = {},
                    label = { Text("Ritmo de progreso") },
                    trailingIcon = { TrailingIcon(paceExpanded) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF0F0F0),
                        unfocusedContainerColor = Color(0xFFF0F0F0),
                        focusedIndicatorColor = JameoGreen,
                        unfocusedIndicatorColor = JameoGreen,
                        focusedLabelColor = JameoGreen,
                        unfocusedLabelColor = Color.Gray
                    )
                )
                ExposedDropdownMenu(expanded = paceExpanded, onDismissRequest = { paceExpanded = false }) {
                    paceOptions.forEach { (label, value) ->
                        DropdownMenuItem(text = { Text(label) }, onClick = {
                            viewmodel.selectPace(value)
                            paceExpanded = false
                        })
                    }
                }
            }

            Button(
                onClick = { viewmodel.saveGoalCalories(userId) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = JameoBlue,
                    contentColor = Color.White
                )
            ) { Text("Guardar configuración de objetivo y ritmo") }

            // ================== Sección 2: Tipo de dieta ==================
            SectionTitle("Tipo de dieta")

            var dietExpanded by remember { mutableStateOf(false) }
            val dietOptions = listOf(
                "Omnívoro" to DietPreset.OMNIVORE,
                "Vegetariano" to DietPreset.VEGETARIAN,
                "Vegano" to DietPreset.VEGAN,
                "Baja en carbohidratos" to DietPreset.LOW_CARB,
                "Alta en proteínas" to DietPreset.HIGH_PROTEIN,
                "Mediterránea" to DietPreset.MEDITERRANEAN
            )
            // 👇 CAMBIADO: firstOrNull con fallback
            val dietSelectedLabel = dietOptions.firstOrNull { it.second == currentDietPreset }?.first
                ?: "Omnívoro"

            ExposedDropdownMenuBox(
                expanded = dietExpanded,
                onExpandedChange = { dietExpanded = !dietExpanded }
            ) {
                TextField(
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    readOnly = true,
                    value = dietSelectedLabel,
                    onValueChange = {},
                    label = { Text("Selecciona el tipo de dieta") },
                    trailingIcon = { TrailingIcon(dietExpanded) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF0F0F0),
                        unfocusedContainerColor = Color(0xFFF0F0F0),
                        focusedIndicatorColor = JameoGreen,
                        unfocusedIndicatorColor = JameoGreen,
                        focusedLabelColor = JameoGreen,
                        unfocusedLabelColor = Color.Gray
                    )
                )
                ExposedDropdownMenu(expanded = dietExpanded, onDismissRequest = { dietExpanded = false }) {
                    dietOptions.forEach { (label, value) ->
                        DropdownMenuItem(text = { Text(label) }, onClick = {
                            viewmodel.selectDietPreset(value)
                            dietExpanded = false
                        })
                    }
                }
            }

            // Macros sugeridos (solo lectura)
            val presetMacros = remember {
                mapOf(
                    DietPreset.OMNIVORE to Triple(30, 40, 30),
                    DietPreset.VEGETARIAN to Triple(25, 50, 25),
                    DietPreset.VEGAN to Triple(20, 55, 25),
                    DietPreset.LOW_CARB to Triple(30, 20, 50),
                    DietPreset.HIGH_PROTEIN to Triple(40, 35, 25),
                    DietPreset.MEDITERRANEAN to Triple(25, 45, 30)
                )
            }
            val (pS, cS, fS) = presetMacros[currentDietPreset] ?: Triple(0, 0, 0)

            Text(
                text = "Macros sugeridos: Proteínas $pS% · Carbohidratos $cS% · Grasas $fS%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = { viewmodel.saveDietType(userId) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = JameoBlue,
                    contentColor = Color.White
                )
            ) { Text("Guardar configuración de tipo de dieta") }
        }

        // Snackbar + Loader
        ErrorSnackbarHost(hostState = snackbarHostState)
        FullscreenLoader(visible = isLoading)
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun Assistive(text: String) {
    Text(text, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
}