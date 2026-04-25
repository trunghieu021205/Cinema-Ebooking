<template>
    <div class="flex items-center gap-8">
        <BaseButton iconOnly size="sm" rounded="full">
            <SearchIcon />
        </BaseButton>
        <BaseButton variant="primary" size="lg" rounded="2xl">
            Đặt vé ngay
        </BaseButton>
        <AuthSection />
    </div>
</template>
<script setup lang="ts">
import BaseButton from '@/components/ui/button/BaseButton.vue'
import SearchIcon from '@/components/ui/icon/SearchIcon.vue'
import AuthSection from '@/components/common/header/AuthSection.vue'
</script>