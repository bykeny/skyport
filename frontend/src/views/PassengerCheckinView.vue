<script setup>
import { computed, ref } from 'vue';
import api from '../services/api';
import SectionCard from '../components/ui/SectionCard.vue';
import InlineAlert from '../components/ui/InlineAlert.vue';
import PrimaryButton from '../components/ui/PrimaryButton.vue';

const step = ref(1);
const loading = ref(false);
const errorMessage = ref('');
const successMessage = ref('');

const form = ref({
  passengerId: '',
  flightId: '',
  seatNumber: '',
  baggageCount: 0,
});

const canContinue = computed(() => form.value.passengerId && form.value.flightId);
const canConfirm = computed(() => form.value.seatNumber);

const validateFlight = async () => {
  const flights = await api.getFlights();
  const exists = flights.some((flight) => String(flight.id) === String(form.value.flightId));
  if (!exists) throw new Error('Flight not found');
};

const nextStep = async () => {
  errorMessage.value = '';
  successMessage.value = '';
  if (step.value === 1) {
    loading.value = true;
    try {
      await validateFlight();
      step.value = 2;
    } catch (err) {
      errorMessage.value = 'Flight ID could not be validated. Please check and try again.';
    } finally {
      loading.value = false;
    }
    return;
  }
  if (step.value === 2) {
    step.value = 3;
  }
};

const submitCheckin = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    await api.createCheckin({
      passengerId: Number(form.value.passengerId),
      flightId: Number(form.value.flightId),
      seatNumber: form.value.seatNumber,
      baggageCount: Number(form.value.baggageCount || 0),
    });
    successMessage.value = 'Check-in confirmed. Your boarding pass will be delivered soon.';
    step.value = 3;
  } catch (err) {
    errorMessage.value = 'Unable to complete check-in right now.';
  } finally {
    loading.value = false;
  }
};

const resetWizard = () => {
  step.value = 1;
  errorMessage.value = '';
  successMessage.value = '';
  form.value = { passengerId: '', flightId: '', seatNumber: '', baggageCount: 0 };
};
</script>

<template>
  <div class="space-y-6">
    <SectionCard title="Passenger Check-in" subtitle="A simple 3-step flow to confirm your seat.">
      <div class="flex flex-wrap gap-3 text-xs font-semibold uppercase tracking-[0.2em]">
        <span :class="step >= 1 ? 'text-slate-900' : 'text-slate-400'">1 · Enter Details</span>
        <span :class="step >= 2 ? 'text-slate-900' : 'text-slate-400'">2 · Confirm Seat</span>
        <span :class="step >= 3 ? 'text-slate-900' : 'text-slate-400'">3 · Done</span>
      </div>

      <InlineAlert v-if="errorMessage" tone="warning" :message="errorMessage" />
      <InlineAlert v-if="successMessage" tone="info" :message="successMessage" />

      <div v-if="step === 1" class="mt-6 grid gap-4 md:grid-cols-2">
        <input v-model="form.passengerId" class="rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-white" placeholder="Passenger ID" />
        <input v-model="form.flightId" class="rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-white" placeholder="Flight ID" />
      </div>

      <div v-if="step === 2" class="mt-6 grid gap-4 md:grid-cols-2">
        <input v-model="form.seatNumber" class="rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-white" placeholder="Seat Number" />
        <input v-model="form.baggageCount" type="number" min="0" class="rounded-xl border border-white/10 bg-slate-950 px-4 py-3 text-sm text-white" placeholder="Baggage Count" />
      </div>

      <div v-if="step === 3" class="mt-6 rounded-xl border border-emerald-200 bg-emerald-50 px-4 py-4 text-sm text-emerald-700">
        Check-in flow completed. You can return to the dashboard or start another check-in.
      </div>

      <div class="mt-6 flex flex-wrap gap-3">
        <PrimaryButton
          v-if="step < 3"
          :label="step === 2 ? 'Confirm Check-in' : 'Continue'"
          :disabled="loading || (step === 1 ? !canContinue : !canConfirm)"
          @click="step === 2 ? submitCheckin() : nextStep()"
        />
        <PrimaryButton v-if="step === 3" label="Start Another" variant="ghost" @click="resetWizard" />
      </div>
    </SectionCard>
  </div>
</template>
