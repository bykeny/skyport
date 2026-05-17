<script setup>
defineProps({
  label: {
    type: String,
    required: true,
  },
  value: {
    type: [String, Number],
    required: true,
  },
  hint: {
    type: String,
    default: '',
  },
});
</script>

<template>
  <div class="rounded-2xl border border-white/10 bg-slate-900/70 p-5 shadow-sm">
    <p class="text-xs font-semibold uppercase tracking-[0.2em] text-slate-400">{{ label }}</p>
    <p class="mt-3 text-3xl font-semibold text-white">{{ value }}</p>
    <p v-if="hint" class="mt-2 text-xs text-slate-300">{{ hint }}</p>
  </div>
</template>
