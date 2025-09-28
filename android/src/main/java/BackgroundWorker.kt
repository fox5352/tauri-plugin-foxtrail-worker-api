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

  private val fetch = Fetch()

  override suspend fun doWork(): Result {
    val userId = inputData.getString("user_id")
    val url = inputData.getString("url")

    if (userId.isNullOrEmpty()) {
      return Result.failure()
    }

    if (url.isNullOrEmpty()) {
      return Result.failure()
    }

    return try {
      val response = fetch.get(url)
      println("API Response: $response")

      // TODO: trigger push notification if needed

      Result.success()
    } catch (e: Exception) {
      e.printStackTrace()
      Result.retry()
    }
  }
}


class BackgroundWorker {
  fun start(context: Context, url: String) {
    val inputData = Data.Builder()
      .putString("url", url)
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
