<template>
    <div class="fixed right-6 top-1/2 -translate-y-1/2 w-80 bg-white rounded-xl shadow-xl border p-4 z-50 space-y-4">
        <div class="flex justify-between items-center border-b pb-2">
            <h3 class="font-semibold text-lg">Chỉnh sửa ghế</h3>
            <span class="text-sm bg-gray-100 px-2 py-1 rounded-full">{{ selectedCount }} ghế</span>
        </div>

        <!-- Đổi loại ghế -->
        <div>
            <label class="block text-sm font-medium mb-1">Đổi loại ghế</label>
            <div class="flex gap-2">
                <select v-model="selectedType" class="flex-1 border rounded-md p-2 text-sm">
                    <option :value="null">-- Chọn loại --</option>
                    <option :value="1">Ghế thường</option>
                    <option :value="2">Ghế VIP</option>
                    <option :value="3">Ghế đôi</option>
                </select>
                <BaseButton variant="primary" size="md" :disabled="!selectedType || selectedCount === 0"
                    @click="applyType">
                    Áp dụng
                </BaseButton>
            </div>
        </div>

        <!-- Kích hoạt -->
        <BaseButton variant="primary" size="md" class="w-full bg-green-600 hover:bg-green-700"
            :disabled="selectedCount === 0" @click="activate">
            Kích hoạt ghế (ACTIVE)
        </BaseButton>

        <!-- Vô hiệu hóa -->
        <BaseButton variant="primary" size="md" class="w-full bg-gray-600 hover:bg-gray-700"
            :disabled="selectedCount === 0" @click="inactivate">
            Vô hiệu hóa ghế (INACTIVE)
        </BaseButton>

        <!-- Bỏ chọn -->
        <div class="border-t pt-2">
            <button class="text-sm text-gray-500 hover:text-gray-700" @click="emit('clear-selection')">
                Bỏ chọn tất cả
            </button>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import BaseButton from '@/components/ui/button/BaseButton.vue'

const props = defineProps<{ selectedSeatIds: Set<number> }>()
const emit = defineEmits<{
    'apply-change': [payload: { seatIds: number[]; newStatus?: string; newSeatTypeId?: number }]
    'clear-selection': []
}>()

const selectedType = ref<number | null>(null)
const selectedCount = computed(() => props.selectedSeatIds.size)
const seatIdArray = computed(() => Array.from(props.selectedSeatIds))

function applyType() {
    if (!selectedType.value || selectedCount.value === 0) return
    emit('apply-change', { seatIds: seatIdArray.value, newSeatTypeId: selectedType.value })
    selectedType.value = null
}

function activate() {
    emit('apply-change', { seatIds: seatIdArray.value, newStatus: 'ACTIVE' })
}

function inactivate() {
    emit('apply-change', { seatIds: seatIdArray.value, newStatus: 'INACTIVE' })
}
</script>