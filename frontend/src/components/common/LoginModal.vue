<script setup lang="ts">
import { ref, computed } from 'vue'
import { useAuthStore } from '@/store/auth.store'
import { useRouter } from 'vue-router'
import BaseButton from '../ui/button/BaseButton.vue'
import { authApi } from '@/api/auth.api'
import { userApi } from '@/api/user.api'

const auth = useAuthStore()
const router = useRouter()
const emit = defineEmits(['close', 'login'])

const email = ref('')
const password = ref('')
const loading = ref(false)
const showPassword = ref(false)
const fieldErrors = ref({ email: '', password: '' })
const generalError = ref('')
const loginFailed = ref(false)
const isLoginDisabled = computed(() => loading.value)

const clearError = (field: 'email' | 'password') => {
    fieldErrors.value[field] = ''
    generalError.value = ''
    loginFailed.value = false  // ← reset highlight
}

const validateEmail = () => {
    if (!email.value) {
        fieldErrors.value.email = 'Vui lòng nhập email'
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.value)) {
        fieldErrors.value.email = 'Email không hợp lệ'
    }
}

const validatePassword = () => {
    if (!password.value) {
        fieldErrors.value.password = 'Vui lòng nhập mật khẩu'
    }
}

const handleLogin = async () => {
    fieldErrors.value = { email: '', password: '' }
    generalError.value = ''

    if (!email.value) fieldErrors.value.email = 'Vui lòng nhập email'
    if (!password.value) fieldErrors.value.password = 'Vui lòng nhập mật khẩu'
    if (fieldErrors.value.email || fieldErrors.value.password) return

    loading.value = true
    try {
        // STEP 1: Login
        const { data: loginData } = await authApi.login({
            email: email.value,
            password: password.value,
        })

        // STEP 2: Lưu token
        localStorage.setItem('token', loginData.accessToken)

        // STEP 3: Gọi /me — truyền token 
        const { data: userProfile } = await userApi.getMe(loginData.accessToken)

        // STEP 4: Lưu store + redirect
        auth.setAuth(userProfile, loginData.accessToken)
        router.push(loginData.role === 'ADMIN' ? '/admin/analystics/dashboard' : '/')
        emit('close')

    } catch (err: any) {
        if (err.code === 2003) {
            generalError.value = 'Email hoặc mật khẩu không chính xác'
            loginFailed.value = true
        } else {
            generalError.value = err.message ?? 'Không thể kết nối đến máy chủ, thử lại sau'
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
                                    fieldErrors.email || loginFailed
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
                                        fieldErrors.password || loginFailed
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
                    <div class="flex flex-col gap-3">
                        <!-- General Error Banner — chỉ hiện khi có lỗi chung -->
                        <div v-if="generalError"
                            class="flex items-start gap-2 px-3 py-2.5 rounded-md border border-border-admin-subtle text-red-500 text-[12px]">
                            <!-- Icon cảnh báo -->
                            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 mt-0.5 shrink-0" fill="none"
                                viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                    d="M12 9v2m0 4h.01M10.29 3.86L1.82 18a2 2 0 001.71 3h16.94a2 2 0 001.71-3L13.71 3.86a2 2 0 00-3.42 0z" />
                            </svg>
                            <span>{{ generalError }}</span>
                        </div>

                        <BaseButton @click="handleLogin" :disabled="isLoginDisabled" variant="primary" size="md"
                            rounded="md" class="w-full disabled:opacity-50 disabled:cursor-not-allowed">
                            {{ loading ? 'Đang đăng nhập...' : 'ĐĂNG NHẬP' }}
                        </BaseButton>
                    </div>
                </div>

                <p class="text-caption text-left w-fit cursor-pointer text-text-secondary hover:text-accent mb-4">
                    Quên mật khẩu?
                </p>

                <div class="pt-4 border-t border-border-subtle">
                    <h2 class="text-center text-text-primary text-body mb-1">Bạn chưa có tài khoản?</h2>
                    <BaseButton variant="secondary" size="sm" rounded="lg" class="w-full py-2">
                        Đăng Ký
                    </BaseButton>
                </div>
            </div>
        </div>
    </div>
</template>