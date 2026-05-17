<script setup>
import { onMounted, ref } from 'vue';
import api from '../services/api';
import SectionCard from '../components/ui/SectionCard.vue';
import StatCard from '../components/ui/StatCard.vue';
import InlineAlert from '../components/ui/InlineAlert.vue';

const loading = ref(false);
const errorMessage = ref('');
const flights = ref([]);
const gates = ref([]);
const notifications = ref([]);

const loadOverview = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    const [flightData, gateData, noticeData] = await Promise.all([
      api.getFlights(),
      api.getGates(),
      api.getNotifications(),
    ]);
    flights.value = flightData || [];
    gates.value = gateData || [];
    notifications.value = noticeData || [];
  } catch (err) {
    errorMessage.value = 'Unable to load operations overview.';
  } finally {
    loading.value = false;
  }
};

const occupiedGates = () => gates.value.filter((gate) => String(gate.status || '').toUpperCase() === 'OCCUPIED').length;

onMounted(loadOverview);
</script>

<template>
  <div class="space-y-6">
    <SectionCard title="Operations Overview" subtitle="Live operational metrics across core services.">
      <template #actions>
        <button class="rounded-full border border-white/20 px-3 py-2 text-xs font-semibold text-slate-100" @click="loadOverview">
          Refresh
        </button>
      </template>

      <InlineAlert
        v-if="errorMessage"
        tone="warning"
        :message="errorMessage"
        action-label="Retry"
        @action="loadOverview"
      />

      <div class="grid gap-4 md:grid-cols-4">
        <StatCard label="Total Flights" :value="flights.length" hint="Flight Scheduling" />
        <StatCard label="Gate Occupancy" :value="`${occupiedGates()}/${gates.length}`" hint="Gate Management" />
        <StatCard label="Alerts" :value="notifications.length" hint="Notification Service" />
        <StatCard label="System Health" :value="errorMessage ? 'Degraded' : 'Healthy'" hint="Gateway" />
      </div>
    </SectionCard>
  </div>
</template>
