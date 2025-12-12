package com.example.mobilelabs.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface DisneyCharacterDao {
    
    @Query("SELECT * FROM disney_characters ORDER BY id ASC")
    fun getAllCharacters(): Flow<List<DisneyCharacterEntity>>
    
    @Query("SELECT * FROM disney_characters WHERE id = :characterId")
    suspend fun getCharacterById(characterId: Int): DisneyCharacterEntity?
    
    @Query("SELECT * FROM disney_characters WHERE id IN (:ids) ORDER BY id ASC")
    fun getCharactersByIds(ids: List<Int>): Flow<List<DisneyCharacterEntity>>
    
    @Query("SELECT COUNT(*) FROM disney_characters")
    suspend fun getCharacterCount(): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: DisneyCharacterEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<DisneyCharacterEntity>)
    
    @Update
    suspend fun updateCharacter(character: DisneyCharacterEntity)
    
    @Delete
    suspend fun deleteCharacter(character: DisneyCharacterEntity)
    
    @Query("DELETE FROM disney_characters")
    suspend fun deleteAllCharacters()
    
    @Query("DELETE FROM disney_characters WHERE id IN (:ids)")
    suspend fun deleteCharactersByIds(ids: List<Int>)
}

