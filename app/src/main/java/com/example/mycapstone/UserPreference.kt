package com.example.mycapstone

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference  private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[PASSWORD_KEY] = user.password
            preferences[STATE_KEY] = user.isLogin
        }
    }

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NAME_KEY] ?:"",
                preferences[EMAIL_KEY] ?:"",
                preferences[PASSWORD_KEY] ?:"",
                preferences[STATE_KEY] ?: false
            )
        }
    }

    suspend fun login() {
        dataStore.edit { preferences ->
            preferences[STATE_KEY] = true
        }
    }

    suspend fun saveUserToken(userToken: UserToken){
        dataStore.edit { preference ->
            preference[TOKEN_KEY] = userToken.token
        }
    }

    suspend fun saveUserID(username: UserID){
        dataStore.edit { preference ->
            preference[NAME_KEY] = username.name
        }
    }

    suspend fun saveUserEmail(username: UserEmail){
        dataStore.edit { preference ->
            preference[EMAIL_KEY] = username.email
        }
    }
    fun getUserEmail(): Flow<UserEmail> {
        return dataStore.data.map { preference ->
            UserEmail(
                preference[EMAIL_KEY] ?: "",
                preference[PASSWORD_KEY] ?: ""

            )
        }
    }
    fun getUserID(): Flow<UserID> {
        return dataStore.data.map { preference ->
            UserID(
                preference[NAME_KEY] ?: ""
            )
        }
    }

    fun getUserToken(): Flow<UserToken> {
        return dataStore.data.map { preference ->
            UserToken(
                preference[TOKEN_KEY] ?: ""
            )
        }
    }



    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val STATE_KEY = booleanPreferencesKey("state")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<androidx.datastore.preferences.core.Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}