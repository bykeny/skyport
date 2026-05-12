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
  getCheckinsByFlight(flightId) {
    return apiClient.get(`/checkin/flight/${flightId}`).then(unwrap);
  },
  getBaggageByFlight(flightId) {
    return apiClient.get(`/baggage/flight/${flightId}`).then(unwrap);
  },
  getNotifications() {
    return apiClient.get('/notifications').then(unwrap);
  },
};
