<script setup>
defineProps({
  label: {
    type: String,
    required: true,
  },
  variant: {
    type: String,
    default: 'primary',
  },
  disabled: Boolean,
});

const emit = defineEmits(['click']);

const variantClasses = {
  primary: 'bg-white text-slate-900 hover:bg-slate-100',
  ghost: 'border border-white/20 bg-white/5 text-slate-100 hover:bg-white/10',
};
</script>

<template>
  <button
    type="button"
    class="inline-flex items-center justify-center rounded-full px-4 py-2 text-sm font-semibold transition"
    :class="variantClasses[variant] || variantClasses.primary"
    :disabled="disabled"
    @click="emit('click')"
  >
    {{ label }}
  </button>
</template>
