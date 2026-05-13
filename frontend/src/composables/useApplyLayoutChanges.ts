// src/composables/useApplyLayoutChanges.ts
import { ref, computed, type Ref } from 'vue'
import { cloneDeep } from 'lodash-es'
import { layoutApi } from '@/api/layout.api'
import type { SeatResponse, RoomLayoutResponse, SeatStatus } from '@/types/seat'
import { usePendingChanges } from '@/composables/usePendingChanges'
import type { MaybeRef } from '@vueuse/core' // hoặc tự định nghĩa

interface MappedError {
  type?: string
  fieldErrors?: Record<string, string>
  globalErrors?: string[]
  code?: string | null
  message?: string
  status?: number
}

export function useApplyLayoutChanges(
  layout: Ref<RoomLayoutResponse | null>,
  effectiveDate: Ref<string>,
  fetchLayout: (roomId: number, date?: string) => Promise<void>,
  fetchLayoutHistory: (roomId: number) => Promise<void>,
  syncSelectedVersion: () => void,
  selectedRoomType?: Ref<string>
) {
  const { changes, addChange, removeChange, clearAll, hasChanges: hasSeatChanges, changeList } = usePendingChanges()

  const applyError = ref<MappedError | null>(null)
    
  function findSeatById(seatId: number): SeatResponse | undefined {
    if (!layout.value) return undefined
    for (const row of layout.value.rows) {
        const seat = row.find(s => s.id === seatId)
        if (seat) return seat
    }
    return undefined
    }
    
  const hasChanges = computed(() => {
    if (hasSeatChanges.value) return true
    if (layout.value && selectedRoomType.value !== layout.value.roomType) return true
    return false
  })
    

  function addChangeSmart(seatId: number, newStatus?: SeatStatus | null, newSeatTypeId?: number | null) {
    const seat = findSeatById(seatId)
    const effectiveNewStatus = newStatus ?? undefined
    const effectiveNewSeatTypeId = newSeatTypeId ?? undefined
    if (!seat) {
        // fallback: không có layout để so sánh -> dùng addChange cũ
        addChange(seatId, newStatus, newSeatTypeId)
        return
    }

    const existing = changes.value.get(seatId) // từ usePendingChanges (ref Map)

    // Giá trị sau khi merge với pending hiện tại (nếu có)
    const mergedStatus = effectiveNewStatus !== undefined ? effectiveNewStatus : (existing?.newStatus !== undefined ? existing.newStatus : seat.status)
    const mergedTypeId = effectiveNewSeatTypeId !== undefined ? effectiveNewSeatTypeId : (existing?.newSeatTypeId !== undefined ? existing.newSeatTypeId : seat.seatTypeId)
      
    const statusChanged = mergedStatus !== seat.status
    const typeChanged = mergedTypeId !== seat.seatTypeId

    if (!statusChanged && !typeChanged) {
        // Không có gì thay đổi → xóa pending nếu có
        if (existing) changes.value.delete(seatId)
        return
    }

    // Có thay đổi thực sự → tạo/cập nhật entry
    if (!existing) {
        const entry: SeatUpdateRequest = { seatId }
        if (statusChanged) entry.newStatus = mergedStatus as SeatStatus
        if (typeChanged) entry.newSeatTypeId = mergedTypeId
        changes.value.set(seatId, entry)
    } else {
        if (statusChanged) {
        existing.newStatus = mergedStatus as SeatStatus
        } else {
        delete existing.newStatus
        }
        if (typeChanged) {
        existing.newSeatTypeId = mergedTypeId
        } else {
        delete existing.newSeatTypeId
        }
        // Nếu xóa hết field thì loại bỏ entry (trường hợp này đã bị loại ở trên nhưng vẫn an toàn)
        if (existing.newStatus === undefined && existing.newSeatTypeId === undefined) {
        changes.value.delete(seatId)
        }
    }
  }

  // Map coupleGroupId -> seatId
  const seatCoupleMap = computed(() => {
    const map = new Map<number, number | null>()
    if (!layout.value) return map
    for (const row of layout.value.rows) {
      for (const seat of row) {
        map.set(seat.id, seat.coupleGroupId ?? null)
      }
    }
    return map
  })

  // Hàm lấy label ghế (A1, B2...)
  function getSeatLabel(seatId: number): string {
    if (!layout.value) return `#${seatId}`
    for (const row of layout.value.rows) {
      const seat = row.find(s => s.id === seatId)
      if (seat) {
        const rowLetter = String.fromCharCode(65 + seat.rowIndex - 1)
        const displayedCol = layout.value.totalCols - seat.colIndex + 1
        return `${rowLetter}${displayedCol}`
      }
    }
    return `#${seatId}`
  }

  // Gom nhóm couple (tận dụng changeList & seatCoupleMap)
  const groupedChangeList = computed(() => {
    const groups: Array<{
      seatIds: number[]
      labels: string[]
      newStatus?: string | null
      newSeatTypeId?: number | null
    }> = []
    const processed = new Set<number>()

    for (const change of changeList.value) {
        if (processed.has(change.seatId)) continue
        
        if (change.newStatus === undefined && change.newSeatTypeId === undefined) continue
      const coupleId = seatCoupleMap.value.get(change.seatId)
      if (coupleId != null) {
        const other = changeList.value.find(
          c => c.seatId !== change.seatId && seatCoupleMap.value.get(c.seatId) === coupleId
        )
        if (other && change.newStatus === other.newStatus && change.newSeatTypeId === other.newSeatTypeId) {
          groups.push({
            seatIds: [change.seatId, other.seatId],
            labels: [getSeatLabel(change.seatId), getSeatLabel(other.seatId)],
            newStatus: change.newStatus,
            newSeatTypeId: change.newSeatTypeId,
          })
          processed.add(change.seatId)
          processed.add(other.seatId)
          continue
        }
      }
      groups.push({
        seatIds: [change.seatId],
        labels: [getSeatLabel(change.seatId)],
        newStatus: change.newStatus,
        newSeatTypeId: change.newSeatTypeId,
      })
      processed.add(change.seatId)
      }
    return groups
  })

    const pendingSeatIds = computed(() => changeList.value.map(c => c.seatId))

  const previewLayout = computed(() => {
    if (!layout.value || changeList.value.length === 0) return layout.value
    const newLayout = cloneDeep(layout.value)
    const changeMap = new Map(
      changeList.value.map(c => [c.seatId, { status: c.newStatus, typeId: c.newSeatTypeId }])
    )
    for (const row of newLayout.rows) {
      for (const seat of row) {
        const change = changeMap.get(seat.id)
        if (change) {
          if (change.status !== undefined) seat.status = change.status as SeatStatus
          if (change.typeId !== undefined) seat.seatTypeId = change.typeId
        }
      }
    }
    return newLayout
  })

  async function applyAllChanges(roomId: number) {
    applyError.value = null
    if (!hasChanges.value) {
      applyError.value = { globalErrors: ['Không có thay đổi nào để áp dụng.'] }
      return
    }
    const updates = changeList.value.map(change => ({
      seatId: change.seatId,
      newStatus: change.newStatus,
      newSeatTypeId: change.newSeatTypeId,
    }))
      try {
      console.log('Applying changes:', selectedRoomType.value, updates)
      await layoutApi.updateLayoutSeats(roomId, {
        effectiveDate: effectiveDate.value,
        roomType: selectedRoomType.value,
        updates,
      })
      clearAll()
      await fetchLayout(roomId, effectiveDate.value)
      await fetchLayoutHistory(roomId)
      syncSelectedVersion()
    } catch (err: any) {
      const mapped: MappedError = err?.fieldErrors || err?.globalErrors || err?.message
        ? err
        : { globalErrors: [err?.message || 'Cập nhật thất bại'] }
      applyError.value = mapped
    }
  }

  return {
    // state & computed
    applyError,
    groupedChangeList,
    pendingSeatIds,
    previewLayout,
    // từ usePendingChanges
    addChange: addChangeSmart,
    removeChange,
    clearAll,
    hasChanges,
    changeList,
    // action
    applyAllChanges,
  }
}