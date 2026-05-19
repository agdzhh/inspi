package com.inspi.app.utils

object XpCalculator {

    private const val DAILY_TASK_XP        = 50
    private const val WEEKLY_CHALLENGE_XP  = 200
    private const val XP_MISSED_DAY_PENALTY = 100
    private const val STREAK_MULTIPLIER_THRESHOLD = 3
    private const val STREAK_MULTIPLIER   = 1.20f
    private const val XP_PER_LEVEL        = 500

    fun dailyTaskXp(currentStreak: Int): Int {
        val base = DAILY_TASK_XP
        return if (currentStreak >= STREAK_MULTIPLIER_THRESHOLD) {
            (base * STREAK_MULTIPLIER).toInt()
        } else {
            base
        }
    }

    fun weeklyChallengeXp(currentStreak: Int): Int {
        val base = WEEKLY_CHALLENGE_XP
        return if (currentStreak >= STREAK_MULTIPLIER_THRESHOLD) {
            (base * STREAK_MULTIPLIER).toInt()
        } else {
            base
        }
    }

    fun applyMissedDayPenalty(currentXp: Int): Int = maxOf(0, currentXp - XP_MISSED_DAY_PENALTY)

    fun levelFromXp(totalXp: Int): Int = maxOf(1, totalXp / XP_PER_LEVEL + 1)

    fun xpToNextLevel(totalXp: Int): Int = XP_PER_LEVEL - (totalXp % XP_PER_LEVEL)

    fun xpProgressFraction(totalXp: Int): Float = (totalXp % XP_PER_LEVEL) / XP_PER_LEVEL.toFloat()
}
