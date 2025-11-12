package pe.edu.upc.jameofit.profile.presentation.di

import pe.edu.upc.jameofit.profile.data.di.DataModule
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileViewModel
import pe.edu.upc.jameofit.profile.presentation.viewmodel.ProfileSetupViewModel
import pe.edu.upc.jameofit.tracking.data.repository.TrackingRepository
import pe.edu.upc.jameofit.tracking.data.remote.TrackingService
import pe.edu.upc.jameofit.shared.data.di.SharedDataModule

object PresentationModule {

    fun getProfileViewModel(): ProfileViewModel {
        val profileRepo = DataModule.getProfileRepository()
        return ProfileViewModel(profileRepo)
    }

    fun getProfileSetupViewModel(): ProfileSetupViewModel {
        val profileRepo = DataModule.getProfileRepository()
        val trackingService = SharedDataModule.getRetrofit().create(TrackingService::class.java)
        val trackingRepo = TrackingRepository(trackingService)
        return ProfileSetupViewModel(profileRepo, trackingRepo)
    }
}