import { ref, computed } from 'vue'
import type { SeatUpdateRequest, SeatStatus } from '@/types/seat'

export function usePendingChanges() {
  const changes = ref<Map<number, SeatUpdateRequest>>(new Map())

  const addChange = (seatId: number, newStatus?: SeatStatus | null, newSeatTypeId?: number | null) => {
    const existing = changes.value.get(seatId) || { seatId }
    if (newStatus !== undefined) existing.newStatus = newStatus
    if (newSeatTypeId !== undefined) existing.newSeatTypeId = newSeatTypeId
    changes.value.set(seatId, existing)
  }

  const removeChange = (seatId: number) => {
    changes.value.delete(seatId)
  }

  const clearAll = () => {
    changes.value.clear()
  }

  const hasChanges = computed(() => changes.value.size > 0)
  const changeList = computed(() => Array.from(changes.value.values()))

  return {
    changes,
    addChange,
    removeChange,
    clearAll,
    hasChanges,
    changeList,
  }
}