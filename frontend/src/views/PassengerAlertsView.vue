<script setup>
import { onMounted, ref } from 'vue';
import api from '../services/api';
import { useAuth } from '../composables/useAuth';
import SectionCard from '../components/ui/SectionCard.vue';
import InlineAlert from '../components/ui/InlineAlert.vue';
import StatusBadge from '../components/ui/StatusBadge.vue';

const { state: authState } = useAuth();
const notifications = ref([]);
const loading = ref(false);
const errorMessage = ref('');

const loadNotifications = async () => {
  if (!authState.userId) return;
  loading.value = true;
  errorMessage.value = '';
  try {
    notifications.value = await api.getNotificationsByRecipient(authState.userId);
  } catch (err) {
    notifications.value = [];
    errorMessage.value = 'Unable to load notifications right now.';
  } finally {
    loading.value = false;
  }
};

onMounted(loadNotifications);
</script>

<template>
  <div class="space-y-6">
    <SectionCard title="Notification Center" subtitle="Personalized Kafka-driven updates delivered in real time.">
      <template #actions>
        <button class="rounded-full border border-slate-200 px-3 py-2 text-xs font-semibold" @click="loadNotifications">
          Refresh
        </button>
      </template>

      <InlineAlert
        v-if="errorMessage"
        tone="warning"
        :message="errorMessage"
        action-label="Retry"
        @action="loadNotifications"
      />

      <div v-if="loading" class="rounded-xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-slate-300">
        Loading alerts...
      </div>
      <div v-else-if="!notifications.length" class="rounded-xl border border-white/10 bg-white/5 px-4 py-3 text-sm text-slate-300">
        No alerts yet.
      </div>
      <div v-else class="space-y-3">
        <div v-for="note in notifications" :key="note.id" class="rounded-xl border border-white/10 bg-white/5 px-4 py-3">
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
