package pe.edu.upc.jameofit.tracking.data.di

import pe.edu.upc.jameofit.tracking.data.repository.TrackingRepository
import pe.edu.upc.jameofit.tracking.data.remote.TrackingService
import pe.edu.upc.jameofit.tracking.data.remote.TrackingGoalService
import pe.edu.upc.jameofit.shared.data.di.SharedDataModule

object DataModule {
    fun getTrackingRepository(): TrackingRepository {
        val retrofit = SharedDataModule.getRetrofit()
        val service = retrofit.create(TrackingService::class.java)
        val goalService = retrofit.create(TrackingGoalService::class.java)
        return TrackingRepository(service, goalService)
    }
}