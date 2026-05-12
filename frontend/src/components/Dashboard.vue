<template>
  <div class="flex h-screen bg-gray-100 font-sans">
    <!-- Sidebar -->
    <aside class="w-64 bg-slate-900 text-white flex flex-col">
      <div class="p-4 flex items-center justify-center border-b border-slate-700">
        <h1 class="text-xl font-bold">Airport Ops</h1>
      </div>
      <nav class="flex-1 p-4 space-y-2">
        <a href="#" class="block py-2.5 px-4 rounded transition duration-200 bg-slate-800 text-white">
          Dashboard
        </a>
        <a href="#" class="block py-2.5 px-4 rounded transition duration-200 hover:bg-slate-700 hover:text-white">
          Flights
        </a>
        <a href="#" class="block py-2.5 px-4 rounded transition duration-200 hover:bg-slate-700 hover:text-white">
          Passengers
        </a>
        <a href="#" class="block py-2.5 px-4 rounded transition duration-200 hover:bg-slate-700 hover:text-white">
          Baggage
        </a>
        <a href="#" class="block py-2.5 px-4 rounded transition duration-200 hover:bg-slate-700 hover:text-white">
          Alerts
        </a>
      </nav>
    </aside>

    <!-- Main Content -->
    <main class="flex-1 overflow-x-hidden overflow-y-auto bg-gray-100">
      <div class="container mx-auto px-6 py-8">
        <h3 class="text-gray-700 text-3xl font-medium">Operations Dashboard</h3>

        <!-- Stat Cards -->
        <div class="mt-4 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <div class="bg-white rounded-md border border-gray-100 p-6 shadow-sm">
            <h4 class="text-gray-500 text-sm font-semibold uppercase tracking-wider">Total Flights</h4>
            <div class="text-3xl font-bold text-gray-800 mt-2">{{ flights.length }}</div>
          </div>
          <div class="bg-white rounded-md border border-gray-100 p-6 shadow-sm">
            <h4 class="text-gray-500 text-sm font-semibold uppercase tracking-wider">Active Check-ins</h4>
            <div class="text-3xl font-bold text-gray-800 mt-2">{{ checkins.length }}</div>
          </div>
          <div class="bg-white rounded-md border border-gray-100 p-6 shadow-sm">
            <h4 class="text-gray-500 text-sm font-semibold uppercase tracking-wider">Tracked Bags</h4>
            <div class="text-3xl font-bold text-gray-800 mt-2">{{ baggages.length }}</div>
          </div>
          <div class="bg-white rounded-md border border-gray-100 p-6 shadow-sm">
            <h4 class="text-gray-500 text-sm font-semibold uppercase tracking-wider">Recent Alerts</h4>
            <div class="text-3xl font-bold text-gray-800 mt-2">{{ notifications.length }}</div>
          </div>
        </div>

        <div class="mt-8 grid grid-cols-1 lg:grid-cols-2 gap-6">
          <!-- Flight Operations -->
          <div class="bg-white rounded-md shadow-sm border border-gray-100 overflow-hidden flex flex-col">
            <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
              <h3 class="text-lg font-medium text-gray-800">Flight Operations</h3>
            </div>
            <div class="p-6 flex-1">
              <div v-if="loading.flights" class="text-gray-500 text-center py-4">Loading flights...</div>
              <div v-else-if="error.flights" class="text-red-500 text-center py-4 text-sm font-medium">Service Unavailable</div>
              <div v-else-if="flights.length === 0" class="text-gray-500 text-center py-4 italic">No Active Data</div>
              <div v-else class="overflow-x-auto">
                <table class="min-w-full leading-normal">
                  <thead>
                    <tr>
                      <th class="px-3 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Flight</th>
                      <th class="px-3 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Origin</th>
                      <th class="px-3 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Dest</th>
                      <th class="px-3 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="flight in flights" :key="flight.flightNumber || flight.id">
                      <td class="px-3 py-3 border-b border-gray-200 text-sm">
                        <p class="text-gray-900 font-medium">{{ flight.flightNumber }}</p>
                      </td>
                      <td class="px-3 py-3 border-b border-gray-200 text-sm">
                        <p class="text-gray-900 whitespace-nowrap">{{ flight.origin }}</p>
                      </td>
                      <td class="px-3 py-3 border-b border-gray-200 text-sm">
                        <p class="text-gray-900 whitespace-nowrap">{{ flight.destination }}</p>
                      </td>
                      <td class="px-3 py-3 border-b border-gray-200 text-sm">
                        <span class="relative inline-block px-3 py-1 font-semibold text-xs leading-tight rounded-full" 
                              :class="getStatusClass(flight.status)">
                          {{ flight.status }}
                        </span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <!-- Passenger Hub -->
          <div class="bg-white rounded-md shadow-sm border border-gray-100 overflow-hidden flex flex-col">
            <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
              <h3 class="text-lg font-medium text-gray-800">Passenger Hub</h3>
            </div>
            <div class="p-6 flex-1">
              <div v-if="loading.checkins" class="text-gray-500 text-center py-4">Loading check-ins...</div>
              <div v-else-if="error.checkins" class="text-red-500 text-center py-4 text-sm font-medium">Service Unavailable</div>
              <div v-else-if="checkins.length === 0" class="text-gray-500 text-center py-4 italic">No Active Data</div>
              <ul v-else class="divide-y divide-gray-200">
                <li v-for="checkin in checkins" :key="checkin.id" class="py-3 flex justify-between items-center">
                  <div>
                    <p class="text-sm font-medium text-gray-900">{{ checkin.passengerName }}</p>
                    <p class="text-xs text-gray-500">Flight: {{ checkin.flightNumber }}</p>
                  </div>
                  <div class="text-sm font-bold text-slate-700 bg-slate-100 px-3 py-1 rounded">
                    Seat {{ checkin.seatNumber }}
                  </div>
                </li>
              </ul>
            </div>
          </div>

          <!-- Baggage Control -->
          <div class="bg-white rounded-md shadow-sm border border-gray-100 overflow-hidden flex flex-col">
            <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
              <h3 class="text-lg font-medium text-gray-800">Baggage Control</h3>
            </div>
            <div class="p-6 flex-1">
              <div v-if="loading.baggages" class="text-gray-500 text-center py-4">Loading baggage...</div>
              <div v-else-if="error.baggages" class="text-red-500 text-center py-4 text-sm font-medium">Service Unavailable</div>
              <div v-else-if="baggages.length === 0" class="text-gray-500 text-center py-4 italic">No Active Data</div>
              <div v-else class="overflow-x-auto">
                <table class="min-w-full leading-normal">
                  <thead>
                    <tr>
                      <th class="px-3 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Tag Number</th>
                      <th class="px-3 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Status</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="bag in baggages" :key="bag.tagNumber || bag.id">
                      <td class="px-3 py-3 border-b border-gray-200 text-sm">
                        <p class="text-gray-900 font-medium">{{ bag.tagNumber }}</p>
                      </td>
                      <td class="px-3 py-3 border-b border-gray-200 text-sm">
                        <p class="text-gray-600">{{ bag.status }}</p>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          <!-- Alert Center -->
          <div class="bg-white rounded-md shadow-sm border border-gray-100 overflow-hidden flex flex-col">
            <div class="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
              <h3 class="text-lg font-medium text-gray-800">Alert Center</h3>
            </div>
            <div class="p-6 flex-1">
               <div v-if="loading.notifications" class="text-gray-500 text-center py-4">Loading alerts...</div>
              <div v-else-if="error.notifications" class="text-red-500 text-center py-4 text-sm font-medium">Service Unavailable</div>
              <div v-else-if="notifications.length === 0" class="text-gray-500 text-center py-4 italic">No Active Data</div>
              <div v-else class="overflow-x-auto">
                <table class="min-w-full leading-normal">
                  <thead>
                    <tr>
                      <th class="px-3 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Subject</th>
                      <th class="px-3 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Recipient</th>
                      <th class="px-3 py-3 border-b-2 border-gray-200 bg-gray-50 text-left text-xs font-semibold text-gray-600 uppercase tracking-wider">Time</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="alert in notifications" :key="alert.id">
                      <td class="px-3 py-3 border-b border-gray-200 text-sm">
                        <p class="text-gray-900 font-medium">{{ alert.subject }}</p>
                        <p class="text-xs text-gray-500 mt-1">{{ alert.status }}</p>
                      </td>
                      <td class="px-3 py-3 border-b border-gray-200 text-sm">
                        <p class="text-gray-600">{{ alert.recipientId }}</p>
                      </td>
                      <td class="px-3 py-3 border-b border-gray-200 text-sm">
                        <p class="text-gray-500 text-xs">{{ formatDate(alert.sentAt) }}</p>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>

        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '../services/api';

const flights = ref([]);
const checkins = ref([]);
const baggages = ref([]);
const notifications = ref([]);

const loading = ref({
  flights: true,
  checkins: true,
  baggages: true,
  notifications: true
});

const error = ref({
  flights: false,
  checkins: false,
  baggages: false,
  notifications: false
});

const getStatusClass = (status) => {
  if (!status) return 'bg-gray-100 text-gray-800';
  const s = status.toUpperCase();
  if (['ON_TIME', 'ON-TIME', 'SCHEDULED'].includes(s)) return 'bg-green-100 text-green-800';
  if (['DELAYED', 'CANCELLED'].includes(s)) return 'bg-red-100 text-red-800';
  if (['BOARDING', 'IN-FLIGHT', 'ACTIVE'].includes(s)) return 'bg-blue-100 text-blue-800';
  return 'bg-gray-100 text-gray-800';
};

const formatDate = (dateString) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
};

const fetchData = async () => {
  try {
    const res = await api.getFlights();
    flights.value = res.data;
  } catch (err) {
    error.value.flights = true;
  } finally {
    loading.value.flights = false;
  }

  try {
    const res = await api.getCheckin();
    checkins.value = res.data;
  } catch (err) {
    error.value.checkins = true;
  } finally {
    loading.value.checkins = false;
  }

  try {
    const res = await api.getBaggage();
    baggages.value = res.data;
  } catch (err) {
    error.value.baggages = true;
  } finally {
    loading.value.baggages = false;
  }

  try {
    const res = await api.getNotifications();
    notifications.value = res.data;
  } catch (err) {
    error.value.notifications = true;
  } finally {
    loading.value.notifications = false;
  }
};

onMounted(() => {
  fetchData();
});
</script>
