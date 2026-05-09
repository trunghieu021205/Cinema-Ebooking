import apiClient from '@/api/axios'
import type { RoomResponse, CreateRoomRequest, UpdateRoomRequest } from '@/types/room'
import type { NestedPage } from '@/types/common.types'

export const roomApi = {

  getListByCinema: (cinemaId: number, page = 0, size = 8) =>
    apiClient.get<NestedPage<RoomResponse>>(`/rooms/cinema/${cinemaId}`, {
      params: { page, size, sort: 'id,desc' },
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