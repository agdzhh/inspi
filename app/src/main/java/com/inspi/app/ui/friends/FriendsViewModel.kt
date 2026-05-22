package com.inspi.app.ui.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspi.app.data.repository.FriendRepository
import com.inspi.app.data.repository.SubmissionRepository
import com.inspi.app.data.repository.UserRepository
import com.inspi.app.domain.models.Friend
import com.inspi.app.domain.models.HobbyType
import com.inspi.app.domain.models.LeaderboardEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FriendsUiState(
    val myCode: String = "",
    val myUsername: String = "You",
    val myHobby: HobbyType = HobbyType.PHOTOGRAPHY,
    val myWeeklyXp: Int = 0,
    val myStreak: Int = 0,
    val friends: List<Friend> = emptyList(),
    val leaderboard: List<LeaderboardEntry> = emptyList(),
    val isLoading: Boolean = true,
    val addCodeInput: String = "",
    val addUsernameInput: String = "",
    val addError: String? = null,
    val showAddForm: Boolean = false,
)

@HiltViewModel
class FriendsViewModel @Inject constructor(
    private val friendRepo: FriendRepository,
    private val userRepo: UserRepository,
    private val submissionRepo: SubmissionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(FriendsUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            friendRepo.seedDemoFriendsIfEmpty()
            loadMyInfo()
        }

        viewModelScope.launch {
            friendRepo.observeFriends().collect { friends ->
                _state.update { it.copy(friends = friends) }
                rebuildLeaderboard()
            }
        }
    }

    private suspend fun loadMyInfo() {
        val code = friendRepo.getMyCode()
        val profile = userRepo.getProfile()
        val weeklyXp = submissionRepo.getWeeklySubmissions().sumOf { it.xpEarned }

        _state.update {
            it.copy(
                myCode = code,
                myUsername = profile?.username ?: "You",
                myHobby = profile?.hobby ?: HobbyType.PHOTOGRAPHY,
                myWeeklyXp = weeklyXp,
                myStreak = profile?.currentStreak ?: 0,
                isLoading = false,
            )
        }
        rebuildLeaderboard()
    }

    private fun rebuildLeaderboard() {
        val s = _state.value
        val entries = mutableListOf<LeaderboardEntry>()

        entries.add(
            LeaderboardEntry(
                code = s.myCode,
                username = s.myUsername,
                hobby = s.myHobby,
                weeklyXp = s.myWeeklyXp,
                currentStreak = s.myStreak,
                isMe = true,
            )
        )

        s.friends.forEach { friend ->
            entries.add(
                LeaderboardEntry(
                    code = friend.code,
                    username = friend.username,
                    hobby = friend.hobby,
                    weeklyXp = friend.weeklyXp,
                    currentStreak = friend.currentStreak,
                    isMe = false,
                    avatarUrl = friend.avatarUrl,
                )
            )
        }

        val ranked = entries
            .sortedByDescending { it.weeklyXp }
            .mapIndexed { index, entry -> entry.copy(rank = index + 1) }

        _state.update { it.copy(leaderboard = ranked) }
    }

    fun onCodeInput(value: String) {
        _state.update { it.copy(addCodeInput = value.uppercase().take(6), addError = null) }
    }

    fun onUsernameInput(value: String) {
        _state.update { it.copy(addUsernameInput = value.take(20), addError = null) }
    }

    fun toggleAddForm() {
        _state.update { it.copy(showAddForm = !it.showAddForm, addError = null) }
    }

    fun addFriend() {
        val code = _state.value.addCodeInput
        val username = _state.value.addUsernameInput
        viewModelScope.launch {
            friendRepo.addFriend(code, username)
                .onSuccess {
                    _state.update {
                        it.copy(
                            addCodeInput = "",
                            addUsernameInput = "",
                            showAddForm = false,
                            addError = null,
                        )
                    }
                }
                .onFailure { err ->
                    _state.update { it.copy(addError = err.message) }
                }
        }
    }

    fun removeFriend(code: String) {
        viewModelScope.launch { friendRepo.removeFriend(code) }
    }

    fun dismissAddError() {
        _state.update { it.copy(addError = null) }
    }
}
