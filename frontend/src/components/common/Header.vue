<template>
  <header class="w-full bg-bg-surface text-text-primary px-16 pt-4 pb-4 flex items-center justify-between">
    <Logo />
    <Navbar />
    <div class="flex items-center gap-8">
      <BaseButton iconOnly>
        <SearchIcon />
      </BaseButton>
      <BaseButton variant="primary" size="lg">
        Đặt vé ngay
      </BaseButton>
      <div class="min-w-50 flex items-center justify-end gap-4">
        <a class="text-text-secondary hover:text-accent transition-colors text-body">
          Đăng nhập
        </a>
      </div>
    </div>


  </header>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useTheme } from '@/composables/useTheme'
import BaseButton from '@/components/ui/button/BaseButton.vue'
import SearchIcon from '@/components/ui/icon/SearchIcon.vue'
import Logo from '@/components/common/header/Logo.vue'
import Navbar from '@/components/common/header/Navbar.vue'

const { toggleTheme, initTheme } = useTheme()

onMounted(() => {
  initTheme()
})
</script>
