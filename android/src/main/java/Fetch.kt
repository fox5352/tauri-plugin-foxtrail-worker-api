@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.plugin.foxtrailworker

import okhttp3.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

data class FetchResponse(
    val status: Int,
    val ok: Boolean,
    val headers: Map<String, String>,
    val text: String
)
@Serializable
data class BatchData (
    val id: Int,
    @SerialName("created_at") val createdAt: String,
    @SerialName("user_id") val userId: String,
    @SerialName("batch_number") val batchNumber: Long,
    @SerialName("message_count") val messageCount: Int,
)

class Fetch {
    private val client = OkHttpClient()

    suspend fun request(
        url: String,
        method: String = "GET",
        headers: Map<String, String> = emptyMap(),
        body: String? = null
    ): FetchResponse = withContext(Dispatchers.IO) {
        val builder = Request.Builder().url(url)

        // Add headers
        headers.forEach { (key, value) -> builder.addHeader(key, value) }

        // Choose method
        val requestBody = body?.let {
            RequestBody.create("application/json; charset=utf-8".toMediaType(), it)
        }

        val request = when (method.uppercase()) {
            "POST" -> builder.post(requestBody ?: error("POST requires a body")).build()
            "PUT" -> builder.put(requestBody ?: error("PUT requires a body")).build()
            "DELETE" -> if (requestBody != null) builder.delete(requestBody).build() else builder.delete().build()
            else -> builder.get().build()
        }

        val response = client.newCall(request).execute()
        FetchResponse(
            status = response.code,
            ok = response.isSuccessful,
            headers = response.headers.toMap(),
            text = response.body?.string() ?: ""
        )
    }
}


suspend fun fetch(user_id: String,url: String, auth: String): BatchData {
    val result = Fetch().request(url="${url}/functions/v1/batch_number", method = "POST",  headers = mapOf(
        "Authorization" to "Bearer ${auth}",
        "Content-Type" to "application/json"
    ),
        body = """{ "name":"Functions", "user_id":"${user_id}" }"""
    )
 
    return Json.decodeFromString<BatchData>(result.text)
}
