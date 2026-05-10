<template>
    <div :class="[
        'flex flex-col gap-1.5',
        { 'w-full': mode !== 'display' }
    ]">
        <label v-if="mode !== 'display'" class="text-xs font-medium text-text-admin-primary">
            {{ column.label }}
            <span v-if="column.required !== false" class="text-red-400">*</span>
        </label>

        <!-- Readonly → hiển thị text thuần -->
        <p v-if="column.readonly && mode !== 'display'"
            class="rounded-lg bg-slate-50 px-3 py-2 text-sm text-text-admin-tertiary">
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
        <select v-else-if="column.type === 'enum' && mode !== 'display'" :class="error
            ? 'border-red-300 bg-red-50/40 focus:border-red-400 focus:ring-red-100'
            : 'border-border-admin-subtle bg-white focus:border-accent focus:ring-slate-100'"
            class="w-full rounded-lg border px-3 py-2 text-sm text-slate-900 outline-none transition focus:ring-2"
            :value="modelValue" @change="emit('update:modelValue', ($event.target as HTMLSelectElement).value)">
            <option value="" disabled>Chọn {{ column.label }}...</option>
            <option v-for="opt in column.options" :key="opt" :value="opt">{{ opt }}</option>
        </select>

        <!-- Mutiselect -->
        <div v-else-if="column.type === 'multiselect' && mode !== 'display'"
            class="rounded-lg border border-border-admin-subtle bg-white p-2"
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
        <textarea v-else-if="column.type === 'textarea' && mode !== 'display'" :class="error
            ? 'border-red-300 bg-red-50/40 focus:border-red-400 focus:ring-red-100'
            : 'border-border-admin-subtle bg-white focus:border-accent focus:ring-slate-100'"
            class="w-full rounded-lg border px-3 py-2 text-sm text-slate-900 outline-none transition focus:ring-2"
            rows="3" :value="String(modelValue ?? '')"
            @input="emit('update:modelValue', ($event.target as HTMLTextAreaElement).value)" />

        <label v-else-if="column.type === 'boolean' && mode !== 'display'"
            class="relative inline-flex items-center cursor-pointer gap-3">
            <input type="checkbox" :checked="!!modelValue"
                @change="emit('update:modelValue', ($event.target as HTMLInputElement).checked)" class="sr-only peer" />
            <div class="w-9 h-5 bg-gray-200 rounded-full
              peer peer-checked:bg-blue-600
              peer-focus:outline-none peer-focus:ring-2 peer-focus:ring-blue-300
              after:content-[''] after:absolute after:top-0.5 after:inset-s-0.5
              after:bg-white after:border-gray-300 after:border after:rounded-full
              after:h-4 after:w-4 after:transition-all
              peer-checked:after:translate-x-full rtl:peer-checked:after:-translate-x-full">
            </div>
            <span class="text-sm text-slate-600">{{ modelValue ? 'Có' : 'Không' }}</span>
        </label>

        <!-- Text / Number / Email / Date -->
        <input v-else-if="mode !== 'display'" :class="error
            ? 'border-red-300 bg-red-50/40 focus:border-red-400 focus:ring-red-100'
            : 'border-border-admin-subtle bg-white focus:border-accent focus:ring-slate-100'"
            class="w-full rounded-lg border px-3 py-2 text-sm text-slate-900 outline-none transition focus:ring-2"
            :type="column.type" :value="modelValue"
            @input="emit('update:modelValue', ($event.target as HTMLInputElement).value)" />

        <div v-if="mode === 'display'" class="text-sm text-slate-700">
            <!-- Boolean display -->
            <template v-if="column.type === 'boolean'">
                <span class="inline-flex items-center gap-1.5">
                    <span class="w-2 h-2 rounded-full" :class="modelValue ? 'bg-green-500' : 'bg-gray-300'">
                    </span>
                    <span>{{ modelValue ? 'Có' : 'Không' }}</span>
                </span>
            </template>
            <!-- Enum display -->
            <template v-else-if="column.type === 'enum'">
                <span class="inline-block rounded-full bg-slate-100 px-2.5 py-0.5 text-xs font-medium text-slate-600">
                    {{ modelValue ?? '—' }}
                </span>
            </template>

            <!-- Multiselect display -->
            <template v-else-if="column.type === 'multiselect'">
                <span v-if="normalizedMultiselectValue.length === 0">—</span>
                <span v-for="id in normalizedMultiselectValue" :key="id"
                    class="mr-1 mb-1 inline-block rounded-full bg-slate-100 px-2 py-0.5 text-xs text-slate-600">
                    {{ getOptionName(id) }}
                </span>
            </template>

            <!-- Textarea, text, number, email, date fallback -->
            <template v-else>
                <span class="line-clamp-1">{{ modelValue ?? '—' }}</span>
            </template>
        </div>

        <!-- Error message từ backend -->
        <p v-if="error && mode !== 'display'" class="text-xs text-red-500">{{ error }}</p>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { ColumnDef } from '@/components/common/table/types/table'

const props = defineProps<{
    column: ColumnDef
    modelValue: unknown
    error?: string
    mode?: 'edit' | 'display' | 'readonly'
}>()

const displayMode = computed(() => props.mode ?? 'edit')

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
    const opts = props.column.options as { id: number; name: string }[] | undefined
    const opt = opts?.find(o => o.id === id)
    return opt?.name || String(id)
}
</script>