package com.example.mycapstone.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mycapstone.UserModel
import com.example.mycapstone.UserPreference
import kotlinx.coroutines.launch

class MainViewModel ( private val pref: UserPreference) : ViewModel() {


    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }



}