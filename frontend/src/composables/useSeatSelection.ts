import { ref, readonly, type Ref } from 'vue'
import type { SeatResponse } from '@/types/seat'

export function useSeatSelection() {
  const selectedIds = ref<Set<number>>(new Set())
  let isDragging = false
  let startX = 0, startY = 0
  let dragContainer: HTMLElement | null = null
  let addToSelection = false

  function toggleSeat(seat: SeatResponse, event?: MouseEvent) {
    const newSet = new Set(selectedIds.value)
    if (event?.ctrlKey || event?.metaKey) {
      if (newSet.has(seat.id)) newSet.delete(seat.id)
      else newSet.add(seat.id)
    } else {
      newSet.clear()
      newSet.add(seat.id)
    }
    selectedIds.value = newSet
  }

  function toggleCouple(left: SeatResponse, right: SeatResponse, event?: MouseEvent) {
    const newSet = new Set(selectedIds.value)
    if (event?.ctrlKey || event?.metaKey) {
      if (newSet.has(left.id) && newSet.has(right.id)) {
        newSet.delete(left.id)
        newSet.delete(right.id)
      } else {
        newSet.add(left.id)
        newSet.add(right.id)
      }
    } else {
      newSet.clear()
      newSet.add(left.id)
      newSet.add(right.id)
    }
    selectedIds.value = newSet
  }

  function clearSelection() {
    selectedIds.value = new Set()
  }

  function initDragSelect(container: HTMLElement) {
    dragContainer = container

    const getRelativeCoords = (clientX: number, clientY: number) => {
      const rect = dragContainer!.getBoundingClientRect()
      return { x: clientX - rect.left, y: clientY - rect.top }
    }

    const isSeatIntersecting = (seatEl: HTMLElement, selectionRect: { left: number; top: number; right: number; bottom: number }) => {
      const containerRect = dragContainer!.getBoundingClientRect()
      const elRect = seatEl.getBoundingClientRect()
      const elLeft = elRect.left - containerRect.left
      const elRight = elLeft + elRect.width
      const elTop = elRect.top - containerRect.top
      const elBottom = elTop + elRect.height
      return !(elRight < selectionRect.left || elLeft > selectionRect.right || elBottom < selectionRect.top || elTop > selectionRect.bottom)
    }

    const onMouseMove = (e: MouseEvent) => {
      if (!isDragging) return
      const { x: currentX, y: currentY } = getRelativeCoords(e.clientX, e.clientY)
      const left = Math.min(startX, currentX)
      const right = Math.max(startX, currentX)
      const top = Math.min(startY, currentY)
      const bottom = Math.max(startY, currentY)
      const selectionRect = { left, top, right, bottom }

      const seats = dragContainer!.querySelectorAll('[data-seat-id]') as NodeListOf<HTMLElement>
      const couples = dragContainer!.querySelectorAll('[data-couple-ids]') as NodeListOf<HTMLElement>
      const newlySelected = new Set<number>()

      seats.forEach(seatEl => {
        if (isSeatIntersecting(seatEl, selectionRect)) {
          const id = Number(seatEl.dataset.seatId)
          if (!isNaN(id)) newlySelected.add(id)
        }
      })
      couples.forEach(coupleEl => {
        if (isSeatIntersecting(coupleEl, selectionRect)) {
          const ids = coupleEl.dataset.coupleIds?.split(',').map(Number).filter(n => !isNaN(n)) || []
          ids.forEach(id => newlySelected.add(id))
        }
      })

      if (addToSelection) {
        selectedIds.value = new Set([...selectedIds.value, ...newlySelected])
      } else {
        selectedIds.value = newlySelected
      }
    }

    const onMouseUp = () => {
      if (!isDragging) return
      isDragging = false
      document.removeEventListener('mousemove', onMouseMove)
      document.removeEventListener('mouseup', onMouseUp)
    }

    const onMouseDown = (e: MouseEvent) => {
        if (e.button !== 0) return
        // Không cần check seatEl — drag được phép bắt đầu từ khoảng trống
        e.preventDefault()
        isDragging = true
        addToSelection = e.ctrlKey || e.metaKey
        const { x, y } = getRelativeCoords(e.clientX, e.clientY)
        startX = x
        startY = y
        if (!addToSelection) selectedIds.value = new Set()
        document.addEventListener('mousemove', onMouseMove)
        document.addEventListener('mouseup', onMouseUp)
    }

    container.addEventListener('mousedown', onMouseDown)
    return () => {
      container.removeEventListener('mousedown', onMouseDown)
      document.removeEventListener('mousemove', onMouseMove)
      document.removeEventListener('mouseup', onMouseUp)
    }
  }

  return {
    selectedIds: readonly(selectedIds) as Readonly<Ref<Set<number>>>,
    toggleSeat,
    toggleCouple,
    clearSelection,
    initDragSelect,
  }
}