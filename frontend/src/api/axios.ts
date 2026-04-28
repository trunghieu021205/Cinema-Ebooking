import axios from 'axios'
import type { ApiResponse } from '@/types/auth.types'
import { mapFieldErrors } from '@/utils/errorMapper'

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api/v1',
    timeout: 10000,
    headers: { 'Content-Type': 'application/json' },
})

// ================= REQUEST =================
apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem('token')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
})

// ================= RESPONSE =================
apiClient.interceptors.response.use(
    (response) => {
        const res: ApiResponse<any> = response.data

        // 🔥 unwrap ApiResponse
        if (res && typeof res.success === 'boolean') {
            if (!res.success) {
                const mapped = mapFieldErrors(res.error)

                return Promise.reject({
                    type: 'api',
                    fieldErrors: mapped.fieldErrors,
                    globalErrors: mapped.globalErrors,
                    code: res.error?.code ?? null,
                    message: res.error?.message ?? 'Lỗi từ server',
                    raw: res.error
                })
            }

            return res.data
        }

        return response.data
    },

    (error) => {
        const status = error.response?.status
        const apiError = error.response?.data?.error

        // ================= 401 =================
        if (status === 401) {
            const token = localStorage.getItem('token')
            if (token) {
                localStorage.removeItem('token')
                window.location.href = '/'
            }
        }

        const mapped = mapFieldErrors(apiError)

        return Promise.reject({
            type: 'http',
            fieldErrors: mapped.fieldErrors,
            globalErrors: mapped.globalErrors,
            code: apiError?.code ?? null,
            message: apiError?.message ?? 'Đã có lỗi xảy ra',
            status,
            raw: apiError
        })
    }
)

export default apiClient