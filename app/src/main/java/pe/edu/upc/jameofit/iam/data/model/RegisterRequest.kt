package pe.edu.upc.jameofit.iam.data.model

import pe.edu.upc.jameofit.iam.domain.model.User

data class RegisterRequest(
    val username: String,
    val password: String,
    val roles: List<String>
) {
    companion object {
        fun fromUser(user: User): RegisterRequest {
            return RegisterRequest(
                username = user.username,
                password = user.password,
                roles = user.roles
            )
        }
    }
}
