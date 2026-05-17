<script setup>
import { computed, ref, watch } from 'vue';
import api from '../services/api';
import { useRoute } from 'vue-router';
import { useAuth } from '../composables/useAuth';

const route = useRoute();
const { state: authState } = useAuth();

const activeTab = ref(route.query.tab || 'trips');
const loading = ref(false);
const message = ref('');

const tripQuery = ref({ passengerId: '', flightId: '' });
const checkinForm = ref({ passengerId: '', flightId: '', seatNumber: '', baggageCount: 0 });
const baggageTag = ref('');

const tripResult = ref(null);
const baggageResult = ref(null);
const notifications = ref([]);

watch(() => route.query.tab, (next) => {
  if (next) activeTab.value = String(next);
});

const showMessage = (text) => {
  message.value = text;
  window.setTimeout(() => (message.value = ''), 3000);
};

const fetchTripStatus = async () => {
  if (!tripQuery.value.passengerId || !tripQuery.value.flightId) return;
  loading.value = true;
  try {
    tripResult.value = await api.getPassengerFlightCheckin(tripQuery.value.passengerId, tripQuery.value.flightId);
  } catch (err) {
    tripResult.value = null;
    showMessage('Check-in status not found for this passenger and flight.');
  } finally {
    loading.value = false;
  }
};

const submitCheckin = async () => {
  loading.value = true;
  try {
    await api.createCheckin({
      passengerId: Number(checkinForm.value.passengerId),
      flightId: Number(checkinForm.value.flightId),
      seatNumber: checkinForm.value.seatNumber,
      baggageCount: Number(checkinForm.value.baggageCount || 0),
    });
    showMessage('Check-in created successfully.');
  } catch (err) {
    showMessage('Unable to create check-in.');
  } finally {
    loading.value = false;
  }
};

const searchBaggage = async () => {
  if (!baggageTag.value) return;
  loading.value = true;
  try {
    baggageResult.value = await api.getBaggageByTag(baggageTag.value);
  } catch (err) {
    baggageResult.value = null;
    showMessage('No baggage found for this tag.');
  } finally {
    loading.value = false;
  }
};

const loadNotifications = async () => {
  if (!authState.userId) return;
  loading.value = true;
  try {
    notifications.value = await api.getNotificationsByRecipient(authState.userId);
  } catch (err) {
    notifications.value = [];
  } finally {
    loading.value = false;
  }
};

const tabs = computed(() => [
  { id: 'trips', label: 'My Trips' },
  { id: 'checkin', label: 'Check-in Status' },
  { id: 'baggage', label: 'Baggage Tracker' },
  { id: 'notifications', label: 'Notification Inbox' },
]);
</script>

<template>
  <section class="space-y-8">
    <div class="flex flex-wrap gap-3">
      <button
        v-for="tab in tabs"
        :key="tab.id"
        class="rounded-full border border-white/10 px-4 py-2 text-xs font-semibold uppercase tracking-[0.2em]"
        :class="activeTab === tab.id ? 'bg-white text-slate-900' : 'text-slate-200 hover:bg-white/10'"
        @click="activeTab = tab.id"
      >
        {{ tab.label }}
      </button>
    </div>

    <p v-if="message" class="rounded-xl border border-cyan-400/30 bg-cyan-400/10 px-4 py-3 text-sm text-cyan-100">
      {{ message }}
    </p>

    <div v-if="activeTab === 'trips'" class="rounded-2xl border border-white/10 bg-white/5 p-6">
      <h2 class="text-lg font-semibold text-white">Search My Trip</h2>
      <p class="mt-2 text-sm text-slate-300">Enter your passenger ID and flight ID to view check-in status.</p>
      <div class="mt-4 grid gap-4 sm:grid-cols-2">
        <input v-model="tripQuery.passengerId" class="rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-white" placeholder="Passenger ID" />
        <input v-model="tripQuery.flightId" class="rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-white" placeholder="Flight ID" />
      </div>
      <button class="mt-4 rounded-xl bg-white px-4 py-2 text-sm font-semibold text-slate-900" @click="fetchTripStatus">
        {{ loading ? 'Searching...' : 'Search status' }}
      </button>

      <div v-if="tripResult" class="mt-6 rounded-xl border border-white/10 bg-slate-900/70 p-4 text-sm text-slate-200">
        <p class="font-semibold text-white">Check-in Status: {{ tripResult.status || 'UNKNOWN' }}</p>
        <p class="mt-2">Seat: {{ tripResult.seatNumber || 'TBD' }}</p>
        <p class="mt-1">Baggage Count: {{ tripResult.baggageCount ?? 0 }}</p>
      </div>
    </div>

    <div v-if="activeTab === 'checkin'" class="rounded-2xl border border-white/10 bg-white/5 p-6">
      <h2 class="text-lg font-semibold text-white">Check-in Portal</h2>
      <p class="mt-2 text-sm text-slate-300">Submit your check-in details to generate a boarding pass.</p>
      <div class="mt-4 grid gap-4 sm:grid-cols-2">
        <input v-model="checkinForm.passengerId" class="rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-white" placeholder="Passenger ID" />
        <input v-model="checkinForm.flightId" class="rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-white" placeholder="Flight ID" />
      </div>
      <div class="mt-4 grid gap-4 sm:grid-cols-2">
        <input v-model="checkinForm.seatNumber" class="rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-white" placeholder="Seat Number" />
        <input v-model="checkinForm.baggageCount" type="number" min="0" class="rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-white" placeholder="Baggage Count" />
      </div>
      <button class="mt-4 rounded-xl bg-white px-4 py-2 text-sm font-semibold text-slate-900" @click="submitCheckin">
        {{ loading ? 'Submitting...' : 'Complete Check-in' }}
      </button>
    </div>

    <div v-if="activeTab === 'baggage'" class="rounded-2xl border border-white/10 bg-white/5 p-6">
      <h2 class="text-lg font-semibold text-white">Baggage Tracker</h2>
      <p class="mt-2 text-sm text-slate-300">Enter your bag tag number to see its current location.</p>
      <div class="mt-4 flex flex-col gap-4 sm:flex-row">
        <input v-model="baggageTag" class="flex-1 rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-white" placeholder="Tag Number" />
        <button class="rounded-xl bg-white px-4 py-2 text-sm font-semibold text-slate-900" @click="searchBaggage">
          {{ loading ? 'Searching...' : 'Track Bag' }}
        </button>
      </div>
      <div v-if="baggageResult" class="mt-6 rounded-xl border border-white/10 bg-slate-900/70 p-4 text-sm text-slate-200">
        <p class="font-semibold text-white">Status: {{ baggageResult.status || 'UNKNOWN' }}</p>
        <p class="mt-2">Location: {{ baggageResult.location || baggageResult.carousel || 'In transit' }}</p>
        <p class="mt-1">Weight: {{ baggageResult.weightValue || 'N/A' }} {{ baggageResult.weightUnit || '' }}</p>
      </div>
    </div>

    <div v-if="activeTab === 'notifications'" class="rounded-2xl border border-white/10 bg-white/5 p-6">
      <div class="flex items-center justify-between">
        <h2 class="text-lg font-semibold text-white">Notification Inbox</h2>
        <button class="rounded-xl border border-white/10 px-3 py-2 text-xs font-semibold uppercase tracking-[0.2em] text-slate-200" @click="loadNotifications">Refresh</button>
      </div>
      <div class="mt-4 space-y-3">
        <div v-if="!notifications.length" class="rounded-xl border border-white/10 bg-slate-900/70 px-4 py-3 text-sm text-slate-300">
          No notifications yet.
        </div>
        <div v-for="note in notifications" :key="note.id" class="rounded-xl border border-white/10 bg-slate-900/70 px-4 py-3 text-sm text-slate-200">
          <p class="font-semibold text-white">{{ note.subject || 'Notification' }}</p>
          <p class="mt-1 text-sm text-slate-300">{{ note.body || 'No message' }}</p>
          <p class="mt-2 text-[10px] uppercase tracking-[0.3em] text-slate-400">{{ note.status || 'UNKNOWN' }}</p>
        </div>
      </div>
    </div>
  </section>
</template>
