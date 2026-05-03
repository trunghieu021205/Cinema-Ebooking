export type SeatStatus = 'ACTIVE' | 'INACTIVE'

export interface SeatResponse {
  id:         number
  rowIndex:   number
  colIndex:   number
  label:      string
  status:     SeatStatus
  seatTypeId: number
  roomId:     number
}

export interface RoomLayoutResponse {
  rows:      SeatResponse[][]  // rows[rowIndex][colIndex]
  totalRows: number
  totalCols: number
}

// ==================== BULK UPDATE TYPES ====================

// Response chung cho tất cả bulk operations
export interface BulkUpdateResponse {
  successCount: number;
  errors: BulkError[];
}

export interface BulkError {
  seatId: number;
  reason: string;
}

// Request cho bulk-update loại ghế
export interface BulkTypeRequest {
  seatIds: number[];
  seatTypeId: number;
}

// Request cho bulk-activate và bulk-inactivate (chỉ seatIds)
export interface BulkSeatIdsRequest {
  seatIds: number[];
}

// ── Seat type color config ────────────────────────────────────────────────────
// Sửa key (seatTypeId) cho đúng với giá trị backend trả về.
// Hiện tại giả định: 1 = STANDARD, 2 = VIP, 3 = COUPLE.
// bg / border / text là Tailwind utility classes → đổi màu ở đây.

export interface SeatTypeStyle {
  label:  string
  bg:     string   // background khi ACTIVE
  border: string   // border khi ACTIVE
  text:   string   // text color
}

export const SEAT_TYPE_STYLES: Record<number, SeatTypeStyle> = {
  1: { label: 'Standard', bg: 'bg-slate-100',  border: 'border-slate-300',  text: 'text-slate-700'  },
  2: { label: 'VIP',      bg: 'bg-amber-100',  border: 'border-amber-400',  text: 'text-amber-800'  },
  3: { label: 'Couple',   bg: 'bg-rose-100',   border: 'border-rose-400',   text: 'text-rose-800'   },
}

// Fallback nếu seatTypeId không có trong map (type chưa được config)
export const SEAT_TYPE_FALLBACK: SeatTypeStyle = {
  label: 'Unknown', bg: 'bg-gray-100', border: 'border-gray-300', text: 'text-gray-500',
}