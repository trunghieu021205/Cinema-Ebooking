import { AgeRating } from './age-rating.enum'
import { MovieStatus } from './movie-status.enum'
import type { Genre } from './genre.model'

export interface Movie {
  id: string
  title: string
  description?: string

  duration: number
  ageRating: AgeRating
  releaseDate: string

  status: MovieStatus

  posterUrl?: string
  bannerUrl?: string

  director?: string
  actors?: string

  genres?: Genre[]

  rating?: number
  ratingCount?: number
}
