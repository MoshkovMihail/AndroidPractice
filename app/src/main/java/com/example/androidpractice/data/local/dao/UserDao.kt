package com.example.androidpractice.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.androidpractice.data.local.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun hardDeleteById(id: Long)

    @Query("DELETE FROM users WHERE isDeleted = 1 AND deletedAt IS NOT NULL AND deletedAt < :thresholdMillis")
    suspend fun cleanupDeleted(thresholdMillis: Long)

    @Query("UPDATE users SET isDeleted = 0, deletedAt = NULL WHERE id = :userId")
    suspend fun restoreAccount(userId: Long)

    @Query(" UPDATE users SET isDeleted = 1, deletedAt = :deletedAt WHERE id = :userId")
    suspend fun softDeleteById(userId: Long, deletedAt: Long)
}
