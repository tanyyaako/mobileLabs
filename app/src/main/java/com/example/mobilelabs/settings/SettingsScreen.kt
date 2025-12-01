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
    var showSuccessMessage by remember { mutableStateOf(false) }
    var successMessage by remember { mutableStateOf("") }

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

    LaunchedEffect(showSuccessMessage) {
        if (showSuccessMessage) {
            delay(3000)
            showSuccessMessage = false
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
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Управление резервными копиями",
                            fontSize = (fontSize + 4).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Divider(color = Color.Gray.copy(alpha = 0.3f), thickness = 1.dp)

                        Text(
                            text = "Настройка имени файла:",
                            fontSize = fontSize.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            if (isEditingFileName) {
                                OutlinedTextField(
                                    value = tempFileName,
                                    onValueChange = { tempFileName = it },
                                    label = { Text("Имя файла (без .txt)") },
                                    modifier = Modifier.weight(1f),
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

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    IconButton(
                                        onClick = {
                                            if (tempFileName.isNotBlank() && tempFileName != fileName) {
                                                fileName = tempFileName
                                                isEditingFileName = false
                                                Toast.makeText(
                                                    context,
                                                    "Имя файла изменено на: $fileName.txt",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else if (tempFileName.isBlank()) {
                                                Toast.makeText(
                                                    context,
                                                    "Имя файла не может быть пустым",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                isEditingFileName = false
                                            }
                                        },
                                        modifier = Modifier.size(48.dp)
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
                                        },
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Отмена",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Текущий файл:",
                                            fontSize = fontSize.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Medium
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
                                                contentDescription = "Изменить имя файла",
                                                tint = Color(0xFFD43D51),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "$fileName.txt",
                                        fontSize = (fontSize).sp,
                                        color = Color(0xFFD43D51),
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        }


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFE8F5E9).copy(alpha = 0.7f))
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
//                            Column {
//                                Text(
//                                    text = "Данные для бэкапа:",
//                                    fontSize = (fontSize - 1).sp,
//                                    fontWeight = FontWeight.Medium,
//                                    color = Color.Black
//                                )
//                                Text(
//                                    text = "Персонажей доступно: ${charactersForBackup.size}",
//                                    fontSize = (fontSize - 2).sp,
//                                    color = Color.DarkGray
//                                )
//                            }
                        }

                        AnimatedVisibility(
                            visible = fileInfo != null,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            OutlinedCard(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.outlinedCardColors(
                                    containerColor = Color.White
                                ),
                                border = BorderStroke(1.dp, Color(0xFFD43D51).copy(alpha = 0.3f))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Информация о файле:",
                                        fontSize = fontSize.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )
                                    fileInfo?.let { info ->
                                        Text(
                                            text = "Название: ${info.name}",
                                            fontSize = (fontSize - 2).sp,
                                            color = Color.DarkGray
                                        )
                                        Text(
                                            text = "Размер: ${info.formattedSize}",
                                            fontSize = (fontSize - 2).sp,
                                            color = Color.DarkGray
                                        )
                                        Text(
                                            text = "Создан: ${info.formattedDate}",
                                            fontSize = (fontSize - 2).sp,
                                            color = Color.DarkGray
                                        )
                                        Text(
                                            text = "Расположение: ${info.path}",
                                            fontSize = (fontSize - 2).sp,
                                            color = Color.DarkGray,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
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
                                color = Color.Red,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        AnimatedVisibility(
                            visible = hasBackup,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE3F2FD))
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Backup,
                                    contentDescription = null,
                                    tint = Color(0xFF2196F3),
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Доступна резервная копия для $fileName.txt",
                                    fontSize = fontSize.sp,
                                    color = Color(0xFF2196F3),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }


                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        isLoading = true

                                        val success = if (charactersForBackup.isNotEmpty()) {
                                            externalStorage.saveDisneyCharacters(charactersForBackup, fileName)
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Нет данных для сохранения! Загрузите персонажей на главном экране.",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            false
                                        }

                                        if (success) {
                                            fileInfo = externalStorage.getFileInfo(fileName)
                                            successMessage = "Файл $fileName.txt создан!\nСохранено ${charactersForBackup.size} персонажей"
                                            showSuccessMessage = true

                                            Toast.makeText(
                                                context,
                                                "Файл $fileName.txt создан!\nСохранено ${charactersForBackup.size} персонажей",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            successMessage = "Ошибка при создании файла"
                                            showSuccessMessage = true
                                        }
                                        isLoading = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
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
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Создание...")
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Save,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Создать файл $fileName.txt")
                                }
                            }

                            Button(
                                onClick = {
                                    coroutineScope.launch {
                                        isDeleting = true

                                        if (charactersForBackup.isNotEmpty()) {
                                            val backupSuccess = internalStorage.saveBackup(charactersForBackup, fileName)
                                            if (backupSuccess) {
                                                hasBackup = true
                                            }
                                        }

                                        val deleteSuccess = externalStorage.deleteFile(fileName)
                                        if (deleteSuccess) {
                                            fileInfo = null
                                            successMessage = " Файл $fileName.txt удален\n(резервная копия создана: ${charactersForBackup.size} персонажей)"
                                            showSuccessMessage = true

                                            Toast.makeText(
                                                context,
                                                " Файл удален\n Создана резервная копия",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            successMessage = " Ошибка при удалении файла"
                                            showSuccessMessage = true
                                        }
                                        isDeleting = false
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !isLoading && !isDeleting && !isRestoring &&
                                        fileInfo != null && !isEditingFileName,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error,
                                    contentColor = Color.White
                                )
                            ) {
                                if (isDeleting) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = Color.White,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Удаление...")
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Удалить файл $fileName.txt ")
                                }
                            }

                            AnimatedVisibility(
                                visible = hasBackup,
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
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

                                                    successMessage = "Данные восстановлены в файл $fileName.txt!\nВосстановлено ${restoredCharacters.size} персонажей"
                                                    showSuccessMessage = true

                                                    Toast.makeText(
                                                        context,
                                                        "Данные восстановлены в $fileName.txt",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                } else {
                                                    successMessage = "Ошибка при восстановлении файла"
                                                    showSuccessMessage = true
                                                }
                                            } else {
                                                successMessage = "Резервная копия пуста или повреждена"
                                                showSuccessMessage = true
                                            }
                                            isRestoring = false
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    enabled = !isLoading && !isDeleting && !isRestoring && !isEditingFileName,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF2196F3),
                                        contentColor = Color.White
                                    )
                                ) {
                                    if (isRestoring) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(20.dp),
                                            color = Color.White,
                                            strokeWidth = 2.dp
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Восстановление...")
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.Restore,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Восстановить в $fileName.txt")
                                    }
                                }
                            }
                        }

                        AnimatedVisibility(
                            visible = showSuccessMessage,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = successMessage,
                                fontSize = (fontSize - 2).sp,
                                color = Color(0xFF4CAF50),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Смена пароля",
                            fontSize = (fontSize + 4).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        if (storedPassword.isNotEmpty()) {
                            OutlinedTextField(
                                value = currentPassword,
                                onValueChange = { currentPassword = it },
                                label = {
                                    Text(
                                        "Текущий пароль",
                                        fontSize = (fontSize - 2).sp
                                    )
                                },
                                visualTransformation = if (showCurrentPassword) {
                                    VisualTransformation.None
                                } else {
                                    PasswordVisualTransformation()
                                },
                                trailingIcon = {
                                    IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                                        Icon(
                                            imageVector = if (showCurrentPassword)
                                                Icons.Filled.Visibility
                                            else
                                                Icons.Filled.VisibilityOff,
                                            contentDescription = if (showCurrentPassword)
                                                "Скрыть пароль"
                                            else
                                                "Показать пароль",
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

                        OutlinedTextField(
                            value = newPassword,
                            onValueChange = { newPassword = it },
                            label = {
                                Text("Новый пароль", fontSize = (fontSize - 2).sp)
                            },
                            visualTransformation = if (showNewPassword) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            trailingIcon = {
                                IconButton(onClick = { showNewPassword = !showNewPassword }) {
                                    Icon(
                                        imageVector = if (showNewPassword)
                                            Icons.Filled.Visibility
                                        else
                                            Icons.Filled.VisibilityOff,
                                        contentDescription = if (showNewPassword)
                                            "Скрыть пароль"
                                        else
                                            "Показать пароль",
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

                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = {
                                Text("Подтвердите пароль", fontSize = (fontSize - 2).sp)
                            },
                            visualTransformation = if (showConfirmPassword) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            trailingIcon = {
                                IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                    Icon(
                                        imageVector = if (showConfirmPassword)
                                            Icons.Filled.Visibility
                                        else
                                            Icons.Filled.VisibilityOff,
                                        contentDescription = if (showConfirmPassword)
                                            "Скрыть пароль"
                                        else
                                            "Показать пароль",
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

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                if (newPassword.length < 6) {
                                    Toast.makeText(context, "Пароль должен быть не менее 6 символов", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (newPassword != confirmPassword) {
                                    Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                if (storedPassword.isNotEmpty() && currentPassword != storedPassword) {
                                    Toast.makeText(context, "Неверный текущий пароль", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                sharedPrefs.setPassword(newPassword)
                                currentPassword = ""
                                newPassword = ""
                                confirmPassword = ""
                                showCurrentPassword = false
                                showNewPassword = false
                                showConfirmPassword = false
                                Toast.makeText(context, "Пароль успешно изменен", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            enabled = newPassword.isNotBlank() && confirmPassword.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFFD43D51)
                            )
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
                        containerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Размер шрифта",
                            fontSize = (fontSize + 4).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Текущий размер:",
                                fontSize = fontSize.sp,
                                color = Color.Black
                            )
                            Text(
                                text = "${fontSize.toInt()}sp",
                                fontSize = (fontSize + 2).sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FontSizeButton(
                                    size = 12f,
                                    currentSize = fontSize,
                                    label = "Маленький",
                                    onClick = { fontSize = 12f },
                                    modifier = Modifier.weight(1f)
                                )
                                FontSizeButton(
                                    size = 14f,
                                    currentSize = fontSize,
                                    label = "Средний",
                                    onClick = { fontSize = 14f },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FontSizeButton(
                                    size = 16f,
                                    currentSize = fontSize,
                                    label = "Большой",
                                    onClick = { fontSize = 16f },
                                    modifier = Modifier.weight(1f)
                                )
                                FontSizeButton(
                                    size = 18f,
                                    currentSize = fontSize,
                                    label = "Очень большой",
                                    onClick = { fontSize = 18f },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FontSizeButton(
                                    size = 20f,
                                    currentSize = fontSize,
                                    label = "Огромный",
                                    onClick = { fontSize = 20f },
                                    modifier = Modifier.weight(1f)
                                )
                                FontSizeButton(
                                    size = 24f,
                                    currentSize = fontSize,
                                    label = "Гигантский",
                                    onClick = { fontSize = 24f },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    dataStore.setFontSize(fontSize)
                                    Toast.makeText(context, "Размер шрифта сохранен", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFFD43D51)
                            )
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
    }
}

@Composable
fun FontSizeButton(
    size: Float,
    currentSize: Float,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(76.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (currentSize == size) Color(0xFFD43D51) else Color.Transparent,
            contentColor = if (currentSize == size) Color.White else Color(0xFFD43D51)
        ),
        border = if (currentSize == size) null else BorderStroke(
            width = 1.dp,
            color = Color.Gray
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "${size.toInt()}sp",
                fontSize = size.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = label,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}