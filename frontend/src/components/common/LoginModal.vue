<script setup lang="ts">
import { ref } from 'vue'
import { useAuthStore } from '@/store/auth.store'
import { useRouter } from 'vue-router'
import BaseButton from '../ui/button/BaseButton.vue'
const auth = useAuthStore()
const router = useRouter()
const emit = defineEmits(['close', 'login'])

const email = ref('')
const password = ref('')
const loading = ref(false)
const error = ref(null)

const handleLogin = async (role: 'admin' | 'user' = 'user') => {
    error.value = null
    loading.value = true

    try {
        await new Promise(resolve => setTimeout(resolve, 800))

        if (!email.value || !password.value) {
            throw new Error('Please fill in all fields')
        }

        const isAdmin = role === 'admin'

        const mockUser = {
            email: isAdmin ? 'admin@gmail.com' : email.value,
            name: isAdmin ? 'Admin' : 'User',
            membership: 'gold',
            points: isAdmin ? 9999 : 100,
            role,
            avatarUrl: isAdmin
                ? '/images/avatars/admin.jpg'
                : '/images/avatars/UserAvatar.jpg',
        }

        const fakeToken = 'mock-token-123'

        // AUTH STORE
        auth.setAuth(mockUser, fakeToken)

        // ROUTE
        router.push(isAdmin ? '/admin' : '/')

        // CLOSE MODAL
        emit('close')

    } catch (err) {
        error.value = err.message
    } finally {
        loading.value = false
    }
}

const closeModal = () => {
    emit('close')
}
</script>

<template>
    <!-- BACKDROP -->
    <div class="fixed inset-0 bg-black/30 flex items-center justify-center z-50" @click.self="closeModal">
        <!-- MODAL -->
        <div
            class="w-[clamp(20rem,30%,30rem)] bg-bg-surface rounded-xl shadow-xl border-border-default pb-10 animate-fadeIn">
            <!-- HEADER -->
            <div class="relative mb-2">
                <img src="/public/images/login/LoginBanner.png" class="w-full h-50 rounded-t-lg" />
                <button
                    class="w-6 h-6 text-center text-black bg-overlay-dark-50 aspect-square rounded-full absolute top-2 right-2"
                    @click="closeModal">
                    ✕
                </button>
            </div>
            <div class="flex flex-col px-6">
                <h1 class="text-title text-center mb-4 text-text-primary">Đăng Nhập Tài Khoản</h1>

                <!-- ERROR -->
                <div v-if="error" class="mb-3 text-sm text-red-500 bg-red-50 p-2 rounded">
                    {{ error }}
                </div>
                <div class="flex flex-col gap-6 mb-4">
                    <!-- FORM -->
                    <div class="flex flex-col gap-1">
                        <!-- EMAIL -->
                        <div>
                            <label class="text-[12px] text-text-secondary">Email</label>
                            <input v-model="email" type="email" placeholder="Nhập Email"
                                class="w-full text-body text-text-primary placeholder-text-secondary mt-0.5 px-3 py-2 border-border-subtle border rounded-md focus:ring-2 focus:ring-blue-500 outline-none" />
                        </div>

                        <!-- PASSWORD -->
                        <div>
                            <label class="text-[12px] text-text-secondary">Password</label>
                            <input v-model="password" type="password" placeholder="Nhập Mật khẩu"
                                class="w-full text-body text-text-primary placeholder-text-secondary mt-0.5 px-3 py-2 border-border-subtle border rounded-md focus:ring-2 focus:ring-blue-500 outline-none" />
                        </div>
                    </div>


                    <!-- BUTTON -->
                    <BaseButton @click="handleLogin('admin')" :disabled="loading" width="full" height="md" text="base"
                        variant="primary" rounded="md" class="disabled:opacity-50">
                        {{ loading ? 'Đang đăng nhập...' : 'Đăng Nhập' }}
                    </BaseButton>
                </div>
                <p class="text-caption text-left cursor-pointer text-text-secondary hover:text-accent mb-4">
                    Quên mật khẩu?
                </p>
                <div class="pt-4 border-t border-border-subtle">
                    <h2 class="text-center text-text-primary text-body mb-1">Bạn chưa có tài khoản?</h2>
                    <BaseButton width="full" height="md" text="md" variant="secondary" rounded="md"
                        class="disabled:opacity-50">
                        Đăng Ký
                    </BaseButton>
                </div>
            </div>


        </div>
    </div>
</template>