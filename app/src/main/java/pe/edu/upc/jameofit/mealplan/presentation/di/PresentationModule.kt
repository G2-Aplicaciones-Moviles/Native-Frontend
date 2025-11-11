package pe.edu.upc.jameofit.mealplan.presentation.di

import pe.edu.upc.jameofit.mealplan.data.di.DataModule as MealPlanDataModule
import pe.edu.upc.jameofit.mealplan.presentation.viewmodel.MealPlanViewModel
import pe.edu.upc.jameofit.tracking.data.di.DataModule as TrackingDataModule

object PresentationModule {
    fun getMealPlanViewModel(): MealPlanViewModel {
        val mealPlanRepo = MealPlanDataModule.getMealPlanRepository()
        val trackingRepo = TrackingDataModule.getTrackingRepository()
        return MealPlanViewModel(mealPlanRepo, trackingRepo)
    }
}