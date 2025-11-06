package pe.edu.upc.jameofit.profile.data.repository

import pe.edu.upc.jameofit.profile.data.remote.ProfileService
import pe.edu.upc.jameofit.profile.domain.model.UserProfileRequest
import pe.edu.upc.jameofit.profile.domain.model.UserProfileResponse

class ProfileRepository(private val service: ProfileService) {
    suspend fun createProfile(request: UserProfileRequest): UserProfileResponse? {
        val resp = service.createUserProfile(request)
        if (resp.isSuccessful) return resp.body()
        throw IllegalStateException("Failed creating profile: ${resp.code()} ${resp.errorBody()?.string()}")
    }
}
