<template>
    <Teleport to="body">
        <div v-if="show" class="fixed inset-0 z-9999 flex items-center justify-center bg-black/50 backdrop-blur-sm">
            <div class="w-90 rounded-2xl bg-bg-surface p-8 text-center shadow-2xl">

                <!-- Title -->
                <h2 class="text-title text-text-primary mb-2">
                    Bạn vẫn còn ở đây không?
                </h2>

                <!-- Description -->
                <p class="text-text-secondary text-body mb-4">
                    Phiên làm việc sẽ tự động kết thúc sau
                </p>

                <!-- Countdown -->
                <div class="text-5xl font-bold tabular-nums mb-6"
                    :class="countdown <= 10 ? 'text-red-500' : 'text-accent'">
                    {{ countdown }}s
                </div>

                <!-- Actions -->
                <div class="flex flex-col gap-2">
                    <BaseButton variant="primary" size="lg" rounded="lg" class="w-full" @click="$emit('stay')">
                        Tiếp tục phiên làm việc
                    </BaseButton>

                    <BaseButton variant="ghost" size="lg" rounded="lg" class="w-full" @click="$emit('logout')">
                        Đăng xuất ngay
                    </BaseButton>
                </div>

            </div>
        </div>
    </Teleport>
</template>

<script setup lang="ts">
import BaseButton from '@/components/ui/button/BaseButton.vue'
defineProps<{
    show: boolean
    countdown: number
}>()

defineEmits<{
    stay: []
    logout: []
}>()
</script>