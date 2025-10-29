@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.plugin.foxtrailworker

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Job(
    val id: Int,
    @SerialName("created_at") val createdAt: String,
    @SerialName("api_id") val apiId: String?,
    val adref: String?,
    val title: String,
    @SerialName("listing_created_at") val listingCreatedAt: String?,
    val category: String?,
    val location: String?,
    val description: String?,
    @SerialName("redirect_url") val redirectUrl: String?,
    val contract: String?,
    val company: String?
)

@Serializable
data class FeedCountResponse(
    @SerialName("get_feed_count") val count: Int
)

@Serializable
data class BatchCreatedAt(
    @SerialName("created_at") val created_at: String,
    @SerialName("batch_number") val batch_number: Long
)


@Serializable
data class NotificationType(
    @SerialName("created_at") val created_at: String,
    @SerialName("user_id") val user_id: String,
    @SerialName("batch_number") val batch_number: Long,
    @SerialName("message_count") val message_count: Long
)

class Supabase(sbUrl: String, sbKey: String) {

    private val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = sbUrl,
        supabaseKey = sbKey
    ) {
        install(Postgrest)
    }

    suspend fun getJobs(): List<Job> {
        val jobs: List<Job> = client
            .from("jobs")
            .select()
            .decodeList<Job>()

        return jobs
    }

    suspend fun getFeedCount(userId: String): Int {
        val count = client
            .from("feed")
            .select {
                filter {
                    eq("user_id", userId)
                }
                count(Count.EXACT)
            }
            .countOrNull()

        return count?.toInt() ?: 0
    }

    suspend fun getLatestBatchCreatedAt(userId: String): Long? {

        val result = client.from("batch_table")
            .select(columns = Columns.list("created_at", "batch_number")) {
                order(column = "created_at", order = Order.DESCENDING)
                limit(1)
            }
            .decodeList<BatchCreatedAt>()
            .firstOrNull()

        return result?.batch_number
    }

    suspend fun getNotificationCount(user_id: String): Long? {
        val batch_number = getLatestBatchCreatedAt(user_id);

        if (batch_number == null){
            return null;
        }

        val result = client.from("notifications")
            .select(columns = Columns.list("created_at", "user_id", "batch_number", "message_count")) {
                filter {
                    eq("user_id", user_id)
                    eq("batch_number", batch_number)
                }
            }.decodeList<NotificationType>()
            .firstOrNull()

        Log.e("GYATTA", "list:[${result}]")

        return result?.message_count
    }
}