
import apiClient from './axios'
import type { LoginRequest, LoginResponse } from '@/types/auth.types'

export const authApi = {
    login: (payload: LoginRequest) =>
        apiClient.post<LoginResponse>('/auth/login', payload),
}