<script setup>
import { computed, ref } from 'vue';
import api from '../services/api';
import SectionCard from '../components/ui/SectionCard.vue';
import InlineAlert from '../components/ui/InlineAlert.vue';

const tagNumber = ref('');
const baggage = ref(null);
const loading = ref(false);
const errorMessage = ref('');

const timelineSteps = [
  { id: 'REGISTERED', label: 'Checked-in' },
  { id: 'IN_TRANSIT', label: 'In Transit' },
  { id: 'ARRIVED', label: 'Arrived' },
  { id: 'DELIVERED', label: 'Delivered' },
];

const currentIndex = computed(() => {
  if (!baggage.value?.status) return -1;
  const normalized = String(baggage.value.status).toUpperCase();
  return timelineSteps.findIndex((step) => step.id === normalized);
});

const searchBaggage = async () => {
  if (!tagNumber.value) return;
  loading.value = true;
  errorMessage.value = '';
  try {
    baggage.value = await api.getBaggageByTag(tagNumber.value);
  } catch (err) {
    baggage.value = null;
    errorMessage.value = 'No baggage found for that tag. Please confirm the number and retry.';
  } finally {
    loading.value = false;
  }
};
</script>

<template>
  <div class="space-y-6">
    <SectionCard title="Track My Bag" subtitle="Search a tag number to view the latest baggage status.">
      <div class="flex flex-wrap gap-3">
        <input v-model="tagNumber" class="flex-1 rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-white" placeholder="Tag Number" />
        <button class="rounded-full bg-white px-4 py-2 text-sm font-semibold text-slate-900" @click="searchBaggage">
          {{ loading ? 'Searching...' : 'Track Bag' }}
        </button>
      </div>

      <InlineAlert v-if="errorMessage" tone="warning" :message="errorMessage" />

      <div v-if="baggage" class="mt-6 space-y-4">
        <div class="rounded-xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-slate-200">
          <p class="font-semibold text-white">Tag {{ baggage.tagNumber }}</p>
          <p class="mt-1 text-slate-300">Current location: {{ baggage.location || baggage.carousel || 'In transit' }}</p>
        </div>

        <div class="space-y-3">
          <div
            v-for="(step, index) in timelineSteps"
            :key="step.id"
            class="flex items-center gap-3"
          >
            <span
              class="flex h-8 w-8 items-center justify-center rounded-full border text-xs font-semibold"
              :class="index <= currentIndex ? 'border-emerald-400 bg-emerald-500/20 text-emerald-100' : 'border-white/10 text-slate-400'"
            >
              {{ index + 1 }}
            </span>
            <div>
              <p class="text-sm font-semibold text-white">{{ step.label }}</p>
              <p class="text-xs text-slate-400">{{ step.id }}</p>
            </div>
          </div>
        </div>
      </div>
    </SectionCard>
  </div>
</template>
