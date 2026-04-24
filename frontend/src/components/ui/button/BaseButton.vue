<script setup lang="ts">
defineProps<{
  variant?: 'primary' | 'secondary' | 'ghost'
  size?: 'sm' | 'md' | 'lg'
  iconOnly?: boolean
}>()
</script>

<template>
  <button :class="[
    'transition hover:brightness-110',

    /* SIZE */
    size === 'sm' && 'px-3 py-1 text-sm',
    size === 'md' && 'px-4 py-2 text-sm',
    size === 'lg' && 'px-6 py-3 text-base',

    /* ICON ONLY */
    iconOnly && 'p-2 aspect-square hover:bg-overlay-dark-10 rounded-full',

    /* VARIANT */
    variant === 'primary' && 'bg-accent text-primary rounded-2xl',
    variant === 'secondary' && 'border border-border-default text-text-primary hover:bg-bg-surface rounded-lg',
    variant === 'ghost' && 'text-text-secondary hover:text-text-primary'
  ]">
    <slot />
  </button>
</template>