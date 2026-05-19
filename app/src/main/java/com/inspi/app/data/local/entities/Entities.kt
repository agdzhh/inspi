package com.inspi.app.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "Creative",
    val hobby: String = "",           // "PHOTOGRAPHY" | "DRAWING"
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastSubmissionDate: Long = 0L // epoch millis
)

@Entity(tableName = "submissions")
data class SubmissionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val imagePath: String,
    val thumbnailPath: String,
    val taskTitle: String,
    val createdAt: Long,              // epoch millis
    val hobbyType: String,
    val xpEarned: Int
)

@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val hobbyType: String,
    val title: String,
    val description: String,
    val weekStartDate: Long,          // Monday 00:00 UTC, epoch millis
    val isCompleted: Boolean = false
)

@Entity(tableName = "coach_messages")
data class CoachMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val role: String,                 // "user" | "assistant"
    val content: String,
    val createdAt: Long               // epoch millis
)
