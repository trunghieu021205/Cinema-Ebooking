// Wrapper chung cho MỌI response từ backend
export interface ApiResponse<T> {
    success: boolean
    data: T | null
    error: ApiError | null
    timestamp: string
    traceId: string
    path: string
}

export interface ApiErrorDetail {
    field: string       // "email", "phoneNumber"...
    category: string    // "DUPLICATE", "INVALID"...
    reason: string
    params?: Record<string, any> // "email 'user7@gmail.com' đã được sử dụng"
}

export interface ApiError {
    code: number
    message: string
    type: string
    details: ApiErrorDetail[] | null 
}

export type UserRole = 'ADMIN' | 'USER'
export type UserStatus = 'ACTIVE' | 'INACTIVE' | 'BANNED'

export interface LoginResponse {
    accessToken: string   // thay vì token
    refreshToken: string  // thêm mới
    role: UserRole
}
export interface RefreshTokenRequest {
    refreshToken: string
}

export interface UserProfile {
    id: number
    fullName: string
    email: string
    phoneNumber: string
    avatarUrl: string
    role: UserRole
    status: UserStatus
}

export interface LoginRequest {
    email: string
    password: string
}

export interface RegisterRequest {
    fullName: string
    email: string
    password: string
    phoneNumber: string
    dateOfBirth: string
    gender: 'MALE'|'FEMALE'
}