import { ref } from 'vue'
import { genreApi } from '@/api/genre.api'
import type { GenreResponse, CreateGenreRequest } from '@/types/genre.types'
import { mapFieldErrors } from '@/utils/errorMapper'

export function useGenre() {
  const genres = ref<GenreResponse[]>([])
  const isLoading = ref(false)
  const fieldErrors = ref<Record<string, string>>({})
  const globalErrors = ref<string[]>([])
  const currentPage = ref(0)
  const totalPages = ref(0)
  const totalItems = ref(0)

  const fetchList = async (page = 0, size = 8) => {
    isLoading.value = true
    fieldErrors.value = {}
    globalErrors.value = []
    try {
      const response = await genreApi.getList(page, size)
      genres.value = response.content
      totalItems.value = response.totalElements
      totalPages.value = response.totalPages
      currentPage.value = response.number
    } catch (err: any) {
      if (err.fieldErrors) fieldErrors.value = err.fieldErrors
      if (err.globalErrors) globalErrors.value = err.globalErrors
    } finally {
      isLoading.value = false
    }
  }

  const create = async (payload: CreateGenreRequest) => {
    isLoading.value = true
    fieldErrors.value = {}
    globalErrors.value = []
    try {
      await genreApi.create(payload)
      await fetchList(currentPage.value) // reload trang hiện tại
      return true
    } catch (err: any) {
      if (err.fieldErrors) fieldErrors.value = err.fieldErrors
      if (err.globalErrors) globalErrors.value = err.globalErrors
      return false
    } finally {
      isLoading.value = false
    }
  }

  const save = async (item: GenreResponse) => {
    isLoading.value = true
    fieldErrors.value = {}
    globalErrors.value = []
    try {
      await genreApi.update(item.id, { name: item.name })
      await fetchList(currentPage.value)
      return true
    } catch (err: any) {
      if (err.fieldErrors) fieldErrors.value = err.fieldErrors
      if (err.globalErrors) globalErrors.value = err.globalErrors
      return false
    } finally {
      isLoading.value = false
    }
  }

  const remove = async (item: GenreResponse) => {
    isLoading.value = true
    try {
      await genreApi.delete(item.id)
      // Nếu trang hiện tại chỉ còn 1 phần tử và không phải trang đầu thì lùi lại
      const shouldGoPrev = genres.value.length === 1 && currentPage.value > 0
      const newPage = shouldGoPrev ? currentPage.value - 1 : currentPage.value
      await fetchList(newPage)
      return true
    } catch (err: any) {
      if (err.fieldErrors) fieldErrors.value = err.fieldErrors
      if (err.globalErrors) globalErrors.value = err.globalErrors
      return false
    } finally {
      isLoading.value = false
    }
  }

  const goToPage = (page: number) => {
    if (page >= 0 && page < totalPages.value) {
      fetchList(page)
    }
  }

  return {
    genres,
    isLoading,
    fieldErrors,
    globalErrors,
    currentPage,
    totalPages,
    totalItems,
    fetchList,
    create,
    save,
    remove,
    goToPage,
  }
}