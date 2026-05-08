<template>
    <div class="flex flex-col gap-3 pr-6">

        <!-- Create button — full width -->
        <button class="flex w-full flex-col items-center 
            justify-center gap-1 rounded-xl border border-dashed border-gray-400 py-3 
            text-text-admin-secondary transition-colors 
            hover:border-gray-600 hover:bg-gray-100 hover:text-text-admin-primary" @click="$emit('create')">
            <Plus class="size-4" />
            <span class="text-xs">{{ createLabel }}</span>
        </button>

        <!-- Table -->
        <div class="overflow-hidden rounded-xl border border-slate-100">
            <table class="w-full text-sm table-fixed">

                <!-- Header -->
                <thead>
                    <tr class="border-b border-slate-100 bg-slate-50">
                        <th v-for="col in visibleColumns" :key="col.key" :style="col.width ? { width: col.width } : {}"
                            class="px-4 py-3 text-left text-xs font-medium text-text-admin-secondary">
                            {{ col.label }}
                        </th>
                        <th class="w-10 px-4 py-3" />
                    </tr>
                </thead>

                <!-- Body -->
                <tbody class="divide-y divide-slate-50">

                    <!-- Empty state -->
                    <tr v-if="!rows.length">
                        <td :colspan="visibleColumns.length + 1" class="py-12 text-center text-sm text-slate-400">
                            Chưa có dữ liệu
                        </td>
                    </tr>

                    <!-- Data rows -->
                    <tr v-for="row in rows" :key="row.id" class="cursor-pointer group"
                        :class="{ 'bg-gray-200': selectedItem?.id === row.id }">
                        <td v-for="col in visibleColumns" :key="col.key"
                            class="px-4 py-3 text-slate-700 transition-colors group-hover:bg-gray-100"
                            :style="col.width ? { width: col.width } : {}" @click="selectedItem = row">
                            <span v-if="col.type === 'enum'"
                                class="inline-block rounded-full bg-slate-100 px-2.5 py-0.5 text-xs font-medium text-slate-600">
                                {{ row[col.key] }}
                            </span>
                            <span v-else-if="col.type === 'multiselect' && Array.isArray(row[col.key])"
                                class="flex flex-wrap gap-1">
                                <span v-for="pillItem in getPillItems(col, row)" :key="pillItem.id"
                                    class="inline-block rounded-full bg-slate-100 px-2 py-0.5 text-xs text-slate-600">
                                    {{ pillItem.name }}
                                </span>
                            </span>
                            <span v-else class="line-clamp-1">{{ row[col.key] }}</span>
                        </td>

                        <!-- Delete button -->
                        <td class="px-2 py-3">
                            <button
                                class="rounded-full p-1.5 text-text-admin-tertiary transition-colors hover:bg-overlay-light-30 hover:text-text-admin-primary"
                                @click="onDeleteClick(row)">
                                <Trash2 class="size-4" />
                            </button>
                        </td>
                    </tr>

                </tbody>
            </table>
        </div>

        <!-- Detail panel — pass-through slot 'detail-actions' → panel's 'actions' slot -->
        <DetailPanel :item="selectedItem" :columns="columns" :errors="fieldErrors" @close="selectedItem = null"
            @save="onSave">
            <!--
                Pass-through: nếu parent truyền #detail-actions thì forward xuống DetailPanel.
                Slot scope { item } là draft hiện tại trong panel (RowItem).
            -->
            <template v-if="$slots['detail-actions']" #actions="slotProps">
                <slot name="detail-actions" v-bind="slotProps" />
            </template>
        </DetailPanel>

        <!-- Confirm delete dialog -->
        <ConfirmDialog v-model="showDeleteConfirm" title="Xác nhận xóa"
            :description="`Bạn có chắc muốn xóa mục này không? Hành động này không thể hoàn tác.`" confirmLabel="Xóa"
            :danger="true" @confirm="onDeleteConfirmed" />

    </div>
</template>

<script setup lang="ts" generic="T extends RowItem">
import { ref, computed } from 'vue'
import { Plus, Trash2 } from 'lucide-vue-next'
import DetailPanel from '@/components/common/table/subcomponents/DetailPanel.vue'
import ConfirmDialog from '@/components/common/table/subcomponents/ConfirmDialog.vue'
import type { ColumnDef, RowItem } from '@/components/common/table/types/table'

// ── Props & Emits ─────────────────────────────────────────────────────────────

const props = withDefaults(
    defineProps<{
        rows: T[]
        columns: ColumnDef<T>[]
        createLabel?: string
        fieldErrors?: Record<string, string>
    }>(),
    {
        createLabel: 'Tạo mới',
        fieldErrors: () => ({}),
    },
)

const emit = defineEmits<{
    create: []
    delete: [item: T]
    save: [item: T]
}>()

// ── Table chỉ hiện column không bị hideInTable ────────────────────────────────
const visibleColumns = computed(() =>
    props.columns.filter((c) => !c.hideInTable)
)

function getPillItems(col: ColumnDef, row: any): { id: number; name: string }[] {
    const raw = row[col.key]
    if (!Array.isArray(raw) || raw.length === 0) return []

    // Nếu là object[] (GenreResponse) -> lấy luôn id & name
    if (typeof raw[0] === 'object' && 'name' in raw[0]) {
        return raw.map((o: any) => ({ id: o.id, name: o.name }))
    }

    // Nếu là number[] -> lookup trong options
    const options = col.options as { id: number; name: string }[] | undefined
    if (!options) return []
    return raw.map((id: number) => {
        const found = options.find(opt => opt.id === id)
        return found ? { id, name: found.name } : { id, name: String(id) }
    })
}
// ── Selected item → mở DetailPanel ───────────────────────────────────────────
const selectedItem = ref<T | null>(null)

// ── Delete flow ───────────────────────────────────────────────────────────────
const showDeleteConfirm = ref(false)
const pendingDelete = ref<T | null>(null)

function onDeleteClick(row: T) {
    pendingDelete.value = row
    showDeleteConfirm.value = true
}

function onDeleteConfirmed() {
    if (pendingDelete.value) {
        emit('delete', pendingDelete.value)
        if (selectedItem.value?.id === pendingDelete.value.id) {
            selectedItem.value = null
        }
        pendingDelete.value = null
    }
}

// ── Save ──────────────────────────────────────────────────────────────────────
function onSave(updated: RowItem) {
    const done = () => {
        selectedItem.value = null   // đóng DetailPanel
    }
    emit('save', updated as T, done)
}
</script>