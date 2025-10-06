package com.example.mobilelabs.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import com.example.mobilelabs.LogActivity
import com.example.mobilelabs.Model.User
import com.example.mobilelabs.home.HomeActivity
import com.example.mobilelabs.signup.SignUpActivity
import com.example.mobilelabs.ui.theme.MobileLabsTheme

class SignInActivity : LogActivity() {
    val signUpLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if (result.resultCode ==RESULT_OK){
            val data = result.data
            val user = data?.getParcelableExtra<User>("user") as User
            Log.d("USER", "name = ${user.name}, password = ${user.password}")

            val userName = data.getStringExtra("name") ?: ""
            val userPassword = data.getStringExtra("password") ?: ""
            nameState.value = userName
            passwordState.value = userPassword
        }
    }

    val nameState = mutableStateOf("")
    val passwordState = mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileLabsTheme {
                SignInScreen(
                    name = nameState.value,
                    password = passwordState.value,
                    onNameChange = {nameState.value = it},
                    onPasswordChange = {passwordState.value = it},
                    onSignIn = {
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    },
                    onSignUp = {
                        signUpLauncher.launch(Intent(this, SignUpActivity::class.java))
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}