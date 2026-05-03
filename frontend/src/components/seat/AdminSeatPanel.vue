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
                <BaseButton variant="primary" size="md" :disabled="!selectedType || selectedCount === 0 || loadingType"
                    @click="applyType">
                    {{ loadingType ? '...' : 'Áp dụng' }}
                </BaseButton>
            </div>
        </div>

        <!-- Kích hoạt -->
        <div>
            <BaseButton variant="primary" size="md"
                class="w-full bg-green-600 hover:bg-green-700 border-none disabled:opacity-50"
                :disabled="selectedCount === 0 || loadingActive" @click="activate">
                {{ loadingActive ? 'Đang xử lý...' : 'Kích hoạt ghế (ACTIVE)' }}
            </BaseButton>
            <p class="text-xs text-gray-500 mt-1">Ghế sẽ hiển thị và có thể đặt</p>
        </div>

        <!-- Vô hiệu hóa -->
        <div>
            <BaseButton variant="primary" size="md"
                class="w-full bg-gray-600 hover:bg-gray-700 border-none disabled:opacity-50"
                :disabled="selectedCount === 0 || loadingInactive" @click="inactivate">
                {{ loadingInactive ? 'Đang xử lý...' : 'Vô hiệu hóa ghế (INACTIVE)' }}
            </BaseButton>
            <p class="text-xs text-gray-500 mt-1">Ghế sẽ bị ẩn/không thể chọn</p>
        </div>

        <!-- Nút bỏ chọn -->
        <div class="border-t pt-2">
            <BaseButton variant="ghost" size="sm" @click="emit('clear-selection')">
                Bỏ chọn tất cả
            </BaseButton>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { layoutApi } from '@/api/layout.api'
import BaseButton from '@/components/ui/button/BaseButton.vue'

const props = defineProps<{
    selectedSeatIds: Set<number>
}>()

const emit = defineEmits<{
    'update-success': []
    'clear-selection': []
}>()

const selectedType = ref<number | null>(null)
const loadingType = ref(false)
const loadingActive = ref(false)
const loadingInactive = ref(false)

const selectedCount = computed(() => props.selectedSeatIds.size)
const seatIdArray = computed(() => Array.from(props.selectedSeatIds))

async function applyType() {
    if (!selectedType.value || selectedCount.value === 0) return
    loadingType.value = true
    try {
        await layoutApi.bulkUpdateSeatType({
            seatIds: seatIdArray.value,
            seatTypeId: selectedType.value
        })
        emit('update-success')
        selectedType.value = null
    } catch (err) {
        console.error(err)
        alert('Cập nhật loại ghế thất bại')
    } finally {
        loadingType.value = false
    }
}

async function activate() {
    if (selectedCount.value === 0) return
    loadingActive.value = true
    try {
        await layoutApi.bulkActivate({ seatIds: seatIdArray.value })
        emit('update-success')
    } catch (err) {
        console.error(err)
        alert('Kích hoạt ghế thất bại')
    } finally {
        loadingActive.value = false
    }
}

async function inactivate() {
    if (selectedCount.value === 0) return
    loadingInactive.value = true
    try {
        await layoutApi.bulkInactivate({ seatIds: seatIdArray.value })
        emit('update-success')
    } catch (err) {
        console.error(err)
        alert('Vô hiệu hóa ghế thất bại')
    } finally {
        loadingInactive.value = false
    }
}
</script>