package pe.edu.upc.jameofit.mealplan.presentation.view

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel
import pe.edu.upc.jameofit.ui.theme.JameoBlue // Assumed color resource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeToMealPlanScreen(
    mealPlanId: Long,
    navController: NavController,
    mealPlanViewModel: MealPlanViewModel
) {
    // Note: This screen uses RecipeResponse which has the list of recipeIngredients
    val recipes by mealPlanViewModel.recipes.collectAsState()
    val loading by mealPlanViewModel.isLoading.collectAsState()
    val error by mealPlanViewModel.error.collectAsState()

    LaunchedEffect(mealPlanViewModel) {
        Log.d("UI", "AddRecipeToMealPlanScreen -> LaunchedEffect fired")
        mealPlanViewModel.loadRecipes()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        Text(
            "Selecciona una receta para agregar",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        Spacer(modifier = Modifier.height(8.dp))

        if (loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }

        // Show error message if loading failed
        if (error != null) {
            Text("Error: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
            return@Column
        }

        LazyColumn {
            items(recipes) { recipe ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            navController.navigate("drawer/recipe_detail/$mealPlanId/${recipe.id}")
                        },
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text(
                            text = recipe.name ?: "Receta sin nombre",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = JameoBlue
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // 2. Description
                        Text(
                            text = recipe.description ?: "Sin descripción.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // 3. Ingredients Preview
                        val ingredientPreview = recipe.recipeIngredients
                            .take(2) // Show up to 2 ingredients for preview
                            .joinToString(separator = ", ") { it.ingredient.name ?: "Ingrediente" }

                        Text(
                            text = if (ingredientPreview.isNotEmpty()) "Ingredientes: $ingredientPreview..." else "Sin ingredientes listados.",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}