package com.plugin.foxtrailworker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.Data
import androidx.work.NetworkType

import java.util.concurrent.TimeUnit
import androidx.work.Constraints



class FeedSyncWorker(
    appContext: Context,
    val params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val userId = inputData.getString("userId")
        val url = inputData.getString("url")
        val key = inputData.getString("key")

        if (userId.isNullOrEmpty() || key.isNullOrEmpty() || url.isNullOrEmpty()) {
            Log.e("FeedSyncWorker", "Missing required parameters")
            return Result.failure()
        }

        return try {
            val supabase = Supabase(url, key)

            Log.i("FeedSyncWorker", "Syncing feed for user $userId")

            val count = supabase.getFeedCount(userId)

            Log.i("FeedSyncWorker", "Feed count for user $userId: $count")

            Result.success()
        } catch (e: Exception) {
            Log.e("FeedSyncWorker", "Error syncing feed: ${e.message}", e)
            e.printStackTrace()
            Result.retry()
        }
    }
}


class BackgroundWorker {
    fun start(context: Context, url: String, key: String, userId: String) {
        val inputData = Data.Builder()
            .putString("url", url)
            .putString("key", key)
            .putString("userId", userId)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)  // Be nice to battery
            .build()


        Log.i("FeedSyncWorker", "Starting production sync worker 15 minutes")
        // Normal 15-minute interval for production
        val workRequest = PeriodicWorkRequestBuilder<FeedSyncWorker>(15, TimeUnit.MINUTES)
            .setInputData(inputData)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
            "SyncWorker",
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest
        )
    }
}
