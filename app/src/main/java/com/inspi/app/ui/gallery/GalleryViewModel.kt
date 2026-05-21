package com.inspi.app.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspi.app.data.repository.SubmissionRepository
import com.inspi.app.domain.models.Submission
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GalleryUiState(
    val submissions: List<Submission> = emptyList(),
    val flashback: Pair<Submission, Submission>? = null,
    val isLoading: Boolean = true,
    val selectedIndex: Int? = null,
)

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val submissionRepo: SubmissionRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(GalleryUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            submissionRepo.observeAll().collect { list ->
                _state.update { it.copy(submissions = list, isLoading = false) }
                if (list.size >= 2) {
                    val pair = submissionRepo.getFlashbackPair()
                    _state.update { s -> s.copy(flashback = pair) }
                }
            }
        }
    }

    fun openViewer(index: Int) {
        _state.update { it.copy(selectedIndex = index) }
    }

    fun closeViewer() {
        _state.update { it.copy(selectedIndex = null) }
    }
}
