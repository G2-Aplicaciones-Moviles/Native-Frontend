package pe.edu.upc.jameofit.tracking.data.remote

import pe.edu.upc.jameofit.tracking.domain.model.CreateTrackingRequest
import pe.edu.upc.jameofit.tracking.data.model.TrackingResponse
import pe.edu.upc.jameofit.tracking.data.model.TrackingProgressResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TrackingService {
    @POST("/api/v1/tracking-goals/from-profile/{profileId}")
    suspend fun createTrackingGoalFromProfile(@Path("profileId") profileId: Long?): Response<Long>

    @POST("/api/v1/tracking")
    suspend fun createTracking(@Body body: CreateTrackingRequest): Response<Any>

    @GET("/api/v1/tracking/user/{userId}")
    suspend fun getTrackingByUserId(@Path("userId") userId: Long): Response<TrackingResponse>

    // ✅ NUEVO: Endpoint de progreso
    @GET("/api/v1/tracking/user/{userId}/progress")
    suspend fun getTrackingProgress(@Path("userId") userId: Long): Response<TrackingProgressResponse>
}