<script setup>
import { computed, onMounted, ref } from 'vue';
import api from '../services/api';
import { useAuth } from '../composables/useAuth';
import SectionCard from '../components/ui/SectionCard.vue';
import StatCard from '../components/ui/StatCard.vue';
import StatusBadge from '../components/ui/StatusBadge.vue';
import InlineAlert from '../components/ui/InlineAlert.vue';

const { state: authState } = useAuth();

const loading = ref(false);
const errorMessage = ref('');
const flights = ref([]);
const checkins = ref([]);
const notifications = ref([]);

const passengerFlights = computed(() => {
  if (!checkins.value.length) return [];
  const flightIds = new Set(checkins.value.map((item) => String(item.flightId)));
  return flights.value.filter((flight) => flight && flightIds.has(String(flight.id)));
});

const nextFlight = computed(() => {
  const upcoming = [...passengerFlights.value]
    .filter((flight) => flight?.scheduledDeparture)
    .sort((a, b) => new Date(a.scheduledDeparture).getTime() - new Date(b.scheduledDeparture).getTime());
  return upcoming[0] || null;
});

const loadDashboard = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    const flightPromise = api.getFlights();
    const noticePromise = authState.userId ? api.getNotificationsByRecipient(authState.userId) : Promise.resolve([]);
    const checkinPromise = authState.userId ? api.getCheckinsByPassenger(authState.userId) : Promise.resolve([]);

    const [flightData, noticeData, checkinData] = await Promise.all([
      flightPromise,
      noticePromise,
      checkinPromise,
    ]);
    flights.value = flightData || [];
    notifications.value = noticeData || [];
    checkins.value = checkinData || [];
  } catch (err) {
    errorMessage.value = 'Some services are temporarily unavailable.';
  } finally {
    loading.value = false;
  }
};

onMounted(loadDashboard);
</script>

<template>
  <div class="space-y-6">
    <header class="rounded-3xl bg-gradient-to-r from-indigo-600 via-slate-900 to-slate-950 p-6 text-white shadow-xl">
      <p class="text-xs font-semibold uppercase tracking-[0.3em] text-indigo-200">Passenger Workspace</p>
      <h1 class="mt-3 text-2xl font-semibold">Welcome back, {{ authState.username || 'Traveler' }}.</h1>
      <p class="mt-2 text-sm text-indigo-100">Your next flight and alerts are ready below.</p>
    </header>

    <InlineAlert
      v-if="errorMessage"
      tone="warning"
      :message="errorMessage"
      action-label="Retry"
      @action="loadDashboard"
    />

    <div class="grid gap-4 md:grid-cols-3">
      <StatCard label="Upcoming Flights" :value="passengerFlights.length" hint="Flight Scheduling" />
      <StatCard label="Active Alerts" :value="notifications.length" hint="Notification Service" />
      <StatCard label="Next Flight" :value="nextFlight?.flightNumber || 'TBD'" hint="Based on departure time" />
    </div>

    <SectionCard title="My Next Flight" subtitle="Keep track of your departure and boarding window.">
      <div v-if="!nextFlight" class="rounded-xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-slate-300">
        No upcoming flights found. Use the Flight Board to browse schedules.
      </div>
      <div v-else class="grid gap-4 md:grid-cols-[2fr_1fr]">
        <div>
          <p class="text-lg font-semibold text-white">{{ nextFlight.origin }} → {{ nextFlight.destination }}</p>
          <p class="mt-1 text-sm text-slate-300">Flight {{ nextFlight.flightNumber }} · {{ nextFlight.airlineCode || 'SkyPort' }}</p>
          <p class="mt-3 text-sm text-slate-300">Departure: {{ nextFlight.scheduledDeparture || 'TBD' }}</p>
        </div>
        <div class="flex items-center justify-end">
          <StatusBadge :status="nextFlight.status" />
        </div>
      </div>
    </SectionCard>

    <SectionCard title="Current Alerts" subtitle="Latest updates delivered for your itinerary.">
      <div v-if="!notifications.length" class="rounded-xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-slate-300">
        No alerts yet. You will see updates here as they arrive.
      </div>
      <div v-else class="space-y-3">
        <div v-for="note in notifications.slice(0, 3)" :key="note.id" class="rounded-xl border border-white/10 bg-white/5 px-4 py-3">
          <div class="flex items-center justify-between">
            <p class="text-sm font-semibold text-white">{{ note.subject || 'Notification' }}</p>
            <StatusBadge :status="note.status" />
          </div>
          <p class="mt-2 text-sm text-slate-300">{{ note.body || 'No message' }}</p>
        </div>
      </div>
    </SectionCard>
  </div>
</template>
