package com.example.mobilelabs.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilelabs.R
import com.example.mobilelabs.signin.ActionButton


@Preview
@Composable
fun SignInScreen(
    onSignIn: ()->Unit = {},
    onSignUp: ()->Unit = {},
    onBack: ()->Unit = {}
) {
    val focusManager = LocalFocusManager.current
    val nameRequester = remember { FocusRequester() }
    val passwordRequester = remember { FocusRequester() }

    var name by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    var showPassword by rememberSaveable { mutableStateOf(false) }

    fun validate(): Boolean {
        focusManager.clearFocus()

        nameError = if (name.isBlank()) "Введите своё имя " else null


        passwordError = when {
            password.isBlank() -> "Введите пароль"
            password.length < 8 -> "Пароль должен быть больше 8 символов"
            password.none { it.isDigit() } -> "Должна быть как минимум 1 цифра"
            else -> null
        }

        return listOf(nameError, passwordError).all { it == null }
    }

    Scaffold(topBar = { TopBar (onBack = onBack) }) { inner ->
        Box(modifier = Modifier) {
            Image(
                painter = painterResource(R.drawable.onboardphone),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(top = 84.dp)
                    .fillMaxSize()
                    .fillMaxHeight()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp
                        )
                    )
                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NameField(
                    name = name,
                    error = nameError,
                    onValueChange = { name = it; if (nameError != null) nameError = null },
                    onNext = { passwordRequester.requestFocus() },
                    modifier = Modifier
                        .focusRequester(nameRequester)
                        .fillMaxWidth(),
                )
                PassField(
                    password = password,
                    show = showPassword,
                    error = passwordError,
                    onValueChange = {
                        password = it; if (passwordError != null) passwordError = null
                    },
                    onToggleShow = { showPassword = !showPassword },
                    modifier = Modifier
                        .focusRequester(passwordRequester)
                        .fillMaxWidth(),
                    onNext = { focusManager.clearFocus() }
                )
                ActionButton(
                    text = "Войти",
                    onClick = {
                        if (validate()) {
                            onSignIn()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().border(
                        width = 1.dp,
                        color = Color(0xFFD43D51),
                        shape = RoundedCornerShape(12.dp)
                    )
                )

                TextButton(onClick = onSignUp) {
                    Text(
                        "Нет аккаунта? Зарегистрироваться",
                        color = Color(0xFFD43D51)
                    )
                }
            }
        }
    }
}
