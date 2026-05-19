package com.inspi.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspi.app.data.repository.ChallengeRepository
import com.inspi.app.data.repository.UserRepository
import com.inspi.app.domain.models.*
import com.inspi.app.utils.StreakManager
import com.inspi.app.utils.TaskSelector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val profile: UserProfile? = null,
    val todayTask: DailyTask? = null,
    val weeklyChallenge: WeeklyChallenge? = null,
    val taskCompletedToday: Boolean = false,
    val streakBroken: Boolean = false,
    val isLoading: Boolean = true,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val challengeRepo: ChallengeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val broken = userRepo.evaluateAndUpdateStreak()
            if (broken) _state.update { it.copy(streakBroken = true) }
        }

        viewModelScope.launch {
            userRepo.observeProfile()
                .filterNotNull()
                .flatMapLatest { profile ->
                    val task = TaskSelector.getTodayTask(profile.hobby)
                    val taskDone = StreakManager.completedToday(profile.lastSubmissionDate)

                    // Ensure challenge row exists for this week
                    challengeRepo.ensureCurrentChallengeExists(profile.hobby)

                    challengeRepo.observeCurrentChallenge(profile.hobby)
                        .map { challenge ->
                            HomeUiState(
                                profile = profile,
                                todayTask = task,
                                weeklyChallenge = challenge,
                                taskCompletedToday = taskDone,
                                streakBroken = _state.value.streakBroken,
                                isLoading = false,
                            )
                        }
                }
                .collect { newState -> _state.value = newState }
        }
    }

    fun dismissStreakBroken() { _state.update { it.copy(streakBroken = false) } }
}
