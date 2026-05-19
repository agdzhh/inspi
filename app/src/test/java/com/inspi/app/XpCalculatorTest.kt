package com.inspi.app.utils

import org.junit.Assert.*
import org.junit.Test

class XpCalculatorTest {

    @Test
    fun `daily task xp no multiplier below threshold`() {
        assertEquals(50, XpCalculator.dailyTaskXp(0))
        assertEquals(50, XpCalculator.dailyTaskXp(2))
    }

    @Test
    fun `daily task xp with multiplier at and above threshold`() {
        // 50 * 1.20 = 60
        assertEquals(60, XpCalculator.dailyTaskXp(3))
        assertEquals(60, XpCalculator.dailyTaskXp(10))
    }

    @Test
    fun `weekly challenge xp with multiplier`() {
        assertEquals(200, XpCalculator.weeklyChallengeXp(1))
        // 200 * 1.20 = 240
        assertEquals(240, XpCalculator.weeklyChallengeXp(5))
    }

    @Test
    fun `missed day penalty floors at zero`() {
        assertEquals(0, XpCalculator.applyMissedDayPenalty(50))
        assertEquals(0, XpCalculator.applyMissedDayPenalty(0))
        assertEquals(50, XpCalculator.applyMissedDayPenalty(150))
    }

    @Test
    fun `level from xp minimum level 1`() {
        assertEquals(1, XpCalculator.levelFromXp(0))
        assertEquals(1, XpCalculator.levelFromXp(499))
        assertEquals(2, XpCalculator.levelFromXp(500))
        assertEquals(3, XpCalculator.levelFromXp(1000))
    }

    @Test
    fun `xp progress fraction`() {
        assertEquals(0f, XpCalculator.xpProgressFraction(0), 0.01f)
        assertEquals(0.5f, XpCalculator.xpProgressFraction(250), 0.01f)
        assertEquals(0.0f, XpCalculator.xpProgressFraction(500), 0.01f)
    }
}
