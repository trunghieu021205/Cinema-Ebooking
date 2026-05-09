import apiClient from './axios'
import type {
  RoomLayoutResponse,
  RoomLayoutSummaryResponse,
  BulkUpdateResponse,
  UpdateRoomLayoutRequest,
} from '@/types/seat'

export const layoutApi = {
  // Lấy layout theo ngày (nếu không có date -> layout hiện tại)
  getLayout: (roomId: number, date?: string) =>
    apiClient.get<RoomLayoutResponse>(`/rooms/${roomId}/layout`, { params: date ? { date } : {} }),

  // Lấy danh sách các phiên bản layout (để hiển thị hint)
  getLayoutHistory: (roomId: number) =>
    apiClient.get<RoomLayoutSummaryResponse[]>(`/rooms/${roomId}/layouts`),

  // Tạo layout mới với các thay đổi
  updateLayoutSeats: (roomId: number, data: UpdateRoomLayoutRequest) =>
    apiClient.post<BulkUpdateResponse>(`/rooms/${roomId}/layouts/update`, data),
}