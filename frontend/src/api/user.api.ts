
import apiClient from './axios'
import type { UserProfile } from '@/types/auth.types'

export const userApi = {
    // token?: string — optional, nếu không truyền thì interceptor tự lo (các call sau login)
    getMe: (token?: string) =>
        apiClient.get<UserProfile>('/users/me', {
            headers: token
                ? { Authorization: `Bearer ${token}` }
                : undefined, // không truyền → interceptor đọc localStorage như bình thường
        }),
}