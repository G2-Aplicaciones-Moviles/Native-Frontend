package pe.edu.upc.jameofit.profile.data.remote

import pe.edu.upc.jameofit.profile.domain.model.UserProfileRequest
import pe.edu.upc.jameofit.profile.domain.model.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfileService {
    @POST("/api/v1/user-profiles")
    suspend fun createUserProfile(@Body body: UserProfileRequest): Response<UserProfileResponse>

    @GET("/api/v1/user-profiles/{id}")
    suspend fun getUserProfile(@Path("id") profileId: Long): Response<UserProfileResponse>

    @PUT("/api/v1/user-profiles/{id}")
    suspend fun updateUserProfile(
        @Path("id") profileId: Long,
        @Body body: UserProfileRequest
    ): Response<Unit>
}