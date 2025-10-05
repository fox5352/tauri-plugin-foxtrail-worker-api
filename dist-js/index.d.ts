export declare function ping(value: string): Promise<string | null>;
export interface WorkerRequest {
    value: string | null;
    publicUrl: string;
    publicKey: string;
    userId: string;
}
export declare function start_worker(value: WorkerRequest): Promise<string | null>;
