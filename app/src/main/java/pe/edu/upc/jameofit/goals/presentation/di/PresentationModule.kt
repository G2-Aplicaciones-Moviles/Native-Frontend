package pe.edu.upc.jameofit.goals.presentation.di

import pe.edu.upc.jameofit.goals.data.di.DataModule.getGoalsRepository
import pe.edu.upc.jameofit.goals.presentation.viewmodel.GoalsViewModel

object PresentationModule {
    fun getGoalsViewModel(): GoalsViewModel {
        return GoalsViewModel(getGoalsRepository())
    }
}