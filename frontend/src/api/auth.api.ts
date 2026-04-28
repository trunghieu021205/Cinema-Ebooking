
import apiClient from './axios'
import type { LoginRequest, LoginResponse , RegisterRequest, ApiResponse } from '@/types/auth.types'

export const authApi = {
    login: (payload: LoginRequest) =>
        apiClient.post<ApiResponse<LoginResponse>>('/auth/login', payload),

    register: (payload: RegisterRequest) =>
        apiClient.post<ApiResponse>('/auth/register',payload),
}