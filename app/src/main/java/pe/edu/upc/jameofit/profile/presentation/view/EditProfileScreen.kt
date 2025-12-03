package pe.edu.upc.jameofit.profile.presentation.view

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.jameofit.profile.domain.model.UserProfileRequest
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileUiState
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileViewModel
import androidx.compose.foundation.text.KeyboardOptions

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    profileId: Long,
    viewModel: ProfileViewModel,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(profileId) {
        viewModel.getProfileById(profileId)
    }

    var weight by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var activityLevel by remember { mutableStateOf(1) }
    var objective by remember { mutableStateOf(1) }
    var gender by remember { mutableStateOf("MALE") }
    var birthDate by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf(0L) }

    LaunchedEffect(uiState) {
        if (uiState is ProfileUiState.Success) {
            val profile = (uiState as ProfileUiState.Success).profile
            weight = profile.weight.toString()
            height = profile.height.toString()
            activityLevel = profile.activityLevelId
            objective = profile.objectiveId
            gender = profile.gender
            birthDate = profile.birthDate
            userId = profile.userId   // ✅ AHORA SÍ: ID DEL USUARIO (IAM), NO EL PROFILE ID
        }
    }

    val objectives = mapOf(
        1 to "Bajar de peso",
        2 to "Mantener peso",
        3 to "Ganar masa muscular"
    )

    val activityLevels = mapOf(
        1 to "Sedentario",
        2 to "Ligeramente Activo",
        3 to "Moderadamente Activo",
        4 to "Muy Activo",
        5 to "Extremadamente Activo"
    )

    val genders = listOf("MALE", "FEMALE", "OTHER")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Preferencias") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is ProfileUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is ProfileUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = (uiState as ProfileUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { viewModel.getProfileById(profileId) }) {
                            Text("Reintentar")
                        }
                    }
                }

                is ProfileUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        // Peso
                        OutlinedTextField(
                            value = weight,
                            onValueChange = { weight = it.filter { c -> c.isDigit() || c == '.' } },
                            label = { Text("Peso (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(16.dp))

                        // Altura
                        OutlinedTextField(
                            value = height,
                            onValueChange = { height = it.filter { c -> c.isDigit() || c == '.' } },
                            label = { Text("Altura (m)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(16.dp))

                        // Género
                        var expandedGender by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expandedGender,
                            onExpandedChange = { expandedGender = !expandedGender }
                        ) {
                            OutlinedTextField(
                                value = gender,
                                onValueChange = {},
                                label = { Text("Género") },
                                readOnly = true,
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedGender,
                                onDismissRequest = { expandedGender = false }
                            ) {
                                genders.forEach { g ->
                                    DropdownMenuItem(
                                        text = { Text(g) },
                                        onClick = {
                                            gender = g
                                            expandedGender = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Objetivo
                        var expandedObjective by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expandedObjective,
                            onExpandedChange = { expandedObjective = !expandedObjective }
                        ) {
                            OutlinedTextField(
                                value = objectives[objective] ?: "",
                                onValueChange = {},
                                label = { Text("Objetivo") },
                                readOnly = true,
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedObjective,
                                onDismissRequest = { expandedObjective = false }
                            ) {
                                objectives.forEach { (id, name) ->
                                    DropdownMenuItem(
                                        text = { Text(name) },
                                        onClick = {
                                            objective = id
                                            expandedObjective = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // Nivel de actividad
                        var expandedActivity by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = expandedActivity,
                            onExpandedChange = { expandedActivity = !expandedActivity }
                        ) {
                            OutlinedTextField(
                                value = activityLevels[activityLevel] ?: "",
                                onValueChange = {},
                                label = { Text("Nivel de actividad") },
                                readOnly = true,
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedActivity,
                                onDismissRequest = { expandedActivity = false }
                            ) {
                                activityLevels.forEach { (id, name) ->
                                    DropdownMenuItem(
                                        text = { Text(name) },
                                        onClick = {
                                            activityLevel = id
                                            expandedActivity = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(32.dp))

                        // Botón guardar
                        Button(
                            onClick = {
                                val weightValue = weight.toDoubleOrNull()
                                val heightValue = height.toDoubleOrNull()

                                if (weightValue == null || heightValue == null) {
                                    Toast.makeText(context, "Ingresa valores válidos", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                val request = UserProfileRequest(
                                    userId = userId,              // ✅ ahora es el IAM id correcto
                                    gender = gender,
                                    height = heightValue,
                                    weight = weightValue,
                                    userScore = 30,
                                    activityLevelId = activityLevel,
                                    objectiveId = objective,
                                    allergyIds = emptyList(),
                                    birthDate = birthDate
                                )

                                viewModel.updateProfileAndRecalculate(
                                    profileId = profileId,        // ✅ este sigue siendo el id del perfil
                                    request = request,
                                    onSuccess = {
                                        Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_SHORT).show()
                                        onSaved()
                                    },
                                    onError = { msg ->
                                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                                    }
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = uiState !is ProfileUiState.Loading
                        ) {
                            Text(
                                if (uiState is ProfileUiState.Loading) "Guardando..." else "Guardar Cambios",
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                else -> {}
            }
        }
    }
}
