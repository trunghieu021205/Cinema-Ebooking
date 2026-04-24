<template>
    <div class="min-w-60 min-h-18 flex items-center justify-end gap-4 cursor-pointer">
        <!-- CHƯA LOGIN -->
        <template v-if="!auth.user">
            <button @click="loginMock('user')"
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
                    <AwardIcon :membership="auth.user.membership"></AwardIcon>
                    <div class="flex flex-col">
                        <div class="truncate max-w-30 text-body font-medium" :title="auth.user.name">
                            {{ auth.user.name }}
                        </div>
                        <div class="text-[10px] text-text-secondary uppercase" :title="auth.user.membership">
                            {{ auth.user.membership }}
                        </div>
                    </div>
                </div>
                <div class="flex items-center gap-2">
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
import AwardIcon from '@/components/ui/icon/AwardIcon.vue'
import GiftIcon from '@/components/ui/icon/GiftIcon.vue'
import { useAuthStore } from '@/store/auth.store'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

function loginMock(role: 'admin' | 'user') {
    const isAdmin = role === 'admin'

    const mockUser = {
        email: isAdmin ? 'admin@gmail.com' : 'user@gmail.com',
        name: isAdmin ? 'Admin' : 'Nguyễn Trung Hiếu',
        membership: 'gold',
        points: isAdmin ? 9999 : 100000000,
        role,
        avatarUrl: isAdmin
            ? '/images/avatars/admin.jpg'
            : '/images/avatar/UserAvatar.jpg',
    }

    const fakeToken = 'mock-token-123'

    auth.setAuth(mockUser, fakeToken)

    // redirect theo role
    if (role === 'admin') {
        router.push('/admin')
    } else {
        router.push('/')
    }
}

function logout() {
    auth.logout()
    router.push('/')
}
</script>