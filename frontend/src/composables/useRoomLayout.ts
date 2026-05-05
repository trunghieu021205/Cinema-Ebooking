import { ref, readonly } from 'vue'
import { layoutApi } from '@/api/layout.api'
import type { RoomLayoutResponse } from '@/types/seat'

export function useRoomLayout() {

  const layout    = ref<RoomLayoutResponse | null>(null)
  const isLoading = ref(false)
  const error     = ref<string | null>(null)

  async function fetchLayout(roomId: number) {
    isLoading.value = true
    error.value     = null
    try {
      layout.value = await layoutApi.getByRoom(roomId)
    } catch (err: unknown) {
      const e = err as { message?: string }
      error.value = e.message ?? 'Không thể tải layout phòng'
    } finally {
      isLoading.value = false
    }
  }

  return {
    layout:    readonly(layout),
    isLoading: readonly(isLoading),
    error:     readonly(error),
    fetchLayout,
  }
}