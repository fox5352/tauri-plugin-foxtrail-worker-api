package com.plugin.foxtrailworker

import android.app.Activity
import app.tauri.annotation.Command
import app.tauri.annotation.InvokeArg
import app.tauri.annotation.TauriPlugin
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin
import app.tauri.plugin.Invoke

@InvokeArg
class WorkerPingArgs {  // Changed name
  var value: String? = null
  var token: String? = null
  var url: String? = null
}

@TauriPlugin
class BackgroundWorkerPlugin(private val activity: Activity) : Plugin(activity) {
  private val worker = BackgroundWorker()
  private val fetch = Fetch()

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


    val url = args.url
    val token = args.token

    // Validate
    if (url.isNullOrBlank()) {
      ret.put("value", "Missing or empty value on url=$url")
      invoke.resolve(ret)

    }else {
      // At this point they're guaranteed to be non-null & non-blank
      worker.start(activity.applicationContext, url)

      ret.put("value", "Starting worker with url=$url")
      invoke.resolve(ret)

    }
  }
}
