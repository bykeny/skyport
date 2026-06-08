import axios from 'axios';

const apiClient = axios.create({
  baseURL: import.meta.env.DEV ? '/api' : 'http://localhost:8080/api',
  timeout: 8000,
  headers: {
    'Content-Type': 'application/json',
  },
});

const unwrap = (response) => response.data;

const STORAGE_KEY = 'ams_auth';

const getStoredAuth = () => {
  try {
    const raw = localStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch (err) {
    return null;
  }
};

const clearStoredAuth = () => {
  localStorage.removeItem(STORAGE_KEY);
};

apiClient.interceptors.request.use((config) => {
  const auth = getStoredAuth();
  if (auth?.token) {
    config.headers.Authorization = `Bearer ${auth.token}`;
    if (auth.userId !== undefined && auth.userId !== null) {
      config.headers['X-User-Id'] = String(auth.userId);
    }
    if (auth.role) {
      config.headers['X-User-Role'] = auth.role;
    }
  }
  return config;
});

apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error?.response?.status;
    if (status === 401) {
      const auth = getStoredAuth();
      if (auth?.token) {
        clearStoredAuth();
        if (typeof window !== 'undefined') {
          window.location.assign('/auth');
        }
      }
    }
    return Promise.reject(error);
  }
);

export default {
  login(payload) {
    return apiClient.post('/auth/login', payload).then(unwrap);
  },
  register(payload) {
    return apiClient.post('/auth/register', payload).then(unwrap);
  },
  getFlights() {
    return apiClient.get('/flights').then(unwrap);
  },
  searchFlights(params) {
    return apiClient.get('/flights/search', { params }).then(unwrap);
  },
  createFlight(payload) {
    return apiClient.post('/flights', payload).then(unwrap);
  },
  updateFlightStatus(flightId, payload) {
    return apiClient.patch(`/flights/${flightId}/status`, payload).then(unwrap);
  },
  updateFlight(flightId, payload) {
    return apiClient.put(`/flights/${flightId}`, payload).then(unwrap);
  },
  getCheckinsByFlight(flightId) {
    return apiClient.get(`/checkin/flight/${flightId}`).then(unwrap);
  },
  getCheckinsByPassenger(passengerId) {
    return apiClient.get(`/checkin/passenger/${passengerId}`).then(unwrap);
  },
  getPassengerFlightCheckin(passengerId, flightId) {
    return apiClient.get(`/checkin/passenger/${passengerId}/flight/${flightId}`).then(unwrap);
  },
  createCheckin(payload) {
    return apiClient.post('/checkin', payload).then(unwrap);
  },
  getBaggageByFlight(flightId) {
    return apiClient.get(`/baggage/flight/${flightId}`).then(unwrap);
  },
  getBaggageByTag(tagNumber) {
    return apiClient.get(`/baggage/tag/${tagNumber}`).then(unwrap);
  },
  createBaggage(payload) {
    return apiClient.post('/baggage', payload).then(unwrap);
  },
  getNotifications() {
    return apiClient.get('/notifications').then(unwrap);
  },
  getNotificationsByRecipient(recipientId) {
    return apiClient.get(`/notifications/recipient/${recipientId}`).then(unwrap);
  },
  createNotification(payload) {
    return apiClient.post('/notifications', payload).then(unwrap);
  },
  getGates() {
    return apiClient.get('/gates').then(unwrap);
  },
  getGateAssignmentByFlight(flightId) {
    return apiClient.get(`/gate-assignments/flight/${flightId}`).then(unwrap);
  },
  createGateAssignment(payload) {
    return apiClient.post('/gate-assignments', payload).then(unwrap);
  },
  getProducts() {
    return apiClient.get('/retail/products').then(unwrap);
  },
  createProduct(payload) {
    return apiClient.post('/retail/products', payload).then(unwrap);
  },
  getRetailOrdersByFlight(flightId) {
    return apiClient.get(`/retail/orders/flight/${flightId}`).then(unwrap);
  },
  createRetailOrder(payload) {
    return apiClient.post('/retail/orders', payload).then(unwrap);
  },
  getClearancesByPassenger(passengerId) {
    return apiClient.get(`/security/clearances/passenger/${passengerId}`).then(unwrap);
  },
  createClearance(payload) {
    return apiClient.post('/security/clearances', payload).then(unwrap);
  },
};
