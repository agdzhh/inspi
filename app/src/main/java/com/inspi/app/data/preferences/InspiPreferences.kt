package com.inspi.app.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "inspi_prefs")

@Singleton
class InspiPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val HOBBY              = stringPreferencesKey("hobby")
        val NOTIFICATIONS_ON   = booleanPreferencesKey("notifications_on")
        val REMINDER_HOUR      = intPreferencesKey("reminder_hour")
        val REMINDER_MINUTE    = intPreferencesKey("reminder_minute")
    }

    val hobby: Flow<String?> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.HOBBY] }

    val notificationsOn: Flow<Boolean> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.NOTIFICATIONS_ON] ?: true }

    val reminderHour: Flow<Int> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.REMINDER_HOUR] ?: 9 }

    val reminderMinute: Flow<Int> = context.dataStore.data
        .catch { emit(emptyPreferences()) }
        .map { it[Keys.REMINDER_MINUTE] ?: 0 }

    suspend fun setHobby(hobby: String) {
        context.dataStore.edit { it[Keys.HOBBY] = hobby }
    }

    suspend fun setNotificationsOn(on: Boolean) {
        context.dataStore.edit { it[Keys.NOTIFICATIONS_ON] = on }
    }

    suspend fun setReminderTime(hour: Int, minute: Int) {
        context.dataStore.edit {
            it[Keys.REMINDER_HOUR] = hour
            it[Keys.REMINDER_MINUTE] = minute
        }
    }
}
