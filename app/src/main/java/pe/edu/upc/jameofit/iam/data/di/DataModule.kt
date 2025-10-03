package pe.edu.upc.jameofit.iam.data.di

import pe.edu.upc.jameofit.iam.data.remote.AuthService
import pe.edu.upc.jameofit.iam.data.repository.AuthRepository
import pe.edu.upc.jameofit.shared.data.di.SharedDataModule.getRetrofit


object DataModule {
    fun getAuthService(): AuthService {
        return getRetrofit().create(AuthService::class.java)
    }

    fun getAuthRepository(): AuthRepository {
        return AuthRepository(getAuthService())
    }
}