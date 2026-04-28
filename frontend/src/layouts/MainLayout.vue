<template>
  <div class="min-h-screen flex flex-col">
    <Header />
    <LoginModal v-if="ui.showLoginModal" @close="ui.closeLoginModal" @login="handleLoginSuccess" />
    <main class="flex-1 container mx-auto px-4">
      <router-view />
    </main>

    <Footer />
  </div>
</template>

<script setup lang="ts">
import Footer from '@/components/common/Footer.vue'
import Header from '@/components/common/header/Header.vue';
import LoginModal from '@/components/common/login/LoginModal.vue';
import { useUIStore } from '@/store/ui.store';
import { ref } from 'vue';

const ui = useUIStore()

const handleLoginSuccess = (user) => {
  console.log('logged in:', user)
  auth.isLoggedIn = true
  auth.user = user
}
</script>
