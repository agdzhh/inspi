package com.inspi.app.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspi.app.data.preferences.InspiPreferences
import com.inspi.app.data.repository.SubmissionRepository
import com.inspi.app.data.repository.UserRepository
import com.inspi.app.domain.models.UserProfile
import com.inspi.app.utils.DailyReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val profile: UserProfile? = null,
    val totalSubmissions: Int = 0,
    val notificationsOn: Boolean = true,
    val profilePhotoUri: String? = null,
    val isLoading: Boolean = true,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val submissionRepo: SubmissionRepository,
    private val prefs: InspiPreferences,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                userRepo.observeProfile(),
                userRepo.observeNotifications(),
                prefs.profilePhotoUri,
            ) { profile, notif, photoUri ->
                val count = submissionRepo.count()
                ProfileUiState(
                    profile = profile,
                    totalSubmissions = count,
                    notificationsOn = notif,
                    profilePhotoUri = photoUri,
                    isLoading = false,
                )
            }.collect { s -> _state.value = s }
        }
    }

    fun setNotifications(on: Boolean) {
        viewModelScope.launch {
            userRepo.setNotificationsOn(on)
            if (on) DailyReminderWorker.schedule(context)
            else DailyReminderWorker.cancel(context)
        }
    }

    fun updateUsername(newName: String) {
        val trimmed = newName.trim().take(20)
        if (trimmed.isBlank()) return
        viewModelScope.launch {
            val profile = userRepo.getProfile() ?: return@launch
            userRepo.updateProfile(profile.copy(username = trimmed))
        }
    }

    fun updateProfilePhoto(uriString: String?) {
        viewModelScope.launch {
            prefs.setProfilePhotoUri(uriString)
        }
    }
}