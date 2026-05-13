import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import type { UserProfile } from '@/types/auth.types'
import { apiClient } from '@/api/axios'

export const useAuthStore = defineStore('auth', () => {

    const user = ref<UserProfile | null>(null)

    const accessToken = ref<string | null>(localStorage.getItem('accessToken'))
    const refreshToken = ref<string | null>(localStorage.getItem('refreshToken'))

    const setAuth = (userProfile: UserProfile, access: string, refresh: string) => {
        user.value = userProfile
        accessToken.value = access
        refreshToken.value = refresh
        localStorage.setItem('accessToken', access)
        localStorage.setItem('refreshToken', refresh)
    }

    const logout = () => {
        user.value = null
        accessToken.value = null
        refreshToken.value = null
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        delete apiClient.defaults.headers.common.Authorization;
    }

    const isLoggedIn = computed(() => !!accessToken.value)
    const isAdmin = computed(() => user.value?.role === 'ADMIN')
    const isActive = computed(() => user.value?.status === 'ACTIVE')

    return {
        user,
        accessToken,
        refreshToken,
        setAuth,
        logout,
        isLoggedIn,
        isAdmin,
        isActive,
    }
},{
    persist: {
        key: 'auth',
        paths: ['user'],   // chỉ persist user, token đã tự lưu localStorage thủ công
    }
})