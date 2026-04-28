import { createApp } from 'vue'
import { createPinia } from 'pinia'
import persistedState from 'pinia-plugin-persistedstate'
import App from './App.vue'
import router from '@/router'
import '@/styles/tailwind.css'

const app = createApp(App)
app.use(createPinia().use(persistedState))
app.use(router)

app.mount('#app')
