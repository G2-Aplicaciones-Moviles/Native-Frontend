package pe.edu.upc.jameofit.mealplan.presentation.view

import androidx.annotation.DrawableRes
import pe.edu.upc.jameofit.R
import pe.edu.upc.jameofit.home.presentation.navigation.RecipeRoute
import pe.edu.upc.jameofit.mealplan.model.Category

data class RecipeCategory(
    val id: Long,
    val title: String,
    @DrawableRes val imageRes: Int,
    val route: String
)

fun Category.toRecipeCategory(): RecipeCategory {
    val normalized = name.trim().lowercase()
    val (drawable, route) = when (normalized) {
        "desayuno" -> R.drawable.desayuno to RecipeRoute.BREAKFAST
        "almuerzo" -> R.drawable.almuerzo to RecipeRoute.LUNCH
        "cena"     -> R.drawable.cena     to RecipeRoute.DINNER
        "snack"    -> R.drawable.desayuno to RecipeRoute.BREAKFAST // fallback temporal
        "postre"   -> R.drawable.cena     to RecipeRoute.DINNER   // fallback temporal
        else       -> R.drawable.desayuno to RecipeRoute.BREAKFAST
    }
    return RecipeCategory(id = id, title = name, imageRes = drawable, route = route)
}
fun localDefaultCategories(): List<RecipeCategory> = listOf(
    RecipeCategory(1, "Desayuno", R.drawable.desayuno, RecipeRoute.BREAKFAST),
    RecipeCategory(2, "Almuerzo", R.drawable.almuerzo, RecipeRoute.LUNCH),
    RecipeCategory(3, "Cena",     R.drawable.cena,     RecipeRoute.DINNER)
)