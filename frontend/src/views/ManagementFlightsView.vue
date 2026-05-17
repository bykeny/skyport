<script setup>
import { onMounted, ref } from 'vue';
import api from '../services/api';
import SectionCard from '../components/ui/SectionCard.vue';
import InlineAlert from '../components/ui/InlineAlert.vue';
import StatusBadge from '../components/ui/StatusBadge.vue';

const flights = ref([]);
const loading = ref(false);
const errorMessage = ref('');
const showCreate = ref(false);
const statusUpdate = ref({});
const form = ref({
  flightNumber: '',
  origin: '',
  destination: '',
  scheduledDeparture: '',
  scheduledArrival: '',
});

const loadFlights = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    flights.value = await api.getFlights();
  } catch (err) {
    flights.value = [];
    errorMessage.value = 'Unable to load flight control data.';
  } finally {
    loading.value = false;
  }
};

const createFlight = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    await api.createFlight({ ...form.value });
    showCreate.value = false;
    form.value = { flightNumber: '', origin: '', destination: '', scheduledDeparture: '', scheduledArrival: '' };
    await loadFlights();
  } catch (err) {
    errorMessage.value = 'Unable to create flight right now.';
  } finally {
    loading.value = false;
  }
};

const updateStatus = async (flightId) => {
  const nextStatus = statusUpdate.value[flightId];
  if (!nextStatus) return;
  loading.value = true;
  errorMessage.value = '';
  try {
    await api.updateFlightStatus(flightId, { status: nextStatus });
    await loadFlights();
  } catch (err) {
    errorMessage.value = 'Unable to update flight status.';
  } finally {
    loading.value = false;
  }
};

onMounted(loadFlights);
</script>

<template>
  <div class="space-y-6">
    <SectionCard title="Flight Control" subtitle="Create flights and update statuses in real time.">
      <template #actions>
        <button class="rounded-full bg-slate-900 px-4 py-2 text-xs font-semibold text-white" @click="showCreate = true">
          Create Flight
        </button>
      </template>

      <InlineAlert
        v-if="errorMessage"
        tone="warning"
        :message="errorMessage"
        action-label="Retry"
        @action="loadFlights"
      />

      <div v-if="loading" class="rounded-xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-slate-300">
        Loading flight control...
      </div>
      <div v-else class="overflow-x-auto rounded-xl border border-white/10">
        <table class="min-w-full divide-y divide-white/10 text-sm">
          <thead class="bg-white/5 text-left text-xs font-semibold uppercase tracking-[0.2em] text-slate-300">
            <tr>
              <th class="px-4 py-3">Flight</th>
              <th class="px-4 py-3">Route</th>
              <th class="px-4 py-3">Departure</th>
              <th class="px-4 py-3">Status</th>
              <th class="px-4 py-3">Actions</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-white/10 bg-slate-950/40">
            <tr v-for="flight in flights" :key="flight.id">
              <td class="px-4 py-3 font-semibold text-white">{{ flight.flightNumber || 'N/A' }}</td>
              <td class="px-4 py-3 text-slate-300">{{ flight.origin }} → {{ flight.destination }}</td>
              <td class="px-4 py-3 text-slate-300">{{ flight.scheduledDeparture || 'TBD' }}</td>
              <td class="px-4 py-3"><StatusBadge :status="flight.status" /></td>
              <td class="px-4 py-3">
                <div class="flex flex-wrap items-center gap-2">
                  <select v-model="statusUpdate[flight.id]" class="rounded-lg border border-white/10 bg-slate-950 px-2 py-1 text-xs text-slate-100">
                    <option value="">Select status</option>
                    <option value="SCHEDULED">SCHEDULED</option>
                    <option value="ON_TIME">ON_TIME</option>
                    <option value="BOARDING">BOARDING</option>
                    <option value="DELAYED">DELAYED</option>
                    <option value="CANCELLED">CANCELLED</option>
                    <option value="DEPARTED">DEPARTED</option>
                    <option value="ARRIVED">ARRIVED</option>
                  </select>
                  <button class="rounded-full border border-white/20 px-3 py-1 text-xs font-semibold text-slate-100" @click="updateStatus(flight.id)">
                    Update
                  </button>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </SectionCard>

    <transition
      enter-active-class="transition duration-200"
      enter-from-class="opacity-0 translate-y-4"
      enter-to-class="opacity-100 translate-y-0"
      leave-active-class="transition duration-150"
      leave-from-class="opacity-100"
      leave-to-class="opacity-0"
    >
      <div v-if="showCreate" class="fixed inset-0 z-40 flex items-center justify-center bg-slate-900/60 px-4">
        <div class="w-full max-w-xl rounded-2xl bg-slate-950 p-6 shadow-2xl text-slate-100">
          <div class="flex items-center justify-between">
            <h3 class="text-lg font-semibold text-white">Create Flight</h3>
            <button class="text-sm text-slate-300" @click="showCreate = false">Close</button>
          </div>
          <div class="mt-4 grid gap-4 md:grid-cols-2">
            <input v-model="form.flightNumber" class="rounded-xl border border-white/10 bg-slate-900 px-3 py-2 text-sm text-white" placeholder="Flight Number" />
            <input v-model="form.origin" class="rounded-xl border border-white/10 bg-slate-900 px-3 py-2 text-sm text-white" placeholder="Origin" />
            <input v-model="form.destination" class="rounded-xl border border-white/10 bg-slate-900 px-3 py-2 text-sm text-white" placeholder="Destination" />
            <input v-model="form.scheduledDeparture" type="datetime-local" class="rounded-xl border border-white/10 bg-slate-900 px-3 py-2 text-sm text-white" />
            <input v-model="form.scheduledArrival" type="datetime-local" class="rounded-xl border border-white/10 bg-slate-900 px-3 py-2 text-sm text-white" />
          </div>
          <div class="mt-6 flex justify-end gap-3">
            <button class="rounded-full border border-white/20 px-4 py-2 text-sm" @click="showCreate = false">Cancel</button>
            <button class="rounded-full bg-white px-4 py-2 text-sm font-semibold text-slate-900" @click="createFlight">
              Create
            </button>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>
