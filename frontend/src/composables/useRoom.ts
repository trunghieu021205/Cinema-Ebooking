import { ref, readonly } from 'vue'
import { roomApi } from '@/api/room.api'
import type { RoomResponse, CreateRoomRequest, UpdateRoomRequest } from '@/types/room'

interface ApiRejected {
  fieldErrors: Record<string, string>
  globalErrors: string[]
  message: string
}

/**
 * useRoom(cinemaId)
 *
 * Nhận cinemaId từ route params — tất cả operations đều scoped trong cinema đó.
 * Pattern giống useCinema: prefetch trang kế, optimistic UI update, dirty cache.
 */
export function useRoom(cinemaId: number) {

  // ── State ──────────────────────────────────────────────────────────────────
  const rooms        = ref<RoomResponse[]>([])
  const isLoading    = ref(false)
  const fieldErrors  = ref<Record<string, string>>({})
  const globalErrors = ref<string[]>([])

  const currentPage = ref(0)
  const totalPages  = ref(0)
  const totalItems  = ref(0)
  const pageSize    = 8

  // ── Prefetch cache ─────────────────────────────────────────────────────────
  const nextPageCache = ref<RoomResponse[]>([])
  const nextPageDirty = ref(false)

  // ── Helpers ────────────────────────────────────────────────────────────────
  function handleError(err: unknown) {
    const e = err as ApiRejected
    fieldErrors.value  = e.fieldErrors  ?? {}
    if (e.globalErrors?.length) {
      globalErrors.value = e.globalErrors
    } else if (!Object.values(fieldErrors.value).some(Boolean)) {
      globalErrors.value = [e.message ?? 'Đã có lỗi xảy ra']  // luôn có gì đó để hiển thị
    } else {
      globalErrors.value = []
    }
  }

  function clearErrors() {
    fieldErrors.value  = {}
    globalErrors.value = []
  }

  async function prefetchNextPage(page: number) {
    if (page >= totalPages.value) {
      nextPageCache.value = []
      return
    }
    try {
      const res = await roomApi.getListByCinema(cinemaId, page, pageSize)
      nextPageCache.value = res.content
      nextPageDirty.value = false
    } catch {
      nextPageCache.value = []
    }
  }

  // ── Core fetch ─────────────────────────────────────────────────────────────
  async function fetchList(page = 0) {
    isLoading.value = true
    clearErrors()
    try {
      const res = await roomApi.getListByCinema(cinemaId, page, pageSize)
      rooms.value       = res.content
      currentPage.value = res.number
      totalPages.value  = res.totalPages
      totalItems.value  = res.totalElements
      nextPageDirty.value = false
      prefetchNextPage(page + 1)
    } catch (err) {
      handleError(err)
    } finally {
      isLoading.value = false
    }
  }

  // ── Navigate ───────────────────────────────────────────────────────────────
  async function goToPage(page: number) {
    const isNextPage = page === currentPage.value + 1
    if (isNextPage && !nextPageDirty.value && nextPageCache.value.length > 0) {
      rooms.value       = nextPageCache.value
      currentPage.value = page
      nextPageCache.value = []
      prefetchNextPage(page + 1)
    } else {
      await fetchList(page)
    }
  }

  // ── Create ─────────────────────────────────────────────────────────────────
  // Nhận Omit<CreateRoomRequest, 'cinemaId'> — cinemaId inject từ closure
  async function create(body: Omit<CreateRoomRequest, 'cinemaId'>): Promise<boolean> {
    clearErrors()
    try {
      const created = await roomApi.create({ ...body, cinemaId })

      // Tạo layout mặc định ngay sau khi room được tạo
      await roomApi.generateLayout(created.id)

      rooms.value.unshift(created)
      totalItems.value++
      totalPages.value = Math.ceil(totalItems.value / pageSize)

      if (rooms.value.length > pageSize) {
        rooms.value.pop()
      }

      nextPageCache.value = []
      prefetchNextPage(currentPage.value + 1)

      return true
    } catch (err) {
      handleError(err)
      return false
    }
  }

  // ── Save (Update) ──────────────────────────────────────────────────────────
  // Chỉ gửi fields có trong UpdateRoomRequest — numberOfRows/Cols bị bỏ qua dù panel có hiện
  async function save(item: RoomResponse): Promise<boolean> {
    clearErrors()
    const body: UpdateRoomRequest = {
      name:     item.name,
      roomType: item.roomType,
      status:   item.status,
    }
    try {
      const updated = await roomApi.update(item.id, body)
      const idx = rooms.value.findIndex((r) => r.id === updated.id)
      if (idx !== -1) rooms.value[idx] = updated
      return true
    } catch (err) {
      handleError(err)
      return false
    }
  }

  // ── Remove ─────────────────────────────────────────────────────────────────
  async function remove(item: RoomResponse): Promise<boolean> {
    clearErrors()
    try {
      await roomApi.delete(item.id)

      rooms.value = rooms.value.filter((r) => r.id !== item.id)
      totalItems.value--
      const newTotalPages = Math.ceil(totalItems.value / pageSize) || 1
      totalPages.value = newTotalPages

      if (nextPageCache.value.length > 0) {
        const [fillItem, ...rest] = nextPageCache.value
        rooms.value.push(fillItem)
        nextPageCache.value = rest
        nextPageDirty.value = true
      } else if (currentPage.value >= newTotalPages) {
        await fetchList(newTotalPages - 1)
      }

      return true
    } catch (err) {
      handleError(err)
      return false
    }
  }

  return {
    rooms:        readonly(rooms),
    isLoading:    readonly(isLoading),
    fieldErrors:  readonly(fieldErrors),
    globalErrors: readonly(globalErrors),
    currentPage:  readonly(currentPage),
    totalPages:   readonly(totalPages),
    totalItems:   readonly(totalItems),
    pageSize,

    fetchList,
    goToPage,
    create,
    save,
    remove,
  }
}