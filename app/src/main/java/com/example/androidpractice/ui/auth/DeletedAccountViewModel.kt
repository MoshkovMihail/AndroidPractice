package com.example.androidpractice.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.data.local.dao.UserDao
import com.example.androidpractice.data.session.SessionPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class DeletedAccountUiState(
    val isLoading: Boolean = false
)

class DeletedAccountViewModel(
    private val userId: Long,
    private val userDao: UserDao,
    private val sessionPrefs: SessionPrefs
) : ViewModel() {

    private val _state = MutableStateFlow(DeletedAccountUiState())
    val state: StateFlow<DeletedAccountUiState> = _state

    fun restore(onDone: () -> Unit) {
        viewModelScope.launch {
            _state.value = DeletedAccountUiState(isLoading = true)
            userDao.restoreAccount(userId)
            sessionPrefs.setUserId(userId)
            _state.value = DeletedAccountUiState(isLoading = false)
            onDone()
        }
    }

    fun deleteForever(onDone: () -> Unit) {
        viewModelScope.launch {
            _state.value = DeletedAccountUiState(isLoading = true)
            userDao.hardDeleteById(userId)
            sessionPrefs.clear()
            _state.value = DeletedAccountUiState(isLoading = false)
            onDone()
        }
    }
}
