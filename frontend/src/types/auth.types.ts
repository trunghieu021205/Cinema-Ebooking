
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