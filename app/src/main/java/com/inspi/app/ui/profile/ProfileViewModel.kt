package com.inspi.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspi.app.data.repository.SubmissionRepository
import com.inspi.app.data.repository.UserRepository
import com.inspi.app.domain.models.UserProfile
import dagger.hilt.android.lifecycle.HiltViewModel
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
        viewModelScope.launch { userRepo.setNotificationsOn(on) }
    }
}
