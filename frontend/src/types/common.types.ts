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

// Spring Data Page wrapper (full version)
export interface Page<T> {
  content: T[]
  pageable: {
    pageNumber: number
    pageSize: number
    sort: { sorted: boolean; unsorted: boolean; empty: boolean }
    offset: number
    paged: boolean
    unpaged: boolean
  }
  last: boolean
  totalElements: number
  totalPages: number
  size: number
  number: number
  sort: { sorted: boolean; unsorted: boolean; empty: boolean }
  first: boolean
  numberOfElements: number
  empty: boolean
}