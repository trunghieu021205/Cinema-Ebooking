<template>
  <div class="min-h-screen flex flex-col">
    <Header />

    <main class="flex-1 container mx-auto px-4">
      <router-view />
    </main>

    <Footer />
  </div>
</template>

<script setup lang="ts">
import Footer from '@/components/common/Footer.vue'
import Header from '@/components/common/Header.vue';
</script>
