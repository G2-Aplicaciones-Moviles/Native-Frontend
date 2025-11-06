package pe.edu.upc.jameofit.tracking.presentation.di

import pe.edu.upc.jameofit.tracking.data.di.DataModule
import pe.edu.upc.jameofit.tracking.presentation.viewmodel.TrackingViewModel

object PresentationModule {
    fun getTrackingViewModel(): TrackingViewModel {
        val repo = DataModule.getTrackingRepository()
        return TrackingViewModel(repo)
    }
}
