<script setup>
import { useWebSocket, useStorage } from '@vueuse/core'
import { computed } from 'vue'

const { send, data, status } = useWebSocket('ws://localhost:8080/live', {
  autoReconnect: true
})

const cart = useStorage('shop-cart', [])

const totalPrice = computed(() => {
  return cart.value.reduce((sum, item) => sum + item.price, 0)
})

const addToCart = (productName, price) => {
  cart.value.push({ name: productName, price: price, id: Date.now() })
  send(`Hinzugefügt: ${productName}`)
}

const clearCart = () => {
  cart.value = []
}
</script>

<template>
  <div class="app-wrapper">
    <main class="shop-card">

      <header class="header">
        <h1>Tech-Store <span class="highlight">Demo</span></h1>
        <div class="status-pill" :class="{ 'is-online': status === 'OPEN' }">
          <span class="dot"></span>
          {{ status === 'OPEN' ? 'ONLINE' : 'OFFLINE' }}
        </div>
      </header>

      <section class="product-grid">
        <div class="item-card" @click="addToCart('Laptop', 999)">
          <div class="item-emoji">💻</div>
          <div class="item-info">
            <span class="name">Laptop</span>
            <span class="price">999 €</span>
          </div>
        </div>

        <div class="item-card" @click="addToCart('Maus', 49)">
          <div class="item-emoji">🖱️</div>
          <div class="item-info">
            <span class="name">Maus</span>
            <span class="price">49 €</span>
          </div>
        </div>
      </section>

      <section class="cart-area">
        <div class="cart-title">
          <h3>🛒 Warenkorb</h3>
          <span class="badge">{{ cart.length }}</span>
        </div>

        <div class="cart-receipt">
          <div v-if="cart.length === 0" class="empty-state">
            System bereit... Warte auf Auswahl.
          </div>

          <div v-for="item in cart" :key="item.id" class="receipt-row">
            <span>{{ item.name }}</span>
            <span class="dots"></span>
            <span class="row-price">{{ item.price }} €</span>
          </div>

          <div v-if="cart.length > 0" class="cart-summary">
            <div class="total-line">
              <span>SUMME</span>
              <span>{{ totalPrice }} €</span>
            </div>
            <div class="button-group">
              <button class="btn-secondary" @click="clearCart">RESET</button>
              <button class="btn-primary" @click="send('Kauf: ' + totalPrice + '€')">KAUFEN</button>
            </div>
          </div>
        </div>
      </section>

      <footer class="server-log">
        <small>>_ SERVER_RESPONSE:</small>
        <code>{{ data || 'Warte auf Signal...' }}</code>
      </footer>

    </main>
  </div>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Fira+Code:wght@400;700&family=Inter:wght@400;800&display=swap');

/* Hintergrund der ganzen Seite: Tiefschwarz */
.app-wrapper {
  font-family: 'Inter', sans-serif;
  background: #0a0a0a;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
}

/* Die Karte: Dunkles Grau/Schwarz */
.shop-card {
  background: #161616;
  width: 100%;
  max-width: 420px;
  border-radius: 20px;
  border: 1px solid #333;
  box-shadow: 0 0 30px rgba(0, 255, 127, 0.05);
  padding: 2rem;
  color: #e0e0e0;
}

.header { display: flex; justify-content: space-between; margin-bottom: 2rem; align-items: center;}
h1 { font-size: 1.4rem; font-weight: 800; margin: 0; color: #fff; }
.highlight { color: #00ff7f; } /* Matrix-Grün */

/* Status-Pille */
.status-pill {
  display: flex; align-items: center; gap: 6px; padding: 4px 10px; border-radius: 4px; font-size: 0.7rem; font-weight: 800; font-family: 'Fira Code', monospace;
  background: #2a1010; color: #ff4444; border: 1px solid #ff4444;
}
.status-pill.is-online { background: #0a2010; color: #00ff7f; border: 1px solid #00ff7f; }
.dot { width: 6px; height: 6px; background: currentColor; border-radius: 50%; box-shadow: 0 0 8px currentColor; }

/* Produkt-Karten */
.product-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 2rem; }
.item-card {
  background: #1f1f1f; padding: 1.2rem; border-radius: 12px; cursor: pointer; text-align: center; transition: 0.2s;
  border: 1px solid #333;
}
.item-card:hover {
  transform: translateY(-3px);
  border-color: #00ff7f;
  background: #252525;
  box-shadow: 0 5px 15px rgba(0, 255, 127, 0.1);
}
.item-emoji { font-size: 2rem; filter: grayscale(0.2); }
.name { display: block; font-weight: 600; margin-top: 8px; color: #bbb; }
.price { color: #00ff7f; font-weight: 800; font-size: 1.1rem; font-family: 'Fira Code', monospace; }

/* Warenkorb Bereich */
.cart-area { background: #1f1f1f; border-radius: 12px; padding: 1.2rem; border: 1px solid #333; }
.cart-title { display: flex; justify-content: space-between; margin-bottom: 1rem; color: #00ff7f; }
.cart-title h3 { margin: 0; font-size: 1rem; text-transform: uppercase; letter-spacing: 1px; }
.badge { background: #00ff7f; color: #000; padding: 2px 8px; border-radius: 4px; font-size: 0.8rem; font-weight: 800; }

.cart-receipt { background: #161616; padding: 1rem; border-radius: 8px; border: 1px solid #222; }
.receipt-row { display: flex; justify-content: space-between; font-size: 0.85rem; margin-bottom: 0.5rem; color: #888; font-family: 'Fira Code', monospace; }
.dots { flex: 1; border-bottom: 1px dotted #444; margin: 0 8px; }
.row-price { color: #e0e0e0; }
.empty-state { text-align: center; color: #555; font-size: 0.8rem; padding: 10px 0; }

/* Summe & Buttons */
.cart-summary { margin-top: 1rem; border-top: 1px solid #333; padding-top: 1rem; }
.total-line { display: flex; justify-content: space-between; font-weight: 800; font-size: 1.1rem; margin-bottom: 1rem; color: #00ff7f; font-family: 'Fira Code', monospace; }
.button-group { display: flex; gap: 8px; }
button { flex: 1; border: none; padding: 10px; border-radius: 4px; font-weight: 800; cursor: pointer; transition: 0.2s; font-family: 'Fira Code', monospace; }

.btn-primary { background: #00ff7f; color: #000; }
.btn-primary:hover { background: #00cc66; box-shadow: 0 0 15px rgba(0, 255, 127, 0.3); }
.btn-secondary { background: #333; color: #888; }
.btn-secondary:hover { background: #444; color: #fff; }

/* Server Log (Terminal Look) */
.server-log { margin-top: 1.5rem; background: #000; padding: 12px; border-radius: 8px; color: #00ff7f; border: 1px solid #333; }
code { font-family: 'Fira Code', monospace; font-size: 0.7rem; display: block; overflow-x: auto; white-space: nowrap; }
</style>