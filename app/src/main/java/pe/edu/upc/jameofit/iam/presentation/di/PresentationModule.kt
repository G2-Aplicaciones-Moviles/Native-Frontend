package pe.edu.upc.jameofit.iam.presentation.di

import pe.edu.upc.jameofit.iam.data.di.DataModule.getAuthRepository
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel

object PresentationModule {
    fun getAuthViewModel(): AuthViewModel {
        return AuthViewModel(getAuthRepository())
    }
}