@file:OptIn(kotlinx.serialization.InternalSerializationApi::class)

package com.plugin.foxtrailworker

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.query.Count
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from

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
}
