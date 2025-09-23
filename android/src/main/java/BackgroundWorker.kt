package com.plugin.foxtrailworker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class FeedSyncWorker(
  appContext: Context,
  val params: WorkerParameters
) : Worker(appContext, params) {

  override fun doWork(): Result {
    val user_id = params.inputData.getString("user_id")

    if (user_id?.isEmpty() ?: true) {
      return Result.failure()
    }

    // TODO: add a push notification here

    return Result.success()
  }
}


class BackgroundWorker {
  fun start(context: Context) {
    val workRequest = PeriodicWorkRequestBuilder<FeedSyncWorker>(1, TimeUnit.HOURS)
      .build()

    WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
      "SyncWorker",
      ExistingPeriodicWorkPolicy.REPLACE,
      workRequest
    )
  }
}
