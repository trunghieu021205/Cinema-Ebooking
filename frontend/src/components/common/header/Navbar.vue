<template>
    <nav class="flex gap-6">
        <a class="flex items-center gap-2 hover:underline hover:text-accent text-text-primary text-body">
            Phim
            <BaseIcon :icon="ChevronDown" :size="14" :scale="1.2" :stroke-width="1.5" />
        </a>
        <a class="flex items-center gap-2 hover:underline hover:text-accent text-text-primary text-body">
            Lịch chiếu
            <BaseIcon :icon="ChevronDown" :size="14" :scale="1.2" :stroke-width="1.5" />
        </a>
        <a class="flex items-center gap-2 hover:underline hover:text-accent text-text-primary text-body">
            Khuyến mãi
            <BaseIcon :icon="ChevronDown" :size="14" :scale="1.2" :stroke-width="1.5" />
        </a>
        <a class="hover:underline hover:text-accent text-text-primary text-body">Vé của tôi</a>
    </nav>
</template>
<script setup lang="ts">
import { ChevronDown } from 'lucide-vue-next';
import BaseIcon from '@/components/ui/icon/BaseIcon.vue';
</script>