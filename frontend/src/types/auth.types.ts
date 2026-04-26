
// Wrapper chung cho MỌI response từ backend
export interface ApiResponse<T> {
    success: boolean
    data: T | null
    error: ApiError | null
    timestamp: string
    traceId: string
    path: string
}

export interface ApiError {
    code: number        // 2003, 2004... — số, không phải string
    message: string     // "Invalid email or password"
    type: string        // "BUSINESS"
    details: null
}

export type UserRole = 'ADMIN' | 'USER'
export type UserStatus = 'ACTIVE' | 'INACTIVE' | 'BANNED'

export interface LoginResponse {
    accessToken: string
    role: UserRole
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