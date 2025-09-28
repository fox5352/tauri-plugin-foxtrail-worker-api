package com.plugin.foxtrailworker

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class Fetch {
    private val client = OkHttpClient()

    // Suspend function to perform a GET request
    suspend fun get(url: String): String = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("HTTP error ${response.code}")
            response.body?.string() ?: throw Exception("Empty response")
        }
    }
}
