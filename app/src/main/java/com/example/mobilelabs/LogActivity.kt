package com.example.mobilelabs

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity

abstract class LogActivity : ComponentActivity() {

    override fun onStart(){
        Log.d("activity", "onStart")
        super.onStart() }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Log.d("activity", "onCreate")
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onRestart() {
        Log.d("activity", "onRestart")
        super.onRestart()
    }

    override fun onResume() {
        super.onResume()
        Log.d("activity","onResume")
    }

    override fun onPause() {
        Log.d("activity","onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("activity","onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("activity","onDestroy")
        super.onDestroy()
    }
}