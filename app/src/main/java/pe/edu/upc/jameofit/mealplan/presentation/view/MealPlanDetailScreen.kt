package pe.edu.upc.jameofit.mealplan.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel
import pe.edu.upc.jameofit.ui.theme.JameoBlue

@Composable
fun MealPlanDetailScreen(
    mealPlanId: Long,
    viewModel: MealPlanViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val mealPlan by viewModel.selectedMealPlan.collectAsState()
    val entries by viewModel.entries.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val authUser by authViewModel.user.collectAsState()

    LaunchedEffect(authUser.id) {
        if (authUser.id > 0) viewModel.setUserId(authUser.id)
    }

    LaunchedEffect(mealPlanId) {
        viewModel.loadMealPlanById(mealPlanId)
        viewModel.loadEntries(mealPlanId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6FB))
            .padding(16.dp)
    ) {
        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

            error != null -> Text(
                text = error ?: "",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )

            mealPlan == null -> Text(
                text = "Meal plan no encontrado.",
                modifier = Modifier.align(Alignment.Center)
            )

            else -> mealPlan?.let { plan ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // -------------------------
                    //   🟦 HEADER DEL MEALPLAN
                    // -------------------------
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Column(
                                Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = plan.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    text = plan.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Gray
                                )
                                Spacer(Modifier.height(12.dp))

                                Column(modifier = Modifier.fillMaxWidth()) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("Calorías", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                            Text("${mealPlan?.calories} kcal", fontSize = 15.sp)
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("Carbs", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                            Text("${mealPlan?.carbs} g", fontSize = 15.sp)
                                        }

                                    }

                                }

                                Column(modifier = Modifier.fillMaxWidth()) {

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("Proteínas", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                            Text("${mealPlan?.proteins} g", fontSize = 15.sp)
                                        }

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("Grasas", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                            Text("${mealPlan?.fats} g", fontSize = 15.sp)
                                        }
                                    }


                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Recetas del plan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (entries.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        "Aún no tienes recetas agregadas",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        "Agrega recetas para completar tu plan",
                                        color = Color.Gray,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    } else {
                        items(entries) { entry ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Column(Modifier.padding(16.dp)) {
                                    Text(
                                        text = entry.recipeName ?: "Receta sin nombre",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "Día: ${entry.day}",
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate("drawer/add_recipe_to_meal_plan/$mealPlanId")
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = JameoBlue,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Agregar receta")
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Button(
                                onClick = {
                                    viewModel.deleteMealPlanWithTracking(
                                        mealPlanId = mealPlanId,
                                        userId = authUser.id,
                                        onSuccess = { navController.popBackStack() },
                                        onError = {}
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = JameoBlue,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }
}
