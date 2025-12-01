package com.example.mobilelabs.signup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.mobilelabs.MainActivity
import com.example.mobilelabs.Model.User
import com.example.mobilelabs.ui.theme.MobileLabsTheme
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.os.bundleOf
import androidx.datastore.dataStore
import androidx.navigation.findNavController
import com.example.mobilelabs.signin.SignInFragment
import com.example.mobilelabs.store.datastore.SettingsDataStore
import com.example.mobilelabs.store.sharedPref.SettingsSharedPreferences
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class SignUpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val context = LocalContext.current
                val sharedPrefs = remember { SettingsSharedPreferences(context) }
                val coroutineScope = rememberCoroutineScope()
                var name by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                MobileLabsTheme {
                    SignUpScreen(
                        name = name,
                        password = password,
                        onNameChange = { name = it },
                        onPasswordChange = { password = it },
                        onSignIn = {
                            val user = User(name = name, email = "", password = password)
                            val action = SignUpFragmentDirections.actionSignUpToSignIn(user)
                            findNavController().navigate(action)
                        },
                        onRegister = {
                            coroutineScope.launch {
                                sharedPrefs.setPassword(password)
                            }
                            val user = User(name = name, email = "", password = password)
                            val action = SignUpFragmentDirections.actionSignUpToSignIn(user)
                            findNavController().navigate(action)
                        },
                        onBack = {
                            parentFragmentManager.popBackStack()
                        }
                    )
                }
            }
        }
    }
}
