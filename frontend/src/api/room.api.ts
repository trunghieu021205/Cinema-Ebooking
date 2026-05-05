import apiClient from '@/api/axios'
import type { RoomResponse, CreateRoomRequest, UpdateRoomRequest } from '@/types/room'
import type { Page } from '@/types/common.types'

export const roomApi = {

  // GET /api/v1/rooms?cinemaId=X&page=0&size=8
  getListByCinema: (cinemaId: number, page = 0, size = 8) =>
    apiClient.get<Page<RoomResponse>>('/rooms', {
      params: { cinemaId, page, size, sort: 'id,desc' },
    }),

  // GET /api/v1/rooms/:id
  getById: (id: number) =>
    apiClient.get<RoomResponse>(`/rooms/${id}`),

  // POST /api/v1/rooms
  create: (body: CreateRoomRequest) =>
    apiClient.post<RoomResponse>('/rooms', body),

  // POST /api/v1/rooms/:id/generate-layout
  // Gọi ngay sau create để tạo layout ghế mặc định.
  generateLayout: (roomId: number) =>
    apiClient.post(`/rooms/${roomId}/generate-layout`),

  // PUT /api/v1/rooms/:id
  update: (id: number, body: UpdateRoomRequest) =>
    apiClient.put<RoomResponse>(`/rooms/${id}`, body),

  // DELETE /api/v1/rooms/:id → 204
  delete: (id: number) =>
    apiClient.delete(`/rooms/${id}`),
}