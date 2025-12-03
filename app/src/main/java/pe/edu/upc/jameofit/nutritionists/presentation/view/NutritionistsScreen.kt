package pe.edu.upc.jameofit.nutritionists.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import pe.edu.upc.jameofit.nutritionists.data.model.NutritionistResponse
import pe.edu.upc.jameofit.nutritionists.presentation.viewmodel.NutritionistViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NutritionistsScreen(
    viewModel: NutritionistViewModel,
    patientUserId: Long
) {
    val list by viewModel.list.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val message by viewModel.message.collectAsState()

    var showServiceDialog by remember { mutableStateOf(false) }
    var selectedNutritionist by remember { mutableStateOf<NutritionistResponse?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedServiceType by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadNutritionists()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Nutricionistas Disponibles",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (list.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No hay nutricionistas disponibles",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(list) { nut ->
                        NutritionistCard(
                            nutritionist = nut,
                            onContactClick = {
                                selectedNutritionist = nut
                                showServiceDialog = true
                            }
                        )
                    }
                }
            }
        }

        // Snackbar para mensajes
        message?.let { msg ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { viewModel.clearMessage() }) {
                        Text("OK")
                    }
                }
            ) {
                Text(msg)
            }
        }
    }

    // Diálogo para seleccionar tipo de servicio
    if (showServiceDialog && selectedNutritionist != null) {
        ServiceSelectionDialog(
            onDismiss = {
                showServiceDialog = false
                selectedNutritionist = null
            },
            onServiceSelected = { serviceType ->
                selectedServiceType = serviceType
                showServiceDialog = false
                showDatePicker = true
            }
        )
    }

    // Diálogo para seleccionar fecha
    if (showDatePicker && selectedNutritionist != null && selectedServiceType != null) {
        DatePickerDialog(
            onDismiss = {
                showDatePicker = false
                selectedNutritionist = null
                selectedServiceType = null
            },
            onDateSelected = { dateString ->
                when (selectedServiceType) {
                    "DIET_PLAN" -> {
                        viewModel.sendContact(
                            patientUserId = patientUserId,
                            nutritionistId = selectedNutritionist!!.id,
                            serviceType = "DIET_PLAN",
                            startDate = dateString,
                            scheduledAt = null
                        )
                    }
                    "PERSONAL_CONSULT" -> {
                        viewModel.sendContact(
                            patientUserId = patientUserId,
                            nutritionistId = selectedNutritionist!!.id,
                            serviceType = "PERSONAL_CONSULT",
                            startDate = null,
                            scheduledAt = dateString
                        )
                    }
                }
                showDatePicker = false
                selectedNutritionist = null
                selectedServiceType = null
            },
            serviceType = selectedServiceType!!
        )
    }
}

@Composable
fun NutritionistCard(
    nutritionist: NutritionistResponse,
    onContactClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                nutritionist.fullName ?: "Nutricionista",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(Modifier.height(8.dp))

            if (nutritionist.speciality != null) {
                InfoRow(label = "Especialidad", value = nutritionist.speciality)
            }

            if (nutritionist.experiencesYears != null && nutritionist.experiencesYears > 0) {
                InfoRow(label = "Experiencia", value = "${nutritionist.experiencesYears} años")
            }

            if (nutritionist.licenseNumber != null) {
                InfoRow(
                    label = "Licencia",
                    value = nutritionist.licenseNumber,
                    valueColor = MaterialTheme.colorScheme.secondary
                )
            }

            if (!nutritionist.bio.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    nutritionist.bio,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onContactClick,
                    enabled = nutritionist.acceptingNewPatients == true
                ) {
                    Text(
                        if (nutritionist.acceptingNewPatients == true)
                            "Contactar"
                        else
                            "No Disponible"
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor
        )
    }
    Spacer(Modifier.height(4.dp))
}

@Composable
fun ServiceSelectionDialog(
    onDismiss: () -> Unit,
    onServiceSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Selecciona el tipo de servicio") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "¿Qué tipo de servicio deseas solicitar?",
                    style = MaterialTheme.typography.bodyMedium
                )

                ServiceOptionButton(
                    title = "Plan de Dieta",
                    description = "Recibe un plan nutricional personalizado",
                    onClick = { onServiceSelected("DIET_PLAN") }
                )

                ServiceOptionButton(
                    title = "Consulta Personal",
                    description = "Agenda una consulta individual",
                    onClick = { onServiceSelected("PERSONAL_CONSULT") }
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun ServiceOptionButton(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(
                description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit,
    serviceType: String
) {
    val datePickerState = rememberDatePickerState()
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (serviceType == "DIET_PLAN")
                    "Fecha de inicio del plan"
                else
                    "Fecha de la consulta"
            )
        },
        text = {
            DatePicker(
                state = datePickerState,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Date(millis)
                        val formattedDate = dateFormatter.format(date)
                        onDateSelected(formattedDate)
                    } ?: run {
                        // Si no hay fecha seleccionada, usar la fecha actual
                        val formattedDate = dateFormatter.format(Date())
                        onDateSelected(formattedDate)
                    }
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}