<script setup lang="ts">
import { ref, computed, watch } from 'vue'

const props = defineProps<{
    modelValue: Date | null
    hasError?: boolean
}>()

const emit = defineEmits<{
    'update:modelValue': [value: Date | null]
}>()

const showCalendar = ref(false)
const calendarYear = ref(new Date().getFullYear() - 18)
const calendarMonth = ref(new Date().getMonth())
const inputValue = ref('')

const MONTH_NAMES = [
    'Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4',
    'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8',
    'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12',
]
const DAY_NAMES = ['CN', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7']

// ─── Sync ngược: khi calendar chọn ngày → cập nhật inputValue ──
watch(() => props.modelValue, (date) => {
    if (!date) return
    const dd = String(date.getDate()).padStart(2, '0')
    const mm = String(date.getMonth() + 1).padStart(2, '0')
    const yyyy = date.getFullYear()
    inputValue.value = `${dd}/${mm}/${yyyy}`
}, { immediate: true })

// ─── Auto-format khi gõ ─────────────────────────────────────────
const handleInput = (e: Event) => {
    const raw = (e.target as HTMLInputElement).value

    // Chỉ giữ số, tối đa 8 chữ số
    const digits = raw.replace(/\D/g, '').slice(0, 8)

    // Chèn "/" tự động: DD/MM/YYYY
    let formatted = digits
    if (digits.length > 2) formatted = digits.slice(0, 2) + '/' + digits.slice(2)
    if (digits.length > 4) formatted = formatted.slice(0, 5) + '/' + digits.slice(4)

    inputValue.value = formatted

    // Khi đủ 8 chữ số → parse và emit
    if (digits.length === 8) {
        const day = parseInt(digits.slice(0, 2))
        const month = parseInt(digits.slice(2, 4)) - 1  // 0-indexed
        const year = parseInt(digits.slice(4, 8))

        const date = new Date(year, month, day)

        // Kiểm tra ngày hợp lệ (tránh 31/02, 00/13, ...)
        const isValid =
            date.getFullYear() === year &&
            date.getMonth() === month &&
            date.getDate() === day

        if (isValid) {
            emit('update:modelValue', date)
            // Nhảy calendar đến tháng/năm vừa nhập
            calendarYear.value = year
            calendarMonth.value = month
        } else {
            emit('update:modelValue', null)
        }
    } else {
        emit('update:modelValue', null)
    }
}

// ─── Xử lý backspace — xóa cả "/" liền trước ───────────────────
const handleKeydown = (e: KeyboardEvent) => {
    if (e.key !== 'Backspace') return
    const val = inputValue.value
    if (val.endsWith('/')) {
        e.preventDefault()
        inputValue.value = val.slice(0, -1)
    }
}

// ─── Calendar ───────────────────────────────────────────────────
const calendarDays = computed(() => {
    const firstDayOfWeek = new Date(calendarYear.value, calendarMonth.value, 1).getDay()
    const totalDays = new Date(calendarYear.value, calendarMonth.value + 1, 0).getDate()
    const days: (number | null)[] = []
    for (let i = 0; i < firstDayOfWeek; i++) days.push(null)
    for (let d = 1; d <= totalDays; d++) days.push(d)
    return days
})

const prevMonth = () => {
    if (calendarMonth.value === 0) { calendarMonth.value = 11; calendarYear.value-- }
    else calendarMonth.value--
}

const nextMonth = () => {
    if (calendarMonth.value === 11) { calendarMonth.value = 0; calendarYear.value++ }
    else calendarMonth.value++
}

const selectDay = (day: number | null) => {
    if (!day) return
    emit('update:modelValue', new Date(calendarYear.value, calendarMonth.value, day))
    showCalendar.value = false
}

const isSelectedDay = (day: number | null) => {
    if (!day || !props.modelValue) return false
    const d = props.modelValue
    return d.getFullYear() === calendarYear.value
        && d.getMonth() === calendarMonth.value
        && d.getDate() === day
}
</script>

<template>
    <div v-if="showCalendar" class="fixed inset-0 z-60" @click="showCalendar = false" />

    <div class="relative">
        <!-- Input + calendar icon -->
        <div :class="[
            'w-full flex items-center border rounded-md transition-all overflow-hidden',
            showCalendar
                ? 'border-blue-500 ring-2 ring-blue-500'
                : hasError
                    ? 'border-red-400 ring-2 ring-red-300'
                    : 'border-border-subtle hover:border-border-default'
        ]">
            <input type="text" inputmode="numeric" placeholder="DD/MM/YYYY" :value="inputValue" @input="handleInput"
                @keydown="handleKeydown"
                class="flex-1 px-3 py-2 text-body text-text-primary bg-transparent outline-none placeholder:text-text-secondary" />
            <button type="button" @click="showCalendar = !showCalendar"
                class="px-2 py-2 text-text-secondary hover:text-text-primary transition-colors">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 shrink-0" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                        d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
            </button>
        </div>

        <!-- Calendar dropdown (giữ nguyên) -->
        <div v-if="showCalendar"
            class="absolute top-full left-0 right-0 z-70 mt-1 bg-bg-surface border border-border-subtle rounded-lg shadow-xl p-3">
            <div class="flex items-center justify-between mb-2">
                <button type="button" @click.stop="prevMonth"
                    class="w-6 h-6 flex items-center justify-center rounded hover:bg-bg-muted text-text-secondary hover:text-text-primary transition-colors">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M15 19l-7-7 7-7" />
                    </svg>
                </button>
                <span class="text-[13px] font-medium text-text-primary">
                    {{ MONTH_NAMES[calendarMonth] }} {{ calendarYear }}
                </span>
                <button type="button" @click.stop="nextMonth"
                    class="w-6 h-6 flex items-center justify-center rounded hover:bg-bg-muted text-text-secondary hover:text-text-primary transition-colors">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M9 5l7 7-7 7" />
                    </svg>
                </button>
            </div>

            <div class="grid grid-cols-7 mb-1">
                <span v-for="day in DAY_NAMES" :key="day"
                    class="text-center text-[10px] font-medium text-text-secondary py-1">
                    {{ day }}
                </span>
            </div>

            <div class="grid grid-cols-7 gap-y-0.5">
                <button v-for="(day, idx) in calendarDays" :key="idx" type="button" :disabled="!day"
                    @click.stop="selectDay(day)" :class="[
                        'h-7 w-full rounded text-[12px] transition-colors',
                        !day
                            ? 'cursor-default'
                            : isSelectedDay(day)
                                ? 'bg-blue-500 text-white font-medium'
                                : 'text-text-primary hover:bg-blue-50 hover:text-blue-600 cursor-pointer'
                    ]">
                    {{ day || '' }}
                </button>
            </div>
        </div>
    </div>
</template>