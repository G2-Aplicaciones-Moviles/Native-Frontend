package pe.edu.upc.jameofit.profile.presentation.view

import androidx.compose.runtime.*

import pe.edu.upc.jameofit.goals.presentation.viewmodel.GoalsViewModel
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.shared.presentation.components.FullscreenLoader

@Composable
fun HealthSetupRoute(
    authViewModel: AuthViewModel,
    goalsViewModel: GoalsViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val userId by authViewModel.currentUserId.collectAsState()

    // Resolver userId si no existe
    LaunchedEffect(Unit) {
        if (userId == null) {
            authViewModel.resolveUserIdFromToken()
        }
    }

    // Loader con timeout
    var timedOut by remember { mutableStateOf(false) }
    LaunchedEffect(userId) {
        if (userId == null) {
            timedOut = false
            kotlinx.coroutines.delay(600)
            if (authViewModel.currentUserId.value == null) {
                timedOut = true
            }
        } else {
            timedOut = false
        }
    }

    when {
        userId != null -> {
            HealthSetup(
                userId = userId!!,
                viewModel = goalsViewModel,
                onNext = onNext,
                onBack = onBack
            )
        }
        !timedOut -> {
            FullscreenLoader(visible = true)
        }
        else -> {
            // Error: no se pudo obtener userId
            // Puedes mostrar un mensaje de error o redirigir
            onBack()
        }
    }
}