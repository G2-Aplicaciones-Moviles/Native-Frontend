package pe.edu.upc.jameofit.recommendations.presentation.di

import pe.edu.upc.jameofit.recommendations.data.di.DataModule.getRecommendationsRepository
import pe.edu.upc.jameofit.recommendations.presentation.viewmodel.RecommendationsViewModel

object PresentationModule {
    fun getRecommendationsViewModel(): RecommendationsViewModel {
        return RecommendationsViewModel(getRecommendationsRepository())
    }
}
