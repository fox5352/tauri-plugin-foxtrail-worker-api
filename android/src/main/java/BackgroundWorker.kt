package com.plugin.foxtrailworker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.Data
import java.util.concurrent.TimeUnit

class FeedSyncWorker(
  appContext: Context,
  val params: WorkerParameters
) : CoroutineWorker(appContext, params) {

  override suspend fun doWork(): Result {
    val userId = inputData.getString("user_id")
    val url = inputData.getString("url")
    val key = inputData.getString("key")

    if (userId.isNullOrEmpty() || key.isNullOrEmpty() || url.isNullOrEmpty()) {
        return Result.failure()
    }

    return try {
        val supabase = Supabase(url, key)

        supabase.getJobs();


        Result.success()
    } catch (e: Exception) {
        e.printStackTrace()
        Result.retry()
    }
  }
}


class BackgroundWorker {
  fun start(context: Context, url: String, key: String, user_id: String) {
    val inputData = Data.Builder()
      .putString("url", url)
      .putString("key", key)
      .putString("user_id", user_id)
      .build()

    val workRequest = PeriodicWorkRequestBuilder<FeedSyncWorker>(15, TimeUnit.MINUTES)
      .setInputData(inputData)
      .build()

    WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
      "SyncWorker",
      ExistingPeriodicWorkPolicy.REPLACE,
      workRequest
    )
  }
}
