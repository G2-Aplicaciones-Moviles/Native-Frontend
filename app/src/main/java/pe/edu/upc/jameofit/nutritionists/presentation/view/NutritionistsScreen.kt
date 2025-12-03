package pe.edu.upc.jameofit.nutritionists.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import pe.edu.upc.jameofit.nutritionists.presentation.viewmodel.NutritionistViewModel

@Composable
fun NutritionistsScreen(
    viewModel: NutritionistViewModel,
    patientUserId: Long
) {
    val list by viewModel.list.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadNutritionists()
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {

        if (loading) {
            CircularProgressIndicator()
            return
        }

        LazyColumn {
            items(list) { nut ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text(
                            nut.fullName ?: "Nutricionista",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(Modifier.height(4.dp))

                        Text("Especialidad: ${nut.speciality ?: "No especificada"}")

                        Spacer(Modifier.height(4.dp))

                        Text(nut.bio ?: "")

                        Spacer(Modifier.height(8.dp))

                        Button(onClick = {
                            viewModel.sendContact(patientUserId, nut.id)
                        }) {
                            Text("Contactar")
                        }
                    }
                }
            }
        }
    }
}
