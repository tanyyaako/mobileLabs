package com.example.mobilelabs.signin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mobilelabs.home.HomeActivity
import com.example.mobilelabs.signup.SignUpActivity
import com.example.mobilelabs.signup.SignUpScreen
import com.example.mobilelabs.ui.theme.MobileLabsTheme

class SignInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileLabsTheme {
                SignInScreen(
                    onSignIn = {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    },
                    onSignUp = {
                        startActivity(Intent(this, SignUpActivity::class.java))
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}