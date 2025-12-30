package com.example.androidpractice.ui.memes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidpractice.data.memes.MemeRepository

class MemeListViewModelFactory(
    private val userId: Long,
    private val repo: MemeRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MemeListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MemeListViewModel(userId, repo) as T
        }
        throw IllegalArgumentException()
    }
}
