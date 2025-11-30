package pe.edu.upc.jameofit.mealplan.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.home.presentation.navigation.DrawerRoute
import pe.edu.upc.jameofit.home.presentation.navigation.RecipeRoute
import pe.edu.upc.jameofit.mealplan.data.model.MealPlanResponse
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileUiState
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileViewModel

data class RecipeCategory(
    val id: Long,
    val title: String,
    val imageRes: Int,
    val route: String
)

@Composable
fun MealPlanScreen(
    navController: NavHostController,
    viewModel: MealPlanViewModel,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val mealPlans by viewModel.mealPlans.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    val profileUiState by profileViewModel.uiState.collectAsState()

    LaunchedEffect(profileUiState) {
        when (val state = profileUiState) {
            is ProfileUiState.Success -> {
                viewModel.loadMealPlansByProfile(state.profile.id)
            }
            else -> Unit
        }
    }

    if (profileUiState is ProfileUiState.Idle || profileUiState is ProfileUiState.Loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Esperando datos del perfil...", color = Color.Gray)
        }
        return
    }

    val categories = listOf(
        // ID 1: Desayuno
        RecipeCategory(
            id = 1L,
            title = "Desayunos",
            imageRes = R.drawable.desayuno,
            route = "recipe_list/1/Desayunos"
        ),
        // ID 2: Almuerzo
        RecipeCategory(
            id = 2L,
            title = "Almuerzos",
            imageRes = R.drawable.almuerzo,
            route = "recipe_list/2/Almuerzos"
        ),
        // ID 3: Cena
        RecipeCategory(
            id = 3L,
            title = "Cenas",
            imageRes = R.drawable.cena,
            route = "recipe_list/3/Cenas"
        ),
        RecipeCategory(
            id = 4L,
            title = "Snacks",
            imageRes = R.drawable.desayuno,
            route = "recipe_list/4/Snacks"
        ),
        RecipeCategory(
            id = 5L,
            title = "Postres",
            imageRes = R.drawable.desayuno,
            route = "recipe_list/5/Postres"
        )
    )

// ...

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(categories) { cat ->
            Card(
                modifier = Modifier
                    // ...
                    .clickable { navController.navigate(cat.route) }, // cat.route = "recipe_list/1/Desayunos"
                // ...
            ) { /* ... */ }
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        item {
            Text(
                text = "Recetas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = "Tap para ver detalle",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(categories) { cat ->
                    Card(
                        modifier = Modifier
                            .width(160.dp)
                            .height(120.dp)
                            .clickable { navController.navigate(cat.route) },
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Image(
                                painter = painterResource(id = cat.imageRes),
                                contentDescription = cat.title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(80.dp)
                            )
                            Text(
                                text = cat.title,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Visualizar recetas creadas por nutricionistas",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = { navController.navigate(DrawerRoute.RECIPE_TEMPLATES) }, // 🆕 Usaremos una nueva ruta: RECIPE_TEMPLATES
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3),
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    "Ver Recetas de Expertos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(24.dp))

            // ✅ NUEVO: Card destacada para Templates de Nutricionistas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(DrawerRoute.TEMPLATES) },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2196F3)
                ),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "🥗 Planes de Expertos",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Descubre planes creados por nutricionistas profesionales",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Templates",
                        modifier = Modifier.size(40.dp),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Tus Meal Plans",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Button(
                onClick = { navController.navigate(DrawerRoute.MEAL_PLAN_CREATE) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    "Crear nuevo MealPlan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))
        }

        if (isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF4CAF50))
                }
            }
        } else if (error != null) {
            item {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else if (mealPlans.isEmpty()) {
            item {
                Text(
                    text = "Aún no tienes MealPlans creados.",
                    color = Color.Gray,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else {
            items(mealPlans) { plan ->
                MealPlanCard(plan = plan, onClick = {
                    navController.navigate("drawer/meal_plan_detail/${plan.id}")
                })
            }
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
fun MealPlanCard(plan: MealPlanResponse, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.menu),
                contentDescription = "MealPlan",
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 12.dp)
            )
            Column {
                Text(
                    text = plan.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = plan.tags.joinToString(", "),
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}
