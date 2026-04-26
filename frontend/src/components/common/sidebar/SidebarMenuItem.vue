<template>
    <SidebarGroup v-if="item.children?.length" :item="item" />
    <SidebarItem v-else :item="item" />
</template>

<script setup>
import SidebarItem from '@/components/common/sidebar/SidebarItem.vue'
import SidebarGroup from '@/components/common/sidebar/SidebarGroup.vue'

defineProps({
    item: Object,
})
</script>