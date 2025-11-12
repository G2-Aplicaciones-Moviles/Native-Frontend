package pe.edu.upc.jameofit.profile.presentation.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileSetupViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HealthSetupRoute(
    authViewModel: AuthViewModel,
    profileSetupViewModel: ProfileSetupViewModel,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    val authUser by authViewModel.user.collectAsState()
    val userId = authUser.id

    HealthSetup(
        userId = userId,
        viewModel = profileSetupViewModel,
        onNext = onNext,
        onBack = onBack
    )
}