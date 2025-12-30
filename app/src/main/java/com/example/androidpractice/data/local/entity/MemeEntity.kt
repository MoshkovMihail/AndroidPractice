package com.example.androidpractice.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "memes",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("userId"),
        Index("userId", "createdAt"),
        Index("userId", "title"),
        Index("userId", "isFavorite")
    ]
)
data class MemeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val userId: Long,
    val title: String,
    val imageUri: String,
    val createdAt: Long,
    val isFavorite: Boolean = false,
    val notes: String? = null
)
