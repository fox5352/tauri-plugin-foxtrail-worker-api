package com.plugin.foxtrailworker

import android.app.Activity
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin
import app.tauri.plugin.Invoke

@InvokeArg
class WorkerPingArgs {
  var value: String? = null
  var publicUrl: String? = null
  var publicKey: String? = null
  var userId: String? = null
}

@TauriPlugin
class BackgroundWorkerPlugin(private val activity: Activity) : Plugin(activity) {
  private val worker = BackgroundWorker()

  @Command
  fun ping(invoke: Invoke) {
    val args = invoke.parseArgs(WorkerPingArgs::class.java)  // Use new name

    val ret = JSObject()
    ret.put("value", args.value ?: "default value :(")
    invoke.resolve(ret)
  }

  @Command
  fun greet(invoke: Invoke) {
    val ret = JSObject()
    ret.put("value", "Hello from kotlin")
    invoke.resolve(ret)
  }


  @Command
  fun start_worker(invoke: Invoke) {
    val args = invoke.parseArgs(WorkerPingArgs::class.java)
    val ret = JSObject()

    val url = args.publicUrl
    val key = args.publicKey
    val user_id = args.userId

    if (url.isNullOrBlank() || key.isNullOrBlank() || user_id.isNullOrBlank()) {
      ret.put("value", "Missing or empty value on url=$url or key=$key or user_id=$user_id")
      invoke.resolve(ret)
      return
    }

    // At this point they're guaranteed to be non-null & non-blank
    worker.start(activity.applicationContext, url, key, user_id)

    ret.put("value", "Starting worker")
    invoke.resolve(ret)
  }
}
