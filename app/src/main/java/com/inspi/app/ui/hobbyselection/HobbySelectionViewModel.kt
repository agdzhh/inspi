package com.inspi.app.ui.hobbyselection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspi.app.data.preferences.InspiPreferences
import com.inspi.app.data.repository.UserRepository
import com.inspi.app.domain.models.HobbyType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HobbySelectionViewModel @Inject constructor(
    private val userRepo: UserRepository,
) : ViewModel() {

    private val _selected = MutableStateFlow<HobbyType?>(null)
    val selected = _selected.asStateFlow()

    fun select(hobby: HobbyType) { _selected.value = hobby }

    fun confirm(onDone: () -> Unit) {
        val hobby = _selected.value ?: return
        viewModelScope.launch {
            userRepo.setHobby(hobby.name)
            onDone()
        }
    }
}
