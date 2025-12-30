package com.example.androidpractice.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidpractice.data.local.dao.MemeDao
import com.example.androidpractice.data.local.dao.UserDao

class ProfileViewModelFactory(
    private val userId: Long,
    private val userDao: UserDao,
    private val memeDao: MemeDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(userId, userDao, memeDao) as T
        }
        throw IllegalArgumentException()
    }
}
