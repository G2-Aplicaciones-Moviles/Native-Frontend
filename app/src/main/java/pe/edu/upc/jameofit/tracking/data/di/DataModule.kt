package pe.edu.upc.jameofit.tracking.data.di

import pe.edu.upc.jameofit.shared.data.di.SharedDataModule
import pe.edu.upc.jameofit.tracking.data.remote.TrackingService
import pe.edu.upc.jameofit.tracking.data.repository.TrackingRepository

object DataModule {
    private fun getTrackingService(): TrackingService =
        SharedDataModule.getRetrofit().create(TrackingService::class.java)

    fun getTrackingRepository(): TrackingRepository =
        TrackingRepository(getTrackingService())
}
