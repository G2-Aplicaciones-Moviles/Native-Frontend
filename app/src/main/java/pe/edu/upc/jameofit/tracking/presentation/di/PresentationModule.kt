package pe.edu.upc.jameofit.tracking.presentation.di

import pe.edu.upc.jameofit.tracking.data.di.DataModule
import pe.edu.upc.jameofit.tracking.presentation.viewmodel.TrackingViewModel
import pe.edu.upc.jameofit.mealplan.data.repository.MealPlanRepository
import pe.edu.upc.jameofit.mealplan.data.remote.MealPlanService
import pe.edu.upc.jameofit.profile.data.di.DataModule as ProfileDataModule
import pe.edu.upc.jameofit.shared.data.di.SharedDataModule

object PresentationModule {
    fun getTrackingViewModel(): TrackingViewModel {
        // Tracking repository
        val trackingRepo = DataModule.getTrackingRepository()

        // MealPlan repository
        val mealPlanService = SharedDataModule.getRetrofit().create(MealPlanService::class.java)
        val mealPlanRepo = MealPlanRepository(mealPlanService)

        // Profile repository
        val profileRepo = ProfileDataModule.getProfileRepository()

        return TrackingViewModel(trackingRepo, mealPlanRepo, profileRepo)
    }
}