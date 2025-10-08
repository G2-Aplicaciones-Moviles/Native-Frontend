package pe.edu.upc.jameofit.recipe.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.home.presentation.navigation.RecipeRoute

@Composable
fun DinnerScreen(navController: NavController) {
    val recipes = listOf(
        "Ensalada de palta con tomate y huevo",
        "Tortilla de espinaca",
        "Sopa ligera de verduras",
        "Sandwich de pavo"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        item {
            Text("Cena", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))
        }

        items(recipes) { title ->
            RecipeItem3(title) {
                navController.navigate(RecipeRoute.DINNER_DETAIL)
            }
            Spacer(Modifier.height(8.dp))
        }

        item {
            Text("Información Nutricional", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(12.dp))

            NutritionInfoItem3("Valor Energético", "450 kcal", R.drawable.info_nutri)
            Spacer(Modifier.height(16.dp))
            NutritionInfoItem3("Proteínas", "30g", R.drawable.info_nutri)
        }
    }
}

@Composable
fun RecipeItem3(title: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = title,
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(32.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun NutritionInfoItem3(title: String, value: String, iconRes: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(60.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(value, fontSize = 14.sp, color = Color.DarkGray)
        }
    }
}
