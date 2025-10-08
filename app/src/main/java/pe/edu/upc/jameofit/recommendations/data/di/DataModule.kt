package pe.edu.upc.jameofit.recommendations.data.di

import pe.edu.upc.jameofit.recommendations.data.remote.RecommendationsService
import pe.edu.upc.jameofit.recommendations.data.repository.RecommendationsRepository
import pe.edu.upc.jameofit.shared.data.di.SharedDataModule.getRetrofit

object DataModule {

    fun getRecommendationsService(): RecommendationsService =
        getRetrofit().create(RecommendationsService::class.java)

    fun getRecommendationsRepository(): RecommendationsRepository =
        RecommendationsRepository(getRecommendationsService())
}
