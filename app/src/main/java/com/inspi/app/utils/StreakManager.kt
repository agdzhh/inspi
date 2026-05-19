package com.inspi.app.utils

import java.util.Calendar
import java.util.TimeZone

object StreakManager {

    /**
     * Returns the start-of-day epoch millis for the given epoch in local timezone.
     */
    fun startOfDay(epochMillis: Long): Long {
        val cal = Calendar.getInstance()
        cal.timeInMillis = epochMillis
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    fun todayStartMillis(): Long = startOfDay(System.currentTimeMillis())

    /** Gap in calendar days between two epoch millis (local timezone). */
    fun daysBetween(olderMillis: Long, newerMillis: Long): Int {
        val older = startOfDay(olderMillis)
        val newer = startOfDay(newerMillis)
        val diff = newer - older
        return (diff / (24 * 60 * 60 * 1000L)).toInt()
    }

    /**
     * Check whether today's task was already completed.
     */
    fun completedToday(lastSubmissionDate: Long): Boolean {
        if (lastSubmissionDate == 0L) return false
        return daysBetween(lastSubmissionDate, System.currentTimeMillis()) == 0
    }

    data class StreakUpdate(
        val newStreak: Int,
        val newLongest: Int,
        val newXp: Int,
        val streakBroken: Boolean,
    )

    /**
     * Called on every app open.
     * Returns updated streak/xp values based on the gap since last submission.
     */
    fun evaluateStreak(
        currentStreak: Int,
        longestStreak: Int,
        currentXp: Int,
        lastSubmissionDate: Long,
    ): StreakUpdate {
        if (lastSubmissionDate == 0L) {
            // Fresh user, no streak yet
            return StreakUpdate(0, longestStreak, currentXp, false)
        }
        val gap = daysBetween(lastSubmissionDate, System.currentTimeMillis())
        return when {
            gap <= 1 -> StreakUpdate(currentStreak, longestStreak, currentXp, false) // streak intact
            else -> {
                // Missed ≥1 day → reset streak, apply XP penalty
                val penalisedXp = XpCalculator.applyMissedDayPenalty(currentXp)
                StreakUpdate(0, longestStreak, penalisedXp, true)
            }
        }
    }

    /** Monday 00:00 local time epoch millis for the current week. */
    fun currentWeekStart(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val daysToMonday = (dayOfWeek - Calendar.MONDAY + 7) % 7
        cal.add(Calendar.DAY_OF_YEAR, -daysToMonday)
        return cal.timeInMillis
    }
}
