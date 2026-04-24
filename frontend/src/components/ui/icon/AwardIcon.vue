<script setup lang="ts">
import { Award } from 'lucide-vue-next'
const membershipStyle = {
    basic: 'text-zinc-400',
    silver: 'text-gray-200 drop-shadow-[0_0_6px_rgba(255,255,255,0.5)]',
    gold: 'text-accent drop-shadow-[0_0_6px_rgba(201,167,78,0.6)]',
}

defineProps<{
    membership?: 'basic' | 'silver' | 'gold'
}>()
</script>

<template>
    <span>
        <Award class="w-5 h-5 stroke-[1.5px]" :class="membershipStyle[membership]" />
    </span>
</template>