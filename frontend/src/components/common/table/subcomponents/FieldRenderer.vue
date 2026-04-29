<template>
    <div class="flex flex-col gap-1.5">
        <label class="text-xs font-medium text-text-admin-primary">
            {{ column.label }}
            <span v-if="column.required !== false" class="text-red-400">*</span>
        </label>

        <!-- Readonly → hiển thị text thuần -->
        <p v-if="column.readonly" class="rounded-lg bg-slate-50 px-3 py-2 text-sm text-text-admin-tertiary">
            {{ modelValue ?? '—' }}
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
import type { ColumnDef } from '@/components/common/table/types/table'

defineProps<{
    column: ColumnDef
    modelValue: unknown
    error?: string
}>()

const emit = defineEmits<{
    'update:modelValue': [value: unknown]
}>()
</script>