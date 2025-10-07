package pe.edu.upc.jameofit.recipedetail.presentation.view

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
fun LunchRecipeDetailScreen() {
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
                        painter = painterResource(id = R.drawable.ceviche_saludable),
                        contentDescription = "Ceviche saludable",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )

                    Spacer(Modifier.height(12.dp))

                    Text("Almuerzo del día", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("390 kcal", fontSize = 14.sp, color = Color.DarkGray)
                    Text("Porción: 1", fontSize = 14.sp, color = Color.DarkGray)
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Ingredientes Almuerzo",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(Modifier.height(12.dp))

            IngredientItem2("Filete de pescado fresco (corvina, lenguado o bonito)")
            IngredientItem2("Limón, ají limo, sal y culantro")
            IngredientItem2("Cebolla roja en pluma")
            IngredientItem2("Camote sancochado")
            IngredientItem2("Choclo sancochado como guarnición")
            IngredientItem2("Lechuga como base")

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
fun IngredientItem2(text: String) {
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
