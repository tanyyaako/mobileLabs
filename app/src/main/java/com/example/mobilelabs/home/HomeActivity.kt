package com.example.mobilelabs.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mobilelabs.onboard.OnBoardScreen
import com.example.mobilelabs.signin.SignInActivity
import com.example.mobilelabs.signup.SignUpActivity
import com.example.mobilelabs.ui.theme.MobileLabsTheme

class HomeActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileLabsTheme {
                HomeScreen(
                )

            }
        }
    }
}