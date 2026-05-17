<script setup>
import { computed } from 'vue';

const props = defineProps({
  status: {
    type: String,
    default: 'UNKNOWN',
  },
});

const badgeClass = computed(() => {
  const normalized = String(props.status || '').toUpperCase();

  if (['ON_TIME', 'SENT', 'ARRIVED', 'CLEARED', 'DELIVERED', 'AVAILABLE', 'CONFIRMED'].includes(normalized)) {
    return 'bg-emerald-100 text-emerald-800';
  }
  if (['BOARDING', 'DEPARTED', 'IN_TRANSIT', 'CHECKED_IN', 'SCREENED', 'CREATED'].includes(normalized)) {
    return 'bg-indigo-100 text-indigo-800';
  }
  if (['DELAYED', 'PENDING', 'REGISTERED', 'MAINTENANCE'].includes(normalized)) {
    return 'bg-amber-100 text-amber-800';
  }
  if (['CANCELLED', 'FAILED', 'DENIED', 'FLAGGED'].includes(normalized)) {
    return 'bg-red-100 text-red-800';
  }
  return 'bg-slate-100 text-slate-700';
});
</script>

<template>
  <span class="inline-flex items-center rounded-full px-2.5 py-1 text-xs font-semibold" :class="badgeClass">
    {{ status || 'UNKNOWN' }}
  </span>
</template>
