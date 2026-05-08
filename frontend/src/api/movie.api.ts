// src/api/movie.api.ts
import apiClient from './axios'
import type { MovieResponse, CreateMovieRequest, UpdateMovieRequest } from '@/types/movie.types'
import type { Page } from '@/types/common.types';
export const movieApi = {
  getList: (page = 0, size = 8) =>
    apiClient.get<Page<MovieResponse>>('/movies', { params: { page, size, sort: 'id,desc' } }),

  getById: (id: number) =>
    apiClient.get<MovieResponse>(`/movies/${id}`),

  create: (body: CreateMovieRequest) =>
    apiClient.post<MovieResponse>('/movies', body),

  update: (id: number, body: UpdateMovieRequest) =>
    apiClient.put<MovieResponse>(`/movies/${id}`, body),

  delete: (id: number) =>
    apiClient.delete(`/movies/${id}`),
}