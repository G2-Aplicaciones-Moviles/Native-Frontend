package pe.edu.upc.jameofit.iam.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.iam.data.repository.AuthRepository
import pe.edu.upc.jameofit.iam.domain.model.User

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val _loginSuccess = MutableStateFlow<Boolean?>(null)
    val loginSuccess: StateFlow<Boolean?> = _loginSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun updateUsername(username: String) {
        _user.value = _user.value.copy(username = username)
    }

    fun updatePassword(password: String) {
        _user.value = _user.value.copy(password = password)
    }

    fun clearUser() {
        _user.value = User()
    }

    fun login() {
        val username = _user.value.username
        val password = _user.value.password

        if (username.isBlank() || username.length < 5) {
            _errorMessage.value = "El nombre de usuario debe tener al menos 5 caracteres"
            return
        }
        if (password.isBlank() || password.length < 3) {
            _errorMessage.value = "La contraseña debe tener al menos 3 caracteres."
            return
        }

        viewModelScope.launch {
            try {
                _loginSuccess.value = authRepository.login(_user.value)
                if (_loginSuccess.value != true) {
                    _errorMessage.value = "Nombre de usuario o contraseña incorrectos."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de autenticación: ${e.message}"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            clearUser()
        }
    }

    fun register() {
        val username = _user.value.username
        val password = _user.value.password

        if (username.isBlank() || username.length < 5) {
            _errorMessage.value = "El nombre de usuario debe tener al menos 5 caracteres"
            return
        }
        if (password.isBlank() || password.length < 6) {
            _errorMessage.value = "La contraseña debe tener al menos 6 caracteres."
            return
        }

        viewModelScope.launch {
            try {
                val ok = authRepository.register(_user.value)
                _loginSuccess.value = ok
                if (!ok) _errorMessage.value = "No se pudo registrar."
            } catch (e: Exception) {
                _errorMessage.value = "Error de registro: ${e.message}"
            }
        }
    }

    fun resetLoginSuccess() {
        _loginSuccess.value = null
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }
}