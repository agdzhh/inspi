package com.inspi.app.domain.models

data class UserProfile(
    val username: String,
    val hobby: HobbyType,
    val totalXp: Int,
    val currentStreak: Int,
    val longestStreak: Int,
    val lastSubmissionDate: Long,
    val level: Int = totalXp / 500 + 1,
    val xpToNextLevel: Int = 500 - (totalXp % 500),
    val xpProgressFraction: Float = (totalXp % 500) / 500f,
)

data class DailyTask(
    val title: String,
    val description: String,
    val hobbyType: HobbyType,
    val xpReward: Int = 50,
)

data class WeeklyChallenge(
    val id: Long,
    val title: String,
    val description: String,
    val daysRemaining: Int,
    val isCompleted: Boolean,
)

data class Submission(
    val id: Long,
    val imagePath: String,
    val thumbnailPath: String,
    val taskTitle: String,
    val createdAt: Long,
    val hobbyType: HobbyType,
    val xpEarned: Int,
)

data class CoachMessage(
    val id: Long,
    val role: MessageRole,
    val content: String,
    val createdAt: Long,
)

enum class MessageRole { USER, ASSISTANT }

enum class HobbyType(val displayName: String) {
    PHOTOGRAPHY("Photography"),
    DRAWING("Drawing");

    companion object {
        fun fromString(s: String) = entries.firstOrNull { it.name == s } ?: PHOTOGRAPHY
    }
}

data class Friend(
    val code: String,
    val username: String,
    val hobby: HobbyType,
    val weeklyXp: Int,
    val currentStreak: Int,
    val avatarUrl: String = "",
)

data class LeaderboardEntry(
    val code: String,
    val username: String,
    val hobby: HobbyType,
    val weeklyXp: Int,
    val currentStreak: Int,
    val isMe: Boolean,
    val rank: Int = 0,
    val avatarUrl: String = "",
)
