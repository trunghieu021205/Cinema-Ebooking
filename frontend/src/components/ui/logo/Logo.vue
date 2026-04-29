<template>
    <a class="flex flex-row items-center gap-2" href="/">
        <img :class="isAdminPage ? 'w-8' : 'w-12'" class="aspect-square" src="/src/assets/images/Logo.png"
            alt="CineLux Logo" />
        <h1 class="text-brand font-brand text-accent" :class="isAdminPage ? 'text-[20px]' : ''">
            CineLux
        </h1>
    </a>
</template>

<script setup lang="ts">
defineProps<{
    isAdminPage?: boolean
}>()
</script>