<script setup>
defineProps({
  tone: {
    type: String,
    default: 'info',
  },
  message: {
    type: String,
    required: true,
  },
  actionLabel: {
    type: String,
    default: '',
  },
});

const emit = defineEmits(['action']);

const toneStyles = {
  info: 'border-indigo-400/40 bg-indigo-500/10 text-indigo-100',
  warning: 'border-amber-400/40 bg-amber-500/10 text-amber-100',
  danger: 'border-red-400/40 bg-red-500/10 text-red-100',
};
</script>

<template>
  <div class="flex flex-wrap items-center justify-between gap-3 rounded-xl border px-4 py-3 text-sm font-medium" :class="toneStyles[tone] || toneStyles.info">
    <span>{{ message }}</span>
    <button
      v-if="actionLabel"
      type="button"
      class="rounded-full border border-current px-3 py-1 text-xs font-semibold"
      @click="emit('action')"
    >
      {{ actionLabel }}
    </button>
  </div>
</template>
