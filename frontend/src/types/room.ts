export type RoomType   = 'TYPE_2D' | 'TYPE_3D' | 'IMAX'
export type RoomStatus = 'ACTIVE' | 'INACTIVE' | 'MAINTENANCE'

export interface RoomResponse {
  id:           number
  name:         string
  roomType:     RoomType
  numberOfRows: number
  numberOfCols: number
  totalSeats:   number
  status:       RoomStatus
  cinemaId:     number
}

export interface RoomIdResponse{
  id: number
}

export interface CreateRoomRequest {
  name:         string
  roomType:     RoomType
  numberOfRows: number
  numberOfCols: number
  cinemaId:     number
}

export interface UpdateRoomRequest {
  name:     string
  status:   RoomStatus
}