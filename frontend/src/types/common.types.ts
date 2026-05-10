// common.types.ts
export interface ApiResponse<T> {
  success: boolean
  data: T | null
  error: ApiError | null
  timestamp: string
  traceId: string
  path: string
}

export interface ApiErrorDetail {
  field: string
  category: string   // "DUPLICATE", "INVALID"...
  reason: string
  params?: Record<string, any>
}

export interface ApiError {
  code: number
  message: string
  type: string
  params?: Record<string, any>
  details: ApiErrorDetail[] | null
}

export interface NestedPage<T> {
  content: T[]
  page: {
    size: number
    number: number
    totalElements: number
    totalPages: number
  }

}