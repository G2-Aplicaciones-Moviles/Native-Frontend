package pe.edu.upc.jameofit.mealplan.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel
import pe.edu.upc.jameofit.profile.domain.model.UserProfileResponse
import pe.edu.upc.jameofit.ui.theme.JameoGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanCreateScreen(
    profile: UserProfileResponse,
    userId: Long,
    viewModel: MealPlanViewModel,
    onMealPlanCreated: () -> Unit,
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var isCurrent by remember { mutableStateOf(true) }
    var tagsText by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            // ------- HEADER MODERNO PREMIUM (igual estilo RecipeCreate & Tracking)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                JameoGreen,
                                JameoGreen.copy(alpha = 0.85f),
                                JameoGreen.copy(alpha = 0.65f),
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                    Text(
                        "Nuevo Plan Alimenticio",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // --------- TARJETA PRINCIPAL GLASS STYLE
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.92f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {

                Column(
                    modifier = Modifier
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    Text(
                        "Detalles del Plan",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = JameoGreen
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nombre del plan") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColorsPremium()
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColorsPremium()
                    )

                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Categoría (Ej: Definición, Mantenimiento)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColorsPremium()
                    )

                    OutlinedTextField(
                        value = tagsText,
                        onValueChange = { tagsText = it },
                        label = { Text("Tags (separa con comas)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = textFieldColorsPremium()
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Checkbox(
                            checked = isCurrent,
                            onCheckedChange = { isCurrent = it },
                            colors = CheckboxDefaults.colors(checkedColor = JameoGreen)
                        )
                        Text("Marcar como plan actual")
                    }
                }
            }

            // ---------- BOTÓN PREMIUM
            Button(
                onClick = {
                    scope.launch {
                        try {
                            viewModel.createMealPlan(
                                name = name,
                                description = description,
                                calories = 0.1,
                                carbs = 0.1,
                                proteins = 0.1,
                                fats = 0.1,
                                userId = userId,
                                profileId = profile.id,
                                category = category,
                                isCurrent = isCurrent,
                                tags = tagsText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            )
                            onMealPlanCreated()
                        } catch (_: Exception) { }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = JameoGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text("Crear Plan", fontWeight = FontWeight.Bold)
                }
            }

            if (error != null) {
                Text(
                    text = error ?: "",
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// ---------- COLORS PREMIUM PARA TEXTFIELDS
@Composable
private fun textFieldColorsPremium() = TextFieldDefaults.colors(
    focusedIndicatorColor = JameoGreen,
    unfocusedIndicatorColor = JameoGreen.copy(alpha = 0.5f),
    focusedLabelColor = JameoGreen,
    unfocusedLabelColor = Color.Gray,
    focusedContainerColor = Color(0xFFF3F6F4),
    unfocusedContainerColor = Color(0xFFF3F6F4),
)

