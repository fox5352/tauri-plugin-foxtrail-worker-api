// Learn more about Tauri commands at https://v2.tauri.app/develop/calling-rust/#commands

use tauri::{AppHandle, Runtime};
use tauri_plugin_foxtrail_worker::{FoxtrailWorkerExt, PingRequest, WorkerRequest};
use tauri_plugin_log::{Target, TargetKind};

#[tauri::command]
async fn greet<R: Runtime>(app: AppHandle<R>, name: String) -> Option<String> {
    app.foxtrail_worker()
        .greet(PingRequest {
            value: Some(name.clone()),
        })
        .ok()?
        .value
}

#[tauri::command]
async fn start_worker<R: Runtime>(app: AppHandle<R>) -> Option<String> {
    app.foxtrail_worker()
        .start_worker(WorkerRequest {
            user_id: None,
            value: Some("testing".to_string()),
            url: None,
        })
        .ok()?
        .value
}

#[cfg_attr(mobile, tauri::mobile_entry_point)]
pub fn run() {
    tauri::Builder::default()
        .plugin(
            tauri_plugin_log::Builder::new()
                .target(Target::new(TargetKind::Stdout))
                .build(),
        )
        .invoke_handler(tauri::generate_handler![greet, start_worker])
        .plugin(tauri_plugin_foxtrail_worker::init())
        .run(tauri::generate_context!())
        .expect("error while running tauri application");
}
