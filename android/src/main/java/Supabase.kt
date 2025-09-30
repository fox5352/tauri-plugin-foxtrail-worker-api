package com.plugin.foxtrailworker

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
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


class Supabase(sbUrl: String, sbKey: String) {

    // Create and configure the client with needed modules
    private val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = sbUrl,
        supabaseKey = sbKey
    ) {
        // install(Auth)      // if you need authentication
        install(Postgrest) // if you need database access
        // install(Realtime) // if you need realtime subscriptions
        // install(Storage)  // if you need file storage
    }

    // Method to fetch all jobs
    suspend fun getJobs(): List<Job> {
        val jobs: List<Job> = client
            .from("jobs")          // table name
            .select()              // select all columns
            .decodeList<Job>()     // decode JSON into Job objects

        return jobs
    }
}
