package pe.edu.upc.jameofit.iam.presentation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pe.edu.upc.jameofit.iam.data.di.DataModule
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel

object PresentationModule {

    /**
     * Devuelve una ViewModelProvider.Factory que crea un AuthViewModel
     * vinculado a su AuthRepository desde DataModule.
     *
     * Uso recomendado:
     *   val authViewModel: AuthViewModel = viewModel(
     *       factory = PresentationModule.authViewModelFactory()
     *   )
     */
    fun authViewModelFactory(): ViewModelProvider.Factory {
        val repo = DataModule.getAuthRepository()
        return object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                    return AuthViewModel(repo) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}
