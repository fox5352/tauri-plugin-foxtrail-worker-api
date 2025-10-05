import { invoke } from '@tauri-apps/api/core';

async function ping(value) {
    return await invoke('plugin:foxtrail-worker|ping', {
        payload: {
            value,
        },
    }).then((r) => (r.value ? r.value : null));
}
async function start_worker(value) {
    return await invoke('plugin:foxtrail-worker|start_worker', {
        payload: value,
    }).then((r) => (r.value ? r.value : null));
}

export { ping, start_worker };
