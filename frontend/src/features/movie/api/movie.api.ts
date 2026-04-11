import axiosClient from '@/app/providers/axios'
import type { ApiResponse } from '@/shared/types/api.type'
import type { Movie } from '@/features/movie/types'

export const getMovies = async (): Promise<Movie[]> => {
  const res = await axiosClient.get<ApiResponse<Movie[]>>('/movies')
  return res.data.data
}
