use tauri::{command, AppHandle, Runtime};

use crate::models::*;
use crate::FoxtrailWorkerExt;
use crate::Result;

#[command]
pub(crate) async fn ping<R: Runtime>(
    app: AppHandle<R>,
    payload: PingRequest,
) -> Result<PingResponse> {
    app.foxtrail_worker().ping(payload)
}

#[command]
pub(crate) async fn start_worker<R: Runtime>(
    app: AppHandle<R>,
    payload: WorkerRequest,
) -> Result<PingResponse> {
    app.foxtrail_worker().start_worker(payload)
}
