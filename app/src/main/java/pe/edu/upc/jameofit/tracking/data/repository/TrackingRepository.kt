package pe.edu.upc.jameofit.tracking.data.repository

import pe.edu.upc.jameofit.tracking.data.remote.TrackingService
import pe.edu.upc.jameofit.tracking.data.remote.TrackingGoalService
import pe.edu.upc.jameofit.tracking.domain.model.CreateTrackingRequest
import pe.edu.upc.jameofit.tracking.data.model.TrackingResponse
import pe.edu.upc.jameofit.tracking.data.model.TrackingProgressResponse

class TrackingRepository(
    private val service: TrackingService,
    private val goalService: TrackingGoalService  // ✅ NUEVO
) {
    suspend fun createGoalFromProfile(profileId: Long?): Long {
        val resp = service.createTrackingGoalFromProfile(profileId)
        if (resp.isSuccessful) return resp.body() ?: throw IllegalStateException("Empty body")
        throw IllegalStateException("Failed create goal: ${resp.code()}")
    }

    suspend fun createTracking(userId: Long) {
        val resp = service.createTracking(CreateTrackingRequest(userId))
        if (!resp.isSuccessful) throw IllegalStateException("Failed create tracking: ${resp.code()}")
    }

    suspend fun getTrackingByUserId(userId: Long): TrackingResponse? {
        val resp = service.getTrackingByUserId(userId)
        if (resp.isSuccessful) return resp.body()
        if (resp.code() == 404) return null
        throw IllegalStateException("Failed get tracking: ${resp.code()} ${resp.errorBody()?.string()}")
    }

    suspend fun getTrackingProgress(userId: Long): TrackingProgressResponse? {
        val resp = service.getTrackingProgress(userId)
        if (resp.isSuccessful) return resp.body()
        if (resp.code() == 404) return null
        throw IllegalStateException("Failed get progress: ${resp.code()} ${resp.errorBody()?.string()}")
    }

    suspend fun updateTrackingGoalFromProfile(profileId: Long): Boolean {
        val resp = goalService.updateTrackingGoalFromProfile(profileId)
        return resp.isSuccessful
    }
}