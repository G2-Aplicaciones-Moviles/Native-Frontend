package pe.edu.upc.jameofit.recommendations.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upc.jameofit.recommendations.presentation.viewmodel.RecommendationsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsScreen(
    userId: Long,
    viewModel: RecommendationsViewModel,
    onBack: () -> Unit
) {
    val list by viewModel.recommendations.collectAsState()
    val loading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // ⚠️ NO LLAMAMOS loadRecommendations() AQUÍ
    // Se llama solo una vez desde RecommendationsRoute.kt

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Recomendaciones") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        when {
            loading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            error != null -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(error ?: "Error desconocido")
            }

            list.isEmpty() -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No tienes recomendaciones todavía.")
            }

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(list) { rec ->
                    ElevatedCard(Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            // Mostramos la información real que devuelve el backend
                            Text(
                                text = "Recomendación #${rec.id}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            rec.reason?.let {
                                Text(it, style = MaterialTheme.typography.bodyMedium)
                            }
                            rec.notes?.let {
                                Text("Nota: $it", style = MaterialTheme.typography.bodySmall)
                            }
                            rec.timeOfDay?.let {
                                Text("Momento del día: $it", style = MaterialTheme.typography.bodySmall)
                            }
                            rec.status?.let {
                                Text("Estado: $it", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
