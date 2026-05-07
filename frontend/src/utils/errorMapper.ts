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
const ERROR_CODE_MESSAGES: Record<number, MessageFn> = {
    1001: () => 'Dữ liệu không hợp lệ',
    2002: () => 'Email hoặc mật khẩu không đúng',

    3006: (p) => `Không thể xóa rạp ${p?.cinemaName} vì vẫn còn phòng chưa bị xóa`,
    3009: (p) => `Không thể xoá phòng ${p?.roomName} vì vẫn còn suất chiếu đã lên lịch hoặc đang diễn ra`,
}

// ─── Field + Category override ─────────────────────────────────
// Khi cần message riêng cho field cụ thể, thêm vào đây
// key: `${field}.${category}`

const FIELD_CATEGORY_MESSAGES: Record<string, MessageFn> = {
  'address.DUPLICATE': () => 'Địa chỉ trong thành phố này đã được sử dụng',
}

// ─── Resolve message cho 1 detail ─────────────────────────────
function resolveDetailMessage(detail: ApiErrorDetail): string {
  // 1. Ưu tiên field-specific override
  const fieldKey = `${detail.field}.${detail.category}`
  const fieldOverride = FIELD_CATEGORY_MESSAGES[fieldKey]
  if (fieldOverride) return fieldOverride(detail.params ?? undefined)

  // 2. Fallback về category map
  const messageFn = CATEGORY_MESSAGES[detail.category]
  if (messageFn) return messageFn(detail.params ?? undefined)

  // 3. Fallback về reason từ backend
  return detail.reason
}

function resolveGlobalMessage(error: ApiError): string {
  const messageFn = error.code ? ERROR_CODE_MESSAGES[error.code] : null

  return messageFn
    ? messageFn(error.params ?? undefined)
    : error.message
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
                fieldErrors[detail.field] = resolveDetailMessage(detail)
            } else if (!detail.field) {
                globalErrors.push(resolveDetailMessage(detail))
            }
        }
    } else {
        globalErrors.push(resolveGlobalMessage(error))
    }

    return { fieldErrors, globalErrors }
}