<script setup lang="ts">
import { ref } from 'vue'
import LoginForm from '@/components/common/login/LoginForm.vue'
import RegisterForm from '@/components/common/login/RegisterForm.vue'

const emit = defineEmits<{ close: [] }>()

const isLogin = ref(true)
const closeModal = () => emit('close')
</script>

<template>
    <div class="fixed inset-0 bg-black/30 overflow-y-auto z-50 py-8" @click.stop>

        <div
            class="w-[clamp(20rem,30%,30rem)] mx-auto bg-bg-surface rounded-xl shadow-xl border border-border-default animate-fadeIn pb-10">

            <!-- HEADER IMAGE -->
            <div class="relative">
                <img src="/images/login/LoginBanner.png" class="w-full h-50 rounded-t-lg object-cover" />
                <button
                    class="w-6 h-6 text-center text-black bg-overlay-dark-50 aspect-square rounded-full absolute top-2 right-2 hover:bg-overlay-dark-70 transition-colors"
                    @click="closeModal">
                    ✕
                </button>
            </div>

            <!-- FORM CONTENT -->
            <div class="px-6 pt-0">
                <LoginForm v-if="isLogin" @switch="isLogin = false" @close="closeModal" />
                <RegisterForm v-else @switch="isLogin = true" @close="closeModal" />
            </div>
        </div>
    </div>
</template>