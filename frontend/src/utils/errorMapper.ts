// src/utils/errorMapper.ts
import type { ApiError, ApiErrorDetail } from '@/types/api.types'

// ─── Category map ──────────────────────────────────────────────
type MessageFn = (params?: Record<string, any>) => string

const CATEGORY_MESSAGES: Record<string, MessageFn> = {
    REQUIRED:             ()  => 'Không được để trống',
    INVALID_FORMAT:       ()  => 'Định dạng không hợp lệ',
    INVALID_VALUE:        ()  => 'Giá trị không hợp lệ',
    DUPLICATE:            ()  => 'Giá trị này đã được sử dụng',
    INVALID_LENGTH_MIN:   (p) => `Tối thiểu ${p?.min} ký tự`,
    INVALID_LENGTH_MAX:   (p) => `Tối đa ${p?.max} ký tự`,
    INVALID_LENGTH_RANGE: (p) => `Từ ${p?.min} đến ${p?.max} ký tự`,
    INVALID_AGE_MIN:      (p) => `Phải đủ ${p?.min} tuổi`,
}

// ─── Error code map ────────────────────────────────────────────
const ERROR_CODE_MESSAGES: Record<number, string> = {
    1001: 'Dữ liệu không hợp lệ',
    2001: 'Email này đã được sử dụng',
    2002: 'Số điện thoại đã được đăng ký',
    2003: 'Email hoặc mật khẩu không đúng',
}

// ─── Resolve message cho 1 detail ─────────────────────────────
function resolveMessage(detail: ApiErrorDetail): string {
    const messageFn = CATEGORY_MESSAGES[detail.category]

    // category có trong map → dùng, truyền params nếu có
    if (messageFn) return messageFn(detail.params ?? undefined)

    // category chưa map → fallback reason từ BE
    return detail.reason
}

// ─── Main export ───────────────────────────────────────────────
export function mapFieldErrors(error: ApiError | null) {
    const fieldErrors: Record<string, string> = {}
    const globalErrors: string[] = []

    if (!error) return { fieldErrors, globalErrors }
    console.log(error)
    if (Array.isArray(error.details) && error.details.length > 0) {
        for (const detail of error.details) {
            if (detail.field && !(detail.field in fieldErrors)) {
                // Lấy lỗi đầu tiên mỗi field, BE đã sort theo priority
                fieldErrors[detail.field] = resolveMessage(detail)
            } else if (!detail.field) {
                globalErrors.push(resolveMessage(detail))
            }
        }
    } else {
        const codeMessage = error.code ? ERROR_CODE_MESSAGES[error.code] : null
        globalErrors.push(codeMessage ?? error.message)
    }

    return { fieldErrors, globalErrors }
}