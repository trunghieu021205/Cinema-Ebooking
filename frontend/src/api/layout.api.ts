import apiClient from '@/api/axios'
import type { RoomLayoutResponse, BulkUpdateResponse, BulkSeatIdsRequest,BulkTypeRequest  } from '@/types/seat'

export const layoutApi = {

  // GET /api/v1/rooms/:roomId/layout
  // Trả về grid 2D ghế của phòng — dùng cho RoomLayoutPage.
  getByRoom: (roomId: number) =>
        apiClient.get<RoomLayoutResponse>(`/rooms/${roomId}/layout`),
    
  // Bulk active (không cần roomId)
  bulkActivate: (data: BulkSeatIdsRequest) =>
    apiClient.patch<BulkUpdateResponse>(`/seats/bulk-activate`, data),

  // Bulk inactive
  bulkInactivate: (data: BulkSeatIdsRequest) =>
    apiClient.patch<BulkUpdateResponse>(`/seats/bulk-inactivate`, data),

  // Bulk update seat type
  bulkUpdateSeatType: (data: BulkTypeRequest) =>
    apiClient.patch<BulkUpdateResponse>(`/seats/bulk-type`, data),
}