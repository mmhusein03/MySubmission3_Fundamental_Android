package com.dicoding.picodiploma.mysubmission2.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPreferences constructor(context: Context) {

    private val setDataStore = context.dataStore
    private val themeKey = booleanPreferencesKey(THEME_KEY)

    fun getThemeSetting(): Flow<Boolean> =
       setDataStore.data.map {
           it[themeKey] ?: false
       }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        setDataStore.edit {
            it[themeKey] = isDarkModeActive
        }
    }

    companion object {
        const val THEME_KEY = "theme_setting"
    }
}