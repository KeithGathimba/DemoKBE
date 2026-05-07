<script setup>
import { useWebSocket, useMouse } from '@vueuse/core'

const { status, data, send } = useWebSocket('ws://localhost:8080/live', {
  autoReconnect: true,
})

const pingServer = () => {
  send('Hallo Backend, hier ist Vue!')
}
const { x, y } = useMouse()
</script>

<template>
  <main style="padding: 2rem; font-family: sans-serif;">
    <h1>Live-Shop Verbindung 🔌</h1>

    <p>Status:
      <strong :style="{ color: status === 'OPEN' ? 'green' : 'red' }">
        {{ status }}
      </strong>
    </p>

    <div v-if="data" style="background: #e0ffe0; padding: 1rem; margin-bottom: 1rem;">
      <strong>Vom Server:</strong>
      <p>{{ data }}</p>
    </div>

    <button @click="pingServer" :disabled="status !== 'OPEN'">
      Nachricht senden
    </button>
    <hr style="margin: 2rem 0; border: 1px solid #ccc;">

    <h2>VueUse Live-Demo 🖱️</h2>
    <p>Bewege die Maus über den Bildschirm:</p>
    <h3 style="color: #aa3bff;">X: {{ x }} | Y: {{ y }}</h3>
  </main>
</template>
