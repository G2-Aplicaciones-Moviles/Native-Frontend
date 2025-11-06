package pe.edu.upc.jameofit.profile.domain.model

data class UserProfileRequest(
    val userId: Long,
    val gender: String,          // "MALE" | "FEMALE" | "OTHER"
    val height: Double,          // meters
    val weight: Double,          // kg
    val userScore: Int,
    val activityLevelId: Int,
    val objectiveId: Int,
    val allergyIds: List<Int> = emptyList(),
    val birthDate: String        // "yyyy-MM-dd"
)
