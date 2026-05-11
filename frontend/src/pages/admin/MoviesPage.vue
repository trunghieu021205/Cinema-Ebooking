<template>
    <div class="flex flex-col gap-6 py-6">
        <div class="flex flex-col gap-2 pr-6">
            <div class="flex items-center text-sm">
                <span class="text-text-admin-primary font-medium">Phim</span>
            </div>
            <div class="flex items-center justify-between">
                <div>
                    <h1 class="text-lg font-semibold text-text-admin-primary">Quản lý phim</h1>
                    <p class="text-sm text-text-admin-tertiary">{{ totalItems }} phim</p>
                </div>
            </div>
        </div>

        <!-- Global errors -->
        <div v-if="globalErrors.length" class="pr-6 rounded-lg bg-red-50 border border-red-100 p-4">
            <p v-for="err in globalErrors" :key="err" class="text-sm text-red-600">{{ err }}</p>
        </div>

        <!-- Table -->
        <DataTable :rows="movies" :columns="allColumns" createLabel="Thêm phim" :fieldErrors="fieldErrors"
            @create="showCreate = true" @delete="handleDelete" @save="handleSave" />

        <!-- Pagination -->
        <div v-if="totalPages > 1" class="flex justify-center gap-1.5 pr-6">
            <button v-for="page in totalPages" :key="page" class="rounded-lg px-3 py-1.5 text-sm transition-colors"
                :class="currentPage === page - 1
                    ? 'bg-accent text-text-on-accent font-medium'
                    : 'text-text-admin-secondary hover:bg-slate-100'" @click="goToPage(page - 1)">
                {{ page }}
            </button>
        </div>

        <!-- CreateModal -->
        <CreateModal v-model="showCreate" title="Thêm phim mới" :columns="createColumns" :isLoading="isLoading"
            :fieldErrors="fieldErrors" @submit="handleCreate" />
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import DataTable from '@/components/common/table/DataTable.vue'
import CreateModal from '@/components/common/table/subcomponents/CreateModal.vue'
import { useMovie } from '@/composables/useMovie'
import type { MovieResponse, CreateMovieRequest } from '@/types/movie'
import type { ColumnDef } from '@/components/common/table/types/table'

const {
    movies, genresList, isLoading, fieldErrors, globalErrors,
    currentPage, totalPages, totalItems,
    fetchList, fetchGenres, goToPage, create, save, remove
} = useMovie()

const showCreate = ref(false)

const baseColumns: ColumnDef<MovieResponse>[] = [
    {
        key: 'id',
        label: 'ID',
        type: 'number',
        readonly: true,
        hideInCreate: true,
        hideInTable: true
    },
    {
        key: 'title',
        label: 'Tiêu đề',
        type: 'text',
        required: true,
        width: '220px'
    },
    {
        key: 'description',
        label: 'Mô tả',
        type: 'textarea',
        hideInTable: true
    },
    {
        key: 'duration',
        label: 'Thời lượng (phút)',
        type: 'number',
        required: true,
        width: '100px'
    },
    {
        key: 'ageRating',
        label: 'Độ tuổi',
        type: 'enum',
        options: [
            { value: 'P', label: 'P - Tất cả' },
            { value: 'T13', label: 'T13' },
            { value: 'T16', label: 'T16' },
            { value: 'T18', label: 'T18' }
        ],
        required: true,
        width: '100px'
    },
    {
        key: 'releaseDate',
        label: 'Ngày phát hành',
        type: 'date',
        required: true,
        width: '120px'
    },
    {
        key: 'status',
        label: 'Trạng thái',
        type: 'enum',
        options: [
            { value: 'COMING_SOON', label: 'Sắp chiếu' },
            { value: 'NOW_SHOWING', label: 'Đang chiếu' },
            { value: 'ENDED', label: 'Kết thúc' }
        ],
        hideInCreate: true,
        readonlyInEdit: true,
        width: '130px'
    },
    {
        key: 'posterUrl',
        label: 'Poster URL',
        type: 'text',
        hideInTable: true
    },
    {
        key: 'bannerUrl',
        label: 'Banner URL',
        type: 'text',
        hideInTable: true
    },
    {
        key: 'director',
        label: 'Đạo diễn',
        type: 'text',
        width: '160px'
    },
    {
        key: 'actors',
        label: 'Diễn viên',
        type: 'text',
        hideInTable: true
    },
]
// Cột genres – không bao giờ trả về null, chỉ có options rỗng khi chưa load
const genresColumn = computed<ColumnDef<MovieResponse>>(() => ({
    key: 'genres',
    label: 'Thể loại',
    type: 'multiselect',
    options: genresList.value.map(g => ({ value: g.id, label: g.name })),
    required: true,
}))

const allColumns = computed(() => {
    const cols = [...baseColumns]
    cols.push(genresColumn.value)
    return cols
})

const createColumns = computed(() => {
    let cols = baseColumns.filter(c => !c.hideInCreate && !c.readonly)
    cols.push({ ...genresColumn.value, readonly: false })
    return cols
})

// ========== Handlers ==========
async function handleCreate(draft: Record<string, unknown>) {
    // draft.genres là number[] (do FieldRenderer checkbox trả về)
    const payload: CreateMovieRequest = {
        title: draft.title as string,
        description: draft.description as string,
        duration: Number(draft.duration),
        ageRating: draft.ageRating as any,
        releaseDate: draft.releaseDate as string,
        posterUrl: draft.posterUrl as string,
        bannerUrl: draft.bannerUrl as string,
        director: draft.director as string,
        actors: draft.actors as string,
        genreIds: (draft.genres as number[]) || []
    }
    const ok = await create(payload)
    if (ok) showCreate.value = false
}

async function handleSave(item: MovieResponse, done: () => void) {
    const ok = await save(item as any)
    if (ok) done()
}

async function handleDelete(item: MovieResponse) {
    await remove(item)
}

// ========== Lifecycle ==========
onMounted(async () => {
    await fetchGenres()   // BẮT BUỘC chạy trước để genresList có dữ liệu
    await fetchList(0)    // Sau đó mới fetch movies
})
</script>