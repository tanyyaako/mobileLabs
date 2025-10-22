package com.example.mobilelabs

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController

class MainActivity : AppCompatActivity() {

    private var backPressedTime: Long = 0
    private var toast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_host)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController(R.id.nav_host_fragment)
                val currentDestination = navController.currentDestination?.id

                if (currentDestination == R.id.home) {
                    if (backPressedTime + 2000 > System.currentTimeMillis()) {
                        toast?.cancel()
                        finish()
                    } else {
                        toast = Toast.makeText(
                            this@MainActivity,
                            "Нажмите ещё раз, чтобы выйти",
                            Toast.LENGTH_SHORT
                        )
                        toast?.show()
                        backPressedTime = System.currentTimeMillis()
                    }
                } else {
                    navController.popBackStack()
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
