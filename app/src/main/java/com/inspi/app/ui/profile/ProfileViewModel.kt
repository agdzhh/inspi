package com.inspi.app.ui.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val isLoading: Boolean = true,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val submissionRepo: SubmissionRepository,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                userRepo.observeProfile(),
                userRepo.observeNotifications(),
            ) { profile, notif ->
                val count = submissionRepo.count()
                ProfileUiState(
                    profile = profile,
                    totalSubmissions = count,
                    notificationsOn = notif,
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
}
