package pe.edu.upc.jameofit.tracking.presentation.utils

fun getMealTypeName(typeId: Int): String {
    return when (typeId) {
        1 -> "Desayuno"
        2 -> "Almuerzo"
        3 -> "Cena"
        4 -> "Snack"
        else -> "Comida"
    }
}