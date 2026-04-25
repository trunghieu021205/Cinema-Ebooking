<template>
    <div class="flex items-center gap-8">
        <BaseButton iconOnly rounded="full">
            <SearchIcon />
        </BaseButton>
        <BaseButton variant="primary" width="lg" height="lg" text="base" rounded="2xl">
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