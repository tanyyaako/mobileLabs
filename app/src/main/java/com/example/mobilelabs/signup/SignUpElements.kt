package com.example.mobilelabs.signup

import android.R.attr.navigationIcon
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.graphics.Color.Companion.White
import com.example.mobilelabs.ui.theme.text

@Composable
fun TopBar(onBack: () -> Unit){
    Box (modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)){
        IconButton(onClick = onBack,
            modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Назад",
                tint = Color.White,

            )
        }
        Text(
            text = "Регистрация",
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext() }
        ),
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor  = Color(0xFFD43D51),
            unfocusedBorderColor   = Color.Gray,
            focusedLabelColor = Color(0xFFD43D51)
        ),
    )
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    show: Boolean,
    error: String?,
    onValueChange: (String) -> Unit,
    onToggleShow: () -> Unit,
    modifier: Modifier = Modifier,
    onNext: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = { error?.let { Text(it) } },
        singleLine = true,
        visualTransformation = if (show) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(onClick = onToggleShow ) {
                Icon(
                    imageVector = if (show)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff,
                    contentDescription = if (show)
                        "Скрыть пароль"
                    else
                        "Показать пароль"
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { onNext() }),
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor  = Color(0xFFD43D51),
            unfocusedBorderColor   = Color.Gray,
            focusedLabelColor = Color(0xFFD43D51)
        )
    )
}

@Composable
fun GenderSelector(
    selectedGender: String?,
    onGenderSelected: (String?) -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Пол",
            style = MaterialTheme.typography.bodyLarge
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    onGenderSelected(
                        if (selectedGender == "male") null else "male"
                    )
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedGender == "male")
                        MaterialTheme.colorScheme.primary
                    else
                        Color.Transparent,
                    contentColor = if (selectedGender == "male")
                        White
                    else
                        text,
                )
            ) {
                Text("Мужской")
            }

            OutlinedButton(
                onClick = {
                    onGenderSelected(
                        if (selectedGender == "female") null else "female"
                    )
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (selectedGender == "female")
                        Color(0xFFD43D51)
                    else
                        Color.Transparent,
                    contentColor = if (selectedGender == "female")
                        White
                    else
                        text,

                )
            ) {
                Text("Женский")
            }
        }

        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color(0xFFD43D51)
        ),
    ) { Text(text) }
}