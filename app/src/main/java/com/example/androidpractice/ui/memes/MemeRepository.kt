package com.example.androidpractice.data.memes

import androidx.annotation.StringRes
import com.example.androidpractice.R
import com.example.androidpractice.data.local.dao.MemeDao
import com.example.androidpractice.data.local.entity.MemeEntity
import com.example.androidpractice.ui.memes.MemeSortOption
import kotlinx.coroutines.flow.Flow

class MemeRepository(
    private val memeDao: MemeDao
) {

    fun observeMemes(userId: Long, sort: MemeSortOption): Flow<List<MemeEntity>> =
        when (sort) {
            MemeSortOption.NEWEST -> memeDao.observeNewest(userId)
            MemeSortOption.TITLE -> memeDao.observeTitle(userId)
            MemeSortOption.FAVORITES_FIRST -> memeDao.observeFavoritesFirst(userId)
        }

    suspend fun addMeme(
        userId: Long,
        title: String,
        imageUri: String,
        notes: String?
    ) {
        memeDao.insert(
            MemeEntity(
                userId = userId,
                title = title.trim(),
                imageUri = imageUri,
                createdAt = System.currentTimeMillis(),
                notes = notes?.trim()?.takeIf { it.isNotBlank() }
            )
        )
    }

    suspend fun toggleFavorite(memeId: Long, current: Boolean) {
        memeDao.setFavorite(memeId, !current)
    }

    suspend fun seedIfEmpty(
        userId: Long,
        packageName: String,
        resolveTitle: (Int) -> String
    ) {
        if (memeDao.countByUser(userId) > 0) return

        val now = System.currentTimeMillis()

        val seeds = listOf(
            Seed(R.string.mem_01, "android.resource://$packageName/drawable/mem_01", isFavorite = true),
            Seed(R.string.mem_02, "android.resource://$packageName/drawable/mem_02"),
            Seed(R.string.mem_03, "android.resource://$packageName/drawable/mem_03"),
            Seed(R.string.mem_04, "android.resource://$packageName/drawable/mem_04", isFavorite = true),
            Seed(R.string.mem_05, "android.resource://$packageName/drawable/mem_05")
        )

        seeds.forEachIndexed { index, s ->
            memeDao.insert(
                MemeEntity(
                    userId = userId,
                    title = resolveTitle(s.titleRes),
                    imageUri = s.uri,
                    createdAt = now - index * 60_000L,
                    isFavorite = s.isFavorite,
                    notes = null
                )
            )
        }
    }

    private data class Seed(
        @StringRes val titleRes: Int,
        val uri: String,
        val isFavorite: Boolean = false
    )
}

