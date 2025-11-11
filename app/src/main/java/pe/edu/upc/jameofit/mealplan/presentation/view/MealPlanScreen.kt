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
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.home.presentation.navigation.RecipeRoute
import pe.edu.upc.jameofit.mealplan.presentation.di.PresentationModule
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel

data class DayMenu(
    val day: String, val breakfast: String, val lunch: String, val dinner: String
)


@Composable
fun MealPlanScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: MealPlanViewModel = remember { PresentationModule.getMealPlanViewModel() }
) {
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) { viewModel.getAllCategories() }

    val categories by viewModel.categories.collectAsState()

    val uiCategories: List<RecipeCategory> =
        if (categories.isNotEmpty()) categories.map { it.toRecipeCategory() }
        else localDefaultCategories()

    val weekMenus = remember {
        listOf(
            DayMenu("Lunes", "Receta 1/Desayuno", "Receta 3/Almuerzo", "Receta 2/Cena"),
            DayMenu("Martes", "Receta 2/Desayuno", "Receta 1/Almuerzo", "Receta 3/Cena"),
            DayMenu("Miércoles", "Receta 10/Desayuno", "Receta 5/Almuerzo", "Receta 7/Cena")
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        item {
            Text(
                "Recetas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                "Tap para ver detalle",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(uiCategories) { cat ->
                    Card(
                        modifier = Modifier
                            .width(160.dp)
                            .height(120.dp)
                            .clickable { navController.navigate(RecipeRoute.recipeList(categoryId = cat.id, title = cat.title)) },
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
                                cat.title,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }

            if (categories.isEmpty()) {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(onClick = { scope.launch { viewModel.getAllCategories() } }) {
                    Text("Intentar cargar categorías")
                }
            }

            Spacer(Modifier.height(20.dp))
            Text(
                "Menú de la semana",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(weekMenus) { menu ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Menu",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(end = 12.dp)
                    )
                    Column {
                        Text("${menu.day} Menu", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(menu.breakfast, fontSize = 14.sp, color = Color.DarkGray)
                        Text(menu.lunch, fontSize = 14.sp, color = Color.DarkGray)
                        Text(menu.dinner, fontSize = 14.sp, color = Color.DarkGray)
                    }
                }
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
            OutlinedButton(
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium
            ) { Text("Reiniciar Plan", fontSize = 16.sp, fontWeight = FontWeight.Bold) }
            Spacer(Modifier.height(80.dp))
        }
    }
}
