package pe.edu.upc.jameofit.iam.data.model

// Deberia devolver solo el token
data class LoginResponse(
    val id: Int,
    val username: String,
    val token: String
)
