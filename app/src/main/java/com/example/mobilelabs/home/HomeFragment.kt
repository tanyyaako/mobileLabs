package com.example.mobilelabs.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.R
import androidx.navigation.findNavController
import com.example.mobilelabs.ui.theme.MobileLabsTheme

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MobileLabsTheme {
                    HomeScreen(
                        onSettingsClick = {
                            findNavController().navigate(
                                HomeFragmentDirections.actionHomeToSettings()
                            )
                        },
                        onNavigateToSettingsWithData = { characters ->
                            findNavController().navigate(
                                HomeFragmentDirections.actionHomeToSettings()
                            )
                        }
                    )
                }
            }
        }
    }
}