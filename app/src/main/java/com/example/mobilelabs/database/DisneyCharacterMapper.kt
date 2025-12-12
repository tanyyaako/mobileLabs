package com.example.mobilelabs.database

import com.example.mobilelabs.Model.Disney.DisneyCharacter

object DisneyCharacterMapper {
    
    fun toEntity(character: DisneyCharacter): DisneyCharacterEntity {
        return DisneyCharacterEntity(
            id = character.id,
            name = character.name,
            films = character.films,
            shortFilms = character.shortFilms,
            tvShows = character.tvShows,
            videoGames = character.videoGames,
            imageUrl = character.imageUrl,
            url = character.url
        )
    }
    
    fun toModel(entity: DisneyCharacterEntity): DisneyCharacter {
        return DisneyCharacter(
            id = entity.id,
            name = entity.name,
            films = entity.films,
            shortFilms = entity.shortFilms,
            tvShows = entity.tvShows,
            videoGames = entity.videoGames,
            imageUrl = entity.imageUrl,
            url = entity.url
        )
    }
    
    fun toEntityList(characters: List<DisneyCharacter>): List<DisneyCharacterEntity> {
        return characters.map { toEntity(it) }
    }
    
    fun toModelList(entities: List<DisneyCharacterEntity>): List<DisneyCharacter> {
        return entities.map { toModel(it) }
    }
}

