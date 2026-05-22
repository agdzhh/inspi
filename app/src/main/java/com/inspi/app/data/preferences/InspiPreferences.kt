package com.inspi.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "inspi_prefs")

@Singleton
class InspiPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val HOBBY                    = stringPreferencesKey("hobby")
        val USERNAME                 = stringPreferencesKey("username")
        val ONBOARDING_COMPLETE      = booleanPreferencesKey("onboarding_complete")
        val NOTIFICATIONS_ON         = booleanPreferencesKey("notifications_on")
        val REMINDER_HOUR            = intPreferencesKey("reminder_hour")
        val REMINDER_MINUTE          = intPreferencesKey("reminder_minute")
        val WEEKLY_INSIGHT_TEXT      = stringPreferencesKey("weekly_insight_text")
        val WEEKLY_INSIGHT_WEEK_START = longPreferencesKey("weekly_insight_week_start")
        val USER_CODE                = stringPreferencesKey("user_code")
        val PROFILE_PHOTO_URI        = stringPreferencesKey("profile_photo_uri")
    }

    val hobby: Flow<String?> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.HOBBY] }

    val username: Flow<String?> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.USERNAME] }

    val isOnboardingComplete: Flow<Boolean> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.ONBOARDING_COMPLETE] ?: false }

    val notificationsOn: Flow<Boolean> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.NOTIFICATIONS_ON] ?: true }

    val reminderHour: Flow<Int> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.REMINDER_HOUR] ?: 9 }

    val reminderMinute: Flow<Int> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.REMINDER_MINUTE] ?: 0 }

    val profilePhotoUri: Flow<String?> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.PROFILE_PHOTO_URI] }

    suspend fun setHobby(hobby: String) {
        context.dataStore.edit { it[Keys.HOBBY] = hobby }
    }

    suspend fun setUsername(name: String) {
        context.dataStore.edit { it[Keys.USERNAME] = name }
    }

    suspend fun setOnboardingComplete() {
        context.dataStore.edit { it[Keys.ONBOARDING_COMPLETE] = true }
    }

    suspend fun setNotificationsOn(on: Boolean) {
        context.dataStore.edit { it[Keys.NOTIFICATIONS_ON] = on }
    }

    suspend fun setProfilePhotoUri(uri: String?) {
        context.dataStore.edit {
            if (uri != null) it[Keys.PROFILE_PHOTO_URI] = uri
            else it.remove(Keys.PROFILE_PHOTO_URI)
        }
    }

    suspend fun setReminderTime(hour: Int, minute: Int) {
        context.dataStore.edit {
            it[Keys.REMINDER_HOUR] = hour
            it[Keys.REMINDER_MINUTE] = minute
        }
    }

    suspend fun getWeeklyInsight(): Pair<String?, Long> {
        val prefs = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it }.first()
        return prefs[Keys.WEEKLY_INSIGHT_TEXT] to (prefs[Keys.WEEKLY_INSIGHT_WEEK_START] ?: 0L)
    }

    suspend fun setWeeklyInsight(text: String, weekStart: Long) {
        context.dataStore.edit {
            it[Keys.WEEKLY_INSIGHT_TEXT] = text
            it[Keys.WEEKLY_INSIGHT_WEEK_START] = weekStart
        }
    }

    suspend fun getOrCreateUserCode(): String {
        val prefs = context.dataStore.data.catch { emit(emptyPreferences()) }.map { it }.first()
        val existing = prefs[Keys.USER_CODE]
        if (!existing.isNullOrBlank()) return existing
        val generated = buildString {
            val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
            repeat(6) { append(chars.random()) }
        }
        context.dataStore.edit { it[Keys.USER_CODE] = generated }
        return generated
    }
}
