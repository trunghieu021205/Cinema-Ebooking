<template>
  <div class="min-h-screen flex flex-col">
    <Navbar />

    <main class="flex-1 container mx-auto px-4">
      <router-view />
    </main>

    <Footer />
  </div>
</template>

<script setup lang="ts">
import Navbar from '@/shared/components/common/Navbar.vue'
import Footer from '@/shared/components/common/Footer.vue'
</script>
