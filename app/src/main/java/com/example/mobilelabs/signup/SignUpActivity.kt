package com.example.mobilelabs.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mobilelabs.LogActivity
import com.example.mobilelabs.Model.User
import com.example.mobilelabs.home.HomeActivity
import com.example.mobilelabs.signin.SignInActivity
import com.example.mobilelabs.ui.theme.MobileLabsTheme

class SignUpActivity : LogActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var name by remember { mutableStateOf("") }
            var password by remember { mutableStateOf("") }
            MobileLabsTheme {
                SignUpScreen(
                    name = name,
                    password = password,
                    onNameChange = {name = it},
                    onPasswordChange = {password = it},
                    onSignIn = {
                        val intent = Intent().apply {
                            putExtra("name", name)
                            putExtra("password", password)
                            putExtra("user", User(name = name, "", password = password))
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    },
                    onRegister = {
                        val intent = Intent().apply {
                            putExtra("name", name)
                            putExtra("password", password)
                            putExtra("user", User(name = name, "", password = password))
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}