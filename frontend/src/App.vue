<script setup>
import { useWebSocket } from '@vueuse/core'

// Verbinde dich mit deinem Quarkus Backend
// Wichtig: 'ws://' nutzen, nicht 'http://'
const { status, data, send, open, close } = useWebSocket('ws://localhost:8080/live', {
  autoReconnect: true, // Verbindet sich automatisch neu, wenn der Server mal weg ist
})

// Eine kleine Funktion, um dem Server etwas zu schicken
const pingServer = () => {
  send('Hallo Backend, hier ist Vue!')
}
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
      <strong>Letzte Nachricht vom Server:</strong>
      <p>{{ data }}</p>
    </div>

    <button @click="pingServer" :disabled="status !== 'OPEN'">
      Nachricht an Server senden
    </button>
  </main>
</template>