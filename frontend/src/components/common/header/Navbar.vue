<template>
    <nav class="flex gap-6">
        <a class="flex items-center gap-2 hover:underline hover:text-accent text-text-primary text-body">
            Phim
            <ChevronDownIcon></ChevronDownIcon>
        </a>
        <a class="flex items-center gap-2 hover:underline hover:text-accent text-text-primary text-body">
            Lịch chiếu
            <ChevronDownIcon></ChevronDownIcon>
        </a>
        <a class="flex items-center gap-2 hover:underline hover:text-accent text-text-primary text-body">
            Khuyến mãi
            <ChevronDownIcon></ChevronDownIcon>
        </a>
        <a class="hover:underline hover:text-accent text-text-primary text-body">Vé của tôi</a>
    </nav>
</template>
<script setup lang="ts">
import ChevronDownIcon from '@/components/ui/icon/ChevronDownIcon.vue'
</script>