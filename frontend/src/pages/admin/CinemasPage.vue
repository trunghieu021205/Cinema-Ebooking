<template>
    <div>
        <div class="mb-6 mt-2 flex flex-col gap-1">
            <p class="text-body text-text-admin-secondary">
                {{ totalItems }} rạp • trang {{ currentPage + 1 }}/{{ totalPages }}
            </p>
        </div>

        <div v-if="globalErrors.length"
            class="mb-4 rounded-lg border border-red-100 bg-red-50 px-4 py-3 text-sm text-red-600">
            {{ globalErrors[0] }}
        </div>

        <div v-if="isLoading" class="flex items-center justify-center py-20 text-sm text-slate-400">
            Đang tải...
        </div>

        <DataTable v-else :rows="cinemas" :columns="columns" createLabel="Tạo rạp mới" :fieldErrors="fieldErrors"
            @create="showCreate = true" @delete="remove" @save="save" />

        <!-- Pagination — dùng goToPage thay vì fetchList -->
        <div v-if="totalPages > 1" class="mt-4 flex items-center justify-center mb-6 mr-6 gap-2">
            <button v-for="p in totalPages" :key="p" class="size-8 rounded-lg text-sm transition-colors"
                :class="p - 1 === currentPage ? 'bg-slate-900 text-white' : 'text-slate-500 hover:bg-gray-200'"
                @click="goToPage(p - 1)">{{ p }}</button>
        </div>

        <CreateModal v-model="showCreate" :columns="columns" title="Tạo rạp mới" submitLabel="Tạo rạp"
            :isLoading="isCreating" :fieldErrors="fieldErrors" @submit="onCreate" />
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import DataTable from '@/components/common/table/DataTable.vue'
import CreateModal from '@/components/common/table/subcomponents/CreateModal.vue'
import { useCinema } from '@/composables/useCinema'
import type { ColumnDef } from '@/components/common/table/types/table'
import type { CinemaResponse, CreateCinemaRequest } from '@/types/cinema'

const {
    cinemas, isLoading, fieldErrors, globalErrors,
    currentPage, totalPages, totalItems,
    fetchList, goToPage, create, save, remove,
} = useCinema()

const showCreate = ref(false)
const isCreating = ref(false)

const columns: ColumnDef<CinemaResponse>[] = [
    { key: 'name', label: 'Tên rạp', type: 'text' },
    { key: 'city', label: 'Thành phố', type: 'text' },
    { key: 'status', label: 'Trạng thái', type: 'enum', options: ['ACTIVE', 'INACTIVE'], hideInCreate: true },
    { key: 'address', label: 'Địa chỉ', type: 'textarea', hideInTable: true },
]

async function onCreate(draft: Record<string, unknown>) {
    isCreating.value = true
    const ok = await create(draft as CreateCinemaRequest)
    isCreating.value = false
    if (ok) showCreate.value = false
}

onMounted(() => fetchList())
</script>