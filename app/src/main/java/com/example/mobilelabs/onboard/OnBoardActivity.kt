package com.example.mobilelabs.onboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mobilelabs.LogActivity
import com.example.mobilelabs.signin.SignInActivity
import com.example.mobilelabs.signup.SignUpActivity
import com.example.mobilelabs.ui.theme.MobileLabsTheme

class OnBoardActivity : LogActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileLabsTheme {
                OnBoardScreen(
                    onSignUp = {
                        startActivity(Intent(this, SignUpActivity::class.java))
                    },
                    onSignIn = {
                        startActivity(Intent(this, SignInActivity::class.java))
                    },
                )

            }
        }
    }
}