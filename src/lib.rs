use tauri::{
    plugin::{Builder, PluginApi, TauriPlugin},
    AppHandle, Manager, Runtime,
};

pub use models::*;

#[cfg(desktop)]
mod desktop;
#[cfg(mobile)]
mod mobile;

mod commands;
mod error;
mod models;

pub use error::{Error, Result};

#[cfg(desktop)]
use desktop::FoxtrailWorker;
#[cfg(mobile)]
use mobile::FoxtrailWorker;

/// Extensions to [`tauri::App`], [`tauri::AppHandle`] and [`tauri::Window`] to access the foxtrail-worker APIs.
pub trait FoxtrailWorkerExt<R: Runtime> {
    fn foxtrail_worker(&self) -> &FoxtrailWorker<R>;
}

impl<R: Runtime, T: Manager<R>> crate::FoxtrailWorkerExt<R> for T {
    fn foxtrail_worker(&self) -> &FoxtrailWorker<R> {
        self.state::<FoxtrailWorker<R>>().inner()
    }
}

/// Initializes the plugin.
pub fn init<R: Runtime>() -> TauriPlugin<R> {
    Builder::new("foxtrail-worker")
        .invoke_handler(tauri::generate_handler![commands::ping])
        .setup(|app: &AppHandle<R>, api: PluginApi<R, ()>| {
            #[cfg(mobile)]
            let foxtrail_worker = mobile::init(app, api)?;

            #[cfg(desktop)]
            let foxtrail_worker = desktop::init(app, api)?;

            // #[cfg(target_os = "android")]
            // let _handle = api.register_android_plugin("com.plugin.foxtrailworker", "ExamplePlugin")?;

            app.manage(foxtrail_worker);
            Ok(())
        })
        .build()
}
