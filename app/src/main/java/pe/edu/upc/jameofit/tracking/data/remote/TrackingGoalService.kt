package pe.edu.upc.jameofit.tracking.data.remote

import retrofit2.Response
import retrofit2.http.PUT
import retrofit2.http.Path

interface TrackingGoalService {
    @PUT("/api/v1/tracking-goals/from-profile/{profileId}")
    suspend fun updateTrackingGoalFromProfile(
        @Path("profileId") profileId: Long
    ): Response<Unit>
}