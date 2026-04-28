<script setup lang="ts">
import { Gift } from 'lucide-vue-next'
import BaseIcon from '@/components/ui/icon/BaseIcon.vue'

const pointIconStyle = {
    basic: 'text-zinc-400 opacity-80',
    silver: 'text-gray-200 opacity-80',
    gold: 'text-accent opacity-80',
}

const props = defineProps<{
    membership?: 'basic' | 'silver' | 'gold'
}>()
</script>

<template>
    <BaseIcon :icon="Gift" :size="16" :scale="1.2" :stroke-width="1.5"
        :class="pointIconStyle[props.membership ?? 'basic']" />
</template>