package pe.edu.upc.jameofit.shared.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FullscreenLoader(visible: Boolean) {
    if (!visible) return
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

suspend fun SnackbarHostState.showErrorOnce(message: String) {
    showSnackbar(message = message, duration = SnackbarDuration.Short)
}

@Composable
fun ErrorSnackbarHost(hostState: SnackbarHostState) {
    SnackbarHost(hostState = hostState)
}
