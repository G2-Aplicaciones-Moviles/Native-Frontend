package pe.edu.upc.jameofit.recommendation.data.remote

import pe.edu.upc.jameofit.recommendation.data.model.RecommendationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RecommendationsService {
    @GET("recommendations/user/{userId}")
    suspend fun getRecommendationsByUser(
        @Path("userId") userId: Long
    ): Response<List<RecommendationResponse>>
}
