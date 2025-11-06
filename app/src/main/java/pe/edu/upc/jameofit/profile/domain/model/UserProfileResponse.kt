package pe.edu.upc.jameofit.profile.domain.model

data class UserProfileResponse(
    val id: Long,
    val userId: Long,
    val gender: String,
    val height: Double,
    val weight: Double,
    val userScore: Int,
    val activityLevelId: Int,
    val objectiveId: Int,
    val allergyIds: List<Int> = emptyList(),
    val birthDate: String
)
