package com.example.androidpractice.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.androidpractice.data.local.entity.MemeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MemeDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(meme: MemeEntity): Long

    @Query("SELECT COUNT(*) FROM memes WHERE userId = :userId")
    suspend fun countByUser(userId: Long): Int

    @Query("SELECT * FROM memes WHERE userId = :userId ORDER BY createdAt DESC")
    fun observeNewest(userId: Long): Flow<List<MemeEntity>>

    @Query("SELECT * FROM memes WHERE userId = :userId ORDER BY title ASC")
    fun observeTitle(userId: Long): Flow<List<MemeEntity>>

    @Query("SELECT * FROM memes WHERE userId = :userId ORDER BY isFavorite DESC, createdAt DESC")
    fun observeFavoritesFirst(userId: Long): Flow<List<MemeEntity>>

    @Query("UPDATE memes SET isFavorite = :value WHERE id = :memeId")
    suspend fun setFavorite(memeId: Long, value: Boolean)

    @Query("SELECT COUNT(*) FROM memes WHERE userId = :userId AND isFavorite = 1")
    suspend fun countFavoritesByUser(userId: Long): Int

}