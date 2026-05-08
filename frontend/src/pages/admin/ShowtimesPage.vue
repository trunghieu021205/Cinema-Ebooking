<template>
    <div class="flex flex-col gap-6 py-6">
        <!-- Header -->
        <div class="flex items-center justify-between">
            <div>
                <h1 class="text-lg font-semibold text-text-admin-primary">Quản lý suất chiếu</h1>
                <p class="text-sm text-text-admin-tertiary">{{ totalItems }} suất chiếu</p>
            </div>
        </div>


        <!-- Global errors -->
        <div v-if="globalErrors.length" class="pr-6 rounded-lg bg-red-50 border border-red-100 p-4">
            <p v-for="err in globalErrors" :key="err" class="text-sm text-red-600">{{ err }}</p>
        </div>

        <!-- Loading skeleton -->
        <div v-if="isLoading && !showtimes.length" class="pr-6 space-y-2">
            <div v-for="i in 5" :key="i" class="h-12 animate-pulse rounded-xl bg-slate-100" />
        </div>

        <!-- Data Table -->
        <DataTable :rows="showtimes" :columns="columns" createLabel="Thêm suất chiếu" :fieldErrors="fieldErrors"
            @create="openCreateModal" @save="handleSave" @delete="handleCancel" @row-select="selectedShowtime = $event">
            <template #detail-actions="{ item }">
                <button
                    class="flex w-full items-center justify-center gap-2 rounded-lg border border-slate-200 py-2.5 text-sm text-slate-600 transition-colors hover:border-slate-300 hover:bg-slate-50 hover:text-slate-900"
                    @click="viewSeatMap(item)">
                    <Armchair class="size-4" />
                    Xem sơ đồ ghế
                </button>
            </template>

            <!-- Custom cell for status (show badge) -->
            <template #cell-status="{ value }">
                <span class="inline-flex rounded-full px-2 py-0.5 text-xs font-medium" :class="{
                    'bg-yellow-100 text-yellow-800': value === 'SCHEDULED',
                    'bg-blue-100 text-blue-800': value === 'ONGOING',
                    'bg-green-100 text-green-800': value === 'FINISHED',
                    'bg-red-100 text-red-800': value === 'CANCELLED',
                }">
                    {{ statusLabel(value) }}
                </span>
            </template>
        </DataTable>

        <!-- Pagination -->
        <div v-if="totalPages > 1" class="flex justify-center gap-1.5 pr-6">
            <button v-for="page in totalPages" :key="page" class="rounded-lg px-3 py-1.5 text-sm transition-colors"
                :class="currentPage === page - 1
                    ? 'bg-accent text-text-on-accent font-medium'
                    : 'text-text-admin-secondary hover:bg-slate-100'" @click="goToPage(page - 1)">
                {{ page }}
            </button>
        </div>

        <!-- Create Modal -->
        <CreateModal v-model="showCreateModal" title="Thêm suất chiếu" submitLabel="Tạo suất chiếu"
            :columns="createColumns" :isLoading="isLoading" :fieldErrors="fieldErrors" @submit="handleCreate" />
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ChevronRight, Armchair } from 'lucide-vue-next'
import DataTable from '@/components/common/table/DataTable.vue'
import CreateModal from '@/components/common/table/subcomponents/CreateModal.vue'
import { useShowtime } from '@/composables/useShowtime'
import { cinemaApi } from '@/api/cinema.api'
import { roomApi } from '@/api/room.api'
import { movieApi } from '@/api/movie.api'
import type { ShowtimeResponse, CreateShowtimeRequest } from '@/types/showtime'
import type { ColumnDef } from '@/components/common/table/types/table'

const route = useRoute()
const router = useRouter()
const cinemaId = Number(route.params.cinemaId)

const {
    showtimes, isLoading, fieldErrors, globalErrors,
    currentPage, totalPages, totalItems,
    fetchList, goToPage, create, save, cancel,
} = useShowtime(cinemaId)

const showCreateModal = ref(false)
const selectedShowtime = ref<ShowtimeResponse | null>(null)

// Hardcoded format options (id -> label)
const formatOptions = [
    { value: 1, label: '2D' },
    { value: 2, label: '3D' },
    { value: 3, label: 'IMAX' },
]

// Column definitions for table (read‑only display)
const columns: ColumnDef<ShowtimeResponse>[] = [
    { key: 'id', label: 'ID', type: 'number', readonly: true, hideInCreate: true },
    { key: 'movieId', label: 'Phim', type: 'relation', readonlyInEdit: true, hideInCreate: true },
    { key: 'roomId', label: 'Phòng', type: 'relation', readonlyInEdit: true, hideInCreate: true },
    { key: 'formatId', label: 'Định dạng', type: 'relation', readonlyInEdit: true, hideInCreate: true },
    { key: 'startTime', label: 'Bắt đầu', type: 'datetime' },
    { key: 'endTime', label: 'Kết thúc', type: 'datetime' },
    { key: 'audioLanguage', label: 'Ngôn ngữ âm thanh', type: 'text' },
    { key: 'subtitleLanguage', label: 'Ngôn ngữ phụ đề', type: 'text' },
    { key: 'status', label: 'Trạng thái', type: 'text', readonly: true, hideInCreate: true },
]

// Columns for CREATE modal (with dropdowns)
const createColumns: ColumnDef<Partial<CreateShowtimeRequest>>[] = [
    { key: 'movieId', label: 'Phim', type: 'select', options: () => fetchMovies() },
    { key: 'roomId', label: 'Phòng', type: 'select', options: () => fetchRooms() },
    // Hardcoded format options – no API call
    { key: 'formatId', label: 'Định dạng', type: 'select', options: () => Promise.resolve(formatOptions) },
    { key: 'startTime', label: 'Thời gian bắt đầu', type: 'datetime-local' },
    { key: 'endTime', label: 'Thời gian kết thúc', type: 'datetime-local' },
    { key: 'audioLanguage', label: 'Ngôn ngữ âm thanh', type: 'text' },
    { key: 'subtitleLanguage', label: 'Ngôn ngữ phụ đề', type: 'text' },
]

// Helper to load movies for select
async function fetchMovies() {
    const res = await movieApi.getAll({ page: 0, size: 100 })
    return res.content.map(m => ({ value: m.id, label: m.title }))
}

// Helper to load rooms for select
async function fetchRooms() {
    const res = await roomApi.getListByCinema(cinemaId, 0, 100)
    return res.content.map(r => ({ value: r.id, label: r.name }))
}

function statusLabel(status: string) {
    const map: Record<string, string> = {
        SCHEDULED: 'Sắp chiếu',
        ONGOING: 'Đang chiếu',
        FINISHED: 'Đã kết thúc',
        CANCELLED: 'Đã hủy',
    }
    return map[status] ?? status
}

function openCreateModal() {
    showCreateModal.value = true
}

async function handleCreate(draft: Record<string, unknown>) {
    const ok = await create({
        movieId: Number(draft.movieId),
        roomId: Number(draft.roomId),
        formatId: Number(draft.formatId),
        startTime: String(draft.startTime),
        endTime: String(draft.endTime),
        audioLanguage: String(draft.audioLanguage),
        subtitleLanguage: String(draft.subtitleLanguage),
    })
    if (ok) showCreateModal.value = false
}

async function handleSave(item: ShowtimeResponse) {
    await save(item)
}

// Called when user clicks "Hủy" on a row
async function handleCancel(item: ShowtimeResponse) {
    if (item.status === 'CANCELLED') {
        alert('Suất chiếu đã bị hủy trước đó')
        return
    }
    if (confirm(`Hủy suất chiếu #${item.id}?`)) {
        await cancel(item)
    }
}

// Placeholder for seat map view
function viewSeatMap(showtime: ShowtimeResponse) {
    alert(`Xem sơ đồ ghế cho suất chiếu #${showtime.id} (tính năng đang phát triển)`)
}

onMounted(() => fetchList(0))
</script>