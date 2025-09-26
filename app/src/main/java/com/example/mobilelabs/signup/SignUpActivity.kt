package com.example.mobilelabs.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mobilelabs.home.HomeActivity
import com.example.mobilelabs.signin.SignInActivity
import com.example.mobilelabs.ui.theme.MobileLabsTheme

class SignUpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileLabsTheme {
                SignUpScreen(
                    onSignIn = {
                        startActivity(Intent(this, SignInActivity::class.java))
                    },
                    onRegister = {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}