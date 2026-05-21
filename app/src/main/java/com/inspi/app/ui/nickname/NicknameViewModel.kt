package com.inspi.app.ui.nickname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inspi.app.data.preferences.InspiPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NicknameViewModel @Inject constructor(
    private val prefs: InspiPreferences,
) : ViewModel() {

    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()

    fun onNicknameChange(value: String) {
        // Не более 20 символов, без пробелов в начале
        _nickname.value = value.take(20)
    }

    fun confirm(onDone: () -> Unit) {
        val name = _nickname.value.trim()
        if (name.isBlank()) return
        viewModelScope.launch {
            prefs.setUsername(name)
            onDone()
        }
    }
}