<script setup>
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import api from '../services/api';
import StatusBadge from '../components/ui/StatusBadge.vue';

const router = useRouter();
const flights = ref([]);
const loading = ref(false);
const errorMessage = ref('');

const loadFlights = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    flights.value = await api.getFlights();
  } catch (err) {
    flights.value = [];
    errorMessage.value = 'Live flight board is temporarily unavailable.';
  } finally {
    loading.value = false;
  }
};

onMounted(loadFlights);
</script>

<template>
  <section class="relative overflow-hidden px-6 py-16 sm:py-20">
    <div class="absolute inset-0 bg-[radial-gradient(circle_at_top,_rgba(56,189,248,0.25),_transparent_55%),radial-gradient(circle_at_bottom,_rgba(99,102,241,0.25),_transparent_50%)]"></div>
    <div class="relative mx-auto flex max-w-6xl flex-col gap-12">
      <header class="space-y-4">
        <p class="text-xs font-semibold uppercase tracking-[0.4em] text-cyan-200">SkyPort Travel Platform</p>
        <h1 class="text-4xl font-semibold leading-tight text-white sm:text-5xl">
          Travel operations, passenger journeys, and airport services in one live system.
        </h1>
        <p class="max-w-2xl text-base text-slate-300">
          Monitor flight movements, check-in progress, gate assignments, and duty-free performance with a unified, secure portal.
        </p>
        <div class="flex flex-wrap gap-4">
          <button
            class="inline-flex items-center gap-2 rounded-full bg-white px-5 py-2 text-sm font-semibold text-slate-900 transition hover:bg-slate-100"
            @click="router.push({ name: 'flight-board' })"
          >
            Open Flight Board
            <span>→</span>
          </button>
          <button
            class="inline-flex items-center gap-2 rounded-full border border-white/20 px-5 py-2 text-sm font-semibold text-white transition hover:bg-white/10"
            @click="router.push({ name: 'auth' })"
          >
            Sign in to continue
            <span>→</span>
          </button>
        </div>
      </header>

      <div class="rounded-3xl border border-white/10 bg-slate-900/70 p-6 shadow-2xl">
        <div class="flex flex-wrap items-center justify-between gap-4">
          <div>
            <p class="text-xs font-semibold uppercase tracking-[0.3em] text-indigo-200">Live Flight Board</p>
            <p class="mt-2 text-lg font-semibold text-white">Arrivals and departures updated on demand.</p>
          </div>
          <button class="rounded-full border border-white/20 px-4 py-2 text-xs font-semibold text-white" @click="loadFlights">
            Refresh
          </button>
        </div>

        <div v-if="errorMessage" class="mt-4 rounded-xl border border-red-500/40 bg-red-500/10 px-4 py-3 text-sm text-red-200">
          {{ errorMessage }}
        </div>

        <div class="mt-4 overflow-x-auto rounded-2xl border border-white/10">
          <table class="min-w-full divide-y divide-white/10 text-sm">
            <thead class="bg-white/5 text-left text-xs font-semibold uppercase tracking-[0.2em] text-slate-300">
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
                <td class="px-4 py-3">
                  <StatusBadge :status="flight.status" />
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </section>
</template>
