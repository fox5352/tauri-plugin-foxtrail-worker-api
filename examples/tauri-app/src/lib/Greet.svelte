<script>
  import { invoke } from "@tauri-apps/api/core";
  import { start_worker } from "tauri-plugin-foxtrail-worker-api";

  let name = $state("");
  let greetMsg = $state("");

  async function greet() {
    // Learn more about Tauri commands at https://v2.tauri.app/develop/calling-rust/#commands
    greetMsg = await start_worker({
      publicUrl: "https://fvlsvvzbywmozvhwxmzl.supabase.co",
      publicKey:
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImZ2bHN2dnpieXdtb3p2aHd4bXpsIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTc0Mzg1MDksImV4cCI6MjA3MzAxNDUwOX0.Si24RvvrYtTNeunPxC-w_qqq8JgdEiFPBh9wNsB0O8g",
      userId: "11668e59-4584-4378-8a4c-cc96eb6c1691",
      value: null,
    });
    // greetMsg = await invoke("start_worker", { value: name });
  }
</script>

<div>
  <div class="row">
    <input id="greet-input" placeholder="Enter a name..." bind:value={name} />
    <button onclick={greet}> Greet </button>
  </div>
  <p>{greetMsg}</p>
</div>
