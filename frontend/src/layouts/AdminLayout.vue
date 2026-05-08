<template>
    <div class="admin-theme flex flex-col min-h-screen bg-bg-admin-base">
        <Sidebar />
        <TopBar />
        <!-- PAGE CONTENT -->
        <main class="w-full h-full" :style="{
            paddingLeft: !ui.isSidebarCollapsed
                ? 'calc(var(--sidebar-width) + var(--sidebar-right-gap))'
                : 'calc(var(--sidebar-collapsed-width) + var(--sidebar-right-gap))'
        }">
            <router-view />
        </main>


    </div>
</template>

<script setup lang="ts">
import Sidebar from '@/components/common/sidebar/Sidebar.vue'
import TopBar from '@/components/common/topbar/TopBar.vue';
import { useUIStore } from '@/stores/ui.store'
const ui = useUIStore()
</script>