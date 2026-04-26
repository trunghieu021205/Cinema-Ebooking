<template>
    <div class="admin-theme min-h-screen flex bg-bg-admin-base">

        <!-- SIDEBAR (TEMP) -->
        <aside
            class="fixed flex flex-col py-10 px-6 top-16 left-0 h-screen bg-bg-admin-sidebar transition-all duration-300"
            :style="{ width: sidebarWidth + 'px' }">
            <div class="flex flex-col items-center gap-2">
                <img src="/images/avatars/UserAvatar.jpg" class="w-12 h-12 rounded-full" />
                <h1 class="text-body text-text-admin-primary">Admin's name</h1>
                <h2 class="text-xs text-text-admin-tertiary">ADMIN</h2>
            </div>
            <nav class="flex flex-col gap-2">
                <div class="group w-full relative pl-4 pr-2 py-2 cursor-pointer ">
                    <div class="h-full absolute w-0 bg-accent left-0 top-0 transition-all ease-in-out group-hover:w-2 ">
                    </div>
                    <div class="flex gap-4 items-center text-text-admin-secondary group-hover:text-text-admin-primary">
                        <BaseIcon :icon="Home" :size="16" :scale="1" :stroke-width="1.5" />
                        <span class="text-body">Dashboard</span>
                    </div>
                </div>
            </nav>
        </aside>

        <!-- TOPBAR -->
        <header class="flex flex-col gap-4 w-full h-fit bg-bg-admin-topbar text-text-on-admin-topbar py-4">
            <div class="flex w-fit" :style="{ gap: sidebarRightGap + 'px' }">
                <div class="flex pl-16 items-center justify-between" :style="{ width: sidebarWidth + 'px' }">
                    <Logo isAdminPage />

                    <BaseButton iconOnly size="sm" rounded="full">
                        <BaseIcon :icon="ChevronLeft" :size="24" :scale="1.3" :stroke-width="1.5" />
                    </BaseButton>
                </div>
                <BaseButton iconOnly size="sm" rounded="full">
                    <BaseIcon :icon="Search" :size="16" :scale="1.2" :stroke-width="1.5" />
                </BaseButton>
            </div>


            <div class="flex items-start w-fit" :style="{
                paddingLeft: sidebarWidth + sidebarRightGap + 'px'
            }">
                <!-- content -->
                <div class="flex flex-col gap-2">
                    <h1 class="text-title">Dashboard Analytics</h1>
                    <h2 class="text-body">/ Dashboard Analytics</h2>
                </div>
            </div>

        </header>

        <!-- PAGE CONTENT -->
        <main class="">
            <router-view />
        </main>


    </div>
</template>

<script setup lang="ts">
import Logo from '@/components/common/header/Logo.vue'
import BaseButton from '@/components/ui/button/BaseButton.vue'
import BaseIcon from '@/components/ui/icon/BaseIcon.vue'
import { ChevronLeft, Search, Home, Users, Settings } from 'lucide-vue-next'

const sidebarWidth = 240
const sidebarRightGap = 24
</script>