package com.example.mobilelabs.settings

import SettingsScreen
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.mobilelabs.ui.theme.MobileLabsTheme

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = androidx.compose.ui.platform.ComposeView(requireContext()).apply {
        setContent {
            MobileLabsTheme {
                SettingsScreen(
                    onBack = {
                        findNavController().popBackStack()
                    }
                )
            }
        }
    }
}