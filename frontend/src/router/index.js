import { createRouter, createWebHistory } from 'vue-router';
import { getStoredAuth } from '../composables/useAuth';
import LandingView from '../views/LandingView.vue';
import AuthView from '../views/AuthView.vue';
import PassengerDashboardView from '../views/PassengerDashboardView.vue';
import PassengerCheckinView from '../views/PassengerCheckinView.vue';
import PassengerBaggageView from '../views/PassengerBaggageView.vue';
import PassengerAlertsView from '../views/PassengerAlertsView.vue';
import ManagementOverviewView from '../views/ManagementOverviewView.vue';
import ManagementFlightsView from '../views/ManagementFlightsView.vue';
import ManagementGatesView from '../views/ManagementGatesView.vue';
import ManagementRetailView from '../views/ManagementRetailView.vue';
import FlightBoardView from '../views/FlightBoardView.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'landing',
      component: LandingView,
      meta: { layout: 'public' },
    },
    {
      path: '/auth',
      name: 'auth',
      component: AuthView,
      meta: { layout: 'public' },
    },
    {
      path: '/flight-board',
      name: 'flight-board',
      component: FlightBoardView,
      meta: { layout: 'public' },
    },
    {
      path: '/passenger',
      redirect: '/passenger/dashboard',
    },
    {
      path: '/passenger/dashboard',
      name: 'passenger-dashboard',
      component: PassengerDashboardView,
      meta: { layout: 'app', requiresAuth: true, roles: ['PASSENGER'] },
    },
    {
      path: '/passenger/checkin',
      name: 'passenger-checkin',
      component: PassengerCheckinView,
      meta: { layout: 'app', requiresAuth: true, roles: ['PASSENGER'] },
    },
    {
      path: '/passenger/baggage',
      name: 'passenger-baggage',
      component: PassengerBaggageView,
      meta: { layout: 'app', requiresAuth: true, roles: ['PASSENGER'] },
    },
    {
      path: '/passenger/alerts',
      name: 'passenger-alerts',
      component: PassengerAlertsView,
      meta: { layout: 'app', requiresAuth: true, roles: ['PASSENGER'] },
    },
    {
      path: '/management',
      redirect: '/management/overview',
    },
    {
      path: '/management/overview',
      name: 'management-overview',
      component: ManagementOverviewView,
      meta: { layout: 'app', requiresAuth: true, roles: ['ADMIN'] },
    },
    {
      path: '/management/flights',
      name: 'management-flights',
      component: ManagementFlightsView,
      meta: { layout: 'app', requiresAuth: true, roles: ['ADMIN'] },
    },
    {
      path: '/management/gates',
      name: 'management-gates',
      component: ManagementGatesView,
      meta: { layout: 'app', requiresAuth: true, roles: ['ADMIN'] },
    },
    {
      path: '/management/retail',
      name: 'management-retail',
      component: ManagementRetailView,
      meta: { layout: 'app', requiresAuth: true, roles: ['ADMIN'] },
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/',
    },
  ],
});

router.beforeEach((to) => {
  if (!to.meta?.requiresAuth) return true;

  const auth = getStoredAuth();
  if (!auth?.token) {
    return { name: 'auth', query: { redirect: to.fullPath } };
  }

  const allowedRoles = to.meta?.roles || (to.meta?.role ? [to.meta.role] : []);
  if (allowedRoles.length && !allowedRoles.includes(auth.role)) {
    return { name: 'landing' };
  }

  return true;
});

export default router;
