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
            <button @click="logout"
                class="text-text-secondary hover:text-accent transition-colors text-body relative group">
                <- <span class="absolute left-0 -bottom-1 w-0 h-px bg-accent transition-all group-hover:w-full"></span>
            </button>
        </div>


        <div class="flex items-start w-fit" :style="{
            paddingLeft: !ui.isSidebarCollapsed
                ? 'calc(var(--sidebar-width) + var(--sidebar-right-gap))'
                : 'calc(var(--sidebar-collapsed-width) + var(--sidebar-right-gap))'
        }">
            <!-- content -->
            <div class="flex flex-col gap-1">
                <h1 class="text-title">{{ pageTitle }}</h1>
            </div>
        </div>
    </header>
</template>
<script setup>
import Logo from '@/components/ui/logo/Logo.vue'
import BaseButton from '@/components/ui/button/BaseButton.vue'
import BaseIcon from '@/components/ui/icon/BaseIcon.vue'
import { ChevronLeft, ChevronRight, Search, Home, Users, Settings } from 'lucide-vue-next'
import { useUIStore } from '@/stores/ui.store'
import { useBreadcrumb } from '@/composables/useBreadcrumb'
import { useAuthStore } from '@/stores/auth.store'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()
const ui = useUIStore()

function logout() {
    auth.logout()
    router.push('/')
}
const { pageTitle, breadcrumb } = useBreadcrumb()
</script>