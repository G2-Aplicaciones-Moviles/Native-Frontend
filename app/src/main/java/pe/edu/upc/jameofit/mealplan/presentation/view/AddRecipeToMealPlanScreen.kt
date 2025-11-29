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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecipeToMealPlanScreen(
    mealPlanId: Long,
    navController: NavController,
    mealPlanViewModel: MealPlanViewModel
) {
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

        LazyColumn {
            items(recipes) { recipe ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            Log.d("UI", "Card clicked recipe.id=${recipe.id}")
                            navController.navigate("drawer/recipe_detail/$mealPlanId/${recipe.id}")
                        }
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text(
                            text = recipe.name,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = recipe.description ?: "",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
