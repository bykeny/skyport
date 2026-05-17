<script setup>
import { onMounted, ref } from 'vue';
import api from '../services/api';
import StatusBadge from '../components/ui/StatusBadge.vue';

const flights = ref([]);
const loading = ref(false);
const errorMessage = ref('');

const loadFlights = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    flights.value = await api.getFlights();
  } catch (err) {
    errorMessage.value = 'Flight board is temporarily unavailable.';
  } finally {
    loading.value = false;
  }
};

onMounted(loadFlights);
</script>

<template>
  <section class="px-6 py-14">
    <div class="mx-auto max-w-6xl">
      <header class="flex flex-wrap items-center justify-between gap-4">
        <p class="text-xs font-semibold uppercase tracking-[0.35em] text-cyan-200">Live Flight Board</p>
        <h1 class="text-3xl font-semibold text-white">Departures & Arrivals</h1>
        <button class="rounded-full border border-white/20 px-4 py-2 text-xs font-semibold text-white" @click="loadFlights">
          Refresh
        </button>
      </header>

      <div v-if="errorMessage" class="mt-6 rounded-xl border border-red-500/40 bg-red-500/10 px-4 py-3 text-sm text-red-200">
        {{ errorMessage }}
      </div>

      <div class="mt-6 overflow-x-auto rounded-2xl border border-white/10 bg-white/5">
        <table class="min-w-full divide-y divide-white/10 text-sm">
          <thead class="bg-white/5 text-left text-xs font-semibold uppercase tracking-[0.25em] text-slate-300">
            <tr>
              <th class="px-4 py-3">Flight</th>
              <th class="px-4 py-3">Route</th>
              <th class="px-4 py-3">Departure</th>
              <th class="px-4 py-3">Status</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-white/10 text-slate-200">
            <tr v-if="loading">
              <td colspan="4" class="px-4 py-6 text-center text-sm text-slate-400">Loading flights...</td>
            </tr>
            <tr v-else-if="!flights.length">
              <td colspan="4" class="px-4 py-6 text-center text-sm text-slate-400">No flights available.</td>
            </tr>
            <tr v-for="flight in flights" :key="flight.id">
              <td class="px-4 py-3 font-semibold text-white">{{ flight.flightNumber || 'N/A' }}</td>
              <td class="px-4 py-3 text-slate-300">{{ flight.origin }} → {{ flight.destination }}</td>
              <td class="px-4 py-3 text-slate-300">{{ flight.scheduledDeparture || 'TBD' }}</td>
              <td class="px-4 py-3"><StatusBadge :status="flight.status" /></td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>
