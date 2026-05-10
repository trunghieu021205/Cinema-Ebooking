import { ref, readonly, computed } from 'vue'
import { earningRuleApi } from '@/api/earning-rule.api'
import type { EarningRuleResponse, CreateEarningRuleRequest, UpdateEarningRuleRequest } from '@/types/earning-rule'

interface ApiRejected {
  fieldErrors: Record<string, string>
  globalErrors: string[]
  message: string
}

export function useEarningRule(tierId: number) {
  const allRules = ref<EarningRuleResponse[]>([])
  const isLoading = ref(false)
  const fieldErrors = ref<Record<string, string>>({})
  const globalErrors = ref<string[]>([])

  const rules = computed(() =>
    allRules.value.filter((r) => r.tierId === tierId)
  )

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

  async function fetchAll() {
    isLoading.value = true
    clearErrors()
    try {
      allRules.value = await earningRuleApi.getAll()
    } catch (err) {
      handleError(err)
    } finally {
      isLoading.value = false
    }
  }

  async function create(body: CreateEarningRuleRequest): Promise<boolean> {
    clearErrors()
    try {
      const created = await earningRuleApi.create(body)
      allRules.value.push(created)
      return true
    } catch (err) {
      handleError(err)
      return false
    }
  }

  async function save(item: EarningRuleResponse): Promise<boolean> {
    clearErrors()
    const body: UpdateEarningRuleRequest = {
      tierId: item.tierId,
      earningType: item.earningType,
      multiplier: item.multiplier,
      fixedPoints: item.fixedPoints,
      description: item.description,
      conditions: item.conditions,
      active: item.active,
      priority: item.priority,
    }
    try {
      const updated = await earningRuleApi.update(item.id, body)
      const idx = allRules.value.findIndex((r) => r.id === updated.id)
      if (idx !== -1) allRules.value[idx] = updated
      return true
    } catch (err) {
      handleError(err)
      return false
    }
  }

  async function remove(item: EarningRuleResponse): Promise<boolean> {
    clearErrors()
    try {
      await earningRuleApi.delete(item.id)
      allRules.value = allRules.value.filter((r) => r.id !== item.id)
      return true
    } catch (err) {
      handleError(err)
      return false
    }
  }

  return {
    rules,
    isLoading: readonly(isLoading),
    fieldErrors: readonly(fieldErrors),
    globalErrors: readonly(globalErrors),
    fetchAll,
    create,
    save,
    remove,
  }
}