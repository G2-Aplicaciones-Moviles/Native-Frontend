package pe.edu.upc.jameofit.shared.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object UserSessionStorage {
    private const val PREF_NAME = "pref_session"
    private const val KEY_USER_ID = "user_id"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserId(id: Long) {
        prefs.edit { putLong(KEY_USER_ID, id) }
    }

    fun getUserId(): Long? {
        if (!::prefs.isInitialized) return null
        val v = prefs.getLong(KEY_USER_ID, Long.MIN_VALUE)
        return if (v == Long.MIN_VALUE) null else v
    }

    fun clear() {
        if (!::prefs.isInitialized) return
        prefs.edit { remove(KEY_USER_ID) }
    }
}
