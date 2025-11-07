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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.home.presentation.navigation.RecipeRoute
import pe.edu.upc.jameofit.mealplan.data.model.MealPlanResponse
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel

data class RecipeCategory(
    val title: String,
    val imageRes: Int,
    val route: String
)

@Composable
fun MealPlanScreen(
    navController: NavHostController,
    viewModel: MealPlanViewModel,
    modifier: Modifier = Modifier
) {
    val mealPlans by viewModel.mealPlans.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadMealPlans()
    }

    val categories = listOf(
        RecipeCategory("Desayunos", R.drawable.desayuno, RecipeRoute.BREAKFAST),
        RecipeCategory("Almuerzos", R.drawable.almuerzo, RecipeRoute.LUNCH),
        RecipeCategory("Cenas", R.drawable.cena, RecipeRoute.DINNER)
    )

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
                text = "Tus Meal Plans",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Button(
                onClick = { navController.navigate(pe.edu.upc.jameofit.home.presentation.navigation.DrawerRoute.MEAL_PLAN_CREATE) },
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
                    "➕ Crear nuevo MealPlan",
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
