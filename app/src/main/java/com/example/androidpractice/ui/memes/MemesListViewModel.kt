package com.example.androidpractice.ui.memes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidpractice.data.local.entity.MemeEntity
import com.example.androidpractice.data.memes.MemeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

data class MemeListUiState(
    val sort: MemeSortOption = MemeSortOption.NEWEST,
    val isSeeding: Boolean = false
)

class MemeListViewModel(
    private val userId: Long,
    private val repo: MemeRepository
) : ViewModel() {

    private val _ui = MutableStateFlow(MemeListUiState())
    val ui: StateFlow<MemeListUiState> = _ui.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val memes: StateFlow<List<MemeEntity>> =
        _ui
            .map { it.sort }
            .distinctUntilChanged()
            .flatMapLatest { sort -> repo.observeMemes(userId, sort) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun setSort(sort: MemeSortOption) {
        _ui.value = _ui.value.copy(sort = sort)
    }

    suspend fun seedIfEmpty(
        packageName: String,
        resolveTitle: (Int) -> String
    ) {
        repo.seedIfEmpty(
            userId = userId,
            packageName = packageName,
            resolveTitle = resolveTitle
        )
    }
}
