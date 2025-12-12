package com.example.mobilelabs.repository

import android.content.Context
import android.util.Log
import com.example.mobilelabs.Model.Disney.DisneyCharacter
import com.example.mobilelabs.database.DisneyCharacterDao
import com.example.mobilelabs.database.DisneyCharacterMapper
import com.example.mobilelabs.database.DisneyDatabase
import com.example.mobilelabs.network.KtorDisneyCharacterApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DisneyCharacterRepository(
    private val characterDao: DisneyCharacterDao
) {
    companion object {
        @Volatile
        private var INSTANCE: DisneyCharacterRepository? = null
        
        private const val TAG = "DisneyCharacterRepository"
        
        fun getInstance(context: Context): DisneyCharacterRepository {
            return INSTANCE ?: synchronized(this) {
                val database = DisneyDatabase.getDatabase(context)
                val instance = DisneyCharacterRepository(database.disneyCharacterDao())
                INSTANCE = instance
                instance
            }
        }
    }
    
    /**
     * Получить всех персонажей из базы данных (реактивно через Flow)
     */
    fun getAllCharacters(): Flow<List<DisneyCharacter>> {
        return characterDao.getAllCharacters()
            .map { entities -> DisneyCharacterMapper.toModelList(entities) }
    }
    
    /**
     * Получить персонажей по диапазону ID из базы данных
     */
    fun getCharactersByIds(ids: List<Int>): Flow<List<DisneyCharacter>> {
        return characterDao.getCharactersByIds(ids)
            .map { entities -> DisneyCharacterMapper.toModelList(entities) }
    }
    
    /**
     * Проверить, есть ли данные в базе данных
     */
    suspend fun hasData(): Boolean {
        return characterDao.getCharacterCount() > 0
    }
    
    /**
     * Получить количество персонажей в базе данных (синхронно для отладки)
     */
    suspend fun getCharacterCountSync(): Int {
        return characterDao.getCharacterCount()
    }
    
    /**
     * Загрузить персонажей из API и сохранить в базу данных
     */
    suspend fun loadCharactersFromApi(ids: IntRange = 1..50): Result<List<DisneyCharacter>> {
        return try {
            val result = KtorDisneyCharacterApi.getCharacters(ids)
            result.onSuccess { characters ->
                // Сохраняем в базу данных
                val entities = DisneyCharacterMapper.toEntityList(characters)
                characterDao.insertCharacters(entities)
                Log.d(TAG, "Сохранено ${characters.size} персонажей в базу данных")
            }
            result
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка загрузки персонажей: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * Обновить персонажей из API (заменить существующие)
     */
    suspend fun refreshCharactersFromApi(ids: IntRange = 1..50): Result<List<DisneyCharacter>> {
        return try {
            val result = KtorDisneyCharacterApi.getCharacters(ids)
            result.onSuccess { characters ->
                // Удаляем старые данные и вставляем новые
                val characterIds = characters.map { it.id }
                characterDao.deleteCharactersByIds(characterIds)
                val entities = DisneyCharacterMapper.toEntityList(characters)
                characterDao.insertCharacters(entities)
                Log.d(TAG, "Обновлено ${characters.size} персонажей в базе данных")
            }
            result
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка обновления персонажей: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * Загрузить дополнительных персонажей (добавить к существующим)
     */
    suspend fun loadMoreCharactersFromApi(startId: Int, count: Int = 100): Result<List<DisneyCharacter>> {
        val ids = startId until (startId + count)
        val countBefore = characterDao.getCharacterCount()
        Log.d(TAG, "Загрузка дополнительных персонажей: диапазон ID $ids (всего в БД до загрузки: $countBefore)")
        return try {
            val result = KtorDisneyCharacterApi.getCharacters(ids)
            result.onSuccess { characters ->
                if (characters.isNotEmpty()) {
                    // Добавляем к существующим (REPLACE стратегия обновит, если уже есть)
                    val entities = DisneyCharacterMapper.toEntityList(characters)
                    characterDao.insertCharacters(entities)
                    val countAfter = characterDao.getCharacterCount()
                    Log.d(TAG, "Добавлено ${characters.size} новых персонажей в базу данных")
                    Log.d(TAG, "ID загруженных персонажей: ${characters.map { it.id }.take(10)}${if (characters.size > 10) "..." else ""}")
                    Log.d(TAG, "Всего в БД: было $countBefore, стало $countAfter, добавлено: ${countAfter - countBefore}")
                } else {
                    Log.w(TAG, "API вернул пустой список персонажей для диапазона $ids")
                }
            }
            result
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка загрузки дополнительных персонажей: ${e.message}", e)
            Result.failure(e)
        }
    }
    
    /**
     * Вставить персонажа в базу данных
     */
    suspend fun insertCharacter(character: DisneyCharacter) {
        val entity = DisneyCharacterMapper.toEntity(character)
        characterDao.insertCharacter(entity)
    }
    
    /**
     * Вставить список персонажей в базу данных
     */
    suspend fun insertCharacters(characters: List<DisneyCharacter>) {
        val entities = DisneyCharacterMapper.toEntityList(characters)
        characterDao.insertCharacters(entities)
    }
    
    /**
     * Обновить персонажа в базе данных
     */
    suspend fun updateCharacter(character: DisneyCharacter) {
        val entity = DisneyCharacterMapper.toEntity(character)
        characterDao.updateCharacter(entity)
    }
    
    /**
     * Удалить персонажа из базы данных
     */
    suspend fun deleteCharacter(character: DisneyCharacter) {
        val entity = DisneyCharacterMapper.toEntity(character)
        characterDao.deleteCharacter(entity)
    }
    
    /**
     * Удалить всех персонажей из базы данных
     */
    suspend fun deleteAllCharacters() {
        characterDao.deleteAllCharacters()
    }
}

