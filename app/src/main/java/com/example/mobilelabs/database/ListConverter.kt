package com.example.mobilelabs.database

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ListConverter {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return if (value.isEmpty()) {
            "[]"
        } else {
            Json.encodeToString(value)
        }
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.isBlank() || value == "[]") {
            emptyList()
        } else {
            try {
                Json.decodeFromString(value)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }
}

