package com.inspi.app.ui.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspi.app.data.repository.CoachRepository
import com.inspi.app.data.repository.UserRepository
import com.inspi.app.domain.models.CoachMessage
import com.inspi.app.domain.models.MessageRole
import com.inspi.app.network.CoachApiService
import com.inspi.app.utils.TaskSelector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CoachUiState(
    val messages: List<CoachMessage> = emptyList(),
    val isTyping: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class CoachViewModel @Inject constructor(
    private val coachRepo: CoachRepository,
    private val userRepo: UserRepository,
    private val coachApi: CoachApiService,   // ← now CoachApiService, not ClaudeApiService
) : ViewModel() {

    private val _state = MutableStateFlow(CoachUiState())
    val state = _state.asStateFlow()

    private var sendDebounced = false

    init {
        viewModelScope.launch {
            coachRepo.observeMessages().collect { msgs ->
                _state.update { it.copy(messages = msgs) }
            }
        }
        viewModelScope.launch {
            if (coachRepo.getHistory().isEmpty()) {
                insertAssistantMessage(
                    "Hey! I'm Inspi Coach 👋 I'm here to help you grow your creative skills. " +
                    "What would you like to work on today?"
                )
            }
        }
    }

    fun sendMessage(text: String) {
        if (sendDebounced || text.isBlank()) return
        sendDebounced = true

        viewModelScope.launch {
            try {
                coachRepo.insertMessage(
                    CoachMessage(0, MessageRole.USER, text.trim(), System.currentTimeMillis())
                )
                _state.update { it.copy(isTyping = true, error = null) }

                val profile = userRepo.getProfile()
                val hobby   = profile?.hobby?.displayName ?: "creative"
                val task    = profile?.let { TaskSelector.getTodayTask(it.hobby).title } ?: "daily task"
                val streak  = profile?.currentStreak ?: 0
                val history = coachRepo.getHistory()

                coachApi.sendMessage(hobby, task, streak, history, text.trim())
                    .fold(
                        onSuccess = { reply -> insertAssistantMessage(reply) },
                        onFailure = { err ->
                            android.util.Log.e("CoachVM", "Gemini error", err)
                            _state.update { s -> s.copy(error = "Coach is unavailable right now — try again later.") }
                        }
                    )
            } finally {
                _state.update { it.copy(isTyping = false) }
                kotlinx.coroutines.delay(500)
                sendDebounced = false
            }
        }
    }

    fun clearConversation() {
        viewModelScope.launch {
            coachRepo.clearAll()
            insertAssistantMessage("Fresh start! What would you like to work on? 😊")
        }
    }

    fun dismissError() { _state.update { it.copy(error = null) } }

    fun retryLastMessage() {
        val lastUser = _state.value.messages.lastOrNull { it.role == MessageRole.USER } ?: return
        sendMessage(lastUser.content)
    }

    private suspend fun insertAssistantMessage(text: String) {
        coachRepo.insertMessage(
            CoachMessage(0, MessageRole.ASSISTANT, text, System.currentTimeMillis())
        )
    }
}
