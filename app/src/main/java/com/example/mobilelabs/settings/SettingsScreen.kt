import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mobilelabs.Model.Disney.DisneyCharacter
import com.example.mobilelabs.R
import com.example.mobilelabs.store.DisneyCacheManager
import com.example.mobilelabs.store.datastore.SettingsDataStore
import com.example.mobilelabs.store.file.ExternalFileStorage
import com.example.mobilelabs.store.file.FileInfo
import com.example.mobilelabs.store.file.InternalFileStorage
import com.example.mobilelabs.store.sharedPref.SettingsSharedPreferences
import com.example.mobilelabs.ui.theme.MobileLabsTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    disneyCharacters: List<DisneyCharacter> = emptyList()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val dataStore = remember { SettingsDataStore(context) }
    val sharedPrefs = remember { SettingsSharedPreferences(context) }

    val externalStorage = remember { ExternalFileStorage(context) }
    val internalStorage = remember { InternalFileStorage(context) }

    var fileName by remember { mutableStateOf("student_1") }
    var isEditingFileName by remember { mutableStateOf(false) }
    var tempFileName by remember { mutableStateOf("student_1") }

    var fileInfo by remember { mutableStateOf<FileInfo?>(null) }
    var hasBackup by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }
    var isDeleting by remember { mutableStateOf(false) }
    var isRestoring by remember { mutableStateOf(false) }

    var showToast by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf("") }

    val storedFontSize by dataStore.currentFontSize.collectAsState(initial = 16f)
    val storedPassword = sharedPrefs.password

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var fontSize by remember { mutableStateOf(storedFontSize) }

    var showCurrentPassword by rememberSaveable { mutableStateOf(false) }
    var showNewPassword by rememberSaveable { mutableStateOf(false) }
    var showConfirmPassword by rememberSaveable { mutableStateOf(false) }

    val charactersForBackup = remember(disneyCharacters) {
        if (disneyCharacters.isNotEmpty()) {
            disneyCharacters
        } else {
            DisneyCacheManager.getCharacters()
        }
    }

    LaunchedEffect(fileName, storedFontSize) {
        fontSize = storedFontSize
        fileInfo = externalStorage.getFileInfo(fileName)
        hasBackup = internalStorage.backupExists(fileName)
    }

    LaunchedEffect(showToast) {
        if (showToast) {
            delay(3000)
            showToast = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.onboardphone),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = "Настройки",
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center,
                    fontSize = (fontSize + 6).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Управление файлами",
                            fontSize = (fontSize + 2).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD43D51)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Имя файла:",
                                fontSize = fontSize.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Medium
                            )

                            if (isEditingFileName) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = tempFileName,
                                        onValueChange = { tempFileName = it },
                                        label = { Text("Имя файла") },
                                        modifier = Modifier.width(180.dp),
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            focusedContainerColor = Color.Transparent,
                                            unfocusedContainerColor = Color.Transparent,
                                            focusedLabelColor = Color(0xFFD43D51),
                                            unfocusedLabelColor = Color.Gray,
                                            focusedTextColor = Color.Black,
                                            unfocusedTextColor = Color.Black
                                        )
                                    )

                                    IconButton(
                                        onClick = {
                                            if (tempFileName.isNotBlank()) {
                                                fileName = tempFileName
                                                isEditingFileName = false
                                                showToast = true
                                                toastMessage = "Имя файла изменено на: $fileName.txt"
                                            }
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Сохранить",
                                            tint = Color(0xFF4CAF50)
                                        )
                                    }

                                    IconButton(
                                        onClick = {
                                            tempFileName = fileName
                                            isEditingFileName = false
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Отмена",
                                            tint = Color.Red
                                        )
                                    }
                                }
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "$fileName.txt",
                                        fontSize = fontSize.sp,
                                        color = Color(0xFFD43D51),
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    IconButton(
                                        onClick = {
                                            tempFileName = fileName
                                            isEditingFileName = true
                                        },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Изменить",
                                            tint = Color(0xFFD43D51),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = fileInfo != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFCE4EC).copy(alpha = 0.5f))
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Файл найден:",
                                    fontSize = (fontSize - 1).sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black
                                )
                                fileInfo?.let { info ->
                                    Text(
                                        text = "Создан: ${info.formattedDate}",
                                        fontSize = (fontSize - 2).sp,
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        text = "Размер: ${info.formattedSize}",
                                        fontSize = (fontSize - 2).sp,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = fileInfo == null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = "Файл $fileName.txt не найден",
                                fontSize = fontSize.sp,
                                color = Color.Red.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }


                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        isLoading = true

                                        val success = if (charactersForBackup.isNotEmpty()) {
                                            externalStorage.saveDisneyCharacters(charactersForBackup, fileName)
                                        } else {
                                            showToast = true
                                            toastMessage = "Нет данных для сохранения!"
                                            false
                                        }

                                        if (success) {
                                            fileInfo = externalStorage.getFileInfo(fileName)
                                            showToast = true
                                            toastMessage = "Файл создан: ${charactersForBackup.size} персонажей"
                                        }
                                        isLoading = false
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading && !isDeleting && !isRestoring &&
                                        charactersForBackup.isNotEmpty() && !isEditingFileName,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50),
                                    contentColor = Color.White
                                )
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Save,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Создать")
                            }

                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        isDeleting = true

                                        val charactersFromFile = externalStorage.readDisneyCharacters(fileName)
                                        var backupCreated = false
                                        var backupCount = 0

                                        if (charactersFromFile != null && charactersFromFile.isNotEmpty()) {
                                            val backupSuccess = internalStorage.saveBackup(charactersFromFile, fileName)
                                            if (backupSuccess) {
                                                hasBackup = true
                                                backupCreated = true
                                                backupCount = charactersFromFile.size
                                            }
                                        } else if (charactersForBackup.isNotEmpty()) {
                                            val backupSuccess = internalStorage.saveBackup(charactersForBackup, fileName)
                                            if (backupSuccess) {
                                                hasBackup = true
                                                backupCreated = true
                                                backupCount = charactersForBackup.size
                                            }
                                        }

                                        val deleteSuccess = externalStorage.deleteFile(fileName)

                                        if (deleteSuccess) {
                                            fileInfo = null
                                            showToast = true
                                            toastMessage = if (backupCreated) {
                                                "Файл удален (резервная копия: $backupCount персонажей)"
                                            } else {
                                                "Файл удален (резервная копия не создана)"
                                            }
                                        } else {
                                            showToast = true
                                            toastMessage = "Ошибка при удалении файла"
                                        }
                                        isDeleting = false
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading && !isDeleting && !isRestoring &&
                                        fileInfo != null && !isEditingFileName,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF44336),
                                    contentColor = Color.White
                                )
                            ) {
                                if (isDeleting) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Удалить")
                            }
                        }

                        AnimatedVisibility(
                            visible = hasBackup,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFE8F5E9).copy(alpha = 0.7f))
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Backup,
                                        contentDescription = null,
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "Резервная копия доступна",
                                            fontSize = (fontSize - 1).sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        coroutineScope.launch {
                                            isRestoring = true

                                            val restoredCharacters = internalStorage.readBackup(fileName)

                                            if (restoredCharacters != null && restoredCharacters.isNotEmpty()) {
                                                val restoreSuccess = externalStorage.saveDisneyCharacters(restoredCharacters, fileName)

                                                if (restoreSuccess) {
                                                    DisneyCacheManager.saveCache(restoredCharacters)
                                                    internalStorage.deleteBackup(fileName)
                                                    hasBackup = false
                                                    fileInfo = externalStorage.getFileInfo(fileName)

                                                    showToast = true
                                                    toastMessage = "Данные восстановлены"
                                                } else {
                                                    showToast = true
                                                    toastMessage = "Ошибка восстановления"
                                                }
                                            } else {
                                                showToast = true
                                                toastMessage = "Резервная копия повреждена"
                                            }
                                            isRestoring = false
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp),
                                    enabled = !isLoading && !isDeleting && !isRestoring && !isEditingFileName,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2196F3),
                                        contentColor = Color.White
                                    )
                                ) {
                                    if (isRestoring) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            color = Color.White,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Восстановление...", fontSize = (fontSize - 1).sp)
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Restore,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Восстановить файл", fontSize = (fontSize - 1).sp)
                                    }
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Смена пароля",
                            fontSize = (fontSize + 2).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD43D51)
                        )

                        if (storedPassword.isNotEmpty()) {
                            PasswordField(
                                value = currentPassword,
                                onValueChange = { currentPassword = it },
                                label = "Текущий пароль",
                                showPassword = showCurrentPassword,
                                onToggleVisibility = { showCurrentPassword = !showCurrentPassword },
                                fontSize = fontSize
                            )
                        }

                        PasswordField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = "Новый пароль",
                            showPassword = showNewPassword,
                            onToggleVisibility = { showNewPassword = !showNewPassword },
                            fontSize = fontSize
                        )

                        PasswordField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = "Подтвердите пароль",
                            showPassword = showConfirmPassword,
                            onToggleVisibility = { showConfirmPassword = !showConfirmPassword },
                            fontSize = fontSize
                        )

                        Button(
                            onClick = {
                                if (newPassword.length < 6) {
                                    showToast = true
                                    toastMessage = "Пароль должен быть не менее 6 символов"
                                    return@Button
                                }
                                if (newPassword != confirmPassword) {
                                    showToast = true
                                    toastMessage = "Пароли не совпадают"
                                    return@Button
                                }
                                if (storedPassword.isNotEmpty() && currentPassword != storedPassword) {
                                    showToast = true
                                    toastMessage = "Неверный текущий пароль"
                                    return@Button
                                }

                                sharedPrefs.setPassword(newPassword)
                                currentPassword = ""
                                newPassword = ""
                                confirmPassword = ""
                                showCurrentPassword = false
                                showNewPassword = false
                                showConfirmPassword = false
                                showToast = true
                                toastMessage = "Пароль успешно изменен"
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = newPassword.isNotBlank() && confirmPassword.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFFD43D51),
                                disabledContainerColor = Color.Gray.copy(alpha = 0.2f),
                                disabledContentColor = Color.Gray
                            ),
                            border = BorderStroke(1.dp, Color(0xFFD43D51))
                        ) {
                            Text(
                                "Сменить пароль",
                                fontSize = fontSize.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.95f)
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Размер шрифта",
                            fontSize = (fontSize + 2).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD43D51)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Текущий:",
                                fontSize = fontSize.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "${fontSize.toInt()}sp",
                                fontSize = (fontSize + 2).sp,
                                color = Color(0xFFD43D51),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(12f, 14f, 16f, 18f, 20f).forEach { size ->
                                Button(
                                    onClick = { fontSize = size },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (fontSize == size) Color(0xFFD43D51) else Color.Transparent,
                                        contentColor = if (fontSize == size) Color.White else Color(0xFFD43D51)
                                    ),
                                    border = if (fontSize == size) null else BorderStroke(
                                        1.dp,
                                        Color(0xFFD43D51).copy(alpha = 0.5f)
                                    )
                                ) {
                                    Text("${size.toInt()}sp", fontSize = 12.sp)
                                }
                            }
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    dataStore.setFontSize(fontSize)
                                    showToast = true
                                    toastMessage = "Размер шрифта сохранен"
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFFD43D51)
                            ),
                            border = BorderStroke(1.dp, Color(0xFFD43D51))
                        ) {
                            Text(
                                "Сохранить размер шрифта",
                                fontSize = fontSize.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = showToast,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD43D51).copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Text(
                    text = toastMessage,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    fontSize = fontSize.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    showPassword: Boolean,
    onToggleVisibility: () -> Unit,
    fontSize: Float
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = (fontSize - 2).sp) },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (showPassword) "Скрыть пароль" else "Показать пароль",
                    tint = Color(0xFFD43D51)
                )
            }
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedLabelColor = Color(0xFFD43D51),
            unfocusedLabelColor = Color.Gray,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        )
    )
}