<template>
    <div class="flex flex-col gap-6 py-6 min-h-screen">

        <!-- Breadcrumb -->
        <div class="flex items-center gap-2 pr-6 text-sm">
            <button class="text-text-admin-tertiary transition-colors hover:text-text-admin-primary"
                @click="router.push('/admin/cinemas')">
                Cinemas
            </button>
            <ChevronRight class="size-3.5 text-text-admin-tertiary" />
            <button class="text-text-admin-tertiary transition-colors hover:text-text-admin-primary"
                @click="router.push(`/admin/cinemas/${cinemaId}/rooms`)">
                {{ cinemaName }}
            </button>
            <ChevronRight class="size-3.5 text-text-admin-tertiary" />
            <button class="text-text-admin-tertiary transition-colors hover:text-text-admin-primary"
                @click="router.push(`/admin/cinemas/${cinemaId}/rooms`)">
                Rooms
            </button>
            <ChevronRight class="size-3.5 text-text-admin-tertiary" />
            <span class="text-text-admin-primary font-medium">{{ roomName }} — Layout</span>
        </div>

        <!-- Header -->
        <div class="flex items-center justify-between pr-6">
            <div>
                <h1 class="text-lg font-semibold text-text-admin-primary">Sơ đồ ghế</h1>
                <p v-if="layout" class="text-sm text-text-admin-tertiary">
                    {{ layout.totalRows }} hàng × {{ layout.totalCols }} cột
                </p>
            </div>
        </div>

        <!-- Error -->
        <div v-if="error" class="pr-6 rounded-lg bg-red-50 border border-red-100 p-4">
            <p class="text-sm text-red-600">{{ error }}</p>
        </div>

        <!-- Loading skeleton -->
        <div v-else-if="isLoading" class="pr-6 flex flex-col items-center gap-3">
            <div class="h-6 w-32 animate-pulse rounded bg-slate-100" />
            <div v-for="r in 6" :key="r" class="flex gap-2">
                <div v-for="c in 10" :key="c" class="h-9 w-9 animate-pulse rounded-md bg-slate-100" />
            </div>
        </div>

        <!-- Grid và Panel (cùng hàng, flex) -->
        <div v-else-if="layout" class="relative pr-6">
            <div class="flex gap-6">
                <!-- Seat map container (để gắn drag-select) -->
                <div ref="gridContainer" class="flex-1 overflow-x-auto">
                    <SeatGrid :layout="layout" :config="adminSeatGridConfig" :selected-ids="Array.from(selectedIds)"
                        @seat-click="handleSeatClick" />
                </div>

                <!-- Panel chỉnh sửa (hiện khi có ghế chọn) -->
                <AdminSeatPanel v-if="selectedIds.size > 0" :room-id="roomId" :selected-seat-ids="selectedIds"
                    @update-success="refreshAfterUpdate" @clear-selection="clearSelection" />
            </div>
        </div>

    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ChevronRight } from 'lucide-vue-next'
import SeatGrid from '@/components/seat/SeatGrid.vue'
import AdminSeatPanel from '@/components/seat/AdminSeatPanel.vue'
import { adminSeatGridConfig } from '@/components/seat/seatGridConfig'
import { useRoomLayout } from '@/composables/useRoomLayout'
import { useSeatSelection } from '@/composables/useSeatSelection'
import { roomApi } from '@/api/room.api'
import { cinemaApi } from '@/api/cinema.api'
import type { SeatResponse } from '@/types/seat'

const route = useRoute()
const router = useRouter()
const cinemaId = Number(route.params.cinemaId)
const roomId = Number(route.params.roomId)

const cinemaName = ref('...')
const roomName = ref('...')

cinemaApi.getById(cinemaId)
    .then((c) => { cinemaName.value = c.name })
    .catch(() => { cinemaName.value = `Cinema #${cinemaId}` })

roomApi.getById(roomId)
    .then((r) => { roomName.value = r.name })
    .catch(() => { roomName.value = `Phòng #${roomId}` })

// Layout data
const { layout, isLoading, error, fetchLayout } = useRoomLayout()

// Selection logic
const { selectedIds, toggleSeat, clearSelection, initDragSelect } = useSeatSelection()
const gridContainer = ref<HTMLElement | null>(null)
let cleanupDrag: (() => void) | undefined

// Xử lý click ghế (truyền event để hỗ trợ Ctrl+Click)
function handleSeatClick(seat: SeatResponse, event: MouseEvent) {
    toggleSeat(seat, event)
}

// Sau khi cập nhật thành công (bulk update)
async function refreshAfterUpdate() {
    await fetchLayout(roomId)
    clearSelection()
}

// Khởi tạo drag-select khi grid được render
function setupDragSelect() {
    if (gridContainer.value) {
        // Tìm phần tử bên trong chứa lưới ghế (div .inline-flex.flex-col do SeatGrid sinh ra)
        const seatGridInner = gridContainer.value.querySelector('.inline-flex.flex-col') as HTMLElement
        if (seatGridInner && !cleanupDrag) {
            cleanupDrag = initDragSelect(seatGridInner)
        }
    }
}

onMounted(async () => {
    await fetchLayout(roomId)
    // Đợi DOM update rồi gắn drag
    await new Promise(resolve => setTimeout(resolve, 0))
    setupDragSelect()
})

// Khi layout thay đổi (sau update), cần gắn lại drag-select vì DOM có thể bị thay thế
// Dùng watch để theo dõi layout
import { watch } from 'vue'
watch(layout, () => {
    // Delay nhỏ để DOM render xong
    setTimeout(() => {
        if (cleanupDrag) {
            cleanupDrag()
            cleanupDrag = undefined
        }
        setupDragSelect()
    }, 0)
})

// Cleanup khi component unmount
onUnmounted(() => {
    if (cleanupDrag) cleanupDrag()
})
</script>