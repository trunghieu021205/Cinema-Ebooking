<template>
    <!-- Backdrop -->
    <Teleport to="body">
        <Transition enter-from-class="opacity-0" enter-active-class="transition-opacity duration-200"
            leave-to-class="opacity-0" leave-active-class="transition-opacity duration-200">
            <div v-if="item" class="fixed inset-0 z-40 bg-black/20" @click="tryClose" />
        </Transition>

        <!-- Panel -->
        <Transition enter-from-class="translate-x-full" enter-active-class="transition-transform duration-250 ease-out"
            leave-to-class="translate-x-full" leave-active-class="transition-transform duration-250 ease-in">
            <div v-if="item" class="fixed inset-y-0 right-0 z-50 flex w-full max-w-md flex-col bg-white shadow-2xl">
                <!-- Header -->
                <div class="flex items-center justify-between border-b border-slate-100 px-5 py-4">
                    <h2 class="text-sm font-semibold text-slate-900">Chi tiết</h2>
                    <button
                        class="rounded-md p-1 text-slate-400 transition-colors hover:bg-slate-100 hover:text-slate-600"
                        @click="tryClose">
                        <X class="size-4" />
                    </button>
                </div>

                <!-- Fields -->
                <div class="flex-1 overflow-y-auto px-5 py-4">
                    <div class="flex flex-col gap-4">
                        <FieldRenderer v-for="col in editableColumns" :key="col.key" :column="col"
                            :modelValue="draft[col.key]" :error="localErrors[col.key]"
                            @update:modelValue="onFieldUpdate(col.key, $event)" />
                    </div>
                </div>

                <!-- Footer -->
                <div class="border-t border-slate-100 px-5 pb-8">
                    <!-- Validation error summary -->
                    <p v-if="emptyFields.length" class="mb-3 text-xs text-red-500">
                        Vui lòng điền: {{ emptyFields.join(', ') }}
                    </p>

                    <BaseButton variant="primary" size="md" rounded="lg" class="w-full"
                        :class="!canSave && 'opacity-50 cursor-not-allowed pointer-events-none'" :disabled="!canSave"
                        @click="onSaveClick">
                        Lưu thay đổi
                    </BaseButton>
                </div>
            </div>
        </Transition>
    </Teleport>

    <!-- Confirm save dialog -->
    <ConfirmDialog v-model="showConfirm" title="Xác nhận cập nhật"
        description="Bạn có chắc muốn lưu các thay đổi này không?" confirmLabel="Lưu" @confirm="doSave" />
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue'
import { X } from 'lucide-vue-next'
import BaseButton from '@/components/ui/button/BaseButton.vue'
import FieldRenderer from '@/components/common/table/subcomponents/FieldRenderer.vue'
import ConfirmDialog from '@/components/common/table/subcomponents/ConfirmDialog.vue'
import type { ColumnDef, RowItem } from '@/components/common/table/types/table'

const props = defineProps<{
    item: RowItem | null
    columns: ColumnDef[]
    errors?: Record<string, string>   // backend validation errors
}>()

const emit = defineEmits<{
    close: []
    save: [item: RowItem]
}>()

const localErrors = ref<Record<string, string>>({})

// Sync từ prop khi backend trả lỗi mới
watch(() => props.errors, (val) => {
    localErrors.value = { ...val }
}, { deep: true })

function onFieldUpdate(key: string, value: unknown) {
    draft.value[key] = value
    delete localErrors.value[key]
}
// ── Draft state ───────────────────────────────────────────────────────────────
// Copy item vào draft khi panel mở — không mutate prop trực tiếp
const draft = ref<Record<string, unknown>>({})

watch(
    () => props.item,
    (val) => {
        if (val) {
            draft.value = { ...val }
            localErrors.value = {}  // ← reset khi mở item mới
        }
    },
    { immediate: true },
)

// ── Columns chỉ dùng trong panel (kể cả hideInTable) ─────────────────────────
const editableColumns = computed(() =>
    props.columns.filter((c) => !c.readonly || c.hideInTable !== true)
    // Readonly fields vẫn hiện trong panel nhưng disabled
)

// ── Detect có thay đổi chưa ──────────────────────────────────────────────────
const isDirty = computed(() => {
    if (!props.item) return false
    return props.columns.some((c) => draft.value[c.key] !== props.item![c.key])
})

// ── Validate: field nào required mà trống ────────────────────────────────────
const emptyFields = computed(() =>
    props.columns
        .filter(
            (c) =>
                !c.readonly &&
                c.required !== false &&
                (draft.value[c.key] === '' || draft.value[c.key] == null),
        )
        .map((c) => c.label),
)

const canSave = computed(() => isDirty.value && emptyFields.value.length === 0)

// ── Confirm dialog ────────────────────────────────────────────────────────────
const showConfirm = ref(false)

function onSaveClick() {
    if (!canSave.value) return
    showConfirm.value = true
}

function doSave() {
    emit('save', draft.value as RowItem)
}

// ── Close ─────────────────────────────────────────────────────────────────────
function tryClose() {
    emit('close')
}
</script>