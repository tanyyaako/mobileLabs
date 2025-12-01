package com.example.mobilelabs.signin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.mobilelabs.ui.theme.MobileLabsTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.example.mobilelabs.MainActivity
import com.example.mobilelabs.Model.User
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mobilelabs.R
import com.example.mobilelabs.home.HomeFragment
import com.example.mobilelabs.signup.SignUpFragment
import com.example.mobilelabs.store.datastore.SettingsDataStore
import com.example.mobilelabs.store.sharedPref.SettingsSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignInFragment : Fragment() {

    private val args: SignInFragmentArgs by navArgs()

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
                val user = args.user
                var name by remember { mutableStateOf(user?.name ?: "") }
                var password by remember { mutableStateOf(user?.password ?: "") }

                MobileLabsTheme {
                    SignInScreen(
                        name = name,
                        password = password,
                        onNameChange = { name = it },
                        onPasswordChange = { password = it },
                        onSignIn = {
                            coroutineScope.launch {
                                sharedPrefs.setPassword(password)
                                withContext(Dispatchers.Main) {
                                    findNavController().navigate(
                                        SignInFragmentDirections.actionSignInToHome(name)
                                    )
                                }
                            }
                        },
                        onSignUp = {
                            findNavController().navigate(
                                SignInFragmentDirections.actionSignInToSignUp()
                            )
                        },
                        onBack = {
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }
}
