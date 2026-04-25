<script setup lang="ts">
defineProps<{
  variant?: 'primary' | 'secondary'
  width?: 'sm' | 'md' | 'lg' | 'full'
  height?: 'sm' | 'md' | 'lg'
  text?: 'sm' | 'md' | 'base'
  iconOnly?: boolean
  rounded?: 'none' | 'sm' | 'md' | 'lg' | '2xl' | 'full'
}>()

</script>

<template>
  <button :class="[
    'transition',

    /* SIZE */
    width === 'sm' && 'px-3',
    width === 'md' && 'px-4',
    width === 'lg' && 'px-6',
    width === 'full' && 'w-full',

    height === 'sm' && 'py-1',
    height === 'md' && 'py-2',
    height === 'lg' && 'py-3',

    /* TEXT */
    text === 'sm' && 'text-sm',
    text === 'md' && 'text-md',
    text === 'base' && 'text-base',
    /* ICON ONLY */
    iconOnly && 'p-2 aspect-square hover:bg-overlay-dark-10',

    /* VARIANT */
    variant === 'primary' && 'bg-accent text-text-primary hover:brightness-110',
    variant === 'secondary' && 'border border-accent text-text-primary hover:bg-accent',
    variant === 'ghost' && 'text-text-secondary hover:text-text-primary',

    /* ROUNDED */
    rounded === 'none' && 'rounded-none',
    rounded === 'sm' && 'rounded-sm',
    rounded === 'md' && 'rounded-md',
    rounded === 'lg' && 'rounded-lg',
    rounded === '2xl' && 'rounded-2xl',
    rounded === 'full' && 'rounded-full'
  ]">
    <slot />
  </button>
</template>