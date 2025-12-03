package pe.edu.upc.jameofit.nutritionists.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.jameofit.nutritionists.data.model.NutritionistResponse
import pe.edu.upc.jameofit.nutritionists.presentation.viewmodel.NutritionistViewModel
import pe.edu.upc.jameofit.ui.theme.JameoGreen
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

        // -------------------------
        // HEADER MODERNO PREMIUM
        // -------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            JameoGreen,
                            JameoGreen.copy(alpha = 0.85f),
                            JameoGreen.copy(alpha = 0.7f),
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(24.dp)
            ) {
                Text(
                    "Nutricionistas",
                    fontSize = 32.sp,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    "Encuentra tu especialista ideal",
                    color = Color.White.copy(alpha = 0.85f)
                )
            }
        }

        // -------------------------
        // LISTA SOBRE TARJETA GLASS
        // -------------------------
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 140.dp)
                .background(Color(0xFFF5F7F6))
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    Color.White.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(Modifier.padding(20.dp)) {

                    if (loading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = JameoGreen)
                        }
                    }
                    else if (list.isEmpty()) {
                        Text(
                            "No hay nutricionistas disponibles",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(20.dp),
                            color = Color.Gray
                        )
                    }
                    else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(list) { nut ->
                                NutritionistCardPremium(
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
            }
        }

        // -------------------------
        // SNACKBAR
        // -------------------------
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

    // -------------------------
    // DIALOGS LOGIC
    // -------------------------
    if (showServiceDialog && selectedNutritionist != null) {
        ServiceSelectionDialogPremium(
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

    if (showDatePicker && selectedNutritionist != null && selectedServiceType != null) {
        DatePickerDialogPremium(
            onDismiss = {
                showDatePicker = false
                selectedNutritionist = null
                selectedServiceType = null
            },
            onDateSelected = { dateString ->
                when (selectedServiceType) {
                    "DIET_PLAN" -> viewModel.sendContact(
                        patientUserId,
                        selectedNutritionist!!.id,
                        "DIET_PLAN",
                        dateString,
                        null
                    )
                    "PERSONAL_CONSULT" -> viewModel.sendContact(
                        patientUserId,
                        selectedNutritionist!!.id,
                        "PERSONAL_CONSULT",
                        null,
                        dateString
                    )
                }
                showDatePicker = false
            },
            serviceType = selectedServiceType!!
        )
    }
}

@Composable
fun NutritionistCardPremium(
    nutritionist: NutritionistResponse,
    onContactClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            Modifier
                .padding(18.dp)
                .fillMaxWidth()
        ) {

            // Nombre grande y moderno
            Text(
                nutritionist.fullName ?: "Nutricionista",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = JameoGreen
            )

            Spacer(Modifier.height(6.dp))

            nutritionist.speciality?.let {
                InfoRowPremium("Especialidad", it)
            }
            nutritionist.experiencesYears?.let { years ->
                if (years > 0) InfoRowPremium("Experiencia", "$years años")
            }
            nutritionist.licenseNumber?.let {
                InfoRowPremium("Licencia", it, valueColor = JameoGreen)
            }

            if (!nutritionist.bio.isNullOrBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    nutritionist.bio,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(12.dp))

            // Botón mejorado
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = onContactClick,
                    enabled = nutritionist.acceptingNewPatients == true,
                    colors = ButtonDefaults.buttonColors(containerColor = JameoGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        if (nutritionist.acceptingNewPatients == true) "Contactar"
                        else "No Disponible",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRowPremium(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label + ":", fontWeight = FontWeight.Medium, color = Color.DarkGray)
        Text(value, fontWeight = FontWeight.Bold, color = valueColor)
    }
    Spacer(Modifier.height(4.dp))
}

@Composable
fun ServiceSelectionDialogPremium(
    onDismiss: () -> Unit,
    onServiceSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Selecciona un servicio", fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

                PremiumServiceOption(
                    title = "Plan de Dieta",
                    description = "Plan personalizado según tus objetivos",
                    onClick = { onServiceSelected("DIET_PLAN") }
                )

                PremiumServiceOption(
                    title = "Consulta Personal",
                    description = "Agenda una cita con el especialista",
                    onClick = { onServiceSelected("PERSONAL_CONSULT") }
                )
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}

@Composable
fun PremiumServiceOption(title: String, description: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color(0xFFF9FBFA)),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = onClick
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = JameoGreen)
            Spacer(Modifier.height(4.dp))
            Text(description, color = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogPremium(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit,
    serviceType: String
) {
    val state = rememberDatePickerState()
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (serviceType == "DIET_PLAN") "Fecha de inicio"
                else "Fecha de consulta",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            DatePicker(state = state)
        },
        confirmButton = {
            TextButton(onClick = {
                val millis = state.selectedDateMillis ?: System.currentTimeMillis()
                onDateSelected(formatter.format(Date(millis)))
            }) {
                Text("Confirmar", color = JameoGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

