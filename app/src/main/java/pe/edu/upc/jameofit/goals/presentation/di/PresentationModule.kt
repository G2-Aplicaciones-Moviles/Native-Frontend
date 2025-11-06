package pe.edu.upc.jameofit.goals.presentation.di

import pe.edu.upc.jameofit.goals.data.di.DataModule.getGoalsRepository
import pe.edu.upc.jameofit.goals.presentation.viewmodel.GoalsViewModel
import pe.edu.upc.jameofit.profile.data.di.DataModule as ProfileDataModule
import pe.edu.upc.jameofit.shared.data.di.SharedDataModule
import pe.edu.upc.jameofit.tracking.data.remote.TrackingService
import pe.edu.upc.jameofit.tracking.data.repository.TrackingRepository

object PresentationModule {
    fun getGoalsViewModel(): GoalsViewModel {
        // Goals repository (existing)
        val goalsRepo = getGoalsRepository()

        // Profile repository via profile DataModule provider
        val profileRepo = ProfileDataModule.getProfileRepository()

        // Tracking repository: build using SharedDataModule's Retrofit instance
        val trackingService = SharedDataModule.getRetrofit().create(TrackingService::class.java)
        val trackingRepo = TrackingRepository(trackingService)

        // Construct GoalsViewModel with the three dependencies
        return GoalsViewModel(
            goalsRepo,
            profileRepo,
            trackingRepo
        )
    }
}
