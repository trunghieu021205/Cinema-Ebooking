import type { RoomLayoutResponse, RoomLayoutSummaryResponse } from '@/types/seat'

export function validateEffectiveDate(
  currentLayout: RoomLayoutResponse | null,
  effectiveDate: string,
  allVersions: RoomLayoutSummaryResponse[]
): string | null {
  if (!currentLayout) return 'Không có dữ liệu layout'
  if (!effectiveDate) return 'Vui lòng chọn ngày hiệu lực'

  const newDate = new Date(effectiveDate + 'T00:00:00') // đảm bảo so sánh local date
  if (isNaN(newDate.getTime())) return 'Ngày hiệu lực không hợp lệ'

  const toDateString = (d: Date) => {
    const year = d.getFullYear()
    const month = String(d.getMonth() + 1).padStart(2, '0')
    const day = String(d.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
  }
  const todayStr = toDateString(new Date())

  const { used, layoutVersion } = currentLayout

  if (used) {
    // Layout đã sử dụng → sẽ tạo version mới
    if (effectiveDate <= currentLayout.effectiveDate) {
      return `Ngày hiệu lực phải sau ngày hiệu lực hiện tại (${currentLayout.effectiveDate})`
    }
    if (currentLayout.lastUsedDate && effectiveDate <= currentLayout.lastUsedDate) {
      return `Ngày hiệu lực phải sau ngày sử dụng cuối cùng (${currentLayout.lastUsedDate}) của phiên bản hiện tại`
    }
    // Không cần kiểm tra quá khứ vì đã có điều kiện trên
  } else {
    // Layout chưa dùng → cập nhật trực tiếp
    if (layoutVersion === 1) {
      if (effectiveDate < todayStr) {
        return `Ngày hiệu lực không được trong quá khứ`
      }
    } else {
      const prev = allVersions.find(v => v.layoutVersion === layoutVersion - 1)
      if (prev) {
        if (effectiveDate < prev.effectiveDate) {
          return `Ngày hiệu lực không được trước ngày có hiệu lực của phiên bản trước (${prev.effectiveDate})`
        }
        if (prev.lastUsedDate && effectiveDate <= prev.lastUsedDate) {
          return `Ngày hiệu lực phải sau ngày sử dụng cuối cùng (${prev.lastUsedDate}) của phiên bản v${prev.layoutVersion}`
        }
      }
    }
  }
  return null // Hợp lệ
}