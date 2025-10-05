'use strict';

var core = require('@tauri-apps/api/core');

async function ping(value) {
    return await core.invoke('plugin:foxtrail-worker|ping', {
        payload: {
            value,
        },
    }).then((r) => (r.value ? r.value : null));
}
async function start_worker(value) {
    return await core.invoke('plugin:foxtrail-worker|start_worker', {
        payload: value,
    }).then((r) => (r.value ? r.value : null));
}

exports.ping = ping;
exports.start_worker = start_worker;
