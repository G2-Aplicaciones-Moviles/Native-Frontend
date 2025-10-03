package pe.edu.upc.jameofit.shared.data.local

import android.content.Context
import android.content.SharedPreferences

object JwtStorage {
    private const val PREF_NAME = "prefs"
    private const val KEY_JWT = "jwt"
    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_JWT, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_JWT, null)
    }

    fun clearToken() {
        prefs.edit().remove(KEY_JWT).apply()
    }
}