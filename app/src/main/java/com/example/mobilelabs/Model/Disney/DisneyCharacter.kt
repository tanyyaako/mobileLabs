package com.example.mobilelabs.Model.Disney

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DisneyCharacter(
    @SerialName("_id")
    val id: Int,

    val name: String,

    val films: List<String> = emptyList(),

    @SerialName("shortFilms")
    val shortFilms: List<String> = emptyList(),

    @SerialName("tvShows")
    val tvShows: List<String> = emptyList(),

    @SerialName("videoGames")
    val videoGames: List<String> = emptyList(),

    @SerialName("imageUrl")
    val imageUrl: String? = null,

    val url: String
)

@Serializable
data class DisneyApiResponse<T>(
    val data: T
)