import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUIStore = defineStore('ui', () => {
  // ===== MODAL =====
  const showLoginModal = ref(false)

  const openLoginModal = () => {
    showLoginModal.value = true
  }

  const closeLoginModal = () => {
    showLoginModal.value = false
  }

  // ===== SIDEBAR =====
  const isSidebarCollapsed = ref(false)

  const toggleSidebar = () => {
    isSidebarCollapsed.value = !isSidebarCollapsed.value
  }

  const openSidebar = () => {
    isSidebarCollapsed.value = false
  }

  const closeSidebar = () => {
    isSidebarCollapsed.value = true
  }

  return {
    showLoginModal,
    openLoginModal,
    closeLoginModal,

    // sidebar
    isSidebarCollapsed,
    toggleSidebar,
    openSidebar,
    closeSidebar
  }
})