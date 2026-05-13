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
        <template v-if="column.readonly && mode !== 'display'">
            <p class="rounded-lg bg-slate-50 px-3 py-2 text-sm text-text-admin-tertiary">
                <!-- Relation readonly: hiển thị label đã resolve -->
                <template v-if="column.type === 'relation'">
                    {{ resolvedLabel }}
                </template>
                <!-- Multiselect readonly -->
                <template v-else-if="column.type === 'multiselect' && normalizedMultiselectValue.length">
                    <span v-for="id in normalizedMultiselectValue" :key="id"
                        class="mr-1 mb-1 inline-block rounded-full bg-slate-200 px-2 py-0.5 text-xs">
                        {{ getOptionName(id) }}
                    </span>
                </template>
                <template v-else-if="column.type === 'enum'">
                    {{ getEnumLabel(modelValue) ?? modelValue ?? '—' }}
                </template>
                <template v-else-if="column.type === 'date'">
                    {{ modelValue ? formatDateOnly(String(modelValue)) : '—' }}
                </template>
                <template v-else-if="column.type === 'datetime'"> <!-- thêm mới -->
                    {{ modelValue ? formatDateTime(String(modelValue)) : '—' }}
                </template>

                <template v-else>
                    {{ modelValue ?? '—' }}
                </template>
            </p>
        </template>

        <!-- ═══════════════════════════════════════════════════════════════════
             RELATION TYPE
             Static options / async optionsLoader / dependentLoader
        ════════════════════════════════════════════════════════════════════ -->
        <template v-else-if="column.type === 'relation' && mode !== 'display'">
            <div class="flex flex-col gap-1">
                <select :value="String(modelValue ?? '')"
                    :disabled="isLoadingOptions || (column.type === 'relation' && !!column.dependentLoader && resolvedOptions.length === 0)"
                    class="w-full rounded-lg border px-3 py-2 text-sm text-slate-900 outline-none transition
                           focus:ring-2 disabled:cursor-not-allowed disabled:opacity-50" :class="error
                            ? 'border-red-300 bg-red-50/40 focus:border-red-400 focus:ring-red-100'
                            : 'border-border-admin-subtle bg-white focus:border-accent focus:ring-slate-100'"
                    @change="onRelationChange">
                    <!-- Placeholder -->
                    <option value="" disabled>
                        <template v-if="isLoadingOptions">Đang tải...</template>
                        <template v-else-if="column.dependentLoader && resolvedOptions.length === 0">
                            Chọn {{ dependsOnLabels }} trước
                        </template>
                        <template v-else>
                            Chọn {{ column.label.toLowerCase() }}...
                        </template>
                    </option>
                    <option v-for="opt in resolvedOptions" :key="opt.id" :value="String(opt.id)">
                        {{ opt.label }}
                    </option>
                </select>

                <!-- Loading spinner -->
                <p v-if="isLoadingOptions" class="flex items-center gap-1.5 text-xs text-slate-400">
                    <span
                        class="inline-block h-3 w-3 animate-spin rounded-full border-2 border-slate-300 border-t-slate-500" />
                    Đang tải dữ liệu...
                </p>

                <!-- Dependent hint: deps chưa đủ -->
                <p v-else-if="column.dependentLoader && !isDepsReady && !isLoadingOptions"
                    class="text-xs text-slate-400 italic">
                    Vui lòng chọn {{ dependsOnLabels }} để tải {{ column.label.toLowerCase() }}
                </p>

                <!-- Load error -->
                <p v-else-if="loadError" class="text-xs text-red-400">
                    ⚠ {{ loadError }}
                </p>

                <!-- Auto-selected info (dependent, 1 kết quả) -->
                <p v-else-if="column.dependentLoader && resolvedOptions.length === 1" class="text-xs text-emerald-600">
                    ✓ Tự động chọn: {{ resolvedOptions[0].label }}
                </p>
            </div>
        </template>

        <!-- Enum → select dropdown -->
        <select v-else-if="column.type === 'enum' && mode !== 'display'" :class="error
            ? 'border-red-300 bg-red-50/40 focus:border-red-400 focus:ring-red-100'
            : 'border-border-admin-subtle bg-white focus:border-accent focus:ring-slate-100'"
            class="w-full rounded-lg border px-3 py-2 text-sm text-slate-900 outline-none transition focus:ring-2"
            :value="modelValue" @change="emit('update:modelValue', ($event.target as HTMLSelectElement).value)">
            <option v-for="opt in column.options" :key="opt.value" :value="opt.value">{{ opt.label }}</option>
        </select>

        <div v-else-if="column.type === 'multiselect' && mode !== 'display'" class="rounded-lg border bg-white p-2"
            :class="error ? 'border-red-300 bg-red-50/40' : 'border-border-admin-subtle'">
            <div v-if="!column.options?.length" class="py-2 text-center text-sm text-slate-400">
                Đang tải...
            </div>
            <div v-else class="max-h-48 overflow-y-auto grid grid-cols-2 gap-x-3 gap-y-1.5 pr-1">
                <label v-for="opt in column.options" :key="opt.value"
                    class="flex cursor-pointer items-center gap-1.5 text-sm text-slate-700">
                    <input type="checkbox" :value="opt.value" :checked="normalizedMultiselectValue.includes(opt.value)"
                        @change="onCheckboxChange(opt.value, $event)" class="rounded border-gray-300" />
                    <span class="truncate">{{ opt.label }}</span>
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
            :type="column.type === 'datetime' ? 'datetime-local' : column.type" :value="modelValue"
            @input="emit('update:modelValue', ($event.target as HTMLInputElement).value)"
            @blur="emit('blur', column.key)" />

        <div v-if="mode === 'display'" class="text-sm text-slate-700">
            <!-- Relation display -->
            <template v-if="column.type === 'relation'">
                <span v-if="isLoadingOptions" class="text-slate-400 italic">Đang tải...</span>
                <span v-else class="line-clamp-1">{{ resolvedLabel }}</span>
            </template>
            <!-- Boolean display -->
            <template v-else-if="column.type === 'boolean'">
                <span class="inline-flex items-center gap-1.5">
                    <span class="w-2 h-2 rounded-full" :class="modelValue ? 'bg-green-500' : 'bg-gray-300'">
                    </span>
                    <span>{{ modelValue ? 'Có' : 'Không' }}</span>
                </span>
            </template>
            <!-- Enum display -->
            <template v-else-if="column.type === 'enum'">
                <span class="inline-block rounded-full bg-slate-100 px-2.5 py-0.5 text-xs font-medium text-slate-600">
                    {{ getEnumLabel(modelValue) ?? modelValue ?? '—' }}
                </span>
            </template>

            <!-- Multiselect display -->
            <template v-else-if="column.type === 'multiselect'">
                <span v-if="normalizedMultiselectValue.length === 0">—</span>
                <span v-for="val in normalizedMultiselectValue" :key="val"
                    class="mr-1 mb-1 inline-block rounded-full bg-slate-100 px-2 py-0.5 text-xs text-slate-600">
                    {{ getOptionName(val) }}
                </span>
            </template>

            <!-- Date display -->
            <template v-else-if="column.type === 'date'">
                <span>{{ modelValue ? formatDateOnly(String(modelValue)) : '—' }}</span>
            </template>
            <template v-else-if="column.type === 'datetime'">
                <span>{{ modelValue ? formatDateTime(String(modelValue)) : '—' }}</span>
            </template>

            <!-- Default display -->
            <template v-else>
                <span class="line-clamp-1">{{ modelValue ?? '—' }}</span>
            </template>
        </div>

        <!-- Error message từ backend -->
        <p v-if="error && mode !== 'display'" class="text-xs text-red-500">{{ error }}</p>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue';
import type { ColumnDef, RelationOption } from '@/components/common/table/types/table'


const props = defineProps<{
    column: ColumnDef
    modelValue: unknown
    error?: string
    mode?: 'edit' | 'display' | 'readonly'

    depValues?: Record<string, unknown>
}>()

const displayMode = computed(() => props.mode ?? 'edit')

const emit = defineEmits<{
    'update:modelValue': [value: unknown]
    'blur': [key: string]
}>()

// ─── Relation state ───────────────────────────────────────────────────────────

const _optionsCache = new Map<Function, RelationOption[]>()
const loadedOptions = ref<RelationOption[]>([])
const isLoadingOptions = ref(false)
const loadError = ref<string | null>(null)

/**
 * Options cuối cùng để render select:
 *  - staticOptions nếu có (hardcode, không fetch)
 *  - loadedOptions nếu đã fetch xong
 */
const resolvedOptions = computed<RelationOption[]>(() => {
    if (props.column.staticOptions?.length) {
        return props.column.staticOptions as RelationOption[]
    }
    return loadedOptions.value
})

/**
 * Label hiển thị ở display mode và readonly.
 * Ưu tiên: displayFn → resolve từ resolvedOptions → raw value
 */
const resolvedLabel = computed<string>(() => {
    const val = props.modelValue
    if (val === null || val === undefined || val === '') return '—'

    // 1. displayFn (page tự cung cấp, không cần load options)
    if (props.column.displayFn) {
        return props.column.displayFn(val, {} as Record<string, unknown>)
    }

    // 2. Resolve từ options đã có
    if (resolvedOptions.value.length) {
        const opt = resolvedOptions.value.find(o => String(o.id) === String(val))
        if (opt) return opt.label
    }

    // 3. Đang load
    if (isLoadingOptions.value) return 'Đang tải...'

    // 4. Nếu có dependentLoader mà chưa sẵn sàng → báo thiếu dữ kiện
    if (props.column.dependentLoader && !isDepsReady.value) {
        return 'Chưa có dữ kiện phụ thuộc'
    }

    // 5. Fallback
    return String(val)
})

function getEnumLabel(value: unknown): string | undefined {
    if (!props.column.options || !Array.isArray(props.column.options)) return undefined
    const opt = props.column.options.find(
        (o: any) => o.value === value || o.id === value
    )
    return opt?.label ?? opt?.name
}

/**
 * Kiểm tra tất cả deps đã có giá trị chưa (để biết khi nào có thể fetch dependent).
 */
const isDepsReady = computed<boolean>(() => {
    if (!props.column.dependsOn?.length) return true
    return props.column.dependsOn.every(k => {
        const v = props.depValues?.[k]
        return v !== null && v !== undefined && v !== ''
    })
})

/**
 * Label của các field deps để hiển thị trong hint placeholder.
 * Ví dụ: "phòng chiếu và giờ bắt đầu"
 */
const dependsOnLabels = computed<string>(() => {
    // Không có cách lấy label từ key nếu không có columns list — dùng key trực tiếp
    return props.column.dependsOn?.join(' và ') ?? ''
})

// ─── Load options (optionsLoader — 1 lần, có cache) ──────────────────────────

async function loadOptions() {
    const loader = props.column.optionsLoader
    if (!loader) return

    // Cache hit
    if (_optionsCache.has(loader)) {
        loadedOptions.value = _optionsCache.get(loader)!
        return
    }

    isLoadingOptions.value = true
    loadError.value = null
    try {
        const opts = await loader()
        _optionsCache.set(loader, opts)
        loadedOptions.value = opts
    } catch {
        loadError.value = 'Không tải được dữ liệu'
    } finally {
        isLoadingOptions.value = false
    }
}

// ─── Load dependent (re-fetch khi deps thay đổi) ─────────────────────────────

async function loadDependent(deps: Record<string, unknown>) {
    const loader = props.column.dependentLoader
    if (!loader) return

    // Reset
    loadedOptions.value = []
    emit('update:modelValue', null)
    loadError.value = null

    if (!isDepsReady.value) return

    isLoadingOptions.value = true
    try {
        const opts = await loader(deps)
        loadedOptions.value = opts

        // Auto-select nếu chỉ có đúng 1 kết quả (layout case)
        if (opts.length === 1) {
            emit('update:modelValue', opts[0].id)
        }
    } catch {
        loadError.value = 'Không tải được dữ liệu liên quan'
    } finally {
        isLoadingOptions.value = false
    }
}

// ─── Watch depValues → reload dependent ──────────────────────────────────────

watch(
    () => props.depValues,
    (newDeps) => {
        if (props.column.dependentLoader && !props.column.readonly && newDeps) {
            loadDependent(newDeps)
        }
    },
    { deep: true }
)

// ─── Mount ────────────────────────────────────────────────────────────────────

// FieldRenderer.vue – phần onMounted()

onMounted(() => {
    if (props.column.type !== 'relation') return
    if (props.column.staticOptions?.length) return

    if (props.column.readonly && props.column.optionsLoader) {
        loadOptions()
        return
    }

    // Nếu là display mode hoặc không có depValues, ưu tiên optionsLoader
    if ((props.mode === 'display' || !props.depValues) && props.column.optionsLoader) {
        loadOptions()
        return
    }

    // Nếu có dependentLoader và depValues đã sẵn sàng, load dependent
    if (props.column.dependentLoader && props.depValues && isDepsReady.value) {
        loadDependent(props.depValues)
        return
    }

    // Fallback cuối cùng: optionsLoader
    if (props.column.optionsLoader) {
        loadOptions()
    }
})

function onRelationChange(e: Event) {
    const raw = (e.target as HTMLSelectElement).value
    if (!raw) return
    const opt = resolvedOptions.value.find(o => String(o.id) === raw)
    emit('update:modelValue', opt ? opt.id : raw)
}

const normalizedMultiselectValue = computed(() => {
    if (props.column.type !== 'multiselect') return []
    const val = props.modelValue
    if (!Array.isArray(val) || val.length === 0) return []
    if (typeof val[0] === 'object' && val[0] !== null) {
        // object[] -> lấy value (nếu có) hoặc id (fallback)
        return val.map((item: any) => item.value ?? item.id)
    }
    return val as (number | string)[]
})

function onCheckboxChange(value: number | string, event: Event) {
    const checked = (event.target as HTMLInputElement).checked
    const current = normalizedMultiselectValue.value
    const next = checked ? [...current, value] : current.filter(v => v !== value)
    emit('update:modelValue', next)
}

function getOptionName(value: number | string) {
    const opts = props.column.options as { value: number | string; label: string }[] | undefined
    const opt = opts?.find(o => o.value === value)
    return opt?.label || String(value)
}

// date-only: "15/05/2026"
function formatDateOnly(iso: string): string {
    try {
        return new Date(iso).toLocaleDateString('vi-VN', {
            day: '2-digit', month: '2-digit', year: 'numeric',
        })
    } catch { return iso }
}

// datetime: "15/05/2026, 14:00"
function formatDateTime(iso: string): string {
    try {
        return new Date(iso).toLocaleString('vi-VN', {
            day: '2-digit', month: '2-digit', year: 'numeric',
            hour: '2-digit', minute: '2-digit',
        })
    } catch { return iso }
}
</script>