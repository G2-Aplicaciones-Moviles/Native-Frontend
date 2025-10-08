package pe.edu.upc.jameofit.recipe.recipedetail.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
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
import pe.edu.upc.jameofit.R

@Composable
fun DinnerRecipeDetailScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ensalada_palta_tomate_huevo),
                        contentDescription = "Ensalada de palta con tomate y huevo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text("Cena Especial", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("350 kcal", fontSize = 14.sp, color = Color.DarkGray)
                    Text("Porción: 1", fontSize = 14.sp, color = Color.DarkGray)
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Ingredientes Cena",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(Modifier.height(12.dp))

            IngredientItem3("Palta en rodajas (¼ unidad)")
            IngredientItem3("Tomate en cubos")
            IngredientItem3("1 huevo sancochado picado")
            IngredientItem3("Hojas verdes (lechuga, espinaca, arúgula)")
            IngredientItem3("Limón, sal ligera y un chorrito de aceite de oliva")

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Descargar receta")
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
            ) {
                Text("Seleccionar receta para el plan")
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun IngredientItem3(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Ingrediente",
            tint = Color(0xFF4CAF50),
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(text, fontSize = 16.sp)
    }
}
