
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { UserProfile } from '@/types/auth.types'

export const useAuthStore = defineStore('auth', () => {
    const user = ref<UserProfile | null>(null)
    const token = ref<string | null>(localStorage.getItem('token'))

    const setAuth = (userProfile: UserProfile, accessToken: string) => {
        user.value = userProfile
        token.value = accessToken
    }

    const logout = () => {
        user.value = null
        token.value = null
        localStorage.removeItem('token')
    }

    // Helper — dùng ở template thay vì check role thủ công
    const isAdmin = computed(() => user.value?.role === 'ADMIN')
    const isActive = computed(() => user.value?.status === 'ACTIVE')

    return { user, token, setAuth, logout, isAdmin, isActive }
})