package pe.edu.upc.jameofit.profile.presentation.view

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.jameofit.goals.model.ObjectiveType
import pe.edu.upc.jameofit.goals.model.PaceType
import pe.edu.upc.jameofit.goals.presentation.viewmodel.GoalsViewModel
import pe.edu.upc.jameofit.shared.presentation.components.FullscreenLoader

@Composable
fun HealthSetup(
    userId: Long,
    viewModel: GoalsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    val pref: SharedPreferences = remember {
        context.getSharedPreferences("pref_health_profile", Context.MODE_PRIVATE)
    }

    var txtObj by remember { mutableStateOf("") }
    var txtAct by remember { mutableStateOf("") }
    var expandedObj by remember { mutableStateOf(false) }
    var expandedAct by remember { mutableStateOf(false) }

    // Observar estados del ViewModel
    val isLoading by viewModel.isLoading.collectAsState()
    val goalSaveSuccess by viewModel.goalSaveSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Mapeo de objetivos UI -> Domain (IGUAL que en GoalsManagement)
    val objectiveMap = mapOf(
        "Bajar de peso" to ObjectiveType.LOSE_WEIGHT,
        "Mantener el peso" to ObjectiveType.MAINTAIN_WEIGHT,
        "Ganar masa muscular" to ObjectiveType.GAIN_MUSCLE
    )

    val objetivos = listOf(
        "Bajar de peso",
        "Mantener el peso",
        "Ganar masa muscular"
    )

    val nivelesActividad = listOf(
        "Sedentario",
        "Ligeramente Activo",
        "Moderadamente Activo",
        "Muy Activo",
        "Extremadamente Activo"
    )

    // Manejar éxito del guardado
    LaunchedEffect(goalSaveSuccess) {
        if (goalSaveSuccess == true) {
            Toast.makeText(context, "Perfil guardado exitosamente", Toast.LENGTH_SHORT).show()
            viewModel.resetGoalSaveSuccess()
            onNext()
        }
    }

    // Manejar errores
    LaunchedEffect(errorMessage) {
        if (!errorMessage.isNullOrBlank()) {
            Toast.makeText(context, "Error: $errorMessage", Toast.LENGTH_LONG).show()
            viewModel.resetErrorMessage()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Regresar",
                tint = Color.Black
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 30.dp)
                .padding(top = 20.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Completa tu información para recibir sugerencias personalizadas",
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )

            // Dropdown de Objetivo
            Box {
                OutlinedTextField(
                    value = txtObj,
                    onValueChange = { },
                    modifier = Modifier.padding(vertical = 8.dp),
                    label = { Text(text = "Objetivo") },
                    placeholder = { Text(text = "Selecciona tu objetivo") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            tint = Color.Gray,
                            contentDescription = "Icono de objetivo"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { expandedObj = !expandedObj }) {
                            Icon(
                                imageVector = if (expandedObj) Icons.Default.KeyboardArrowUp
                                else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Dropdown arrow",
                                tint = Color.Gray
                            )
                        }
                    },
                    readOnly = true,
                    singleLine = true
                )

                DropdownMenu(
                    expanded = expandedObj,
                    onDismissRequest = { expandedObj = false }
                ) {
                    objetivos.forEach { objetivo ->
                        DropdownMenuItem(
                            text = { Text(text = objetivo, fontSize = 16.sp) },
                            onClick = {
                                txtObj = objetivo
                                expandedObj = false
                                // Actualizar el ViewModel
                                objectiveMap[objetivo]?.let {
                                    viewModel.selectObjective(it)
                                }
                            }
                        )
                    }
                }
            }

            // Dropdown de Nivel de Actividad
            Box {
                OutlinedTextField(
                    value = txtAct,
                    onValueChange = { },
                    modifier = Modifier.padding(vertical = 8.dp),
                    label = { Text(text = "Nivel de Actividad") },
                    placeholder = { Text(text = "Selecciona tu nivel de actividad") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            tint = Color.Gray,
                            contentDescription = "Icono de actividad"
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { expandedAct = !expandedAct }) {
                            Icon(
                                imageVector = if (expandedAct) Icons.Default.KeyboardArrowUp
                                else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Dropdown arrow",
                                tint = Color.Gray
                            )
                        }
                    },
                    readOnly = true,
                    singleLine = true
                )

                DropdownMenu(
                    expanded = expandedAct,
                    onDismissRequest = { expandedAct = false }
                ) {
                    nivelesActividad.forEach { nivel ->
                        DropdownMenuItem(
                            text = { Text(text = nivel, fontSize = 16.sp) },
                            onClick = {
                                txtAct = nivel
                                expandedAct = false
                            }
                        )
                    }
                }
            }

            Text(
                text = "Recuerda que puedes editar tu perfil más adelante",
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp)
            )

            ElevatedButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF099FE1)
                ),
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .height(48.dp),
                enabled = !isLoading,
                onClick = {
                    if (txtObj.trim().isNotEmpty() && txtAct.trim().isNotEmpty()) {
                        // Guardar en SharedPreferences (para mantener compatibilidad)
                        pref.edit()
                            .putString("objetivo", txtObj.trim())
                            .putString("nivel_actividad", txtAct.trim())
                            .putLong("health_profile_updated", System.currentTimeMillis())
                            .putBoolean("health_profile_completed", true)
                            .apply()

                        // ✅ GUARDAR CON PARÁMETROS OVERRIDE
                        viewModel.saveGoalCalories(
                            userId = userId,
                            overrideWeight = 70.0,
                            overridePace = PaceType.MODERATE
                        )
                    } else {
                        Toast.makeText(
                            context,
                            "Por favor completa todos los campos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text(
                    text = if (isLoading) "Guardando..." else "Continuar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // Loader
        FullscreenLoader(visible = isLoading)
    }
}