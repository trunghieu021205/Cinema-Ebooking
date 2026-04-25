<script setup lang="ts">
import { computed } from 'vue'

type Variant = 'primary' | 'secondary' | 'ghost'
type Size = 'sm' | 'md' | 'lg'
type Rounded = 'none' | 'sm' | 'md' | 'lg' | '2xl' | 'full'

const props = defineProps<{
  variant?: Variant
  size?: Size
  rounded?: Rounded
  iconOnly?: boolean
}>()

/* ================= CONFIG ================= */

const base =
  'inline-flex items-center justify-center transition focus:outline-none'

const variants: Record<Variant, string> = {
  primary: 'bg-accent text-text-primary hover:brightness-110',
  secondary: 'border border-accent text-text-primary hover:bg-accent',
  ghost: 'text-text-secondary hover:text-text-primary'
}

const sizes: Record<Size, string> = {
  sm: 'px-3 py-1 text-sm',
  md: 'px-4 py-2 text-md',
  lg: 'px-6 py-3 text-base'
}

const roundeds: Record<Rounded, string> = {
  none: 'rounded-none',
  sm: 'rounded-sm',
  md: 'rounded-md',
  lg: 'rounded-lg',
  '2xl': 'rounded-2xl',
  full: 'rounded-full'
}

/* ============ COMPOUND VARIANTS ============ */

function getCompoundClasses() {
  const classes: string[] = []

  // iconOnly override size
  if (props.iconOnly) {
    classes.push('aspect-square')

    if (props.size === 'sm') classes.push('w-8 h-8')
    if (props.size === 'md') classes.push('w-10 h-10')
    if (props.size === 'lg') classes.push('w-12 h-12')
    classes.push('bg-transparent')

    if (props.variant === 'primary') {
      classes.push('hover:bg-overlay-dark-10')
    }

    if (props.variant === 'secondary') {
      classes.push('hover:bg-overlay-dark-10 border-none')
    }

    if (props.variant === 'ghost' || !props.variant) {
      classes.push('hover:bg-overlay-dark-10')
    }
  }

  return classes
}

/* ================= FINAL CLASS ================= */

const buttonClass = computed(() => [
  base,

  variants[props.variant ?? 'primary'],
  !props.iconOnly && sizes[props.size ?? 'md'],
  roundeds[props.rounded ?? 'md'],

  ...getCompoundClasses()
])
</script>

<template>
  <button :class="buttonClass">
    <slot />
  </button>
</template>