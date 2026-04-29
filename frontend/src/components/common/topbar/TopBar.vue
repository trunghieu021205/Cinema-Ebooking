<template>
    <!-- TOPBAR -->
    <header class="flex flex-col gap-2 w-full h-fit bg-bg-admin-topbar text-text-on-admin-topbar py-4">
        <div class="flex w-fit " :style="{ gap: 'var(--sidebar-right-gap)' }">
            <div class="flex pl-16 items-center justify-between" :style="{ width: 'var(--sidebar-width)' }">
                <Logo isAdminPage />

                <BaseButton @click="ui.toggleSidebar()" iconOnly size="sm" rounded="full">
                    <BaseIcon :size="24" :scale="1.1" :icon="ui.isSidebarCollapsed ? ChevronRight : ChevronLeft" />
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
            <div class="flex flex-col gap-1">
                <h1 class="text-title">{{ pageTitle }}</h1>
                <h2 class="text-body">{{ breadcrumb }}</h2>
            </div>
        </div>

    </header>
</template>
<script setup>
import Logo from '@/components/ui/logo/Logo.vue'
import BaseButton from '@/components/ui/button/BaseButton.vue'
import BaseIcon from '@/components/ui/icon/BaseIcon.vue'
import { ChevronLeft, ChevronRight, Search, Home, Users, Settings } from 'lucide-vue-next'
import { useUIStore } from '@/store/ui.store'
import { useBreadcrumb } from '@/composables/useBreadcrumb'
const ui = useUIStore()
const { pageTitle, breadcrumb } = useBreadcrumb()
</script>