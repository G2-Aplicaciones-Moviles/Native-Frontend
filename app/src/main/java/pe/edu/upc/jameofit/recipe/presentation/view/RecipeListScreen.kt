package pe.edu.upc.jameofit.recipe.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.home.presentation.navigation.RecipeRoute
import pe.edu.upc.jameofit.recipe.data.di.DataModule
import pe.edu.upc.jameofit.recipe.model.Recipe
import pe.edu.upc.jameofit.recipe.presentation.viewmodel.RecipeListVMFactory
import pe.edu.upc.jameofit.recipe.presentation.viewmodel.RecipeListViewModel

@Composable
fun RecipeListScreen(
    navController: NavController,
    categoryId: Long,
    categoryTitle: String
) {
    val vm: RecipeListViewModel = viewModel(
        factory = RecipeListVMFactory(DataModule.getRecipeRepository(), categoryId)
    )
    val state by vm.uiState.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        item {
            Text(
                text = categoryTitle,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
            Spacer(Modifier.height(16.dp))
        }

        when {
            state.isLoading -> {
                items(4) { ShimmerRow() }
            }
            state.error != null -> {
                item {
                    Text(
                        "No se pudo cargar la lista (${state.error})",
                        color = MaterialTheme.colorScheme.error
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = vm::load) { Text("Reintentar") }
                }
            }
            state.items.isEmpty() -> {
                item { Text("No hay recetas en esta categoría.") }
            }
            else -> {
                items(state.items) { recipe ->
                    RecipeRow(
                        recipe = recipe,
                        onClick = {
                            navController.navigate(detailRouteFor(categoryTitle))
                        }
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }

        // Información Nutricional (placeholder como en tus pantallas actuales)
        item {
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Información Nutricional",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(Modifier.height(12.dp))
            NutritionInfoItem4("Valor Energético", "300 kcal", R.drawable.info_nutri)
            Spacer(Modifier.height(16.dp))
            NutritionInfoItem4("Proteínas", "15g", R.drawable.info_nutri)
        }
    }
}

@Composable
private fun RecipeRow(recipe: Recipe, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.AddCircle,
            contentDescription = recipe.name,
            tint = Color(0xFF4CAF50)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(recipe.name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            if (recipe.description.isNotBlank()) {
                Text(
                    recipe.description,
                    fontSize = 12.sp,
                    color = Color.DarkGray,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun ShimmerRow() {
    // placeholder simple
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(28.dp)
            .background(Color(0xFFF1F1F1))
    )
}

/** Reutilizamos tu componente existente */
@Composable
fun NutritionInfoItem4(title: String, value: String, iconRes: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Icon(imageVector = Icons.Default.AddCircle, contentDescription = null, tint = Color(0xFF4CAF50))
        Spacer(Modifier.width(16.dp))
        Column { Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp); Text(value, fontSize = 14.sp, color = Color.DarkGray) }
    }
}

/** Mapea el título de la categoría al detalle existente */
private fun detailRouteFor(categoryTitle: String): String {
    return when (categoryTitle.trim().lowercase()) {
        "desayuno" -> RecipeRoute.BREAKFAST_DETAIL
        "almuerzo" -> RecipeRoute.LUNCH_DETAIL
        "cena"     -> RecipeRoute.DINNER_DETAIL
        else       -> RecipeRoute.BREAKFAST_DETAIL // fallback
    }
}
