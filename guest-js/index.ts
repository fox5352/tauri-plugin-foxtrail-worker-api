import { invoke } from '@tauri-apps/api/core'

export async function ping(value: string): Promise<string | null> {
  return await invoke<{ value?: string }>('plugin:foxtrail-worker|ping', {
    payload: {
      value,
    },
  }).then((r) => (r.value ? r.value : null));
}


export interface WorkerRequest {
  value: string | null,
  publicUrl: string,
  publicKey: string
  userId: string
}

export async function start_worker(value: WorkerRequest): Promise<string | null> {
  return await invoke<{ value?: string }>('plugin:foxtrail-worker|start_worker', {
    payload: value,
  }).then((r) => (r.value ? r.value : null));
}
