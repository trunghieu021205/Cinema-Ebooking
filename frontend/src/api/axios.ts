import axios, { AxiosError } from 'axios'
import type { InternalAxiosRequestConfig } from 'axios'
import type { ApiResponse, LoginResponse } from '@/types/auth.types'
import { mapFieldErrors } from '@/utils/errorMapper'


const BASE_URL = 'http://localhost:8080/api/v1'

const apiClient = axios.create({
    baseURL: BASE_URL,
    timeout: 10000,
    headers: { 'Content-Type': 'application/json' },
})

const PUBLIC_ENDPOINTS = [
  '/auth/login',
  '/auth/register',
  '/auth/forgot_password',
  '/auth/reset_password',
  '/auth/refresh_token' 
]

const isPublicEndpoint = (url?: string): boolean => {
  if (!url) return false
  return PUBLIC_ENDPOINTS.some(publicPath => url.includes(publicPath))
}

// ================= REQUEST =================
apiClient.interceptors.request.use((config) => {
    if (isPublicEndpoint(config.url)) return config 
    const token = localStorage.getItem('accessToken')
    if (token) config.headers.Authorization = `Bearer ${token}`
    return config
})

// ================= RESPONSE =================


let isRefreshing = false
let failedQueue: Array<{
    resolve: (value?: any) => void
    reject: (reason?: any) => void
}> = []

const processQueue = (error: any = null) => {
    failedQueue.forEach(prom => {
        if (error) prom.reject(error)
        else prom.resolve()
    })
    failedQueue = []
}

const logout = () => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    window.dispatchEvent(new CustomEvent('auth:logout'))
}

apiClient.interceptors.response.use(
    (response) => {
        const res: ApiResponse<any> = response.data
        if (res && typeof res.success === 'boolean') {
            if (!res.success) {
                const mapped = mapFieldErrors(apiError ?? null)
                return Promise.reject({
                    type: 'api',
                    fieldErrors: mapped.fieldErrors,
                    globalErrors: mapped.globalErrors,
                    code: res.error?.code ?? null,
                    message: res.error?.message ?? 'Lỗi từ server',
                    raw: res.error,
                })
            }
            return res.data
        }
        return response.data
    },

    async (error: AxiosError) => {
        const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean }
        const status = error.response?.status
        const apiError = (error.response?.data as any)?.error
        const url = originalRequest.url

        if (status !== 401) {
            const mapped = mapFieldErrors(apiError ?? null)
            return Promise.reject({
                type: 'http',
                fieldErrors: mapped.fieldErrors,
                globalErrors: mapped.globalErrors,
                code: apiError?.code ?? null,
                message: apiError?.message ?? 'Đã có lỗi xảy ra',
                status,
                raw: apiError,
            })
        }

        if (isPublicEndpoint(url)) {
            const mapped = mapFieldErrors(apiError ?? null)
            return Promise.reject({
                type: 'api',
                fieldErrors: mapped.fieldErrors,
                globalErrors: mapped.globalErrors,
                code: apiError?.code ?? null,
                message: apiError?.message ?? 'Đã có lỗi xảy ra',
                status,
                raw: apiError,
            })
        }

        if (originalRequest._retry) {
            logout()
            return Promise.reject(error)
        }

        if (url?.includes('/auth/refresh_token')) {
            logout()
            return Promise.reject(error)
        }

        originalRequest._retry = true

        if (isRefreshing) {
            return new Promise((resolve, reject) => {
                failedQueue.push({ resolve, reject })
            })
                .then(() => apiClient(originalRequest))
                .catch(err => Promise.reject(err))
        }

        isRefreshing = true

        const refreshToken = localStorage.getItem('refreshToken')
        if (!refreshToken) {
            isRefreshing = false  // ✅ Reset trước khi logout
            logout()
            return Promise.reject(error)
        }

        try {
            // ✅ Gọi thẳng axios thô, KHÔNG qua apiClient
            // → tránh circular dependency + tránh response interceptor unwrap
            const { data } = await axios.post<ApiResponse<LoginResponse>>(
                `${BASE_URL}/auth/refresh_token`,
                { refreshToken },
                { headers: { 'Content-Type': 'application/json' } }
            )

            // ✅ Validate response trước khi dùng
            if (!data.success || !data.data?.accessToken || !data.data?.refreshToken) {
                throw new Error('Invalid refresh response')
            }

            const { accessToken: newAccessToken, refreshToken: newRefreshToken } = data.data

            localStorage.setItem('accessToken', newAccessToken)
            localStorage.setItem('refreshToken', newRefreshToken)

            apiClient.defaults.headers.common.Authorization = `Bearer ${newAccessToken}`
            originalRequest.headers.Authorization = `Bearer ${newAccessToken}`

            processQueue(null)
            return apiClient(originalRequest)
        } catch (refreshError) {
            processQueue(refreshError)
            logout()
            return Promise.reject(refreshError)
        } finally {
            isRefreshing = false
        }
    }
)

export default apiClient