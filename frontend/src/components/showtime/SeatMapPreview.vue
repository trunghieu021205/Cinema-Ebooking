<template>
    <div class="mt-4 border-t pt-4">
        <h4 class="text-sm font-medium text-text-admin-primary mb-2">Sơ đồ phòng tại thời điểm chiếu</h4>

        <!-- Chưa chọn ngày -->
        <div v-if="!startDate" class="text-center py-4 text-sm text-slate-400 italic">
            Vui lòng chọn ngày chiếu để xem sơ đồ phòng
        </div>

        <!-- Đã có đủ dữ liệu -->
        <template v-else>
            <div v-if="isLoading" class="flex justify-center py-6">
                <div class="h-6 w-6 animate-spin rounded-full border-2 border-slate-300 border-t-slate-600" />
            </div>
            <div v-else-if="error" class="text-center py-4 text-sm text-red-500">
                {{ error }}
            </div>
            <div v-else-if="!layout" class="text-center py-4 text-sm text-slate-400 italic">
                {{ noDataMessage }}
            </div>
            <SeatGrid v-else :layout="layout" :config="previewSeatGridConfig" :disabled="true" />
        </template>
    </div>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import SeatGrid from '@/components/seat/SeatGrid.vue'
import { previewSeatGridConfig } from '@/components/seat/seatGridConfig'
import { layoutApi } from '@/api/layout.api'
import type { RoomLayoutResponse } from '@/types/seat'

const props = defineProps<{
    roomId: number | null | undefined
    startDate?: string | null
}>()

const isLoading = ref(false)
const error = ref<string | null>(null)
const layout = ref<RoomLayoutResponse | null>(null)

const formattedDate = computed(() => {
    if (!props.startDate) return undefined
    const date = new Date(props.startDate)
    if (isNaN(date.getTime())) return undefined
    return date.toISOString().split('T')[0]
})

const noDataMessage = computed(() => {
    if (!props.roomId) return 'Chưa chọn phòng'
    if (!props.startDate) return 'Chưa có ngày chiếu'
    return `Chưa có layout cho ngày ${formattedDate.value}`
})

async function fetchLayout() {
    // ⭐ Chỉ fetch khi có cả roomId và startDate
    if (!props.roomId || !props.startDate) {
        layout.value = null
        error.value = null
        return
    }

    isLoading.value = true
    error.value = null
    try {
        // Giả sử response đã unwrap thành layout object trực tiếp
        const response = await layoutApi.getLayout(props.roomId, formattedDate.value)
        // Nếu response đã là layout (có rows)
        if (response && response.rows) {
            layout.value = response
        }
        // Nếu response có dạng { success, data } và interceptor chưa unwrap hết
        else if (response && response.data && response.data.rows) {
            layout.value = response.data
        }
        else {
            throw new Error('Invalid layout response')
        }
    } catch (err: any) {
        console.error(err)
        if (err.response?.status === 404) {
            error.value = `Chưa có layout cho ngày ${formattedDate.value}`
        } else {
            error.value = 'Không thể tải sơ đồ phòng'
        }
        layout.value = null
    } finally {
        isLoading.value = false
    }
}

// Watch cả roomId và startDate
watch(() => [props.roomId, props.startDate], () => {
    fetchLayout()
}, { immediate: true })
</script> phần thông báo header sơ đồ phòng tại thời điểm chiếu bị render lại chọn phòng mà chưa chọn thời gian