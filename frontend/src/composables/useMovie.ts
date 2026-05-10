// src/composables/useMovie.ts
import { ref } from 'vue'
import { movieApi } from '@/api/movie.api'
import { genreApi } from '@/api/genre.api'
import type { MovieResponse, CreateMovieRequest, UpdateMovieRequest } from '@/types/movie.types'
import type { GenreResponse } from '@/types/genre'

export function useMovie() {
  // ── State ──────────────────────────────────────────────────────────────
  const movies = ref<MovieResponse[]>([])
  const genresList = ref<GenreResponse[]>([])
  const isLoading = ref(false)
  const fieldErrors = ref<Record<string, string>>({})
  const globalErrors = ref<string[]>([])

  const currentPage = ref(0)
  const totalPages = ref(0)
  const totalItems = ref(0)
  const pageSize = 8

  // Prefetch state
  const nextPageCache = ref<MovieResponse[]>([])
  const nextPageDirty = ref(false)

  // ── Helpers ────────────────────────────────────────────────────────────
  const handleError = (err: any) => {
    fieldErrors.value = err.fieldErrors ?? {}
    if (err.globalErrors?.length) {
      globalErrors.value = err.globalErrors
    } else if (!Object.values(fieldErrors.value).some(Boolean)) {
      globalErrors.value = [err.message ?? 'Đã có lỗi xảy ra']
    } else {
      globalErrors.value = []
    }
  }

  const clearErrors = () => {
    fieldErrors.value = {}
    globalErrors.value = []
  }

  // Prefetch next page in background (no loading flag)
  const prefetchNextPage = async (page: number) => {
    if (page >= totalPages.value) {
      nextPageCache.value = []
      return
    }
    try {
      const res = await movieApi.getList(page, pageSize)
      nextPageCache.value = res.content
      nextPageDirty.value = false
    } catch {
      nextPageCache.value = []
    }
  }

  // ── Core fetch ─────────────────────────────────────────────────────────
  const fetchList = async (page = 0) => {
    isLoading.value = true
    clearErrors()
    try {
      const res = await movieApi.getList(page, pageSize)
      movies.value = res.content
      currentPage.value = res.page.number
      totalPages.value = res.page.totalPages
      totalItems.value = res.page.totalElements
      nextPageDirty.value = false

      // Prefetch next page in background
      prefetchNextPage(page + 1)
    } catch (err: any) {
      handleError(err)
    } finally {
      isLoading.value = false
    }
  }

  // ── Navigate (with cache) ──────────────────────────────────────────────
  const goToPage = async (page: number) => {
    if (page < 0 || page >= totalPages.value) return

    const isNextPage = page === currentPage.value + 1

    if (isNextPage && !nextPageDirty.value && nextPageCache.value.length > 0) {
      // Use cached data for the next page
      movies.value = nextPageCache.value
      currentPage.value = page
      nextPageCache.value = []

      // Prefetch the next page after this one
      prefetchNextPage(page + 1)
    } else {
      // Fallback to regular fetch
      await fetchList(page)
    }
  }

  // ── Genres (unchanged) ─────────────────────────────────────────────────
    const fetchGenres = async () => {
        try {
        const response = await genreApi.getList(0, 1000)  // lấy tất cả thể loại
        genresList.value = response.content               // content là mảng GenreResponse[]
        } catch (err) {
        console.error('Failed to fetch genres', err)
        globalErrors.value = ['Không thể tải danh sách thể loại']
        genresList.value = [] // fallback an toàn
        }
    }


  // ── Create (local insertion + cache invalidation) ──────────────────────
  const create = async (payload: CreateMovieRequest): Promise<boolean> => {
    clearErrors()
    isLoading.value = true
    try {
      const created = await movieApi.create(payload)

      // Insert at the beginning (assuming newest first)
      movies.value.unshift(created)
      totalItems.value++
      totalPages.value = Math.ceil(totalItems.value / pageSize)

      // Remove overflow item if current page exceeds pageSize
      if (movies.value.length > pageSize) {
        movies.value.pop()
      }

      // Invalidate next page cache because order has changed
      nextPageCache.value = []
      prefetchNextPage(currentPage.value + 1)

      return true
    } catch (err: any) {
      handleError(err)
      return false
    } finally {
      isLoading.value = false
    }
  }

  // ── Save (local update only) ───────────────────────────────────────────
  const save = async (item: MovieResponse): Promise<boolean> => {
    clearErrors()
    isLoading.value = true
    const updatePayload: UpdateMovieRequest = {
      title: item.title,
      description: item.description,
      duration: item.duration,
      ageRating: item.ageRating,
      releaseDate: item.releaseDate,
      status: item.status,
      posterUrl: item.posterUrl,
      bannerUrl: item.bannerUrl,
      director: item.director,
      actors: item.actors,
      genreIds: item.genres.map(g => g.id)
    }
    try {
      const updated = await movieApi.update(item.id, updatePayload)
      const idx = movies.value.findIndex(m => m.id === updated.id)
      if (idx !== -1) movies.value[idx] = updated
      return true
    } catch (err: any) {
      handleError(err)
      return false
    } finally {
      isLoading.value = false
    }
  }

  // ── Remove (fill from cache if available) ──────────────────────────────
  const remove = async (item: MovieResponse): Promise<boolean> => {
    clearErrors()
    isLoading.value = true
    try {
      await movieApi.delete(item.id)

      // Remove locally
      movies.value = movies.value.filter(m => m.id !== item.id)
      totalItems.value--
      const newTotalPages = Math.ceil(totalItems.value / pageSize) || 1
      totalPages.value = newTotalPages

      // If there is cached data for the next page, pull the first item to refill current page
      if (nextPageCache.value.length > 0) {
        const [fillItem, ...rest] = nextPageCache.value
        movies.value.push(fillItem)
        nextPageCache.value = rest
        // Cache is now missing one element – mark dirty so next navigation refetches
        nextPageDirty.value = true
      } else if (currentPage.value >= newTotalPages && currentPage.value > 0) {
        // Current page became empty and we are not on the first page → go back
        await fetchList(newTotalPages - 1)
      }

      return true
    } catch (err: any) {
      handleError(err)
      return false
    } finally {
      isLoading.value = false
    }
  }

  // ── Expose API (same return values as original) ────────────────────────
  return {
    movies,
    genresList,
    isLoading,
    fieldErrors,
    globalErrors,
    currentPage,
    totalPages,
    totalItems,
    pageSize,      // added for consistency, but original didn't expose it – optional
    fetchList,
    fetchGenres,
    create,
    save,
    remove,
    goToPage,
  }
}