package com.inspi.app.ui.coach

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspi.app.data.repository.CoachRepository
import com.inspi.app.data.repository.SubmissionRepository
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
    private val submissionRepo: SubmissionRepository,
    private val coachApi: CoachApiService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(CoachUiState())
    val state = _state.asStateFlow()

    private val submissionId: Long = savedStateHandle.get<Long>("submissionId") ?: -1L

    private var sendDebounced = false
    private var activeRequestJob: kotlinx.coroutines.Job? = null


    init {
        viewModelScope.launch {
            coachRepo.observeMessages().collect { msgs ->
                _state.update { it.copy(messages = msgs) }
            }
        }
        viewModelScope.launch {
            if (submissionId > 0L) {
                triggerImageCritique(submissionId)
            } else if (coachRepo.getHistory().isEmpty()) {
                insertAssistantMessage(
                    "Hey! I'm Inspi Coach 👋 I'm here to help you grow your creative skills. " +
                            "What would you like to work on today?"
                )
            }
        }
    }

    private suspend fun triggerImageCritique(id: Long) {
        val submission = submissionRepo.getById(id) ?: return
        val profile = userRepo.getProfile()
        val hobby = profile?.hobby?.displayName ?: "creative"
        val streak = profile?.currentStreak ?: 0

        // Показываем фото как будто пользователь его отправил в чат
        coachRepo.insertMessage(
            CoachMessage(
                id = 0,
                role = MessageRole.USER,
                content = "[img]${submission.imagePath}",
                createdAt = System.currentTimeMillis()
            )
        )

        _state.update { it.copy(isTyping = true, error = null) }
        try {
            coachApi.sendImageCritique(submission.imagePath, submission.taskTitle, hobby, streak)
                .fold(
                    onSuccess = { reply -> insertAssistantMessage(reply) },
                    onFailure = {
                        _state.update { s ->
                            s.copy(error = "Coach couldn't review your image — ask me anything directly!")
                        }
                    }
                )
        } finally {
            _state.update { it.copy(isTyping = false) }
        }
    }

    fun sendMessage(text: String) {
        val cleanText = text.trim()

        if (cleanText.isBlank()) return
        if (_state.value.isTyping) return

        activeRequestJob?.cancel()

        activeRequestJob = viewModelScope.launch {
            try {
                coachRepo.insertMessage(
                    CoachMessage(
                        id = 0,
                        role = MessageRole.USER,
                        content = cleanText,
                        createdAt = System.currentTimeMillis()
                    )
                )

                _state.update {
                    it.copy(
                        isTyping = true,
                        error = null
                    )
                }

                val profile = userRepo.getProfile()

                val hobby = profile?.hobby?.displayName ?: "creative"
                val task = profile?.let {
                    TaskSelector.getTodayTask(it.hobby).title
                } ?: "daily task"

                val streak = profile?.currentStreak ?: 0

                val history = coachRepo.getHistory()

                val result = coachApi.sendMessage(
                    hobby = hobby,
                    currentTask = task,
                    streak = streak,
                    history = history,
                    userMessage = cleanText
                )

                result.fold(
                    onSuccess = { reply ->
                        insertAssistantMessage(reply)
                    },
                    onFailure = { err ->
                        android.util.Log.e("CoachVM", "Gemini error", err)

                        _state.update {
                            it.copy(
                                error = "AI Coach is temporarily unavailable. Please try again.",
                                isTyping = false
                            )
                        }
                    }
                )

            } catch (e: Exception) {
                android.util.Log.e("CoachVM", "sendMessage crash", e)

                _state.update {
                    it.copy(
                        error = "Message failed to send.",
                        isTyping = false
                    )
                }

            } finally {
                _state.update {
                    it.copy(isTyping = false)
                }
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