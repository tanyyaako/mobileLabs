package com.example.mobilelabs.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilelabs.R


data class Character(
    val name: String,
    val gender: String,
    val title: String,
    val photo: Int? = null
)
@Preview
@Composable
fun HomeScreen(){
    val allCharacters =
        listOf(
            Character("Тиана", "female", "Принцесса и лягушка", R.drawable.tiana),
            Character("Рапунцель", "female", "Рапунцель", R.drawable.rapunc),
            Character("Аладдин", "male", "Аладдин", R.drawable.aladin),
            Character("Белоснежка", "female", "Белоснежка и 7 гномов", R.drawable.belosnezh),
            Character("Тиана", "female", "Принцесса и лягушка", R.drawable.tiana),
            Character("Рапунцель", "female", "Рапунцель", R.drawable.rapunc),
            Character("Аладдин", "male", "Аладдин", R.drawable.aladin),
            Character("Белоснежка", "female", "Белоснежка и 7 гномов", R.drawable.belosnezh)
        )

    Box(modifier = Modifier){
        Image(
            painter = painterResource(R.drawable.onboardphone),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopBar()
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(allCharacters) { character ->
                    CharacterCard(
                        character = character,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}