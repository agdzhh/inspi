package com.inspi.app.utils

import org.junit.Assert.*
import org.junit.Test
import java.util.concurrent.TimeUnit

class StreakManagerTest {

    @Test
    fun `fresh user has no streak broken`() {
        val result = StreakManager.evaluateStreak(0, 0, 0, 0L)
        assertFalse(result.streakBroken)
        assertEquals(0, result.newStreak)
    }

    @Test
    fun `streak continues when gap is 1 day`() {
        val yesterday = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        val result = StreakManager.evaluateStreak(5, 5, 300, yesterday)
        assertFalse(result.streakBroken)
        assertEquals(5, result.newStreak)
        assertEquals(300, result.newXp)
    }

    @Test
    fun `streak breaks when gap is more than 1 day`() {
        val twoDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2)
        val result = StreakManager.evaluateStreak(7, 7, 500, twoDaysAgo)
        assertTrue(result.streakBroken)
        assertEquals(0, result.newStreak)
        // 500 - 100 = 400
        assertEquals(400, result.newXp)
    }

    @Test
    fun `completed today true if submitted today`() {
        val now = System.currentTimeMillis()
        assertTrue(StreakManager.completedToday(now))
    }

    @Test
    fun `completed today false if not submitted`() {
        assertFalse(StreakManager.completedToday(0L))
    }

    @Test
    fun `completed today false if submitted yesterday`() {
        val yesterday = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1) - 1
        assertFalse(StreakManager.completedToday(yesterday))
    }
}
