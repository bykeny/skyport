<script setup>
import { onMounted, ref } from 'vue';
import api from '../services/api';
import SectionCard from '../components/ui/SectionCard.vue';
import InlineAlert from '../components/ui/InlineAlert.vue';
import StatusBadge from '../components/ui/StatusBadge.vue';

const gates = ref([]);
const assignments = ref([]);
const flights = ref([]);
const loading = ref(false);
const errorMessage = ref('');
const form = ref({ gateId: '', flightId: '' });

const loadGates = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    const [gateData, flightData] = await Promise.all([api.getGates(), api.getFlights()]);
    gates.value = gateData || [];
    flights.value = flightData || [];

    const results = await Promise.allSettled(
      flightData.map((flight) => api.getGateAssignmentByFlight(flight.id))
    );
    assignments.value = results
      .filter((result) => result.status === 'fulfilled')
      .map((result) => result.value);
  } catch (err) {
    errorMessage.value = 'Unable to load gate control data.';
    gates.value = [];
    assignments.value = [];
  } finally {
    loading.value = false;
  }
};

const assignGate = async () => {
  if (!form.value.gateId || !form.value.flightId) return;
  loading.value = true;
  errorMessage.value = '';
  try {
    await api.createGateAssignment({
      gateId: Number(form.value.gateId),
      flightId: Number(form.value.flightId),
      assignedAt: null,
    });
    form.value = { gateId: '', flightId: '' };
    await loadGates();
  } catch (err) {
    errorMessage.value = 'Unable to assign gate.';
  } finally {
    loading.value = false;
  }
};

onMounted(loadGates);
</script>

<template>
  <div class="space-y-6">
    <SectionCard title="Gate Control" subtitle="Assign gates and manage terminal availability.">
      <InlineAlert
        v-if="errorMessage"
        tone="warning"
        :message="errorMessage"
        action-label="Retry"
        @action="loadGates"
      />

      <div class="grid gap-4 md:grid-cols-2">
        <div class="rounded-xl border border-white/10 bg-white/5 p-4">
          <p class="text-sm font-semibold text-white">Assign Gate</p>
          <div class="mt-3 grid gap-3">
            <input v-model="form.gateId" class="rounded-lg border border-white/10 bg-slate-950 px-3 py-2 text-sm text-white" placeholder="Gate ID" />
            <input v-model="form.flightId" class="rounded-lg border border-white/10 bg-slate-950 px-3 py-2 text-sm text-white" placeholder="Flight ID" />
            <button class="rounded-full bg-white px-4 py-2 text-sm font-semibold text-slate-900" @click="assignGate">
              Assign Gate
            </button>
          </div>
        </div>
        <div class="rounded-xl border border-white/10 bg-white/5 p-4">
          <p class="text-sm font-semibold text-white">Active Assignments</p>
          <div class="mt-3 space-y-2">
            <div v-if="!assignments.length" class="text-sm text-slate-300">No assignments yet.</div>
            <div v-for="assignment in assignments" :key="assignment.id" class="rounded-lg border border-white/10 bg-slate-950/40 px-3 py-2 text-sm">
              <p class="font-semibold text-white">Gate {{ assignment.gateNumber || assignment.gateId }} → Flight {{ assignment.flightId }}</p>
              <p class="text-xs text-slate-400">Assigned {{ assignment.assignedAt || 'Just now' }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="mt-6 overflow-x-auto rounded-xl border border-white/10">
        <table class="min-w-full divide-y divide-white/10 text-sm">
          <thead class="bg-white/5 text-left text-xs font-semibold uppercase tracking-[0.2em] text-slate-300">
            <tr>
              <th class="px-4 py-3">Gate</th>
              <th class="px-4 py-3">Terminal</th>
              <th class="px-4 py-3">Type</th>
              <th class="px-4 py-3">Status</th>
            </tr>
          </thead>
          <tbody class="divide-y divide-white/10 bg-slate-950/40">
            <tr v-for="gate in gates" :key="gate.id">
              <td class="px-4 py-3 font-semibold text-white">{{ gate.gateNumber || gate.id }}</td>
              <td class="px-4 py-3 text-slate-300">{{ gate.terminal || 'T1' }}</td>
              <td class="px-4 py-3 text-slate-300">{{ gate.gateType || 'Standard' }}</td>
              <td class="px-4 py-3"><StatusBadge :status="gate.status" /></td>
            </tr>
          </tbody>
        </table>
      </div>
    </SectionCard>
  </div>
</template>
