package pe.edu.upc.jameofit.iam.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import pe.edu.upc.jameofit.iam.data.model.LoginRequest
import pe.edu.upc.jameofit.iam.data.model.RegisterRequest
import pe.edu.upc.jameofit.iam.data.remote.AuthService
import pe.edu.upc.jameofit.iam.domain.model.User
import pe.edu.upc.jameofit.shared.data.local.JwtStorage
import java.nio.charset.StandardCharsets
import android.util.Base64 // URL_SAFE | NO_WRAP

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

    // Devuelve el id desde el JWT
    suspend fun currentUserId(): Long? = withContext(Dispatchers.IO) {
        val token = JwtStorage.getToken() ?: return@withContext null
        decodeUserIdFromJwt(token)
    }

    private fun decodeUserIdFromJwt(jwt: String): Long? {
        val parts = jwt.split('.')
        if (parts.size < 2) return null
        return try {
            val payloadBytes = android.util.Base64.decode(
                parts[1], android.util.Base64.URL_SAFE or android.util.Base64.NO_WRAP
            )
            val json = org.json.JSONObject(String(payloadBytes, Charsets.UTF_8))

            // claves típicas: userId, uid, sub (string o número)
            when {
                json.has("userId") -> json.optLong("userId")
                json.has("uid")    -> json.optLong("uid")
                json.has("sub")    -> json.optString("sub").toLongOrNull()
                // a veces viene anidado: { "user": { "id": 123 } }
                json.optJSONObject("user")?.has("id") == true ->
                    json.optJSONObject("user")!!.optLong("id")
                else -> null
            }
        } catch (_: Exception) {
            null
        }
    }
}
