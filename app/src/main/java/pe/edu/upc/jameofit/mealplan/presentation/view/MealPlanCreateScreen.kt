package pe.edu.upc.jameofit.mealplan.presentation.view

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel
import pe.edu.upc.jameofit.profile.domain.model.UserProfileResponse
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Scaffold
import androidx.navigation.NavController
import androidx.compose.foundation.background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanCreateScreen(
    profile: UserProfileResponse,
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
            TopAppBar(
                title = { Text("Crear Plan Alimenticio", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
                .padding(paddingValues)
                .padding(top = 16.dp), // Espaciado extra entre la barra verde y el primer input
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del plan") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoría (Ej. Definición, Mantenimiento)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = tagsText,
                onValueChange = { tagsText = it },
                label = { Text("Tags (separa con comas)") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Checkbox(
                    checked = isCurrent,
                    onCheckedChange = { isCurrent = it }
                )
                Text("Marcar como plan actual")
            }

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
                                profileId = profile.id,
                                category = category,
                                isCurrent = isCurrent,
                                tags = tagsText.split(",")
                                    .map { it.trim() }
                                    .filter { it.isNotEmpty() }
                            )
                            onMealPlanCreated()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3), // Celeste igual que otras vistas
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Text("Crear Plan", style = MaterialTheme.typography.titleMedium)
                }
            }

            if (error != null) {
                Text(
                    text = error ?: "",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
