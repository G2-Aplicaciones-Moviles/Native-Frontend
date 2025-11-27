package pe.edu.upc.jameofit.profile.presentation.di

import pe.edu.upc.jameofit.profile.data.di.DataModule
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileViewModel
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileSetupViewModel
import pe.edu.upc.jameofit.tracking.data.repository.TrackingRepository
import pe.edu.upc.jameofit.tracking.data.remote.TrackingService
import pe.edu.upc.jameofit.tracking.data.remote.TrackingGoalService
import pe.edu.upc.jameofit.shared.data.di.SharedDataModule
import pe.edu.upc.jameofit.iam.presentation.viewmodel.AuthViewModel
import pe.edu.upc.jameofit.iam.data.repository.AuthRepository
import pe.edu.upc.jameofit.iam.data.remote.AuthService

object PresentationModule {

    private val authViewModel: AuthViewModel by lazy {
        val authRepo = AuthRepository(
            SharedDataModule.getRetrofit().create(AuthService::class.java)
        )
        AuthViewModel(authRepo)
    }

    fun provideAuthViewModel(): AuthViewModel = authViewModel

    fun getProfileViewModel(): ProfileViewModel {
        val profileRepo = DataModule.getProfileRepository()

        // ✅ Crear TrackingRepository con ambos services
        val retrofit = SharedDataModule.getRetrofit()
        val trackingService = retrofit.create(TrackingService::class.java)
        val goalService = retrofit.create(TrackingGoalService::class.java)
        val trackingRepo = TrackingRepository(trackingService, goalService)

        return ProfileViewModel(profileRepo, trackingRepo)
    }

    fun getProfileSetupViewModel(): ProfileSetupViewModel {
        val profileRepo = DataModule.getProfileRepository()
        val retrofit = SharedDataModule.getRetrofit()
        val trackingService = retrofit.create(TrackingService::class.java)
        val goalService = retrofit.create(TrackingGoalService::class.java)
        val trackingRepo = TrackingRepository(trackingService, goalService)
        return ProfileSetupViewModel(profileRepo, trackingRepo)
    }
}