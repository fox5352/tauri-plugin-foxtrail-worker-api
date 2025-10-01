use serde::{Deserialize, Serialize};

#[derive(Debug, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct PingRequest {
    pub value: Option<String>,
}

#[derive(Debug, Clone, Default, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct PingResponse {
    pub value: Option<String>,
}

#[derive(Debug, Deserialize, Serialize)]
#[serde(rename_all = "camelCase")]
pub struct WorkerRequest {
    pub value: Option<String>,
    pub public_url: Option<String>,
    pub public_key: Option<String>,
    pub user_id: Option<String>,
}


#[derive(Debug, Deserialize, Serialize)]
#[serde(rename_all= "camelCase")]
pub  struct WorkerResponse {
    pub value: Option<String>
}