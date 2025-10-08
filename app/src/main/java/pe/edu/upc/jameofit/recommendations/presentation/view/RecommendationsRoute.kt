package pe.edu.upc.jameofit.recommendations.presentation.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.recommendations.presentation.viewmodel.RecommendationsViewModel
import pe.edu.upc.jameofit.shared.presentation.components.FullscreenLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecommendationsRoute(
    authViewModel: AuthViewModel,
    recommendationsViewModel: RecommendationsViewModel,
    onBack: () -> Unit
) {
    val userId by authViewModel.currentUserId.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Resolver userId si no está listo
    LaunchedEffect(Unit) {
        if (userId == null) authViewModel.resolveUserIdFromToken()
    }

    // Timeout pequeño para mostrar pantalla de error si el userId no llega
    var timedOut by remember { mutableStateOf(false) }
    LaunchedEffect(userId) {
        if (userId == null) {
            timedOut = false
            kotlinx.coroutines.delay(600)
            if (authViewModel.currentUserId.value == null) timedOut = true
        } else {
            timedOut = false
        }
    }

    when {
        // ✅ Usuario identificado → mostrar lista
        userId != null -> {
            RecommendationsScreen(
                userId = userId!!,
                viewModel = recommendationsViewModel,
                onBack = onBack
            )
        }

        // ⏳ Mientras se obtiene el ID del usuario
        !timedOut -> {
            FullscreenLoader(visible = true)
        }

        // ❌ Timeout o error de autenticación
        else -> {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    TopAppBar(
                        title = { Text("Mis Recomendaciones") },
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    Icons.AutoMirrored.Outlined.ArrowBack,
                                    contentDescription = "Volver"
                                )
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No pudimos identificar tu usuario.", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Vuelve a intentar o regresa.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = onBack) { Text("Volver") }
                        Button(onClick = {
                            scope.launch { snackbarHostState.showSnackbar("Reintentando…") }
                            authViewModel.resolveUserIdFromToken()
                        }) { Text("Reintentar") }
                    }
                }
            }
        }
    }
}
