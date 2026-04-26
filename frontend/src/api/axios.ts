
import axios from 'axios'
import type { ApiResponse } from '@/types/auth.types'

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
    timeout: 10000,
    headers: { 'Content-Type': 'application/json' },
})

apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem('token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
})

apiClient.interceptors.response.use(
    (response) => {
        if (response.data?.success !== undefined) {
            response.data = response.data.data
        }
        return response
    },

    (error) => {
        const status = error.response?.status
        const apiError = error.response?.data?.error

        if (status === 401) {
            const token = localStorage.getItem('token')
            // ✅ Chỉ redirect nếu user ĐANG đăng nhập (có token sẵn)
            // Không redirect nếu đang trong flow login (chưa có token)
            if (token) {
                localStorage.removeItem('token')
                window.location.href = '/'
            }
        }

        // Ném lỗi về component để tự xử lý
        return Promise.reject({
            status,
            code: apiError?.code ?? null,
            message: apiError?.message ?? error.message ?? 'Đã có lỗi xảy ra',
        })
    }
)

export default apiClient