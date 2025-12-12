package com.example.mobilelabs.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.mobilelabs.R
import com.example.mobilelabs.Model.Disney.DisneyCharacter
import com.example.mobilelabs.repository.DisneyCharacterRepository
import com.example.mobilelabs.store.datastore.SettingsDataStore
import kotlinx.coroutines.launch

@Preview
@Composable
fun HomeScreen(
    onSettingsClick: () -> Unit,
    onNavigateToSettingsWithData: (List<DisneyCharacter>) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val repository = remember { DisneyCharacterRepository.getInstance(context) }

    val characters by repository.getAllCharacters().collectAsState(initial = emptyList())
    
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val dataStore = remember { SettingsDataStore(context) }
    val fontSize by dataStore.currentFontSize.collectAsState(initial = 16f)

    LaunchedEffect(Unit) {
        isLoading = true
        error = null
        
        if (repository.hasData()) {
            Log.d("HomeScreen", "Данные найдены в базе данных, отображаем их")
            isLoading = false
        } else {
            Log.d("HomeScreen", "База данных пуста, загружаем данные из API")
            val result = repository.loadCharactersFromApi(1..50)
            result.onFailure { exception ->
                error = exception.message
                Log.e("HomeScreen", "Ошибка загрузки: ${exception.message}")
            }
            isLoading = false
        }
    }

    fun refreshData() {
        scope.launch {
            isLoading = true
            error = null
            
            val result = repository.refreshCharactersFromApi(1..50)
            result.onFailure { exception ->
                error = exception.message
                Log.e("HomeScreen", "Ошибка обновления: ${exception.message}")
            }
            
            isLoading = false
        }
    }

    fun loadMore() {
        scope.launch {
            isLoading = true
            error = null
            
            val currentMaxId = if (characters.isNotEmpty()) {
                characters.maxOf { it.id }
            } else {
                0
            }
            var startId = currentMaxId + 1
            val rangeSize = 50
            var attempts = 0
            val maxAttempts = 10
            var loadedCharacters: List<DisneyCharacter>? = null
            
            Log.d("HomeScreen", "Текущее количество персонажей: ${characters.size}")
            Log.d("HomeScreen", "Максимальный ID: $currentMaxId, начинаем загрузку с ID: $startId")
            
            while (attempts < maxAttempts && loadedCharacters == null) {
                Log.d("HomeScreen", "Попытка ${attempts + 1}: загрузка персонажей с ID $startId-${startId + rangeSize - 1}")
                
                val result = repository.loadMoreCharactersFromApi(startId, rangeSize)
                
                result.onSuccess { characters ->
                    if (characters.isNotEmpty()) {
                        loadedCharacters = characters
                        Log.d("HomeScreen", "Успешно загружено ${characters.size} персонажей (ID: $startId-${startId + rangeSize - 1})")
                        error = null
                    } else {
                        Log.w("HomeScreen", "Диапазон $startId-${startId + rangeSize - 1} пустой, пробуем следующий диапазон")
                        startId += rangeSize
                        attempts++
                    }
                }
                
                result.onFailure { exception ->
                    Log.w("HomeScreen", "Ошибка загрузки диапазона $startId-${startId + rangeSize - 1}: ${exception.message}, пробуем следующий")
                    startId += rangeSize
                    attempts++
                }
            }
            
            if (loadedCharacters == null) {
                error = "Не удалось загрузить новых персонажей после $maxAttempts попыток"
                Log.w("HomeScreen", "Не удалось загрузить персонажей после $maxAttempts попыток")
            }
            
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.onboardphone),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBar(
                onSettingsClick = onSettingsClick,
                onRefreshClick = { refreshData() },
                fontSize = fontSize
            )
            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading && characters.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator()
                            Text(
                                text = "Загрузка Disney персонажей...",
                                color = androidx.compose.ui.graphics.Color.White,
                                fontSize = fontSize.sp
                            )
                        }
                    }
                }

                error != null && characters.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "Ошибка: $error",
                                color = androidx.compose.ui.graphics.Color.White,
                                fontSize = fontSize.sp
                            )
                            Button(onClick = { refreshData() }) {
                                Text("Повторить")
                            }
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
//                        item {
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 8.dp),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Button(
//                                    onClick = {
//                                        scope.launch {
//                                            repository.deleteAllCharacters()
//                                            Log.d("HomeScreen", "База данных очищена")
//                                        }
//                                    },
//                                    enabled = !isLoading,
//                                    colors = ButtonDefaults.buttonColors(
//                                        containerColor = androidx.compose.ui.graphics.Color.Red.copy(alpha = 0.7f),
//                                        contentColor = androidx.compose.ui.graphics.Color.White
//                                    )
//                                ) {
//                                    Text("Очистить БД (тест)")
//                                }
//                            }
//                        }
                        
                        items(characters) { character ->
                            DisneyCharacterCard(
                                character = character,
                                modifier = Modifier,
                                fontSize = fontSize
                            )
                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = { loadMore() },
                                    enabled = !isLoading,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = androidx.compose.ui.graphics.Color.White,
                                        contentColor = androidx.compose.ui.graphics.Color(0xFFFF69B4) 
                                    )
                                ) {
                                    if (isLoading) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            color = androidx.compose.ui.graphics.Color(0xFFFF69B4)
                                        )
                                    } else {
                                        Text(
                                            "Загрузить еще",
                                            color = androidx.compose.ui.graphics.Color(0xFFFF69B4) 
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}