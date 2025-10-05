package pe.edu.upc.jameofit.goals.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import pe.edu.upc.jameofit.goals.presentation.viewmodel.GoalsViewModel
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.shared.presentation.components.FullscreenLoader
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsManagementRoute(
    authViewModel: AuthViewModel,
    goalsViewModel: GoalsViewModel,
    onBack: () -> Unit
) {
    val userId by authViewModel.currentUserId.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // 1) Resolver apenas entramos si no hay userId
    LaunchedEffect(Unit) {
        if (userId == null) authViewModel.resolveUserIdFromToken()
    }

    // 2) Loader con pequeño timeout para evitar error prematuro
    var timedOut by remember { mutableStateOf(false) }
    LaunchedEffect(userId) {
        if (userId == null) {
            timedOut = false
            kotlinx.coroutines.delay(600) // ajusta si quieres (500–800ms)
            if (authViewModel.currentUserId.value == null) timedOut = true
        } else {
            timedOut = false
        }
    }

    when {
        userId != null -> {
            GoalsManagement(
                userId = userId!!,              // tu firma actual lo usa
                viewmodel = goalsViewModel,
                onBack = onBack
            )
        }

        !timedOut -> {
            FullscreenLoader(visible = true)
        }

        else -> {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    TopAppBar(
                        title = { Text("Gestión de objetivos") },
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
                    Text(
                        "No pudimos identificar tu usuario.",
                        style = MaterialTheme.typography.titleMedium
                    )
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
