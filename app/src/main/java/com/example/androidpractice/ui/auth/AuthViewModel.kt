package com.example.androidpractice.ui.auth

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.R
import com.example.androidpractice.data.local.dao.UserDao
import com.example.androidpractice.data.local.entity.UserEntity
import com.example.androidpractice.data.session.SessionPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    @StringRes val errorRes: Int? = null
)

class AuthViewModel(
    private val userDao: UserDao,
    private val sessionPrefs: SessionPrefs
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun clearError() {
        _state.value = _state.value.copy(errorRes = null)
    }

    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onDeleted: (userId: Long) -> Unit
    ) {
        val e = email.trim()
        if (e.isEmpty() || password.isEmpty()) {
            _state.value = _state.value.copy(errorRes = R.string.error_enter_email_password)
            return
        }

        viewModelScope.launch {
            _state.value = AuthUiState(isLoading = true)

            val sevenDays = 7L * 24 * 60 * 60 * 1000
            userDao.cleanupDeleted(System.currentTimeMillis() - sevenDays)

            val user = userDao.getByEmail(e)
            when {
                user == null -> _state.value = AuthUiState(errorRes = R.string.error_user_not_found)
                user.passwordHash != password -> _state.value = AuthUiState(errorRes = R.string.error_wrong_password)
                user.isDeleted -> {
                    val deletedAt = user.deletedAt ?: 0L
                    val now = System.currentTimeMillis()
                    if (deletedAt != 0L && now - deletedAt < sevenDays) {
                        _state.value = AuthUiState()
                        onDeleted(user.id)
                    } else {
                        // safety: if stale deleted user slipped past cleanup
                        userDao.hardDeleteById(user.id)
                        _state.value = AuthUiState(errorRes = R.string.error_user_not_found)
                    }
                }
                else -> {
                    sessionPrefs.setUserId(user.id)
                    _state.value = AuthUiState()
                    onSuccess()
                }
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        confirm: String,
        onSuccess: () -> Unit
    ) {
        val n = name.trim()
        val e = email.trim()

        when {
            n.isEmpty() -> {
                _state.value = _state.value.copy(errorRes = R.string.error_enter_name)
                return
            }
            !isValidEmail(e) -> {
                _state.value = _state.value.copy(errorRes = R.string.error_invalid_email)
                return
            }
            password.length < 4 -> {
                _state.value = _state.value.copy(errorRes = R.string.error_password_too_short)
                return
            }
            password != confirm -> {
                _state.value = _state.value.copy(errorRes = R.string.error_passwords_not_match)
                return
            }
        }

        viewModelScope.launch {
            _state.value = AuthUiState(isLoading = true)

            val existing = userDao.getByEmail(e)
            if (existing != null) {
                _state.value = AuthUiState(errorRes = R.string.error_email_taken)
                return@launch
            }

            val newId = userDao.insert(
                UserEntity(
                    name = n,
                    email = e,
                    passwordHash = password
                )
            )
            sessionPrefs.setUserId(newId)
            _state.value = AuthUiState()
            onSuccess()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        if (email.isBlank()) return false
        val at = email.indexOf('@')
        val dot = email.lastIndexOf('.')
        return at > 0 && dot > at + 1 && dot < email.length - 1
    }
}
