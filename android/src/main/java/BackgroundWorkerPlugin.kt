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
}
