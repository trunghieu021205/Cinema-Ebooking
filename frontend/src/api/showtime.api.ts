import apiClient from '@/api/axios'
import type {
  ShowtimeResponse,
  CreateShowtimeRequest,
  UpdateShowtimeRequest,
  ShowtimeFormatResponse,
} from '@/types/showtime'
import type { NestedPage } from '@/types/common.types'

export const showtimeApi = {
  // GET /api/v1/admin/showtimes?cinemaId=&roomId=&status=&page=&size=&sort=
  getList: (params: {
    page?: number
    size?: number
    sort?: string
    cinemaId?: number
    movieId?: number
    roomId?: number
    status?: string
  }) =>
    apiClient.get<NestedPage<ShowtimeResponse>>('/admin/showtimes', { params }),

  getById: (id: number) =>
    apiClient.get<ShowtimeResponse>(`/admin/showtimes/${id}`),

  create: (body: CreateShowtimeRequest) =>
    apiClient.post<ShowtimeResponse>('/admin/showtimes', body),

  update: (id: number, body: UpdateShowtimeRequest) =>
    apiClient.put<ShowtimeResponse>(`/admin/showtimes/${id}`, body),

  cancel: (id: number) =>
    apiClient.patch<ShowtimeResponse>(`/admin/showtimes/${id}/cancel`),
}
