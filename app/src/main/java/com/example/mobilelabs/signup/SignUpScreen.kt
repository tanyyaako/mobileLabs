package com.example.mobilelabs.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobilelabs.R

@Preview
@Composable
fun SignUpScreen(
    name: String,
    password: String,
    onNameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegister: () -> Unit = {},
    onSignIn: () -> Unit = {},
    onBack: () -> Unit = {}
){
    val focusManager = LocalFocusManager.current
    var nameRequester = remember { FocusRequester() }
    val emailRequester = remember { FocusRequester() }
    val passwordRequester = remember { FocusRequester() }
    val confirmRequester = remember { FocusRequester() }

    var email by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf<String?>(null) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var confirmError by remember { mutableStateOf<String?>(null) }
    var genderError by remember { mutableStateOf<String?>(null) }

    var showPassword by rememberSaveable { mutableStateOf(false) }

    val emailRegex = remember {
        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$".toRegex(RegexOption.IGNORE_CASE)
    }

    fun validate(): Boolean{
        focusManager.clearFocus()

        nameError = if(name.isBlank()) "Введите своё имя " else null

        emailError = when{
            email.isBlank() -> "Введите электронную почту"
            !emailRegex.matches(email) -> "Введите корректный email"
            else -> null
        }

        passwordError = when{
            password.isBlank() -> "Введите пароль"
            password.length < 8 -> "Пароль должен быть больше 8 символов"
            password.none {it.isDigit()} -> "Должна быть как минимум 1 цифра"
            else -> null
        }

        confirmError = when{
            confirm.isBlank() -> "Введите пароль повторно"
            confirm != password -> "Пароли не совпадают"
            else -> null
        }
        genderError = if (gender == null) "Выберите пол" else null

        return listOf(nameError, emailError, passwordError, confirmError, genderError).all { it == null }
    }
    Scaffold( topBar = {TopBar ( onBack = onBack )}) {
        inner ->
        Box(modifier = Modifier) {
            Image(
                painter = painterResource(R.drawable.onboardphone),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column (
                modifier = Modifier
                    .padding(top = 84.dp)
                    .fillMaxSize()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(
                            topStart = 24.dp,
                            topEnd = 24.dp
                        )
                    )
                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                TextField(
                    value = name,
                    error = nameError,
                    onValueChange = { onNameChange(it); if (nameError != null) nameError = null},
                    label = "Имя",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    onNext = { emailRequester.requestFocus() },
                    modifier = Modifier
                        .focusRequester(nameRequester)
                        .fillMaxWidth(),
                )
                TextField(
                    value = email,
                    error = emailError,
                    onValueChange = { email = it; if (emailError != null) emailError = null },
                    label = "Электронная почта",
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                    onNext = { passwordRequester.requestFocus() },
                    modifier = Modifier
                        .focusRequester(emailRequester)
                        .fillMaxWidth(),
                )
                PasswordField(
                    label = "Пароль",
                    value = password,
                    show = showPassword,
                    error = passwordError,
                    onValueChange = { onPasswordChange(it); if (passwordError != null) passwordError = null} ,
                    onToggleShow = { showPassword = !showPassword },
                    modifier = Modifier
                        .focusRequester(passwordRequester)
                        .fillMaxWidth(),
                    onNext = { confirmRequester.requestFocus() }
                )
                PasswordField(
                    label = "Подтвердить пароль",
                    value = confirm,
                    show = showPassword,
                    error = confirmError,
                    onValueChange = { confirm = it; if (confirmError != null) confirmError = null },
                    onToggleShow = { showPassword = !showPassword },
                    modifier = Modifier
                        .focusRequester(confirmRequester)
                        .fillMaxWidth(),
                    onNext = { focusManager.clearFocus() }
                )
                GenderSelector(
                    selectedGender = gender,
                    onGenderSelected = { selected ->
                        gender = selected
                        genderError = null
                    },
                    error = genderError,
                    modifier = Modifier.fillMaxWidth()
                )
                ActionButton(
                    text = "Зарегистрироваться",
                    onClick = {
                        if (validate()) {
                            onRegister()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().border(
                        width = 1.dp,
                        color = Color(0xFFD43D51),
                        shape =  RoundedCornerShape(12.dp)
                    )
                )

                TextButton(onClick = onSignIn) { Text("Есть аккаунт? Войти", color =  Color(0xFFD43D51)) }
            }
        }

    }

}