import { ref, readonly } from 'vue'
import { showtimeApi } from '@/api/showtime.api'
import type { ShowtimeResponse, CreateShowtimeRequest, UpdateShowtimeRequest } from '@/types/showtime'

interface ApiRejected {
  fieldErrors: Record<string, string>
  globalErrors: string[]
  message: string
}

export function useShowtime(cinemaId: number) {
  const showtimes = ref<ShowtimeResponse[]>([])
  const isLoading = ref(false)
  const fieldErrors = ref<Record<string, string>>({})
  const globalErrors = ref<string[]>([])

  const currentPage = ref(0)
  const totalPages = ref(0)
  const totalItems = ref(0)
  const pageSize = 8

  // Prefetch next page cache
  const nextPageCache = ref<ShowtimeResponse[]>([])
  const nextPageDirty = ref(false)

  function handleError(err: unknown) {
    const e = err as ApiRejected
    fieldErrors.value = e.fieldErrors ?? {}
    if (e.globalErrors?.length) {
      globalErrors.value = e.globalErrors
    } else if (!Object.values(fieldErrors.value).some(Boolean)) {
      globalErrors.value = [e.message ?? 'Đã có lỗi xảy ra']  // luôn có gì đó để hiển thị
    } else {
      globalErrors.value = []
    }
  }

  function clearErrors() {
    fieldErrors.value = {}
    globalErrors.value = []
  }

  async function prefetchNextPage(page: number) {
    if (page >= totalPages.value) {
      nextPageCache.value = []
      return
    }
    try {
      const res = await showtimeApi.getListByCinema(cinemaId, page, pageSize)
      nextPageCache.value = res.content
      nextPageDirty.value = false
    } catch {
      nextPageCache.value = []
    }
  }

  async function fetchList(page = 0) {
    isLoading.value = true
    clearErrors()
    try {
      const res = await showtimeApi.getListByCinema(cinemaId, page, pageSize)
      showtimes.value = res.content
      currentPage.value = res.page.number
      totalPages.value = res.page.totalPages
      totalItems.value = res.page.totalElements
      nextPageDirty.value = false
      prefetchNextPage(page + 1)
    } catch (err) {
      handleError(err)
    } finally {
      isLoading.value = false
    }
  }

  async function goToPage(page: number) {
    const isNextPage = page === currentPage.value + 1
    if (isNextPage && !nextPageDirty.value && nextPageCache.value.length > 0) {
      showtimes.value = nextPageCache.value
      currentPage.value = page
      nextPageCache.value = []
      prefetchNextPage(page + 1)
    } else {
      await fetchList(page)
    }
  }

  async function create(body: Omit<CreateShowtimeRequest, 'cinemaId'>): Promise<boolean> {
    clearErrors()
    try {
      const created = await showtimeApi.create({ ...body, cinemaId })
      showtimes.value.unshift(created)
      totalItems.value++
      totalPages.value = Math.ceil(totalItems.value / pageSize)

      if (showtimes.value.length > pageSize) {
        showtimes.value.pop()
      }
      nextPageCache.value = []
      prefetchNextPage(currentPage.value + 1)
      return true
    } catch (err) {
      handleError(err)
      return false
    }
  }

  async function save(item: ShowtimeResponse): Promise<boolean> {
    clearErrors()
    const body: UpdateShowtimeRequest = {
      startTime: item.startTime,
      endTime: item.endTime,
      audioLanguage: item.audioLanguage,
      subtitleLanguage: item.subtitleLanguage,
    }
    try {
      const updated = await showtimeApi.update(item.id, body)
      const idx = showtimes.value.findIndex((s) => s.id === updated.id)
      if (idx !== -1) showtimes.value[idx] = updated
      return true
    } catch (err) {
      handleError(err)
      return false
    }
  }

  async function cancel(item: ShowtimeResponse): Promise<boolean> {
    clearErrors()
    try {
      await showtimeApi.cancel(item.id)
      // Update local status
      const idx = showtimes.value.findIndex((s) => s.id === item.id)
      if (idx !== -1) showtimes.value[idx] = { ...showtimes.value[idx], status: 'CANCELLED' }
      return true
    } catch (err) {
      handleError(err)
      return false
    }
  }

  return {
    showtimes: readonly(showtimes),
    isLoading: readonly(isLoading),
    fieldErrors: readonly(fieldErrors),
    globalErrors: readonly(globalErrors),
    currentPage: readonly(currentPage),
    totalPages: readonly(totalPages),
    totalItems: readonly(totalItems),
    pageSize,
    fetchList,
    goToPage,
    create,
    save,
    cancel,
  }
}