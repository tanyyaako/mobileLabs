package com.example.mobilelabs.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "disney_characters")
@TypeConverters(ListConverter::class)
data class DisneyCharacterEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val films: List<String>,
    val shortFilms: List<String>,
    val tvShows: List<String>,
    val videoGames: List<String>,
    val imageUrl: String?,
    val url: String
)

