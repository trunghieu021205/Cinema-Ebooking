import apiClient from '@/api/axios'
import type {
  ShowtimeResponse,
  CreateShowtimeRequest,
  UpdateShowtimeRequest,
  ShowtimeFormatResponse,
} from '@/types/showtime'
import type { NestedPage } from '@/types/common.types'

// ─── Showtime API ─────────────────────────────────────────────────────────────
export const showtimeApi = {

  // GET /api/v1/showtimes?roomId=X&page=0&size=8
  getListByRoom: (roomId: number, page = 0, size = 8) =>
    apiClient.get<NestedPage<ShowtimeResponse>>('/showtimes', {
      params: { roomId, page, size, sort: 'startTime,desc' },
    }),

  // GET /api/v1/showtimes/:id
  getById: (id: number) =>
    apiClient.get<ShowtimeResponse>(`/showtimes/${id}`),

  // POST /api/v1/showtimes
  create: (body: CreateShowtimeRequest) =>
    apiClient.post<ShowtimeResponse>('/showtimes', body),

  // PUT /api/v1/showtimes/:id
  update: (id: number, body: UpdateShowtimeRequest) =>
    apiClient.put<ShowtimeResponse>(`/showtimes/${id}`, body),

  // DELETE /api/v1/showtimes/:id → 204
  delete: (id: number) =>
    apiClient.delete(`/showtimes/${id}`),
}

// ─── Showtime Format API ──────────────────────────────────────────────────────
export const showtimeFormatApi = {

  // GET /api/v1/showtime-formats
  getAll: () =>
    apiClient.get<ShowtimeFormatResponse[]>('/showtime-formats'),
}