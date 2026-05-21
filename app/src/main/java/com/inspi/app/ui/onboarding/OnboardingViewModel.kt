package com.inspi.app.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspi.app.data.preferences.InspiPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val prefs: InspiPreferences,
) : ViewModel() {
    /** true — онбординг уже пройден, сразу идём на Home */
    val isOnboardingComplete = prefs.isOnboardingComplete
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}