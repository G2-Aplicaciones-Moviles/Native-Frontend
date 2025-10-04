package pe.edu.upc.jameofit.iam.domain.model

data class User(
    val username: String = "",
    val password: String = "",
    val roles: List<String> = emptyList()
)