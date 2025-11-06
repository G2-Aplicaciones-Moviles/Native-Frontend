package pe.edu.upc.jameofit.profile.presentation.view

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import pe.edu.upc.jameofit.goals.model.ObjectiveType
import pe.edu.upc.jameofit.profile.domain.model.UserProfileRequest
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileSetupUiState
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileSetupViewModel
import pe.edu.upc.jameofit.shared.presentation.components.FullscreenLoader

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthSetup(
    userId: Long,
    viewModel: ProfileSetupViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val pref = remember { context.getSharedPreferences("pref_profile", Context.MODE_PRIVATE) }

    val uiState by viewModel.uiState.collectAsState()

    var txtObj by remember { mutableStateOf(pref.getString("objetivo", "") ?: "") }
    var txtAct by remember { mutableStateOf(pref.getString("nivel_actividad", "") ?: "") }

    val objetivosMap = mapOf(
        "Bajar de peso" to ObjectiveType.LOSE_WEIGHT,
        "Mantener el peso" to ObjectiveType.MAINTAIN_WEIGHT,
        "Ganar masa muscular" to ObjectiveType.GAIN_MUSCLE
    )
    val objetivos = objetivosMap.keys.toList()

    val nivelesActividad = listOf(
        "Sedentario",
        "Ligeramente Activo",
        "Moderadamente Activo",
        "Muy Activo",
        "Extremadamente Activo"
    )

    // Observar el estado
    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is ProfileSetupUiState.Success -> {
                Toast.makeText(context, "¡Perfil creado exitosamente!", Toast.LENGTH_SHORT).show()
                viewModel.reset()
                onNext()
            }
            is ProfileSetupUiState.Error -> {
                Toast.makeText(context, "Error: ${state.message}", Toast.LENGTH_LONG).show()
                viewModel.reset()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Completa tu información de salud",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // DROPDOWN OBJETIVO
            var expandedObj by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedObj,
                onExpandedChange = { expandedObj = !expandedObj }
            ) {
                OutlinedTextField(
                    value = txtObj,
                    onValueChange = {},
                    label = { Text("Objetivo") },
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedObj,
                    onDismissRequest = { expandedObj = false }
                ) {
                    objetivos.forEach { objetivo ->
                        DropdownMenuItem(
                            text = { Text(objetivo) },
                            onClick = {
                                txtObj = objetivo
                                expandedObj = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // DROPDOWN NIVEL DE ACTIVIDAD
            var expandedAct by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expandedAct,
                onExpandedChange = { expandedAct = !expandedAct }
            ) {
                OutlinedTextField(
                    value = txtAct,
                    onValueChange = {},
                    label = { Text("Nivel de actividad") },
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandedAct,
                    onDismissRequest = { expandedAct = false }
                ) {
                    nivelesActividad.forEach { nivel ->
                        DropdownMenuItem(
                            text = { Text(nivel) },
                            onClick = {
                                txtAct = nivel
                                expandedAct = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ElevatedButton(
                onClick = {
                    if (txtObj.isBlank() || txtAct.isBlank()) {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                        return@ElevatedButton
                    }

                    // Guardar objetivo y nivel
                    pref.edit {
                        putString("objetivo", txtObj)
                        putString("nivel_actividad", txtAct)
                    }

                    // Leer todos los datos guardados
                    val height = pref.getString("height", "1.70")?.toDoubleOrNull() ?: 1.70
                    val weight = pref.getString("peso", "70")?.toDoubleOrNull() ?: 70.0
                    val birthDate = pref.getString("birthDate", "2000-01-01") ?: "2000-01-01"
                    val gender = pref.getString("gender", "MALE") ?: "MALE"

                    val selectedObjectiveId = when (objetivosMap[txtObj]) {
                        ObjectiveType.LOSE_WEIGHT -> 1
                        ObjectiveType.MAINTAIN_WEIGHT -> 2
                        ObjectiveType.GAIN_MUSCLE -> 3
                        else -> 2
                    }

                    val selectedActivityId = (nivelesActividad.indexOf(txtAct).takeIf { it >= 0 }?.plus(1)) ?: 1

                    // Crear el request completo
                    val request = UserProfileRequest(
                        userId = userId,
                        gender = gender,
                        height = height,
                        weight = weight,
                        userScore = 30,
                        activityLevelId = selectedActivityId,
                        objectiveId = selectedObjectiveId,
                        allergyIds = emptyList(),
                        birthDate = birthDate
                    )

                    // Llamar al ViewModel para crear todo
                    viewModel.createCompleteProfile(request, context)
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = uiState !is ProfileSetupUiState.Loading
            ) {
                Text(if (uiState is ProfileSetupUiState.Loading) "Creando perfil..." else "Continuar")
            }
        }

        FullscreenLoader(visible = uiState is ProfileSetupUiState.Loading)
    }
}