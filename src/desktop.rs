use serde::de::DeserializeOwned;
use tauri::{plugin::PluginApi, AppHandle, Runtime};

use crate::models::*;

pub fn init<R: Runtime, C: DeserializeOwned>(
  app: &AppHandle<R>,
  _api: PluginApi<R, C>,
) -> crate::Result<FoxtrailWorker<R>> {
  Ok(FoxtrailWorker(app.clone()))
}

/// Access to the foxtrail-worker APIs.
pub struct FoxtrailWorker<R: Runtime>(AppHandle<R>);

impl<R: Runtime> FoxtrailWorker<R> {
  pub fn ping(&self, payload: PingRequest) -> crate::Result<PingResponse> {
    Ok(PingResponse {
      value: payload.value,
    })
  }
}
