<script setup>
import { computed, onMounted, onUnmounted, ref, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import api from '../services/api';
import { useAuth } from '../composables/useAuth';

const router = useRouter();
const route = useRoute();
const { state: authState, clearAuth } = useAuth();

const isManagementRoute = computed(() => String(route.path || '').startsWith('/management'));

const profileOpen = ref(false);
const notificationsOpen = ref(false);
const notifications = ref([]);
const lastSeenAt = ref(0);
const sentCount = computed(() => notifications.value.filter((item) => String(item.status || '').toUpperCase() === 'SENT').length);
const hasUnread = computed(() => notifications.value.some((item) => {
  if (!item?.sentAt) return false;
  const sentTime = new Date(item.sentAt).getTime();
  return sentTime > lastSeenAt.value;
}));

let pollingTimer = null;

const loadNotifications = async () => {
  if (!authState.userId) return;
  try {
    notifications.value = await api.getNotificationsByRecipient(authState.userId);
  } catch (err) {
    notifications.value = [];
  }
};

const getSeenKey = () => (authState.userId ? `ams_notifications_seen_${authState.userId}` : '');

const loadLastSeen = () => {
  const key = getSeenKey();
  if (!key) return;
  const stored = Number(localStorage.getItem(key));
  lastSeenAt.value = Number.isNaN(stored) ? 0 : stored;
};

const markNotificationsSeen = () => {
  const key = getSeenKey();
  if (!key) return;
  lastSeenAt.value = Date.now();
  localStorage.setItem(key, String(lastSeenAt.value));
};

const startPolling = () => {
  if (pollingTimer) window.clearInterval(pollingTimer);
  loadNotifications();
  pollingTimer = window.setInterval(loadNotifications, 30000);
};

const stopPolling = () => {
  if (pollingTimer) window.clearInterval(pollingTimer);
  pollingTimer = null;
};

const logout = () => {
  clearAuth();
  stopPolling();
  router.push({ name: 'auth' });
};

const isPassenger = computed(() => authState.role === 'PASSENGER');
const isManagement = computed(() => authState.role === 'ADMIN' || authState.role === 'STAFF');

watch(() => authState.userId, (next) => {
  if (next) {
    loadLastSeen();
    startPolling();
  } else {
    stopPolling();
  }
});

watch(() => notificationsOpen.value, (open) => {
  if (open) markNotificationsSeen();
});

onMounted(() => {
  if (authState.userId) {
    loadLastSeen();
    startPolling();
  }
});

onUnmounted(() => stopPolling());
</script>

<template>
  <div class="min-h-screen bg-slate-950 text-slate-100">
    <header class="border-b border-white/10 bg-slate-950/80 backdrop-blur">
      <div class="mx-auto flex max-w-6xl items-center justify-between px-6 py-4">
        <div class="flex items-center gap-4">
          <div class="flex h-10 w-10 items-center justify-center rounded-full bg-gradient-to-br from-indigo-400 via-sky-400 to-cyan-300 text-slate-900">
            <span class="text-sm font-bold">SP</span>
          </div>
          <div>
            <p class="text-xs font-semibold uppercase tracking-[0.3em] text-cyan-200">SkyPort</p>
            <p class="text-lg font-semibold">Airport Management</p>
          </div>
        </div>

        <nav class="hidden items-center gap-6 text-sm font-semibold text-slate-200 md:flex">
          <button
            v-if="isPassenger"
            class="transition hover:text-white"
            :class="route.name === 'passenger-dashboard' ? 'text-white' : ''"
            @click="router.push({ name: 'passenger-dashboard' })"
          >
            Dashboard
          </button>
          <button
            v-if="isPassenger"
            class="transition hover:text-white"
            :class="route.name === 'passenger-checkin' ? 'text-white' : ''"
            @click="router.push({ name: 'passenger-checkin' })"
          >
            Check-in
          </button>
          <button
            v-if="isPassenger"
            class="transition hover:text-white"
            :class="route.name === 'passenger-baggage' ? 'text-white' : ''"
            @click="router.push({ name: 'passenger-baggage' })"
          >
            Baggage
          </button>
          <button
            v-if="isPassenger"
            class="transition hover:text-white"
            :class="route.name === 'passenger-alerts' ? 'text-white' : ''"
            @click="router.push({ name: 'passenger-alerts' })"
          >
            Alerts
          </button>
          <button
            v-if="isManagement"
            class="transition hover:text-white"
            :class="route.name === 'management-overview' ? 'text-white' : ''"
            @click="router.push({ name: 'management-overview' })"
          >
            Ops Overview
          </button>
          <button
            v-if="isManagement"
            class="transition hover:text-white"
            :class="route.name === 'management-flights' ? 'text-white' : ''"
            @click="router.push({ name: 'management-flights' })"
          >
            Flight Control
          </button>
          <button
            v-if="isManagement"
            class="transition hover:text-white"
            :class="route.name === 'management-gates' ? 'text-white' : ''"
            @click="router.push({ name: 'management-gates' })"
          >
            Gate Control
          </button>
          <button
            v-if="isManagement"
            class="transition hover:text-white"
            :class="route.name === 'management-retail' ? 'text-white' : ''"
            @click="router.push({ name: 'management-retail' })"
          >
            Retail
          </button>
        </nav>

        <div class="flex items-center gap-3">
          <div class="relative">
            <button
              class="relative flex h-10 w-10 items-center justify-center rounded-full border border-white/10 bg-white/5 transition hover:bg-white/10"
              @click="notificationsOpen = !notificationsOpen"
            >
              <span class="text-lg">🔔</span>
              <span v-if="hasUnread" class="absolute -right-0.5 -top-0.5 h-2.5 w-2.5 rounded-full bg-red-500"></span>
              <span v-if="sentCount" class="absolute -right-1 -top-1 flex h-5 min-w-[1.25rem] items-center justify-center rounded-full bg-amber-400 px-1 text-xs font-bold text-slate-900">
                {{ sentCount }}
              </span>
            </button>

            <div
              v-if="notificationsOpen"
              class="absolute right-0 mt-3 w-72 rounded-xl border border-white/10 bg-slate-900/95 p-4 shadow-xl"
            >
              <div class="flex items-center justify-between">
                <p class="text-sm font-semibold text-white">Alert Center</p>
                <button class="text-xs text-slate-300" @click="loadNotifications">Refresh</button>
              </div>
              <div class="mt-3 space-y-3 text-xs text-slate-300">
                <div v-if="!notifications.length" class="rounded-lg border border-white/10 bg-white/5 px-3 py-2">
                  No notifications yet.
                </div>
                <div v-for="note in notifications.slice(0, 5)" :key="note.id" class="rounded-lg border border-white/10 bg-white/5 px-3 py-2">
                  <p class="font-semibold text-white">{{ note.subject || 'Notification' }}</p>
                  <p class="mt-1 text-[11px] text-slate-300">{{ note.body || 'No message' }}</p>
                  <p class="mt-2 text-[10px] uppercase tracking-[0.2em] text-slate-400">{{ note.status || 'UNKNOWN' }}</p>
                </div>
              </div>
            </div>
          </div>

          <div class="relative">
            <button
              class="flex items-center gap-2 rounded-full border border-white/10 bg-white/5 px-3 py-2 text-sm transition hover:bg-white/10"
              @click="profileOpen = !profileOpen"
            >
              <span class="font-semibold text-white">{{ authState.username || 'User' }}</span>
              <span class="rounded-full bg-white/10 px-2 py-0.5 text-[10px] uppercase tracking-[0.2em] text-slate-200">{{ authState.role || 'ROLE' }}</span>
            </button>

            <div v-if="profileOpen" class="absolute right-0 mt-3 w-48 rounded-xl border border-white/10 bg-slate-900/95 p-3 text-sm shadow-xl">
              <button class="w-full rounded-lg px-3 py-2 text-left text-slate-200 transition hover:bg-white/10" @click="logout">
                Logout
              </button>
            </div>
          </div>
        </div>
      </div>
    </header>

    <main :class="isManagementRoute ? 'w-full' : 'mx-auto max-w-6xl px-6 py-8'">
      <slot />
    </main>
  </div>
</template>
