package com.example.mobilelabs.store.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "disney_settings")

class SettingsDataStore(private val context: Context) {

    val currentFontSize: Flow<Float> = context.dataStore.data.map { preferences ->
        preferences[FONT_SIZE_KEY] ?: DEFAULT_FONT_SIZE
    }

    suspend fun setFontSize(size: Float) {
        context.dataStore.edit { preferences ->
            preferences[FONT_SIZE_KEY] = size
        }
    }

    companion object {
        private val FONT_SIZE_KEY = floatPreferencesKey("settings.font_size")
        private const val DEFAULT_FONT_SIZE = 16f
    }
}