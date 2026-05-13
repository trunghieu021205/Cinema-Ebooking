import { ref, readonly } from 'vue'
import { membershipTierApi } from '@/api/membership-tier.api'
import type { MembershipTierResponse, CreateMembershipTierRequest, UpdateMembershipTierRequest } from '@/types/membership-tier'

interface ApiRejected {
  fieldErrors: Record<string, string>
  globalErrors: string[]
  message: string
}

export function useMembershipTier() {
  const tiers = ref<MembershipTierResponse[]>([])
  const isLoading = ref(false)
  const fieldErrors = ref<Record<string, string>>({})
  const globalErrors = ref<string[]>([])

  const currentPage = ref(0)
  const totalPages = ref(0)
  const totalItems = ref(0)
  const pageSize = 8

  const nextPageCache = ref<MembershipTierResponse[]>([])
  const nextPageDirty = ref(false)

  function handleError(err: unknown) {
    const e = err as ApiRejected
    fieldErrors.value = e.fieldErrors ?? {}
    if (e.globalErrors?.length) {
      globalErrors.value = e.globalErrors
    } else if (!Object.values(fieldErrors.value).some(Boolean)) {
      globalErrors.value = [e.message ?? 'Đã có lỗi xảy ra']
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
      const res = await membershipTierApi.getList(page, pageSize)
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
      const res = await membershipTierApi.getList(page, pageSize)
      tiers.value = res.content
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
      tiers.value = nextPageCache.value
      currentPage.value = page
      nextPageCache.value = []
      prefetchNextPage(page + 1)
    } else {
      await fetchList(page)
    }
  }

  async function create(body: CreateMembershipTierRequest): Promise<boolean> {
    clearErrors()
    try {
      const created = await membershipTierApi.create(body)
      tiers.value.unshift(created)
      totalItems.value++
      totalPages.value = Math.ceil(totalItems.value / pageSize)
      if (tiers.value.length > pageSize) {
        tiers.value.pop()
      }
      nextPageCache.value = []
      prefetchNextPage(currentPage.value + 1)
      return true
    } catch (err) {
      handleError(err)
      return false
    }
  }

  async function save(item: MembershipTierResponse): Promise<boolean> {
    clearErrors()
    const body: UpdateMembershipTierRequest = {
      name: item.name,
      minSpendingRequired: item.minSpendingRequired,
      discountPercent: item.discountPercent,
      benefitsDescription: item.benefitsDescription,
      tierLevel: item.tierLevel,
    }
    try {
      const updated = await membershipTierApi.update(item.id, body)
      const idx = tiers.value.findIndex((t) => t.id === updated.id)
      if (idx !== -1) tiers.value[idx] = updated
      return true
    } catch (err) {
      handleError(err)
      return false
    }
  }

  async function remove(item: MembershipTierResponse): Promise<boolean> {
    clearErrors()
    try {
      await membershipTierApi.delete(item.id)
      tiers.value = tiers.value.filter((t) => t.id !== item.id)
      totalItems.value--
      const newTotalPages = Math.ceil(totalItems.value / pageSize) || 1
      totalPages.value = newTotalPages
      if (nextPageCache.value.length > 0) {
        const [fillItem, ...rest] = nextPageCache.value
        tiers.value.push(fillItem)
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
    tiers: readonly(tiers),
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
    remove,
  }
}