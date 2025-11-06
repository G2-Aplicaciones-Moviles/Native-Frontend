package pe.edu.upc.jameofit.profile.data.remote

import pe.edu.upc.jameofit.profile.domain.model.UserProfileRequest
import pe.edu.upc.jameofit.profile.domain.model.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ProfileService {
    @POST("/api/v1/user-profiles")
    suspend fun createUserProfile(@Body body: UserProfileRequest): Response<UserProfileResponse>
}
