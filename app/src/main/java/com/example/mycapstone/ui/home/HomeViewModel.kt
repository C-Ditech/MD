package com.example.mycapstone.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mycapstone.UserID
import com.example.mycapstone.UserPreference
import com.example.mycapstone.UserToken

class HomeViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUserID(): LiveData<UserID> {
        return pref.getUserID().asLiveData()
    }
}