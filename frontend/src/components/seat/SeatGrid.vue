<template>
    <div class="w-fit flex flex-col items-start" :class="config.gap">

        <!-- Screen TOP -->
        <ScreenBar v-if="config.screenPosition === 'top'" />

        <!-- Seat grid -->
        <div class="overflow-x-auto w-fit">
            <div class="inline-flex flex-col min-w-max" :class="config.gap">
                <div v-for="(row, rowIdx) in layout.rows" :key="rowIdx" class="flex items-center" :class="config.gap">

                    <!-- Row label trái -->
                    <RowLabel v-if="config.showRowLabel" :label="rowLabel(rowIdx)" :mode="config.mode" />

                    <!-- Seats -->
                    <div v-for="seat in row" :key="seat.id" :data-seat-id="seat.id"
                        class="flex items-center justify-center border font-medium transition-all select-none" :class="[
                            config.seatSize,
                            config.seatRadius,
                            seatClasses(seat),
                            isInteractive(seat) ? 'cursor-pointer' : 'cursor-not-allowed',
                        ]" :title="`${seat.label} · ${typeConfig(seat.seatTypeId).label} · ${seat.status}`"
                        @click="onSeatClick(seat, $event)">
                        <span class="text-xs leading-none">
                            {{ config.showColNumber ? colNumber(seat.colIndex) : seat.label }}
                        </span>
                    </div>

                    <!-- Row label phải -->
                    <RowLabel v-if="config.showRowLabel && config.rowLabelBothSides" :label="rowLabel(rowIdx)"
                        :mode="config.mode" />

                </div>
            </div>
        </div>

        <!-- Screen BOTTOM -->
        <ScreenBar v-if="config.screenPosition === 'bottom'" />

        <!-- Legend -->
        <div class="w-full flex flex-wrap items-center justify-end gap-x-5 gap-y-2 pt-1">

            <!-- Web: chọn + đã bán trước -->
            <template v-if="config.mode === 'web'">
                <LegendItem>
                    <span class="h-5 w-5 rounded-md bg-amber-500" />
                    <span>Ghế đã chọn</span>
                </LegendItem>
                <LegendItem>
                    <span class="h-5 w-5 rounded-md bg-white/20" />
                    <span>Ghế đã bán</span>
                </LegendItem>
            </template>

            <!-- Seat types -->
            <LegendItem v-for="(cfg, typeId) in SEAT_TYPE_CONFIGS" :key="typeId">
                <span v-if="config.mode === 'admin'" class="h-5 w-5 rounded-md border"
                    :class="[cfg.adminBg, cfg.adminBorder]" />
                <span v-else class="h-5 w-9 rounded-md border-2 bg-transparent" :class="cfg.webBorder" />
                <span>{{ cfg.label }}</span>
            </LegendItem>

            <!-- Admin: inactive -->
            <template v-if="config.mode === 'admin'">
                <LegendItem>
                    <span class="h-5 w-5 rounded-md border border-gray-300 bg-gray-200 opacity-70" />
                    <span>Không hoạt động</span>
                </LegendItem>
            </template>

        </div>

    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import type { RoomLayoutResponse, SeatResponse } from '@/types/seat'
import { SEAT_TYPE_CONFIGS, SEAT_TYPE_FALLBACK } from '@/components/seat/seatGridConfig'
import type { SeatGridConfig } from '@/components/seat/seatGridConfig'

// ── Sub-components (inline để tránh tạo thêm file) ────────────────────────────
import { defineComponent, h } from 'vue'

const RowLabel = defineComponent({
    props: { label: String, mode: String },
    setup: (props) => () =>
        h('span', {
            class: [
                'w-6 shrink-0 text-center text-xs font-semibold select-none',
                props.mode === 'web'
                    ? 'text-white/40'
                    : 'text-text-admin-tertiary',
            ],
        }, props.label),
})

const ScreenBar = defineComponent({
    setup: () => () =>
        h('div', { class: 'w-full max-w-2xl flex flex-col items-center gap-1 py-1' }, [
            h('span', {
                class: 'text-xs font-medium tracking-widest uppercase text-slate-400',
            }, 'Màn hình'),
            h('div', {
                class: 'h-1 w-full rounded-full bg-linear-to-r from-transparent via-amber-400/60 to-transparent',
            }),
        ]),
})

const LegendItem = defineComponent({
    setup: (_, { slots }) =>
        () => h('div', { class: 'flex items-center gap-1.5 text-xs text-slate-400' },
            slots.default?.()),
})

// ── Props & Emits ─────────────────────────────────────────────────────────────

const props = withDefaults(
    defineProps<{
        layout: RoomLayoutResponse
        config: SeatGridConfig
        selectedIds?: number[]   // web: ghế đang chọn
        bookedIds?: number[]   // web: ghế đã bán
    }>(),
    {
        selectedIds: () => [],
        bookedIds: () => [],
    },
)

const emit = defineEmits<{
    'seat-click': [seat: SeatResponse, event: MouseEvent]
}>()

// ── Helpers ───────────────────────────────────────────────────────────────────

function rowLabel(rowIdx: number): string {
    return String.fromCharCode(65 + rowIdx)
}

function colNumber(colIdx: number): number {
    return props.config.rtl
        ? props.layout.totalCols - colIdx
        : colIdx + 1
}

function typeConfig(typeId: number) {
    return SEAT_TYPE_CONFIGS[typeId] ?? SEAT_TYPE_FALLBACK
}

const selectedSet = computed(() => new Set(props.selectedIds))
const bookedSet = computed(() => new Set(props.bookedIds))

function isInteractive(seat: SeatResponse): boolean {
    // Admin có thể chọn mọi ghế (kể cả inactive) để cập nhật
    if (props.config.mode === 'admin') {
        return true;
    }
    // Web mode: chỉ cho phép chọn ghế ACTIVE và chưa bị đặt
    if (seat.status === 'INACTIVE') return false;
    if (bookedSet.value.has(seat.id)) return false;
    return true;
}

function onSeatClick(seat: SeatResponse, event: MouseEvent) {
    if (!isInteractive(seat)) return
    emit('seat-click', seat, event)
}

// ── Class builder ─────────────────────────────────────────────────────────────

function seatClasses(seat: SeatResponse): string {
    const cfg = typeConfig(seat.seatTypeId)
    const inactive = seat.status === 'INACTIVE'
    const selected = selectedSet.value.has(seat.id)
    const booked = bookedSet.value.has(seat.id)

    if (props.config.mode === 'admin') {
        // ── Admin ──
        if (selected)
            return 'bg-blue-500 border-blue-600 text-white shadow-md ring-2 ring-blue-300'
        // INACTIVE: màu nhạt, mờ
        if (inactive)
            return 'bg-slate-50 border-slate-200 text-slate-300 opacity-50'
        // ACTIVE: bg theo loại, hover brightness-110
        return `${cfg.adminBg} ${cfg.adminBorder} ${cfg.adminText} hover:brightness-110`
    }

    // ── Web ──
    // Ghế không hoạt động
    if (inactive)
        return 'bg-white/5 border-white/10 text-white/20 opacity-40'

    // Ghế đã bán
    if (booked)
        return 'bg-white/20 border-white/20 text-white/30'

    // Ghế đang được chọn → tô màu theo loại
    if (selected)
        return `${cfg.webSelectedBg} border-transparent ${cfg.webSelectedText}`

    // Default: bg tối, border theo loại, hover sáng nhẹ
    return `bg-white/10 border-2 ${cfg.webBorder} text-white/80 hover:bg-white/20`
}
</script>