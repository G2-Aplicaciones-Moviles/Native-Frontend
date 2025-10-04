package pe.edu.upc.jameofit.iam.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.jameofit.iam.data.model.LoginRequest
import pe.edu.upc.jameofit.iam.data.model.RegisterRequest
import pe.edu.upc.jameofit.iam.data.remote.AuthService
import pe.edu.upc.jameofit.iam.domain.model.User
import pe.edu.upc.jameofit.shared.data.local.JwtStorage

class AuthRepository(
    private val authService: AuthService
) {
    suspend fun login(user: User): Boolean = withContext(Dispatchers.IO) {
        val res = authService.login(LoginRequest.fromUser(user))

        if (!res.isSuccessful) return@withContext false

        val token = res.body()?.token

        if (token != null) {
            JwtStorage.saveToken(token)
            return@withContext true
        }

        return@withContext false
    }

    suspend fun register(user: User): Boolean = withContext(Dispatchers.IO) {
        val res = authService.register(RegisterRequest.fromUser(user))

        if (!res.isSuccessful) return@withContext false

        val token = res.body()?.token

        if (token != null) {
            JwtStorage.saveToken(token)
            return@withContext true
        }

        return@withContext false
    }

    suspend fun logout(): Boolean = withContext(Dispatchers.IO) {
        JwtStorage.clearToken()

        return@withContext true
    }
}