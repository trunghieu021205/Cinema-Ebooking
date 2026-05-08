<template>
  <router-view />
  <IdleWarningModal v-if="isLoggedIn" :show="showWarning" :countdown="countdown" @stay="stayLoggedIn"
    @logout="handleLogout" />
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted } from 'vue'
import { useAuthStore } from '@/stores/auth.store'
import { useRouter } from 'vue-router'
import { useUIStore } from './stores/ui.store'
import { useIdleTimeout } from '@/composables/useIdleTimeout'
import IdleWarningModal from '@/components/common/IdleWarningModal.vue'

const authStore = useAuthStore()
const uiStore = useUIStore()
const isLoggedIn = computed(() => !!authStore.accessToken)
const router = useRouter()
const handleLogout = () => {
  authStore.logout()
  router.push('/')
  uiStore.openLoginModal()
}

const { showWarning, countdown, stayLoggedIn } = useIdleTimeout(handleLogout)

onMounted(() => {
  window.addEventListener('auth:logout', handleLogout)
})

onUnmounted(() => {
  window.removeEventListener('auth:logout', handleLogout)
})
</script>
