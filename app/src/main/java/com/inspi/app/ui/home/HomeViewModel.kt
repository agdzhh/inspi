package com.inspi.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspi.app.data.preferences.InspiPreferences
import com.inspi.app.data.repository.ChallengeRepository
import com.inspi.app.data.repository.SubmissionRepository
import com.inspi.app.data.repository.UserRepository
import com.inspi.app.domain.models.*
import com.inspi.app.network.CoachApiService
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
    val retakeSubmission: Submission? = null,
    val weeklyInsight: String? = null,
    val isLoading: Boolean = true,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepo: UserRepository,
    private val challengeRepo: ChallengeRepository,
    private val submissionRepo: SubmissionRepository,
    private val coachApi: CoachApiService,
    private val prefs: InspiPreferences,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            val broken = userRepo.evaluateAndUpdateStreak()
            if (broken) _state.update { it.copy(streakBroken = true) }
        }

        viewModelScope.launch {
            userRepo.ensureProfileExists()
            userRepo.observeProfile()
                .filterNotNull()
                .flatMapLatest { profile ->
                    val task = TaskSelector.getTodayTask(profile.hobby)
                    val taskDone = StreakManager.completedToday(profile.lastSubmissionDate)

                    challengeRepo.ensureCurrentChallengeExists(profile.hobby)

                    challengeRepo.observeCurrentChallenge(profile.hobby)
                        .map { challenge ->
                            HomeUiState(
                                profile = profile,
                                todayTask = task,
                                weeklyChallenge = challenge,
                                taskCompletedToday = taskDone,
                                streakBroken = _state.value.streakBroken,
                                retakeSubmission = _state.value.retakeSubmission,
                                weeklyInsight = _state.value.weeklyInsight,
                                isLoading = false,
                            )
                        }
                }
                .collect { newState -> _state.value = newState }
        }

        viewModelScope.launch { loadRetakeCandidate() }
        viewModelScope.launch { loadOrGenerateWeeklyInsight() }
    }

    private suspend fun loadRetakeCandidate() {
        val profile = userRepo.getProfile() ?: return
        val retake = submissionRepo.getRetakeCandidate(profile.hobby)
        _state.update { it.copy(retakeSubmission = retake) }
    }

    private suspend fun loadOrGenerateWeeklyInsight() {
        val weekStart = StreakManager.currentWeekStart()
        val (storedText, storedWeekStart) = prefs.getWeeklyInsight()

        if (storedText != null && storedWeekStart == weekStart) {
            _state.update { it.copy(weeklyInsight = storedText) }
            return
        }

        val weeklySubmissions = submissionRepo.getWeeklySubmissions()
        if (weeklySubmissions.size < 3) return

        val profile = userRepo.getProfile() ?: return
        coachApi.generateWeeklyInsight(
            hobby = profile.hobby.displayName,
            taskTitles = weeklySubmissions.map { it.taskTitle },
            streak = profile.currentStreak,
        ).onSuccess { insight ->
            prefs.setWeeklyInsight(insight, weekStart)
            _state.update { it.copy(weeklyInsight = insight) }
        }
    }

    fun dismissStreakBroken() { _state.update { it.copy(streakBroken = false) } }
}