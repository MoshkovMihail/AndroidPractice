package com.example.androidpractice.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.R
import com.example.androidpractice.data.local.dao.MemeDao
import com.example.androidpractice.data.local.dao.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = true,
    val name: String = "",
    val email: String = "",
    val memesCount: Int = 0,
    val favoritesCount: Int = 0,
    val errorRes: Int? = null
)


class ProfileViewModel(
    private val userId: Long,
    private val userDao: UserDao,
    private val memeDao: MemeDao
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.value = ProfileUiState(isLoading = true)

            val user = userDao.getById(userId)
            if (user == null) {
                _state.value = ProfileUiState(
                    isLoading = false,
                    errorRes = R.string.user_not_found
                )
                return@launch
            }

            val memesCount = memeDao.countByUser(userId)
            val favoritesCount = memeDao.countFavoritesByUser(userId)

            _state.value = ProfileUiState(
                isLoading = false,
                name = user.name,
                email = user.email,
                memesCount = memesCount,
                favoritesCount = favoritesCount
            )
        }
    }

    fun deleteAccount(onDone: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            userDao.softDeleteById(userId, System.currentTimeMillis())
            onDone()
        }
    }
}
