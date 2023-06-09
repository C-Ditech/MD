package com.example.mycapstone.ui.Akun

import androidx.lifecycle.*
import com.example.mycapstone.UserPreference
import kotlinx.coroutines.launch



class AkunViewModel( private val pref: UserPreference) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "History"
    }
    val text: LiveData<String> = _text

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}
