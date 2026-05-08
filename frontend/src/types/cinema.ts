export type CinemaStatus = 'ACTIVE' | 'INACTIVE'

export interface CinemaResponse {
  id: number
  name: string
  address: string
  city: string
  status: CinemaStatus
}

export interface CreateCinemaRequest {
  name: string
  address: string
  city: string
}

export interface UpdateCinemaRequest {
  name: string
  address: string
  city: string
  status: CinemaStatus
}

