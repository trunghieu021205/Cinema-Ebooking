<template>
    <div class="flex items-center gap-8">
        <BaseButton iconOnly size="sm" rounded="full">
            <BaseIcon :icon="Search" :size="16" :scale="1.2" :stroke-width="1.5" />
        </BaseButton>
        <BaseButton variant="primary" size="lg" rounded="2xl">
            Đặt vé ngay
        </BaseButton>
        <AuthSection />
    </div>
</template>
<script setup lang="ts">
import BaseButton from '@/components/ui/button/BaseButton.vue'
import { Search } from 'lucide-vue-next'
import AuthSection from '@/components/common/header/subcomponents/AuthSection.vue'
import BaseIcon from '@/components/ui/icon/BaseIcon.vue';
</script>