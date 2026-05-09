import { ref, readonly, watch, computed } from 'vue'
import { layoutApi } from '@/api/layout.api'
import type { RoomLayoutResponse } from '@/types/seat'

export function useRoomLayout() {
  const layout = ref<RoomLayoutResponse | null>(null)
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  const layoutVersions = ref<RoomLayoutSummaryResponse[]>([])        
  const selectedVersionId = ref<number | null>(null)
  const selectedVersion = ref<RoomLayoutSummaryResponse | null>(null) 

  const selectedRoomType = ref<string>('TYPE_2D')

  const today = computed(() => {
    const d = new Date();
    const year = d.getFullYear();
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  });

  const viewDate = ref('')  

  watch([selectedVersionId, layoutVersions], ([id, versions]) => {
    if (id && versions.length) {
      selectedVersion.value = versions.find(v => v.id === id) ?? null
    } else {
      selectedVersion.value = null
    }
  }, { immediate: true })

  watch(layout, (newLayout) => {
  if (newLayout) {
    selectedRoomType.value = newLayout.roomType
  }
  })
  
  async function fetchLayout(roomId: number, date?: string) {
    isLoading.value = true
    error.value = null
    try {
      const res = await layoutApi.getLayout(roomId, date)
      layout.value = res
      viewDate.value = date ?? today.value
    } catch (err: any) {
      error.value = err.message ?? 'Không thể tải layout phòng'
    } finally {
      isLoading.value = false
    }
  }

  async function fetchLayoutHistory(roomId: number) {
    try {
      const res = await layoutApi.getLayoutHistory(roomId)
      layoutVersions.value = Array.isArray(res) ? res : []
    } catch (err) {
      console.error('Failed to load layout history', err)
      layoutVersions.value = []
    }
  }

  // đồng bộ selectedVersionId dựa trên layout hiện tại
  function syncSelectedVersion() {
    if (!layout.value || !layoutVersions.value.length) return
    const found = layoutVersions.value.find(v => v.id === layout.value!.id)
    selectedVersionId.value = found?.id ?? layoutVersions.value[0]?.id ?? null
  }

  // tải layout theo ngày được chọn từ date input
  async function fetchLayoutByDate(roomId: number, date: string) {
    await fetchLayout(roomId, date)
  }

  // reset về layout hôm nay
  async function fetchCurrentLayout(roomId: number) {
    await fetchLayout(roomId, today.value)
    viewDate.value = today.value          
    syncSelectedVersion()   
  }

  // xử lý khi người dùng đổi ngày
  async function onViewDateChange(roomId: number) {
    if (viewDate.value) {
      await fetchLayout(roomId, viewDate.value)
      syncSelectedVersion()
    } else {
      await fetchLayout(roomId)
    }
  }

  // chuyển đến phiên bản được chọn trong dropdown
  async function goToVersion(roomId: number) {
    const selected = layoutVersions.value.find(v => v.id === selectedVersionId.value)
    if (selected) {
      viewDate.value = selected.effectiveDate
      await fetchLayout(roomId, selected.effectiveDate)
    }
  }

  const latestLayout = computed(() => {
    if (!layoutVersions.value.length) return null
    const sorted = [...layoutVersions.value].sort((a, b) => b.layoutVersion - a.layoutVersion)
    return sorted[0]
  })

  const isLatestLayout = computed(() => {
    return layout.value?.id === latestLayout.value?.id
  })

  return {
    layout: readonly(layout),
    isLoading: readonly(isLoading),
    error: readonly(error),
    fetchLayout,

    layoutVersions: readonly(layoutVersions),
    viewDate,
    selectedVersionId,
    selectedVersion: readonly(selectedVersion),
    latestLayout: readonly(latestLayout),
    isLatestLayout: readonly(isLatestLayout),
    today: readonly(today),
    fetchLayoutHistory,
    fetchLayoutByDate,
    fetchCurrentLayout,
    onViewDateChange,
    goToVersion,
    syncSelectedVersion,

    selectedRoomType,
  }
}