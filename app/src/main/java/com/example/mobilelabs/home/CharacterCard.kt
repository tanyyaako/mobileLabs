package com.example.mobilelabs.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.mobilelabs.Model.Disney.DisneyCharacter

@Composable
fun DisneyCharacterCard(
    character: DisneyCharacter,
    modifier: Modifier = Modifier,
    fontSize: Float,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            ) {
                if (!character.imageUrl.isNullOrEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(character.imageUrl),
                        contentDescription = character.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Нет фото",
                                tint = Color.Gray,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Нет фото",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
            Column(
                modifier = Modifier.weight(2f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = character.name.ifEmpty { "Неизвестно" },
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    fontSize = fontSize.sp,
                    overflow = TextOverflow.Ellipsis
                )
                CharacterInfoRow(
                    label = "Фильмы",
                    items = character.films,
                    fontSize = fontSize
                )
                CharacterInfoRow(
                    label = "Короткометражки",
                    items = character.shortFilms,
                    fontSize = fontSize
                )
                CharacterInfoRow(
                    label = "TV шоу",
                    items = character.tvShows,
                    fontSize = fontSize
                )
                CharacterInfoRow(
                    label = "Видеоигры",
                    items = character.videoGames,
                    fontSize = fontSize
                )
//                Text(
//                    text = "ID: ${character.id}",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = Color.Gray
//                )
            }
        }
    }
}

@Composable
private fun CharacterInfoRow(
    label: String,
    items: List<String>,
    fontSize: Float
) {
    Text(
        text = "$label: ${getFormattedList(items)}",
        fontSize = (fontSize * 0.9).sp,
        style = MaterialTheme.typography.bodySmall,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

private fun getFormattedList(list: List<String>): String {
    return when {
        list.isEmpty() -> "отсутствует"
        list.size == 1 -> list.first()
        else -> list.joinToString(limit = 3)
    }
}