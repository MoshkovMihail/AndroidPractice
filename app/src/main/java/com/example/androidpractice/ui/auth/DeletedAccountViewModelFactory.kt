package com.example.androidpractice.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidpractice.data.local.dao.UserDao
import com.example.androidpractice.data.session.SessionPrefs

class DeletedAccountViewModelFactory(
    private val userId: Long,
    private val userDao: UserDao,
    private val sessionPrefs: SessionPrefs
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeletedAccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeletedAccountViewModel(userId, userDao, sessionPrefs) as T
        }
        throw IllegalArgumentException()
    }
}
