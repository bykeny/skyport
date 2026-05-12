import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080/api',
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
};
