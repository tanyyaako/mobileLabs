package com.example.mobilelabs.network

import kotlin.Result
import android.util.Log
import com.example.mobilelabs.Model.Disney.DisneyCharacter
import com.example.mobilelabs.Model.Disney.DisneyApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorDisneyCharacterApi {

    private companion object {
        const val TAG = "DisneyService"
        const val BASE_URL = "https://api.disneyapi.dev/character"
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.d(TAG, "Ktor: $message")
                }
            }
            level = LogLevel.ALL
        }
    }

    suspend fun getCharacters(ids: IntRange = 1..50): Result<List<DisneyCharacter>> {

        return try {
            val characters = mutableListOf<DisneyCharacter>()

            for (id in ids) {
                try {

                    val response: HttpResponse = client.get("$BASE_URL/$id")
                    val apiResponse: DisneyApiResponse<DisneyCharacter> = response.body()

                    characters.add(apiResponse.data)

                    Log.d(TAG, "Успешно загружен: ${apiResponse.data.name} (ID: $id)")

                } catch (e: Exception) {
                    Log.w(TAG, " Ошибка загрузки персонажа ID: $id - ${e.message}")
                }

                kotlinx.coroutines.delay(100)
            }

            if (characters.isEmpty()) {
                Log.e(TAG, "Не удалось загрузить ни одного персонажа")
                Result.failure(Exception("Не удалось загрузить ни одного персонажа"))
            } else {
                Log.i(TAG, "Загрузка завершена. Всего персонажей: ${characters.size}")
                Result.success(characters)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Ошибка сети: ${e.message}", e)
            Result.failure(Exception("Ошибка сети: ${e.message}"))
        }
    }

    suspend fun getCharacter(id: Int): Result<DisneyCharacter> {

        return try {
            val response: HttpResponse = client.get("$BASE_URL/$id")
            val apiResponse: DisneyApiResponse<DisneyCharacter> = response.body()

            Log.d(TAG, "Персонаж загружен: ${apiResponse.data.name}")
            Result.success(apiResponse.data)

        } catch (e: Exception) {
            Log.e(TAG, "Ошибка загрузки персонажа $id: ${e.message}")
            Result.failure(e)
        }
    }
}