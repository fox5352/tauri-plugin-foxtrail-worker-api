package com.plugin.foxtrailworker

import android.util.Log

class Example {
    fun pong(value: String): String {
        Log.i("Pong", value)
        val greeting = String.format("Hello from kotlin, ", value);
        return greeting
    }
}
