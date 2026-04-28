<template>
  <header class="w-full bg-bg-surface text-text-primary px-16 pt-4 pb-4 flex items-center justify-between">
    <Logo :isAdminPage="false" />
    <Navbar />
    <HeaderActions />
  </header>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useTheme } from '@/composables/useTheme'
import Logo from '@/components/common/header/Logo.vue'
import Navbar from '@/components/common/header/Navbar.vue'
import HeaderActions from '@/components/common/header/HeaderActions.vue'

const { toggleTheme, initTheme } = useTheme()

onMounted(() => {
  initTheme()
})
</script>
