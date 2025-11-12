package pe.edu.upc.jameofit.iam.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pe.edu.upc.jameofit.iam.domain.model.User
import pe.edu.upc.jameofit.iam.data.repository.AuthRepository
import pe.edu.upc.jameofit.shared.data.local.JwtStorage

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _loginSuccess = MutableStateFlow<Boolean?>(null)
    val loginSuccess: StateFlow<Boolean?> = _loginSuccess

    private val _registerSuccess = MutableStateFlow<Boolean?>(null)
    val registerSuccess: StateFlow<Boolean?> = _registerSuccess

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun login(user: User, onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
        _loading.value = true
        viewModelScope.launch {
            val result = repository.login(user)
            _loading.value = false
            if (result != null) {
                _user.value = result  // 🚨 ahora usamos el User con id real
                _loginSuccess.value = true
                onSuccess()
            } else {
                _loginSuccess.value = false
                _errorMessage.value = "Usuario o contraseña incorrectos"
                onError()
            }
        }
    }

    fun register(user: User, onSuccess: () -> Unit = {}, onError: () -> Unit = {}) {
        _loading.value = true
        viewModelScope.launch {
            val result = repository.register(user) // devuelve User con id real
            _loading.value = false
            if (result != null) {
                _user.value = result  // ✅ usamos el id real
                _registerSuccess.value = true
                onSuccess()
            } else {
                _registerSuccess.value = false
                _errorMessage.value = "No se pudo registrar"
                onError()
            }
        }
    }

    fun logout() {
        JwtStorage.clearToken()
        _user.value = User()
    }

    fun resetLoginSuccess() { _loginSuccess.value = null }
    fun resetRegisterSuccess() { _registerSuccess.value = null }
    fun resetErrorMessage() { _errorMessage.value = null }

    fun updateUsername(value: String) { _user.value = _user.value.copy(username = value) }
    fun updatePassword(value: String) { _user.value = _user.value.copy(password = value) }
}
