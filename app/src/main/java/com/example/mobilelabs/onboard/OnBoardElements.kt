package com.example.mobilelabs.onboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mobilelabs.R


@Composable
fun Logo(){
    Image(
        painter = painterResource(R.drawable.disney_logo),
        contentDescription = null,
    )
}

@Composable
fun TitleText(){
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Добро пожаловать в мир Disney!",
            color = Color.White,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )
        Text(
            text = "Проведите время с  известными героями Disney. Погрузитесь в магию и приключения вместе с любимыми персонажами.",
            color = Color.White,
            textAlign = TextAlign.Center,

            )
    }

}
@Preview()
@Composable
fun Buttons(
    onSignIn: () -> Unit = {},
    onSignUp: () -> Unit = {},
){
    Column (
        verticalArrangement =Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ){
        Button(
            onClick = onSignUp,

            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFFD43D51)
            ),
        ){
            Text("Зарегистрироваться")
        }

        TextButton(onClick = onSignIn,
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(12.dp)
                ) ) {
            Text(
                text = "Войти",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 62.dp))

        }
    }
}