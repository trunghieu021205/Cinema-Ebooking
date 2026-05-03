// composables/useSeatSelection.ts
import { ref, readonly, type Ref } from 'vue'
import type { SeatResponse } from '@/types/seat'

export function useSeatSelection() {
  const selectedIds = ref<Set<number>>(new Set())
  let isDragging = false
  let startX = 0, startY = 0
  let dragContainer: HTMLElement | null = null
  let addToSelection = false  // giữ Ctrl khi drag

  // Hỗ trợ click thường (chọn một) và Ctrl+click (toggle)
  function toggleSeat(seat: SeatResponse, event?: MouseEvent) {
    const newSet = new Set(selectedIds.value)
    if (event?.ctrlKey || event?.metaKey) {
      // Ctrl+click: thêm/bớt
      if (newSet.has(seat.id)) newSet.delete(seat.id)
      else newSet.add(seat.id)
    } else {
      // Click thường: chỉ chọn ghế này, bỏ chọn tất cả khác
      newSet.clear()
      newSet.add(seat.id)
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
      const scrollLeft = dragContainer!.scrollLeft
      const scrollTop = dragContainer!.scrollTop
      return {
        x: clientX - rect.left + scrollLeft,
        y: clientY - rect.top + scrollTop,
      }
    }

    // Kiểm tra một phần tử ghế có nằm trong vùng chọn (hình chữ nhật) không
    const isSeatIntersecting = (
      seatEl: HTMLElement,
      selectionRect: { left: number; top: number; right: number; bottom: number }
    ) => {
      const containerRect = dragContainer!.getBoundingClientRect()
      const scrollLeft = dragContainer!.scrollLeft
      const scrollTop = dragContainer!.scrollTop
      const elRect = seatEl.getBoundingClientRect()
      const elLeft = elRect.left - containerRect.left + scrollLeft
      const elRight = elLeft + elRect.width
      const elTop = elRect.top - containerRect.top + scrollTop
      const elBottom = elTop + elRect.height

      return !(
        elRight < selectionRect.left ||
        elLeft > selectionRect.right ||
        elBottom < selectionRect.top ||
        elTop > selectionRect.bottom
      )
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
      const newlySelected = new Set<number>()
      seats.forEach((seatEl) => {
        if (isSeatIntersecting(seatEl, selectionRect)) {
          const id = Number(seatEl.dataset.seatId)
          if (!isNaN(id)) newlySelected.add(id)
        }
      })

      if (addToSelection) {
        // Giữ Ctrl: thêm vào vùng chọn hiện tại
        selectedIds.value = new Set([...selectedIds.value, ...newlySelected])
      } else {
        // Không giữ Ctrl: thay thế vùng chọn
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
      // Chỉ kéo bằng chuột trái
      if (e.button !== 0) return
      const target = e.target as HTMLElement
      const seatEl = target.closest('[data-seat-id]')
      if (!seatEl) return

      e.preventDefault()
      isDragging = true
      addToSelection = e.ctrlKey || e.metaKey

      const { x, y } = getRelativeCoords(e.clientX, e.clientY)
      startX = x
      startY = y

      // Nếu không giữ Ctrl, xóa vùng chọn cũ trước khi bắt đầu kéo
      if (!addToSelection) {
        selectedIds.value = new Set()
      }

      document.addEventListener('mousemove', onMouseMove)
      document.addEventListener('mouseup', onMouseUp)
    }

    container.addEventListener('mousedown', onMouseDown)

    // Cleanup function
    return () => {
      container.removeEventListener('mousedown', onMouseDown)
      document.removeEventListener('mousemove', onMouseMove)
      document.removeEventListener('mouseup', onMouseUp)
    }
  }

  return {
    selectedIds: readonly(selectedIds) as Readonly<Ref<Set<number>>>,
    toggleSeat,
    clearSelection,
    initDragSelect,
  }
}