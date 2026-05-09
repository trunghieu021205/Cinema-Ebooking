export type SeatStatus = 'ACTIVE' | 'INACTIVE'
import type { RoomType } from "@types/room"
export interface SeatResponse {
  id:            number
  rowIndex:      number
  colIndex:      number
  label:         string
  status:        SeatStatus
  seatTypeId:    number
  roomLayoutId:  number     
  coupleGroupId: number | null
}

export interface RoomLayoutResponse {
  id:             number
  layoutVersion: number
  roomType:      RoomType
  effectiveDate:  string      
  totalRows:      number
  totalCols:      number
  rows:           SeatResponse[][]
}
export interface SeatUpdateRequest {
  seatId:         number
  newStatus?:     SeatStatus | null
  newSeatTypeId?: number | null
}

export interface UpdateRoomLayoutRequest {
  effectiveDate: string  
  roomType:      RoomType
  updates:       SeatUpdateRequest[]
}

// Summary dùng cho dropdown chọn phiên bản layout
export interface RoomLayoutSummaryResponse {
  id:            number
  layoutVersion: number
  roomType:      RoomType
  effectiveDate: string
  totalRows:     number
  totalCols:     number
  createdAt:     Date
}

// Response trả về sau khi bulk-update thành công
export interface BulkUpdateResponse {
  updatedCount: number
  newLayoutId:  number
  message?:     string
}
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