package com.example.mobilelabs.onboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mobilelabs.MainActivity
import com.example.mobilelabs.R
import com.example.mobilelabs.signin.SignInFragment
import com.example.mobilelabs.signup.SignUpFragment
import com.example.mobilelabs.ui.theme.MobileLabsTheme

class OnBoardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MobileLabsTheme {
                    OnBoardScreen(
                        onSignIn = {
                            findNavController().navigate(OnBoardFragmentDirections.actionOnboardToSignIn(user = null))
                        },
                        onSignUp = {
                            findNavController().navigate(R.id.action_onboard_to_sign_up)
                        }
                    )
                }
            }
        }
    }
}
