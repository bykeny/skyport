<script setup>
defineProps({
  title: {
    type: String,
    required: true,
  },
  subtitle: {
    type: String,
    default: '',
  },
});
</script>

<template>
  <section class="rounded-2xl border border-white/10 bg-slate-900/70 p-6 shadow-sm text-slate-100">
    <header class="flex flex-wrap items-start justify-between gap-4">
      <div>
        <h2 class="text-lg font-semibold text-white">{{ title }}</h2>
        <p v-if="subtitle" class="mt-1 text-sm text-slate-300">{{ subtitle }}</p>
      </div>
      <div class="flex items-center gap-2">
        <slot name="actions" />
      </div>
    </header>
    <div class="mt-5">
      <slot />
    </div>
  </section>
</template>
