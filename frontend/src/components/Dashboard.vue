<template>
  <div class="min-h-screen bg-slate-100 text-slate-900">
    <div class="flex min-h-screen">
      <aside class="hidden w-72 shrink-0 flex-col bg-slate-950 text-white lg:flex">
        <div class="border-b border-white/10 px-6 py-6">
          <p class="text-xs font-semibold uppercase tracking-[0.28em] text-cyan-300">AMS</p>
          <h1 class="mt-2 text-2xl font-bold">Command Center</h1>
        </div>

        <nav class="flex-1 space-y-1 px-4 py-6">
          <a v-for="item in navItems" :key="item.id" :href="`#${item.id}`" class="flex items-center justify-between rounded-lg px-4 py-3 text-sm font-medium text-slate-300 transition hover:bg-white/10 hover:text-white">
            <span>{{ item.label }}</span>
            <span class="rounded-full bg-white/10 px-2 py-0.5 text-xs text-slate-300">{{ item.owner }}</span>
          </a>
        </nav>

        <div class="border-t border-white/10 px-6 py-5 text-sm text-slate-400">
          <p>Gateway</p>
          <p class="mt-1 font-mono text-xs text-cyan-200">localhost:8080/api</p>
        </div>
      </aside>

      <main class="flex-1 overflow-y-auto">
        <header class="border-b border-slate-200 bg-white/90 px-4 py-5 shadow-sm backdrop-blur sm:px-6 lg:px-8">
          <div class="mx-auto flex max-w-7xl flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
            <div>
              <p class="text-sm font-semibold uppercase tracking-[0.2em] text-cyan-700">Airport Management System</p>
              <h2 class="mt-1 text-3xl font-bold tracking-tight text-slate-950">Operations Dashboard</h2>
            </div>
            <button class="inline-flex w-fit items-center rounded-md bg-slate-950 px-4 py-2 text-sm font-semibold text-white shadow-sm transition hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-60" :disabled="refreshing" @click="fetchDashboard">
              {{ refreshing ? 'Refreshing...' : 'Refresh data' }}
            </button>
          </div>
        </header>

        <div class="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
          <section class="grid grid-cols-1 gap-4 sm:grid-cols-2 xl:grid-cols-4">
            <article v-for="card in statCards" :key="card.label" class="rounded-lg border border-slate-200 bg-white p-5 shadow-sm">
              <div class="flex items-start justify-between gap-4">
                <div>
                  <p class="text-sm font-medium text-slate-500">{{ card.label }}</p>
                  <p class="mt-2 text-3xl font-bold text-slate-950">{{ card.value }}</p>
                </div>
                <span class="rounded-md px-2.5 py-1 text-xs font-semibold" :class="card.badgeClass">{{ card.owner }}</span>
              </div>
              <p class="mt-3 text-xs text-slate-500">{{ card.caption }}</p>
            </article>
          </section>

          <section class="mt-6 grid grid-cols-1 gap-6 xl:grid-cols-2">
            <DomainPanel id="flights" title="Flight Scheduling" owner="Ibrahim" :loading="loading.flights" :error="errors.flights" :has-data="flights.length > 0" empty-message="No flights returned by Flight Scheduling.">
              <div class="overflow-x-auto">
                <table class="min-w-full divide-y divide-slate-200 text-sm">
                  <thead class="bg-slate-50 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                    <tr>
                      <th class="px-4 py-3">Flight</th>
                      <th class="px-4 py-3">Route</th>
                      <th class="px-4 py-3">Departure</th>
                      <th class="px-4 py-3">Status</th>
                    </tr>
                  </thead>
                  <tbody class="divide-y divide-slate-100 bg-white">
                    <tr v-for="flight in flights" :key="flight.id || flight.flightNumber">
                      <td class="px-4 py-3">
                        <p class="font-semibold text-slate-950">{{ flight.flightNumber || 'Unnumbered' }}</p>
                        <p class="text-xs text-slate-500">{{ flight.airlineCode || 'Airline N/A' }} · {{ flight.aircraftType || 'Aircraft N/A' }}</p>
                      </td>
                      <td class="px-4 py-3 text-slate-700">{{ flight.origin }} to {{ flight.destination }}</td>
                      <td class="px-4 py-3 text-slate-600">{{ formatDateTime(flight.scheduledDeparture) }}</td>
                      <td class="px-4 py-3">
                        <StatusBadge :status="flight.status" />
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </DomainPanel>

            <DomainPanel id="checkins" title="Passenger Check-in" owner="Umid" :loading="loading.checkins" :error="errors.checkins" :has-data="checkins.length > 0" empty-message="No check-ins found for the loaded flights.">
              <div class="grid grid-cols-1 gap-3 sm:grid-cols-2">
                <article v-for="checkin in checkins" :key="checkin.id" class="rounded-lg border border-slate-200 bg-white p-4">
                  <div class="flex items-start justify-between gap-3">
                    <div>
                      <p class="font-semibold text-slate-950">Passenger #{{ checkin.passengerId }}</p>
                      <p class="mt-1 text-sm text-slate-500">Flight ID {{ checkin.flightId }}</p>
                    </div>
                    <StatusBadge :status="checkin.status" />
                  </div>
                  <dl class="mt-4 grid grid-cols-2 gap-3 text-sm">
                    <div>
                      <dt class="text-xs uppercase tracking-wide text-slate-400">Seat</dt>
                      <dd class="font-semibold text-slate-800">{{ checkin.seatNumber || 'N/A' }}</dd>
                    </div>
                    <div>
                      <dt class="text-xs uppercase tracking-wide text-slate-400">Bags</dt>
                      <dd class="font-semibold text-slate-800">{{ checkin.baggageCount ?? 0 }}</dd>
                    </div>
                    <div class="col-span-2">
                      <dt class="text-xs uppercase tracking-wide text-slate-400">Boarding Pass</dt>
                      <dd class="font-mono text-xs text-slate-700">{{ checkin.boardingPassCode || 'Not issued' }}</dd>
                    </div>
                  </dl>
                </article>
              </div>
            </DomainPanel>

            <DomainPanel id="baggage" title="Baggage Tracking" owner="Tofig" :loading="loading.baggage" :error="errors.baggage" :has-data="baggage.length > 0" empty-message="No baggage found for the loaded flights.">
              <div class="overflow-x-auto">
                <table class="min-w-full divide-y divide-slate-200 text-sm">
                  <thead class="bg-slate-50 text-left text-xs font-semibold uppercase tracking-wide text-slate-500">
                    <tr>
                      <th class="px-4 py-3">Tag</th>
                      <th class="px-4 py-3">Passenger</th>
                      <th class="px-4 py-3">Weight</th>
                      <th class="px-4 py-3">Status</th>
                    </tr>
                  </thead>
                  <tbody class="divide-y divide-slate-100 bg-white">
                    <tr v-for="bag in baggage" :key="bag.id || bag.tagNumber">
                      <td class="px-4 py-3">
                        <p class="font-semibold text-slate-950">{{ bag.tagNumber }}</p>
                        <p class="font-mono text-xs text-slate-500">{{ bag.barcode || 'No barcode' }}</p>
                      </td>
                      <td class="px-4 py-3 text-slate-700">#{{ bag.passengerId }}</td>
                      <td class="px-4 py-3 text-slate-600">{{ formatWeight(bag) }}</td>
                      <td class="px-4 py-3">
                        <StatusBadge :status="bag.status" />
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </DomainPanel>

            <DomainPanel id="notifications" title="Notification Service" owner="Kanan" :loading="loading.notifications" :error="errors.notifications" :has-data="notifications.length > 0" empty-message="No notifications returned by Notification Service.">
              <div class="space-y-3">
                <article v-for="notification in notifications" :key="notification.id" class="rounded-lg border border-slate-200 bg-white p-4">
                  <div class="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between">
                    <div>
                      <p class="font-semibold text-slate-950">{{ notification.subject || 'Notification' }}</p>
                      <p class="mt-1 text-sm text-slate-500">{{ notification.body || 'No message body' }}</p>
                    </div>
                    <StatusBadge :status="notification.status" />
                  </div>
                  <div class="mt-4 flex flex-wrap gap-2 text-xs text-slate-600">
                    <span class="rounded-md bg-slate-100 px-2.5 py-1">Recipient #{{ notification.recipientId }}</span>
                    <span class="rounded-md bg-slate-100 px-2.5 py-1">{{ notification.recipientType || 'Recipient' }}</span>
                    <span class="rounded-md bg-slate-100 px-2.5 py-1">{{ notification.channel || 'Channel N/A' }}</span>
                    <span class="rounded-md bg-slate-100 px-2.5 py-1">{{ formatDateTime(notification.sentAt) }}</span>
                  </div>
                </article>
              </div>
            </DomainPanel>
          </section>
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed, defineComponent, h, onMounted, ref } from 'vue';
import api from '../services/api';

const navItems = [
  { id: 'flights', label: 'Flight Scheduling', owner: 'Ibrahim' },
  { id: 'checkins', label: 'Passenger Check-in', owner: 'Umid' },
  { id: 'baggage', label: 'Baggage Tracking', owner: 'Tofig' },
  { id: 'notifications', label: 'Notifications', owner: 'Kanan' },
];

const flights = ref([]);
const checkins = ref([]);
const baggage = ref([]);
const notifications = ref([]);
const refreshing = ref(false);

const loading = ref({
  flights: true,
  checkins: true,
  baggage: true,
  notifications: true,
});

const errors = ref({
  flights: '',
  checkins: '',
  baggage: '',
  notifications: '',
});

const StatusBadge = defineComponent({
  props: {
    status: {
      type: String,
      default: 'UNKNOWN',
    },
  },
  setup(props) {
    return () => h('span', {
      class: ['inline-flex rounded-full px-2.5 py-1 text-xs font-semibold', statusClass(props.status)],
    }, props.status || 'UNKNOWN');
  },
});

const DomainPanel = defineComponent({
  props: {
    id: String,
    title: String,
    owner: String,
    loading: Boolean,
    error: String,
    hasData: Boolean,
    emptyMessage: String,
  },
  setup(props, { slots }) {
    return () => h('section', { id: props.id, class: 'rounded-lg border border-slate-200 bg-white shadow-sm' }, [
      h('div', { class: 'flex items-center justify-between gap-4 border-b border-slate-200 px-5 py-4' }, [
        h('div', [
          h('h3', { class: 'text-lg font-semibold text-slate-950' }, props.title),
          h('p', { class: 'mt-1 text-sm text-slate-500' }, `${props.owner}'s domain`),
        ]),
      ]),
      h('div', { class: 'p-5' }, [
        props.loading
          ? h('div', { class: 'rounded-lg border border-slate-200 bg-slate-50 px-4 py-8 text-center text-sm font-medium text-slate-500' }, 'Loading service data...')
          : props.error
            ? h('div', { class: 'rounded-lg border border-red-200 bg-red-50 px-4 py-8 text-center' }, [
              h('p', { class: 'font-semibold text-red-700' }, 'Service Offline'),
              h('p', { class: 'mt-1 text-sm text-red-600' }, props.error),
            ])
            : !props.hasData
              ? h('div', { class: 'rounded-lg border border-slate-200 bg-slate-50 px-4 py-8 text-center text-sm font-medium text-slate-500' }, props.emptyMessage)
              : h('div', [
                slots.default(),
              ]),
      ]),
    ]);
  },
});

const statCards = computed(() => [
  {
    label: 'Flights',
    value: flights.value.length,
    owner: 'Ibrahim',
    caption: 'Loaded from /api/flights',
    badgeClass: 'bg-cyan-100 text-cyan-800',
  },
  {
    label: 'Passengers',
    value: checkins.value.length,
    owner: 'Umid',
    caption: 'Derived from /api/checkin/flight/{flightId}',
    badgeClass: 'bg-emerald-100 text-emerald-800',
  },
  {
    label: 'Bags',
    value: baggage.value.length,
    owner: 'Tofig',
    caption: 'Derived from /api/baggage/flight/{flightId}',
    badgeClass: 'bg-amber-100 text-amber-800',
  },
  {
    label: 'Notifications',
    value: notifications.value.length,
    owner: 'Kanan',
    caption: 'Loaded from /api/notifications',
    badgeClass: 'bg-violet-100 text-violet-800',
  },
]);

const normalizeError = (err) => {
  if (err?.response?.status) {
    return `Gateway returned HTTP ${err.response.status}.`;
  }

  if (err?.code === 'ECONNABORTED') {
    return 'The request timed out.';
  }

  return 'The service did not respond through the gateway.';
};

const statusClass = (status) => {
  const normalized = String(status || '').toUpperCase();

  if (['SENT', 'SCHEDULED', 'CHECKED_IN', 'SCREENED', 'LOADED', 'ARRIVED', 'DELIVERED'].includes(normalized)) {
    return 'bg-emerald-100 text-emerald-800';
  }

  if (['BOARDING', 'DEPARTED', 'IN_TRANSIT', 'BAGGAGE_DROPPED', 'BOARDED'].includes(normalized)) {
    return 'bg-blue-100 text-blue-800';
  }

  if (['PENDING', 'REGISTERED'].includes(normalized)) {
    return 'bg-amber-100 text-amber-800';
  }

  if (['DELAYED', 'CANCELLED', 'FAILED'].includes(normalized)) {
    return 'bg-red-100 text-red-800';
  }

  return 'bg-slate-100 text-slate-700';
};

const formatDateTime = (value) => {
  if (!value) return 'N/A';
  return new Intl.DateTimeFormat(undefined, {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(value));
};

const formatWeight = (bag) => {
  if (bag.weightValue === null || bag.weightValue === undefined) return 'N/A';
  return `${bag.weightValue} ${bag.weightUnit || 'kg'}`;
};

const resetState = () => {
  loading.value = {
    flights: true,
    checkins: true,
    baggage: true,
    notifications: true,
  };
  errors.value = {
    flights: '',
    checkins: '',
    baggage: '',
    notifications: '',
  };
};

const fetchFlightScopedData = async (flightList, loader, domain) => {
  const results = await Promise.allSettled(
    flightList.map((flight) => loader(flight.id)),
  );

  const rejected = results.find((result) => result.status === 'rejected');
  if (rejected) {
    errors.value[domain] = normalizeError(rejected.reason);
  }

  return results
    .filter((result) => result.status === 'fulfilled')
    .flatMap((result) => result.value);
};

const fetchDashboard = async () => {
  refreshing.value = true;
  resetState();

  try {
    flights.value = await api.getFlights();
  } catch (err) {
    flights.value = [];
    errors.value.flights = normalizeError(err);
  } finally {
    loading.value.flights = false;
  }

  const flightList = flights.value.filter((flight) => flight.id);

  if (flightList.length === 0 && !errors.value.flights) {
    checkins.value = [];
    baggage.value = [];
    loading.value.checkins = false;
    loading.value.baggage = false;
  } else {
    try {
      checkins.value = await fetchFlightScopedData(flightList, api.getCheckinsByFlight, 'checkins');
    } finally {
      loading.value.checkins = false;
    }

    try {
      baggage.value = await fetchFlightScopedData(flightList, api.getBaggageByFlight, 'baggage');
    } finally {
      loading.value.baggage = false;
    }
  }

  try {
    notifications.value = await api.getNotifications();
  } catch (err) {
    notifications.value = [];
    errors.value.notifications = normalizeError(err);
  } finally {
    loading.value.notifications = false;
    refreshing.value = false;
  }
};

onMounted(fetchDashboard);
</script>
