package com.tauri.dev

import android.util.Log
import android.os.Bundle
import androidx.activity.enableEdgeToEdge

class MainActivity : TauriActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    Log.d("FOXTEST", "Hello from Android")
    Log.w("FOXTEST", "Hello from Android")
    Log.e("FOXTEST", "Hello from Android")

    System.out.println("SYSOUT: Hello from Android MainActivity")

    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
  }
}