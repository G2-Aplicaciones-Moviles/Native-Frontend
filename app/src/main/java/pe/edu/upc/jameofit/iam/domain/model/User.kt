package pe.edu.upc.jameofit.iam.domain.model

data class User(
    val id: Long = 0L,
    val username: String = "",
    val password: String = "",
    val roles: List<String> = emptyList()
)
