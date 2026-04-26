<script setup lang="ts">
import SidebarMenuItem from '@/components/common/sidebar/SidebarMenuItem.vue'
import { useSidebarMenu } from '@/composables/useSidebarMenu'
import { useUIStore } from '@/store/ui.store';
const ui = useUIStore();
const menu = useSidebarMenu()
const adminName = 'Nguyễn Văn A'
</script>
<template>
    <aside
        class="flex h-screen fixed left-0 top-16 shrink-0 flex-col gap-8 border-r border-border-admin-default bg-bg-admin-sidebar"
        :class="ui.isSidebarCollapsed ? 'p-3' : 'p-6'" :style="{
            width: ui.isSidebarCollapsed
                ? 'var(--sidebar-collapsed-width)'
                : 'var(--sidebar-width)'
        }">

        <div class="flex group relative flex-col items-center gap-2">
            <img src="/images/avatars/UserAvatar.jpg" class="w-12 h-12 rounded-full" />
            <div v-if="!ui.isSidebarCollapsed" class="flex flex-col items-center gap-1">
                <h1 class="text-body text-text-admin-primary">{{ adminName }}</h1>
                <h2 class="text-xs text-text-admin-tertiary">ADMIN</h2>
            </div>
            <div v-if="ui.isSidebarCollapsed"
                class="absolute left-full ml-3 top-1/2 -translate-y-1/2 opacity-0 group-hover:opacity-100 transition-opacity duration-200 pointer-events-none bg-overlay-light-30 text-white text-xs px-2 py-1 rounded whitespace-nowrap">
                {{ adminName }}
            </div>
        </div>

        <nav class="flex flex-1 flex-col gap-1 overflow-y-auto">
            <SidebarMenuItem v-for="item in menu" :key="item.key" :item="item" />
        </nav>

    </aside>
</template>
