package pe.edu.upc.jameofit.mealplan.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

data class RecipeCategory(
    val title: String,
    val imageRes: Int,
    val route: String
)

data class DayMenu(
    val day: String,
    val breakfast: String,
    val lunch: String,
    val dinner: String
)

@Composable
fun MealPlanScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val categories = listOf(
        RecipeCategory("Desayunos", R.drawable.desayuno, RecipeRoute.BREAKFAST),
        RecipeCategory("Almuerzos", R.drawable.almuerzo, RecipeRoute.LUNCH),
        RecipeCategory("Cenas", R.drawable.cena, RecipeRoute.DINNER)
    )

    val weekMenus = listOf(
        DayMenu("Lunes", "Receta 1/Desayuno", "Receta 3/Almuerzo", "Receta 2/Cena"),
        DayMenu("Martes", "Receta 2/Desayuno", "Receta 1/Almuerzo", "Receta 3/Cena"),
        DayMenu("Miércoles", "Receta 10/Desayuno", "Receta 5/Almuerzo", "Receta 7/Cena")
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
                            .clickable { navController.navigate(cat.route) }, // <-- Navegación
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

            Spacer(Modifier.height(20.dp))
            Text(
                text = "Menú de la semana",
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
                        Text(
                            text = "${menu.day} Menu",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
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
            ) {
                Text("Reiniciar Plan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}
