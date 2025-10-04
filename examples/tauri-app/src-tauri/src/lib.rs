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
            value: None,
            public_key: Some("https://fvlsvvzbywmozvhwxmzl.supabase.co".to_string()),
            public_url: Some("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZ2bHN2dnpieXdtb3p2aHd4bXpsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc0Mzg1MDksImV4cCI6MjA3MzAxNDUwOX0.Si24RvvrYtTNeunPxC-w_qqq8JgdEiFPBh9wNsB0O8g".to_string()),
            user_id: Some("11668e59-4584-4378-8a4c-cc96eb6c1691".to_string()),
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
