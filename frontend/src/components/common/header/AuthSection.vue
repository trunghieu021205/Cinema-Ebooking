<template>
    <div class="min-w-60 min-h-18 flex items-center justify-end gap-4 cursor-pointer">
        <!-- CHƯA LOGIN -->
        <template v-if="!auth.user">
            <button @click="ui.openLoginModal()"
                class="text-text-secondary hover:text-accent transition-colors text-body relative group">
                Đăng nhập
                <span class="absolute left-0 -bottom-1 w-0 h-px bg-accent transition-all group-hover:w-full"></span>
            </button>
        </template>

        <!-- ĐÃ LOGIN -->
        <template v-else>
            <img :src="auth.user.avatarUrl" class="w-10 h-10 rounded-full" />
            <div class="flex flex-col gap-2">
                <div class="flex items-center gap-2">
                    <MembershipIcon :membership="auth.user.membership"></MembershipIcon>
                    <div class="flex flex-col">
                        <div class="truncate max-w-30 text-body font-medium" :title="auth.user.name">
                            {{ auth.user.name }}
                        </div>
                        <div class="text-[10px] text-text-secondary uppercase" :title="auth.user.membership">
                            {{ auth.user.membership }}
                        </div>
                    </div>
                </div>
                <div class="flex items-center gap-3">
                    <GiftIcon :membership="auth.user.membership"></GiftIcon>
                    <div class="flex max-w-30">
                        <span class="truncate" :title="auth.user.points">
                            {{ auth.user.points }}
                        </span>

                        <!-- chữ Points (fixed) -->
                        <span class="ml-1 shrink-0">
                            Points
                        </span>
                    </div>
                </div>
            </div>
            <button @click="logout"
                class="text-text-secondary hover:text-accent transition-colors text-body relative group">
                <- <span class="absolute left-0 -bottom-1 w-0 h-px bg-accent transition-all group-hover:w-full"></span>
            </button>
        </template>
    </div>
</template>
<script setup lang="ts">
import GiftIcon from '@/components/ui/icon/GiftIcon.vue'
import { useUIStore } from '@/store/ui.store'
import { useAuthStore } from '@/store/auth.store'
import { useRouter } from 'vue-router'
import MembershipIcon from '@/components/ui/icon/MembershipIcon.vue'
const auth = useAuthStore()
const router = useRouter()
const ui = useUIStore()
function logout() {
    auth.logout()
    router.push('/')
}
</script>