import apiClient from './axios'
import type { GenreResponse, CreateGenreRequest, UpdateGenreRequest } from '@/types/genre'
import type { NestedPage } from '@/types/common.types'
export const genreApi = {
  // GET /api/v1/genres?page=0&size=8
  getList: (page = 0, size = 8) =>
    apiClient.get<NestedPage<GenreResponse>>('/genres', { params: { page, size, sort: 'id,desc' } }),

  // POST /api/v1/genres
  create: (body: CreateGenreRequest) =>
    apiClient.post<GenreResponse>('/genres', body),

  // PUT /api/v1/genres/{id}
  update: (id: number, body: UpdateGenreRequest) =>
    apiClient.put<GenreResponse>(`/genres/${id}`, body),

  // DELETE /api/v1/genres/{id} → 204 No Content
  delete: (id: number) =>
    apiClient.delete(`/genres/${id}`),
}