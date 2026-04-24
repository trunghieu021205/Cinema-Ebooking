<script setup lang="ts">
import { Gift } from 'lucide-vue-next'
const pointIconStyle = {
    basic: 'text-zinc-400 opacity-70',
    silver: 'text-gray-200 opacity-70',
    gold: 'text-accent opacity-70',
}
defineProps<{
    membership?: 'basic' | 'silver' | 'gold'
}>()
</script>

<template>
    <span>
        <Gift class="w-5 h-5 stroke-[1.5px]" :class="pointIconStyle[membership]" />
    </span>
</template>