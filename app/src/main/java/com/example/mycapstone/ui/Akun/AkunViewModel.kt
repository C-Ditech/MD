package com.example.mycapstone.ui.Akun

import androidx.lifecycle.*
import com.example.mycapstone.*
import kotlinx.coroutines.launch



class AkunViewModel( private val pref: UserPreference) : ViewModel() {

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getUserData(): LiveData<UserToken> {
        return pref.getUserToken().asLiveData()
    }

    fun getUserID(): LiveData<UserID> {
        return pref.getUserID().asLiveData()
    }
    fun getEmail(): LiveData<UserEmail> {
        return pref.getUserEmail().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}
