package pe.edu.upc.jameofit.iam.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pe.edu.upc.jameofit.shared.data.local.JwtStorage
import pe.edu.upc.jameofit.iam.data.remote.AuthService
import pe.edu.upc.jameofit.iam.data.model.RegisterRequest
import pe.edu.upc.jameofit.iam.data.model.LoginRequest
import pe.edu.upc.jameofit.shared.data.local.UserSessionStorage
import pe.edu.upc.jameofit.iam.domain.model.User

class AuthRepository(private val authService: AuthService) {

    suspend fun login(user: User): User? = withContext(Dispatchers.IO) {
        try {
            val response = authService.login(LoginRequest(user.username, user.password))
            if (!response.isSuccessful) return@withContext null
            val body = response.body() ?: return@withContext null

            // Guardamos token y userId
            JwtStorage.saveToken(body.token)
            UserSessionStorage.saveUserId(body.id.toLong())

            // Devolvemos el User con id real
            return@withContext user.copy(id = body.id.toLong())
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }

    suspend fun register(user: User): User? = withContext(Dispatchers.IO) {
        try {
            val res = authService.register(RegisterRequest.fromUser(user))
            if (!res.isSuccessful) return@withContext null
            val body = res.body() ?: return@withContext null

            val registeredUser = user.copy(id = body.id.toLong())
            UserSessionStorage.saveUserId(registeredUser.id)

            // Hacemos login automático para obtener token
            return@withContext login(registeredUser)
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext null
        }
    }
}
