use tauri::{AppHandle, command, Runtime};

use crate::models::*;
use crate::Result;
use crate::FoxtrailWorkerExt;

#[command]
pub(crate) async fn ping<R: Runtime>(
    app: AppHandle<R>,
    payload: PingRequest,
) -> Result<PingResponse> {
    app.foxtrail_worker().ping(payload)
}
