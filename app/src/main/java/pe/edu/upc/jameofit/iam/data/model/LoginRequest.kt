package pe.edu.upc.jameofit.iam.data.model

import pe.edu.upc.jameofit.iam.domain.model.User

data class LoginRequest(
    val username: String,
    val password: String
) {
    companion object {
        fun fromUser(user: User): LoginRequest {
            return LoginRequest(
                username = user.username,
                password = user.password
            )
        }
    }
}