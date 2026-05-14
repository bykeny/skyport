import axios from 'axios';

const apiClient = axios.create({
  baseURL: import.meta.env.DEV ? '/api' : 'http://localhost:8080/api',
  timeout: 8000,
  headers: {
    'Content-Type': 'application/json',
  },
});

const unwrap = (response) => response.data;

export default {
  getFlights() {
    return apiClient.get('/flights').then(unwrap);
  },
  createFlight(payload) {
    return apiClient.post('/flights', payload).then(unwrap);
  },
  getCheckinsByFlight(flightId) {
    return apiClient.get(`/checkin/flight/${flightId}`).then(unwrap);
  },
  createCheckin(payload) {
    return apiClient.post('/checkin', payload).then(unwrap);
  },
  getBaggageByFlight(flightId) {
    return apiClient.get(`/baggage/flight/${flightId}`).then(unwrap);
  },
  createBaggage(payload) {
    return apiClient.post('/baggage', payload).then(unwrap);
  },
  getNotifications() {
    return apiClient.get('/notifications').then(unwrap);
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
