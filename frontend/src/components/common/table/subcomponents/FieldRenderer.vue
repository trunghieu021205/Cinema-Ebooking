<template>
    <div class="flex flex-col gap-1.5">
        <label class="text-xs font-medium text-text-admin-primary">
            {{ column.label }}
            <span v-if="column.required !== false" class="text-red-400">*</span>
        </label>

        <!-- Readonly → hiển thị text thuần -->
        <p v-if="column.readonly" class="rounded-lg bg-slate-50 px-3 py-2 text-sm text-text-admin-tertiary">
            <template v-if="column.type === 'multiselect' && normalizedMultiselectValue.length">
                <span v-for="id in normalizedMultiselectValue" :key="id"
                    class="mr-1 mb-1 inline-block rounded-full bg-slate-200 px-2 py-0.5 text-xs">
                    {{ getOptionName(id) }}
                </span>
            </template>
            <template v-else>
                {{ modelValue ?? '—' }}
            </template>
        </p>

        <!-- Enum → select dropdown -->
        <select v-else-if="column.type === 'enum'" :class="error
            ? 'border-red-300 bg-red-50/40 focus:border-red-400 focus:ring-red-100'
            : 'border-border-admin-subtle bg-white focus:border-accent focus:ring-slate-100'"
            class="w-full rounded-lg border px-3 py-2 text-sm text-slate-900 outline-none transition focus:ring-2"
            :value="modelValue" @change="emit('update:modelValue', ($event.target as HTMLSelectElement).value)">
            <option value="" disabled>Chọn {{ column.label }}...</option>
            <option v-for="opt in column.options" :key="opt" :value="opt">{{ opt }}</option>
        </select>

        <!-- Mutiselect -->
        <div v-else-if="column.type === 'multiselect'" class="rounded-lg border border-border-admin-subtle bg-white p-2"
            :class="error ? 'border-red-300 bg-red-50/40' : ''">
            <!-- Loading state -->
            <div v-if="!column.options?.length" class="py-2 text-center text-sm text-slate-400">
                Đang tải thể loại...
            </div>
            <!-- Grid checkboxes với scroll -->
            <div v-else class="grid grid-cols-2 gap-x-3 gap-y-1.5 max-h-48 overflow-y-auto pr-1">
                <label v-for="opt in column.options" :key="opt.id"
                    class="flex cursor-pointer items-center gap-1.5 text-sm text-slate-700">
                    <input type="checkbox" :value="opt.id" :checked="normalizedMultiselectValue.includes(opt.id)"
                        @change="onCheckboxChange(opt.id, $event)" class="rounded border-gray-300" />
                    <span class="truncate">{{ opt.name }}</span>
                </label>
            </div>
        </div>

        <!-- Textarea -->
        <textarea v-else-if="column.type === 'textarea'" :class="error
            ? 'border-red-300 bg-red-50/40 focus:border-red-400 focus:ring-red-100'
            : 'border-border-admin-subtle bg-white focus:border-accent focus:ring-slate-100'"
            class="w-full rounded-lg border px-3 py-2 text-sm text-slate-900 outline-none transition focus:ring-2"
            rows="3" :value="String(modelValue ?? '')"
            @input="emit('update:modelValue', ($event.target as HTMLTextAreaElement).value)" />

        <!-- Text / Number / Email / Date -->
        <input v-else :class="error
            ? 'border-red-300 bg-red-50/40 focus:border-red-400 focus:ring-red-100'
            : 'border-border-admin-subtle bg-white focus:border-accent focus:ring-slate-100'"
            class="w-full rounded-lg border px-3 py-2 text-sm text-slate-900 outline-none transition focus:ring-2"
            :type="column.type" :value="modelValue"
            @input="emit('update:modelValue', ($event.target as HTMLInputElement).value)" />

        <!-- Error message từ backend -->
        <p v-if="error" class="text-xs text-red-500">{{ error }}</p>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { ColumnDef } from '@/components/common/table/types/table'

const props = defineProps<{
    column: ColumnDef
    modelValue: unknown
    error?: string
}>()

const emit = defineEmits<{
    'update:modelValue': [value: unknown]
}>()

const normalizedMultiselectValue = computed(() => {
    if (props.column.type !== 'multiselect') return []
    const val = props.modelValue
    if (!Array.isArray(val)) return []
    if (val.length === 0) return []
    // Nếu phần tử đầu tiên có property 'id' -> assume object[]
    if (typeof val[0] === 'object' && 'id' in val[0]) {
        return val.map((item: any) => Number(item.id))
    }
    // Nếu đã là number[] thì giữ nguyên
    return val as number[]
})

function onCheckboxChange(id: number, event: Event) {
    const checked = (event.target as HTMLInputElement).checked
    const current = normalizedMultiselectValue.value
    const next = checked ? [...current, id] : current.filter(v => v !== id)
    emit('update:modelValue', next)
}

function getOptionName(id: number) {
    const opt = (props.column.options as { id: number; name: string }[])?.find(o => o.id === id)
    return opt?.name || id
}
</script>