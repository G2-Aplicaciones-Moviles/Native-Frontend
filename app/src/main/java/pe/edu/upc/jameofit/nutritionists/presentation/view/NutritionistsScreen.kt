package pe.edu.upc.jameofit.nutritionists.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.edu.upc.jameofit.R

data class Nutritionist(
    val name: String,
    val specialty: String,
    val experience: String
)

@Composable
fun NutritionistsScreen() {
    var searchText by remember { mutableStateOf("") }

    val recommended = listOf(
        Nutritionist("Carlos Martín", "Especialista en dieta vegana", "5 años de experiencia"),
        Nutritionist("María López", "Experta en nutrición infantil", "3 años de experiencia")
    )

    val others = listOf(
        Nutritionist("Sofía Martínez", "Especializada en pérdida de peso", ""),
        Nutritionist("Eduardo Torres", "Experto en nutrición para atletas", "")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // 🔹 Barra inferior fija
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedButton(
                    onClick = { /* TODO contactar */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF4B5EFF)
                    )
                ) {
                    Text("Contactar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = { /* TODO guardar */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF4B5EFF)
                    )
                ) {
                    Text("Guardar", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = { /* TODO ver más */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(vertical = 4.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text("Ver más", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { paddingValues ->
        // 🔹 Contenido desplazable (scrollable)
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Text(
                    text = "Nutricionistas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Buscar nutricionistas") },
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Encuentra los mejores nutricionistas",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
                )

                Text(
                    text = "Recomendados",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(recommended) { nut ->
                        Card(
                            modifier = Modifier
                                .width(200.dp)
                                .height(150.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = nut.specialty,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(text = nut.name, fontWeight = FontWeight.Bold)
                                Text(text = nut.experience, fontSize = 13.sp, color = Color.Gray)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Otros Nutricionistas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(others) { nut ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = nut.name, fontWeight = FontWeight.Bold)
                        Text(text = "Nutricionista", fontSize = 14.sp, color = Color.Gray)
                    }
                    Text(
                        text = nut.specialty,
                        fontSize = 13.sp,
                        color = Color.DarkGray,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item { Spacer(Modifier.height(80.dp)) } // espacio final por la bottomBar
        }
    }
}

