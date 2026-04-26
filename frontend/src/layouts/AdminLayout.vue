<template>
    <div class="admin-theme flex flex-col min-h-screen bg-bg-admin-base">

        <Sidebar />
        <!-- TOPBAR -->
        <header class="flex flex-col gap-4 w-full h-fit bg-bg-admin-topbar text-text-on-admin-topbar py-4">
            <div class="flex w-fit " :style="{ gap: 'var(--sidebar-right-gap)' }">
                <div class="flex pl-16 items-center justify-between" :style="{ width: 'var(--sidebar-width)' }">
                    <Logo isAdminPage />

                    <BaseButton @click="ui.toggleSidebar" iconOnly size="sm" rounded="full">
                        <BaseIcon :icon="ChevronLeft" :size="24" :scale="1.3" :stroke-width="1.5" />
                    </BaseButton>
                </div>
                <BaseButton iconOnly size="sm" rounded="full">
                    <BaseIcon :icon="Search" :size="16" :scale="1.2" :stroke-width="1.5" />
                </BaseButton>
            </div>


            <div class="flex items-start w-fit" :style="{
                paddingLeft: !ui.isSidebarCollapsed
                    ? 'calc(var(--sidebar-width) + var(--sidebar-right-gap))'
                    : 'calc(var(--sidebar-collapsed-width) + var(--sidebar-right-gap))'
            }">
                <!-- content -->
                <div class="flex flex-col gap-2">
                    <h1 class="text-title">Dashboard Analytics</h1>
                    <h2 class="text-body">/ Dashboard Analytics</h2>
                </div>
            </div>

        </header>

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
import Logo from '@/components/common/header/Logo.vue'
import Sidebar from '@/components/common/sidebar/Sidebar.vue'
import BaseButton from '@/components/ui/button/BaseButton.vue'
import BaseIcon from '@/components/ui/icon/BaseIcon.vue'
import { ChevronLeft, Search, Home, Users, Settings } from 'lucide-vue-next'
import { useUIStore } from '@/store/ui.store'
const ui = useUIStore()
</script>