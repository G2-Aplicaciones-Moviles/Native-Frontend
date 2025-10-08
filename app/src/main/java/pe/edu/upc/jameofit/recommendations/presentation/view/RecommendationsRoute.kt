package pe.edu.upc.jameofit.recommendations.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pe.edu.upc.jameofit.recommendations.presentation.viewmodel.RecommendationsViewModel
import pe.edu.upc.jameofit.shared.presentation.components.FullscreenLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsRoute(
    recommendationsViewModel: RecommendationsViewModel,
    onBack: () -> Unit
) {
    val isLoading by recommendationsViewModel.isLoading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Cargar TODAS las recomendaciones
    LaunchedEffect(Unit) {
        recommendationsViewModel.loadRecommendations() // sin userId → traer todas
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            RecommendationsScreen(
                userId = -1L, // -1 no se usa realmente, solo por compatibilidad
                viewModel = recommendationsViewModel,
                onBack = onBack
            )

            FullscreenLoader(visible = isLoading)
        }
    }
}
