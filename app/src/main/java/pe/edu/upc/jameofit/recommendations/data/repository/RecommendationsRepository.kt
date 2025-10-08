package pe.edu.upc.jameofit.recommendations.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.jameofit.recommendations.data.remote.RecommendationsService
import pe.edu.upc.jameofit.recommendations.data.model.RecommendationResponse

class RecommendationsRepository(
    private val api: RecommendationsService
) {
    suspend fun getRecommendations(userId: Long): List<RecommendationResponse>? =
        withContext(Dispatchers.IO) {
            val response = api.getRecommendationsByUser(userId)
            if (response.isSuccessful) response.body() else null
        }

    suspend fun getAllRecommendations(): List<RecommendationResponse>? =
        withContext(Dispatchers.IO) {
            val response = api.getAllRecommendations()
            if (response.isSuccessful) response.body() else null
        }
}
