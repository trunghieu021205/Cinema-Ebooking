<template>
    <RouterLink :to="item.to"
        class="flex items-center gap-4 rounded-r-lg pl-4 pr-2 py-2 text-sm transition-all group relative" :class="isActive
            ? 'bg-accent font-medium text-text-on-accent'
            : 'text-text-admin-secondary hover:text-text-admin-primary'
            ">
        <div v-if="!isActive"
            class="absolute left-0 top-0 h-full w-0 bg-accent transition-all ease-in-out group-hover:w-2"></div>
        <component :is="item.icon" class="size-4 shrink-0" />
        <span v-if="!ui.isSidebarCollapsed"">{{ item.label }}</span>
    </RouterLink>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useUIStore } from '@/stores/ui.store'
const ui = useUIStore()
const props = defineProps({
    item: Object, // { key, label, icon, to }
})

const route = useRoute()
const isActive = computed(() => route.path.startsWith(props.item.to))
</script>