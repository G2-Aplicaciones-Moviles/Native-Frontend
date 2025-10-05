package pe.edu.upc.jameofit.iam.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import pe.edu.upc.jameofit.iam.data.model.LoginRequest
import pe.edu.upc.jameofit.iam.data.model.RegisterRequest
import pe.edu.upc.jameofit.iam.data.remote.AuthService
import pe.edu.upc.jameofit.iam.domain.model.User
import pe.edu.upc.jameofit.shared.data.local.JwtStorage
import pe.edu.upc.jameofit.shared.data.local.UserSessionStorage
import android.util.Base64 // URL_SAFE | NO_WRAP

class AuthRepository(
    private val authService: AuthService
) {
    suspend fun login(user: User): Boolean = withContext(Dispatchers.IO) {
        val res = authService.login(LoginRequest.fromUser(user))
        if (!res.isSuccessful) return@withContext false

        val body = res.body()
        val token = body?.token
        val id = body?.id

        if (!token.isNullOrBlank() && id != null) {
            JwtStorage.saveToken(token)
            UserSessionStorage.saveUserId(id.toLong())
            true
        } else false
    }

    suspend fun register(user: User): Boolean = withContext(Dispatchers.IO) {
        val res = authService.register(RegisterRequest.fromUser(user))
        if (!res.isSuccessful) return@withContext false

        val body = res.body()
        val token = body?.token
        val id = body?.id

        if (id != null) {
            UserSessionStorage.saveUserId(id.toLong())
        }
        if (!token.isNullOrBlank()) {
            JwtStorage.saveToken(token)
        }
        true
    }

    suspend fun logout(): Boolean = withContext(Dispatchers.IO) {
        JwtStorage.clearToken()
        UserSessionStorage.clear()
        true
    }


    suspend fun currentUserId(): Long? = withContext(Dispatchers.IO) {
        UserSessionStorage.getUserId() ?: run {
            val raw = JwtStorage.getToken() ?: return@withContext null
            val token = raw.removePrefix("Bearer ").trim()
            decodeUserIdFromJwt(token)
        }
    }

    private fun decodeUserIdFromJwt(jwt: String): Long? {
        val parts = jwt.split('.')
        if (parts.size < 2) return null
        return try {
            val payload = padBase64Url(parts[1])
            val bytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP)
            val json = JSONObject(String(bytes, Charsets.UTF_8))
            when {
                json.has("userId") -> json.optLong("userId")
                json.has("uid") -> json.optLong("uid")
                json.has("id") -> json.optLong("id")
                json.has("sub") -> json.optString("sub").toLongOrNull()
                json.optJSONObject("user")?.has("id") == true ->
                    json.optJSONObject("user")!!.optLong("id")

                else -> null
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun padBase64Url(s: String): String {
        val rem = s.length % 4
        return if (rem == 0) s else s + "=".repeat(4 - rem)
    }
}
