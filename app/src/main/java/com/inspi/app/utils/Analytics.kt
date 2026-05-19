package com.inspi.app.utils

/**
 * Analytics stub — wire up a real provider (Firebase, Mixpanel, etc.) in a future version.
 */
interface Analytics {
    fun logTaskCompleted(hobbyType: String, taskTitle: String)
    fun logStreakUpdated(newStreak: Int)
    fun logChallengeCompleted(hobbyType: String, title: String)
    fun logHobbyChanged(newHobby: String)
}

/** No-op implementation used at runtime until analytics is wired. */
class NoOpAnalytics : Analytics {
    override fun logTaskCompleted(hobbyType: String, taskTitle: String) = Unit
    override fun logStreakUpdated(newStreak: Int) = Unit
    override fun logChallengeCompleted(hobbyType: String, title: String) = Unit
    override fun logHobbyChanged(newHobby: String) = Unit
}
