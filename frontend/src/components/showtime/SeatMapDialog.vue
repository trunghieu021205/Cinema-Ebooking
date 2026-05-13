<!-- components/showtime/SeatMapDialog.vue -->
<template>
    <Teleport to="body">
        <div v-if="isOpen" class="fixed inset-0 z-50 overflow-y-auto flex justify-center">
            <!-- Backdrop -->
            <div class="fixed inset-0 bg-black/50 transition-opacity" />

            <!-- Dialog panel -->
            <div class="relative h-fit w-fit max-w-5xl rounded-2xl bg-white shadow-xl">
                <!-- Header -->
                <div class="flex items-center justify-between border-b px-6 py-4">
                    <div>
                        <h3 class="text-lg font-semibold text-gray-900">
                            Sơ đồ ghế - Suất chiếu #{{ showtimeId }}
                        </h3>
                        <p class="text-sm text-gray-500">
                            Tổng: {{ stats.total }} | Đã đặt: {{ stats.booked }} | Đã khóa: {{ stats.locked }} |
                            Trống: {{ stats.available }}
                        </p>
                    </div>
                    <button @click="close" class="rounded-full p-1 hover:bg-gray-100">
                        <X class="h-5 w-5 text-gray-500" />
                    </button>
                </div>

                <!-- Content -->
                <div class="p-6">
                    <div v-if="isLoading" class="flex justify-center py-12">
                        <div class="h-8 w-8 animate-spin rounded-full border-4 border-accent border-t-transparent" />
                    </div>

                    <div v-else-if="error" class="rounded-lg bg-red-50 p-4 text-center text-red-600">
                        {{ error }}
                    </div>

                    <div v-else-if="seatLayout" class="overflow-x-auto">
                        <SeatGrid :layout="seatLayout" :config="adminSeatGridConfig" :booked-ids="bookedIds"
                            :locked-ids="lockedIds" disabled :show-booking-legend="true" />
                    </div>
                </div>
            </div>
        </div>
    </Teleport>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import { X } from 'lucide-vue-next'
import SeatGrid from '@/components/seat/SeatGrid.vue'
import { adminSeatGridConfig } from '@/components/seat/seatGridConfig'
import { showtimeApi } from '@/api/showtime.api'
import type { ShowtimeSeatLayoutResponse, ShowtimeSeatResponse } from '@/types/showtime-seat'
import type { RoomLayoutResponse, SeatResponse } from '@/types/seat'

const props = defineProps<{
    showtimeId: number | null
    isOpen: boolean
}>()

const emit = defineEmits<{ (e: 'update:isOpen', value: boolean): void }>()

const isLoading = ref(false)
const error = ref<string | null>(null)
const seatLayoutData = ref<ShowtimeSeatLayoutResponse | null>(null)

// Convert ShowtimeSeatResponse to SeatResponse expected by SeatGrid
function toSeatResponse(seat: ShowtimeSeatResponse): SeatResponse {
    return {
        id: seat.roomLayoutSeatId,        // use roomLayoutSeatId as stable identifier
        seatTypeId: seat.seatTypeId,
        status: seat.active ? seat.status : 'INACTIVE',  // treat inactive seats as a special status
        rowIndex: seat.rowIndex,
        colIndex: seat.colIndex,
        label: seat.seatNumber,
    } as SeatResponse
}

const seatLayout = computed<RoomLayoutResponse | null>(() => {
    if (!seatLayoutData.value) return null

    const data = seatLayoutData.value
    const rows = data.rows.map(row =>
        row.map(seat => seat !== null ? toSeatResponse(seat) : null)
    )
    console.log("rows after mapping to SeatResponse:", rows)  // Debug log
    return {
        rows,
        totalRows: data.totalRows,
        totalCols: data.totalCols,
    } as RoomLayoutResponse
})

const bookedIds = computed(() => {
    if (!seatLayoutData.value) return []
    return seatLayoutData.value.rows
        .flat()
        .filter((seat): seat is ShowtimeSeatResponse => seat !== null)
        .filter(seat => seat.status === 'BOOKED')
        .map(seat => seat.roomLayoutSeatId)
})

const lockedIds = computed(() => {
    if (!seatLayoutData.value) return []
    return seatLayoutData.value.rows
        .flat()
        .filter((seat): seat is ShowtimeSeatResponse => seat !== null)
        .filter(seat => seat.status === 'LOCKED')
        .map(seat => seat.roomLayoutSeatId)
})

const stats = computed(() => {
    if (!seatLayoutData.value) return { total: 0, booked: 0, locked: 0, available: 0 }
    const allSeats = seatLayoutData.value.rows.flat().filter((s): s is ShowtimeSeatResponse => s !== null)
    const total = allSeats.length
    const booked = allSeats.filter(s => s.status === 'BOOKED').length
    const locked = allSeats.filter(s => s.status === 'LOCKED').length
    const available = allSeats.filter(s => s.status === 'AVAILABLE').length
    return { total, booked, locked, available }
})

async function fetchSeatMap() {
    if (!props.showtimeId) return
    isLoading.value = true
    error.value = null
    try {
        const res = await showtimeApi.getSeatMap(props.showtimeId)
        console.log('Fetched seat map:', res)
        seatLayoutData.value = res
    } catch (err: any) {
        error.value = err.message || 'Không thể tải sơ đồ ghế'
        seatLayoutData.value = null
    } finally {
        isLoading.value = false
    }
}

function close() {
    emit('update:isOpen', false)
}

watch(
    () => [props.isOpen, props.showtimeId],
    ([isOpen]) => {
        if (isOpen && props.showtimeId) {
            fetchSeatMap()
        } else {
            seatLayoutData.value = null
            error.value = null
        }
    },
    { immediate: true }
)
</script>