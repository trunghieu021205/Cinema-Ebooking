<script setup lang="ts">
import { ref, computed } from 'vue'
import { useAuthStore } from '@/store/auth.store'
import { useRouter } from 'vue-router'
import BaseButton from '../ui/button/BaseButton.vue'

const auth = useAuthStore()
const router = useRouter()
const emit = defineEmits(['close', 'login'])

const email = ref('')
const password = ref('')
const loading = ref(false)
const showPassword = ref(false)
const fieldErrors = ref({ email: '', password: '' })

// Chỉ disable khi đang loading — không làm khó người dùng
const isLoginDisabled = computed(() => loading.value)

// Clear lỗi của field khi người dùng bắt đầu gõ lại
const clearError = (field: 'email' | 'password') => {
    fieldErrors.value[field] = ''
}

const handleLogin = async (role: 'admin' | 'user' = 'user') => {
    // Reset lỗi cũ
    fieldErrors.value = { email: '', password: '' }

    // Chỉ check empty — đủ để block call vô nghĩa
    if (!email.value) fieldErrors.value.email = 'Vui lòng nhập email'
    if (!password.value) fieldErrors.value.password = 'Vui lòng nhập mật khẩu'
    if (fieldErrors.value.email || fieldErrors.value.password) return

    loading.value = true
    try {
        await new Promise(resolve => setTimeout(resolve, 800))

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

        auth.setAuth(mockUser, 'mock-token-123')
        router.push(isAdmin ? '/admin' : '/')
        emit('close')
    } catch (err: any) {
        // Map lỗi từ backend vào đúng field
        const code = err?.code

        if (code === 'USER_NOT_FOUND') {
            fieldErrors.value.email = 'Email không tồn tại trong hệ thống'
        } else if (code === 'WRONG_PASSWORD') {
            fieldErrors.value.password = 'Mật khẩu không chính xác'
        } else {
            fieldErrors.value.email = err?.message ?? 'Đã có lỗi xảy ra, thử lại sau'
        }
    } finally {
        loading.value = false
    }
}

const closeModal = () => emit('close')
</script>

<template>
    <!-- BACKDROP -->
    <div class="fixed inset-0 bg-black/30 flex items-center justify-center z-50" @click.self="closeModal">
        <!-- MODAL -->
        <div
            class="w-[clamp(20rem,30%,30rem)] bg-bg-surface rounded-xl shadow-xl border-border-default pb-10 animate-fadeIn">

            <!-- HEADER IMAGE -->
            <div class="relative mb-2">
                <img src="/images/login/LoginBanner.png" class="w-full h-50 rounded-t-lg" />
                <button
                    class="w-6 h-6 text-center text-black bg-overlay-dark-50 aspect-square rounded-full absolute top-2 right-2"
                    @click="closeModal">
                    ✕
                </button>
            </div>

            <div class="flex flex-col px-6">
                <h1 class="text-title text-center mb-4 text-text-primary">Đăng Nhập Tài Khoản</h1>

                <div class="flex flex-col gap-6 mb-4">
                    <div class="flex flex-col gap-3">

                        <!-- EMAIL -->
                        <div>
                            <label class="text-[12px] text-text-secondary">Email</label>
                            <input v-model="email" type="email" placeholder="Nhập Email" @blur="validateEmail"
                                @input="clearError('email')" :class="[
                                    'w-full text-body text-text-primary placeholder-text-secondary mt-0.5 px-3 py-2 border rounded-md focus:ring-2 outline-none',
                                    fieldErrors.email
                                        ? 'border-red-400 focus:ring-red-300'
                                        : 'border-border-subtle focus:ring-blue-500'
                                ]" />
                            <p v-if="fieldErrors.email" class="mt-1 text-[11px] text-red-500">
                                {{ fieldErrors.email }}
                            </p>
                        </div>

                        <!-- PASSWORD -->
                        <div>
                            <label class="text-[12px] text-text-secondary">Password</label>
                            <div class="relative mt-0.5">
                                <input v-model="password" :type="showPassword ? 'text' : 'password'"
                                    placeholder="Nhập Mật khẩu" @blur="validatePassword" @input="clearError('password')"
                                    :class="[
                                        'w-full text-body text-text-primary placeholder-text-secondary px-3 py-2 pr-10 border rounded-md focus:ring-2 outline-none',
                                        fieldErrors.password
                                            ? 'border-red-400 focus:ring-red-300'
                                            : 'border-border-subtle focus:ring-blue-500'
                                    ]" />
                                <!-- Toggle hiện/ẩn mật khẩu -->
                                <button type="button"
                                    class="absolute right-3 top-1/2 -translate-y-1/2 text-text-secondary hover:text-text-primary transition-colors"
                                    @click="showPassword = !showPassword" tabindex="-1">
                                    <!-- Eye open -->
                                    <svg v-if="!showPassword" xmlns="http://www.w3.org/2000/svg" class="w-4 h-4"
                                        fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M2.458 12C3.732 7.943 7.523 5 12 5c4.477 0 8.268 2.943 9.542 7-1.274 4.057-5.065 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
                                    </svg>
                                    <!-- Eye closed -->
                                    <svg v-else xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none"
                                        viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M13.875 18.825A10.05 10.05 0 0112 19c-4.477 0-8.268-2.943-9.542-7a9.97 9.97 0 012.308-3.592M6.938 6.938A9.97 9.97 0 0112 5c4.477 0 8.268 2.943 9.542 7a9.97 9.97 0 01-1.497 2.567M3 3l18 18" />
                                    </svg>
                                </button>
                            </div>
                            <p v-if="fieldErrors.password" class="mt-1 text-[11px] text-red-500">
                                {{ fieldErrors.password }}
                            </p>
                        </div>
                    </div>

                    <!-- BUTTON ĐĂNG NHẬP -->
                    <BaseButton @click="handleLogin('admin')" :disabled="isLoginDisabled" width="full" height="md"
                        text="base" variant="primary" rounded="md"
                        class="disabled:opacity-50 disabled:cursor-not-allowed">
                        {{ loading ? 'Đang đăng nhập...' : 'Đăng Nhập' }}
                    </BaseButton>
                </div>

                <p class="text-caption text-left cursor-pointer text-text-secondary hover:text-accent mb-4">
                    Quên mật khẩu?
                </p>

                <div class="pt-4 border-t border-border-subtle">
                    <h2 class="text-center text-text-primary text-body mb-1">Bạn chưa có tài khoản?</h2>
                    <!-- Bỏ disabled ở đây -->
                    <BaseButton width="full" height="md" text="md" variant="secondary" rounded="md">
                        Đăng Ký
                    </BaseButton>
                </div>
            </div>
        </div>
    </div>
</template>